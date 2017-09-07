package com.socool.soft.controller;

import com.socool.soft.bo.*;
import com.socool.soft.bo.constant.RoleIdEnum;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.constant.Constants;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.exception.ErrorCode;
import com.socool.soft.exception.ErrorValue;
import com.socool.soft.exception.SystemException;
import com.socool.soft.service.*;
import com.socool.soft.util.JsonUtil;
import com.socool.soft.util.VOConversionUtil;
import com.socool.soft.vo.Code;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.Result;
import com.socool.soft.vo.newvo.android.OrderNumVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestMapping(value = { "loginRegister", "user" })
@Controller
public class LoginRegisterController extends BaseController {
	@Autowired
	private IMemberService memberService;
	@Autowired
	private IBuyerService buyerService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IMerchantUserService merchantUserService;
	@Autowired
	private IVendorUserService vendorUserService;
	@Autowired
	private IBuyerConsigneeService buyerConsigneeService;
	@Autowired
	private IFileInfoService fileInfoService;
	@Autowired
	private ICityService cityService;
	@Autowired
	private IBuyerTradeService buyerTradeService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IRedisService redisService;
	@Autowired
	private IIMService imService;

	@Autowired
	private IRoleService roleService;
	@Autowired
	private IMenuService menuService;
	@Autowired
	private IVendorService vendorService;

	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private PropertyConstants propertyConstants;
    @Autowired
    private IUserGetuiService userGetuiService;
    @Autowired
	private IDokuUserTokenService dokuUserTokenService;

	/**
	 * 绑定支付密码
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SystemException 
	 */
	@RequestMapping(value = "mypaypwd")
	@ResponseBody
	public Result<?> payCode(String data, String timestamp, String nonceStr, String product, String signature)
			throws IOException, ClassNotFoundException, SystemException {
		Result<?> result = new Result<Object>();
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}
		int buyerId = json.getInt("memberId");
		String oldPayPwd = null;
		try {
			oldPayPwd = json.getString("oldPayPwd");
		} catch (Exception e) {
		}
		String code = null;
		try {
			code = json.getString("code");
		} catch (Exception e) {
		}
		String newPayPwd = json.getString("newPayPwd");
		RcBuyer buyer = buyerService.findBuyerById(buyerId);

		if (StringUtils.isBlank(oldPayPwd) && StringUtils.isBlank(code)) {
			buyer.setWalletPwd(DigestUtils.md5Hex(newPayPwd));
		} else if (StringUtils.isBlank(oldPayPwd)) {
			String string = propertyConstants.whiteListMobile;
			String[] whiteList = string.split(",");
			int status = 0;
			for(int i = 0; i < whiteList.length; i++){
				if(buyer.getMobile().equals(whiteList[i])){
					status = 1;
					break;
				}
			}
			if(status == 0){
				// 验证码逻辑
				Code codeRedis = (Code) redisService.hgetObject(Constants.REDIS_MOBILE_VERIFICATION_CODE, buyer.getMobile());
				if (codeRedis != null) {
					if (new Date().getTime() - codeRedis.getCreateDate().getTime() > 3 * 60 * 1000) {
						result.setCode(ErrorValue.VERIFICATION_CODE_FAILURE.getStr());
						result.setResult("Verification code has expired");
						return result;
					} else if (!codeRedis.getCode().equals(code)) {
						result.setCode(ErrorValue.ERROR_VERIFICATION_CODE.getStr());
						result.setResult("Incorrect verification code");
						return result;
					} else {
						buyer.setWalletPwd(DigestUtils.md5Hex(newPayPwd));
						// 验证成功销毁验证码
						redisService.hdelObject(Constants.REDIS_MOBILE_VERIFICATION_CODE, buyer.getMobile());
					}
				}
			} else {
				buyer.setWalletPwd(DigestUtils.md5Hex(newPayPwd));
			}
		} else {
			if (DigestUtils.md5Hex(oldPayPwd).equals(buyer.getWalletPwd())) {
				buyer.setWalletPwd(DigestUtils.md5Hex(newPayPwd));
			} else {
				result.setCode(ErrorValue.ERROR_PASSWORD.getStr());
				result.setResult("Incorrect old payment password error!");
				return result;
			}
		}

		buyerService.modifyBuyer(buyer);
		return result;
	}/**
	 * 绑定登陆密码
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SystemException 
	 */
	@RequestMapping(value = "checkloginpwd")
	@ResponseBody
	public Result<String> checkLoginPwd(String data, String timestamp, String nonceStr, String product, String signature)
			throws IOException, ClassNotFoundException, SystemException {
		Result<String> result = new Result<String>();
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}

		Integer buyerId = null;
		try {
			buyerId = json.getInt("memberId");
		} catch (Exception e) {
		}
		String mobile = null;
		try {
			mobile = json.getString("mobile");
		} catch (Exception e) {
		}
		String oldLoginPwd = null;
		try {
			oldLoginPwd = json.getString("oldLoginPwd");
		} catch (Exception e) {
		}
		RcBuyer buyer = null;
		if (buyerId != null) {
			buyer = buyerService.findBuyerById(buyerId);
		} else {
			buyer = buyerService.findBuyerByMobile(mobile);
			if(buyer == null) {
				result.setCode(ErrorValue.ACCOUNT_NOT_EXISTS.getStr());
				result.setResult("account not exists");
				return result;
			}
		}

		if (StringUtils.isBlank(oldLoginPwd)) {
		} else {
			if (DigestUtils.md5Hex(oldLoginPwd).equals(buyer.getPassword())) {
			} else {
				result.setCode(ErrorValue.ERROR_PASSWORD.getStr());
				result.setResult("Incorrect old payment password error!");
				return result;
			}
		}
		buyerService.modifyBuyer(buyer);
		return result;
	}

	/**
	 * 绑定登陆密码
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SystemException 
	 */
	@RequestMapping(value = "myloginpwd")
	@ResponseBody
	public Result<String> myLoginPwd(String data, String timestamp, String nonceStr, String product, String signature)
			throws IOException, ClassNotFoundException, SystemException {
		Result<String> result = new Result<String>();
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}

		Integer buyerId = null;
		try {
			buyerId = json.getInt("memberId");
		} catch (Exception e) {
		}
		String mobile = null;
		try {
			mobile = json.getString("mobile");
		} catch (Exception e) {
		}
//		String oldLoginPwd = null;
//		try {
//			oldLoginPwd = json.getString("oldLoginPwd");
//		} catch (Exception e) {
//		}
		String code = null;
		try {
			code = json.getString("code");
		} catch (Exception e) {
		}
		String newLoginPwd = json.getString("newLoginPwd");
		RcBuyer buyer = null;
		if (buyerId != null) {
			buyer = buyerService.findBuyerById(buyerId);
		} else {
			buyer = buyerService.findBuyerByMobile(mobile);
			if(buyer == null) {
				result.setCode(ErrorValue.ACCOUNT_NOT_EXISTS.getStr());
				result.setResult("account not exists");
				return result;
			}
		}

		if (!StringUtils.isBlank(code)) {
			String string = propertyConstants.whiteListMobile;
			String[] whiteList = string.split(",");
			int status = 0;
			for(int i = 0; i < whiteList.length; i++){
				if(buyer.getMobile().equals(whiteList[i])){
					status = 1;
					break;
				}
			}
			if(status == 0) {
				// 验证码逻辑
				Code codeRedis = (Code) redisService.hgetObject(Constants.REDIS_MOBILE_VERIFICATION_CODE, buyer.getMobile());
				if (codeRedis != null) {
					if (new Date().getTime() - codeRedis.getCreateDate().getTime() > 3 * 60 * 1000) {
						result.setCode(ErrorValue.VERIFICATION_CODE_FAILURE.getStr());
						result.setResult("Verification code has expired");
						return result;
					} else if (!codeRedis.getCode().equals(code)) {
						result.setCode(ErrorValue.ERROR_VERIFICATION_CODE.getStr());
						result.setResult("Incorrect verification code");
						return result;
					} else {
						buyer.setPassword(DigestUtils.md5Hex(newLoginPwd));
						// 验证成功销毁验证码
						redisService.hdelObject(Constants.REDIS_MOBILE_VERIFICATION_CODE, buyer.getMobile());
					}
				} else {
					result.setCode(ErrorValue.ERROR_VERIFICATION_CODE.getStr());
					result.setResult("并未发送验证码！");
					return result;
				}
			}
			buyer.setPassword(DigestUtils.md5Hex(newLoginPwd));
		} else {
//			if (DigestUtils.md5Hex(oldLoginPwd).equals(buyer.getPassword())) {
				buyer.setPassword(DigestUtils.md5Hex(newLoginPwd));
//			} else {
//				result.setCode(ErrorValue.ERROR_PASSWORD.getStr());
//				result.setResult("Incorrect old payment password error!");
//				return result;
//			}
		}
		buyerService.modifyBuyer(buyer);
		return result;
	}

	/**
	 * 手机验证码
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
//	@RequestMapping(value = "verificationcode")
//	@ResponseBody
//	public Result<?> verificationCode(String data, String timestamp, String nonceStr, String product, String signature)
//			throws IOException, ClassNotFoundException {
//		Result<String> result = new Result<String>();
//		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
//		if (json == null) {
//			result.setCode("-1");
//			result.setResult("token验证失败！");
//			return result;
//		}
//		String mobile = json.getString("mobile");
//		String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(4);
//		// 将验证码存入redis中
//		Code code = new Code();
//		code.setCode(randomAlphanumeric);
//		code.setCreateDate(new Date());
//		redisTemplate.boundHashOps("code").put(mobile, SerializationUtils.serialize(code));
//		byte[] codeObject = (byte[]) redisTemplate.opsForHash().entries("code").get(mobile);
//		// 验证成功销毁验证码
////		redisTemplate.opsForHash().entries("code").remove(mobile);
////		ByteArrayInputStream bi = new ByteArrayInputStream(codeObject);
////		ObjectInputStream oi = new ObjectInputStream(bi);
//
////		Code codeRedis = (Code) oi.readObject();
////		IOUtils.closeQuietly(bi);
////		IOUtils.closeQuietly(oi);
////		System.out.println(codeRedis.getCode());
//		result.setResult(randomAlphanumeric);
//		return result;
//	}

	/**
	 * 手机注册
	 * @throws Exception 
	 */
	@RequestMapping(value = "regist")
	@ResponseBody
	public Result<RcBuyer> regist(String data, String timestamp, String nonceStr, String product, String signature)
			throws Exception {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		Result<RcBuyer> result = new Result<RcBuyer>();
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}
		String mobile = json.getString("mobile");
		String nickName = json.getString("name");
		String password = json.getString("password");
		String verificationCode = json.getString("verificationCode");

		/*
		 * HttpSessionContext SessCon =
		 * request.getSession().getSessionContext(); HttpSession Sess =
		 * SessCon.getSession(sessionId); String verificationCodeSession =
		 * (String) Sess.getAttribute(mobile); if
		 * (!verificationCode.equalsIgnoreCase(verificationCodeSession)) {
		 * request.getSession().removeAttribute(mobile); result.setCode("-1");
		 * result.setResult("验证码错误！请重新获取！"); return result;
		 * 
		 * }
		 */
		String string = propertyConstants.whiteListMobile;
		String[] whiteList = string.split(",");
		int status = 0;
		for(int i = 0; i < whiteList.length; i++){
			if(mobile.equals(whiteList[i])){
				status = 1;
				break;
			}
		}
		if(status == 0){
			// 验证码逻辑
			Code codeRedis = (Code) redisService.hgetObject(Constants.REDIS_MOBILE_VERIFICATION_CODE, mobile);
			if (codeRedis != null) {
				if (new Date().getTime() - codeRedis.getCreateDate().getTime() > 3 * 60 * 1000) {
					result.setCode(ErrorValue.VERIFICATION_CODE_FAILURE.getStr());
					result.setResult("Verification code has expired");
					return result;
				} else if (!codeRedis.getCode().equals(verificationCode)) {
					result.setCode(ErrorValue.ERROR_VERIFICATION_CODE.getStr());
					result.setResult("Incorrect verification code");
					return result;
				}
				// 验证成功销毁验证码
				redisService.hdelObject(Constants.REDIS_MOBILE_VERIFICATION_CODE, mobile);
			} else {
				result.setCode(ErrorValue.ERROR_VERIFICATION_CODE.getStr());
				result.setResult("并未发送验证码！");
				return result;
			}
		}

		RcMember member = memberService.findMemberByMobile(mobile);
		if (member != null) {
			result.setCode(ErrorValue.ACCOUNT_ALREADY_EXISTS.getStr());
			result.setResult("手机号已经被使用！");
			return result;
		}
		RcBuyer buyer = new RcBuyer();
		buyer.setPassword(DigestUtils.md5Hex(password));
		buyer.setMobile(mobile);
		buyer.setName(nickName);
		int buyerId = buyerService.addBuyer(buyer);
		//注册环信IM账号
		imService.regist(propertyConstants.huanxinBuyerAccountPrefix + "_" + buyerId, buyer.getName());
		VOConversionUtil.Entity2VO(buyer, new String[] { "memberId", "name", "mobile", "password" }, null);
		result.setData(buyer);
		return result;
	}

	@RequestMapping(value = "loginpwd")
	@ResponseBody
	public Result<RcBuyer> loginPwd(String data, String timestamp, String nonceStr, String product, String signature,
			HttpServletRequest httpServletRequest)
			throws IllegalAccessException, InvocationTargetException, IOException, ClassNotFoundException, SystemException {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		Result<RcBuyer> result = new Result<RcBuyer>();
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}
		String mobile = json.getString("mobile");
		String password = null;
		String verificationCode = null;
		try {
			verificationCode = json.getString("verificationCode");
		} catch (Exception e) {
		}
		try {
			password = json.getString("password");
		} catch (Exception e) {
		}
		RcBuyer buyer = buyerService.findBuyerByMobile(mobile);
		if (buyer == null) {
			result.setCode(ErrorValue.ERROR_PASSWORD_OR_USER_NAME.getStr());
			result.setResult("User does not exist！");
			return result;
		} else {
			RcDokuUserToken dokuUserToken = dokuUserTokenService.findByMemberId(buyer.getMemberId());
			if(dokuUserToken==null){
				buyer.setToken("");
			}
			else{
				buyer.setToken(dokuUserToken.getToken());
			}
			if (StringUtils.isBlank(verificationCode)) {
				if (buyer.getPassword().equals(DigestUtils.md5Hex(password))) {
					buyer.setLastLoginTime(new Date());
					buyerService.modifyBuyer(buyer);
					
					VOConversionUtil.Entity2VO(buyer, new String[] { "memberId", "name", "mobile", "token"}, null);
					result.setData(buyer);

					return result;
				} else {
					result.setCode(ErrorValue.ERROR_PASSWORD_OR_USER_NAME.getStr());
					result.setResult(" Incorrect phone number or password！");
					return result;
				}
			} else {
				String string = propertyConstants.whiteListMobile;
				String[] whiteList = string.split(",");
				int status = 0;
				for(int i = 0; i < whiteList.length; i++){
					if(buyer.getMobile().equals(whiteList[i])){
						status = 1;
						break;
					}
				}
				if(status == 0) {
					// 验证码在此验证
					// 验证码逻辑
					Code codeRedis = (Code) redisService.hgetObject(Constants.REDIS_MOBILE_VERIFICATION_CODE, mobile);
					if (codeRedis != null) {
						if (new Date().getTime() - codeRedis.getCreateDate().getTime() > 3 * 60 * 1000) {
							result.setCode(ErrorValue.VERIFICATION_CODE_FAILURE.getStr());
							result.setResult("Verification code has expired");
							return result;
						} else if (!codeRedis.getCode().equals(verificationCode)) {
							result.setCode(ErrorValue.ERROR_VERIFICATION_CODE.getStr());
							result.setResult("Incorrect verification code");
							return result;
						}
						// 验证成功销毁验证码
						redisService.hdelObject(Constants.REDIS_MOBILE_VERIFICATION_CODE, mobile);
					} else {
						result.setCode("-6");
						result.setResult("并未发送验证码！");
						return result;
					}
				}
				VOConversionUtil.Entity2VO(buyer, new String[] { "memberId", "name", "mobile", "token" }, null);
				result.setData(buyer);
				return result;
			}
		}
	}

	/**
	 * 注册
	 * 
	 * @param rcMemberInfo
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	// @RequestMapping(value = "register")
	// @ResponseBody
	// public String register(RcMember rcMemberInfo, Integer roleId) throws
	// NoSuchAlgorithmException {
	// try {
	// rcMemberInfo.setPassword(DigestUtils.md5Hex(rcMemberInfo.getPassword()));
	// /*
	// * rcMemberInfo.setMemberId(serialGeneratorService
	// * .getSerialKey("memberIdSerial"));
	// */
	//
	// /*
	// * memberRole.setId(serialGeneratorService
	// * .getSerialKey("memberRoleIdSerial"));
	// */
	// /*
	// * RcMemberWallet rcMemberWallet=new RcMemberWallet();
	// * rcMemberWallet.setMemberId(rcMemberInfo.getMemberId());
	// */
	// Integer memberId = memberService.addMember(rcMemberInfo);
	// RcMemberRole memberRole = new RcMemberRole();
	// memberRole.setMemberId(memberId);
	// memberRole.setRoleId(roleId);
	// memberRoleService.addMemberRole(memberRole);
	// } catch (Exception e) {
	// return "{\"code\":\"0\",\"result\":\"fail.\"}";
	// }
	// return "{\"code\":\"1\",\"result\":\"Done.\"}";
	//
	// }

	/**
	 * 异步检验验证码是否ok
	 */
	@RequestMapping(value = "validateCode")
	@ResponseBody
	public boolean validateCode(String verificationCode, HttpServletRequest httpServletRequest) {
		if (verificationCode.equals(httpServletRequest.getSession().getAttribute("piccode"))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证登录
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@RequestMapping(value = "login")
	@ResponseBody
	public String login(String account, String password, String verificationCode, HttpServletRequest httpServletRequest)
			throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
		if (!verificationCode.equals(httpServletRequest.getSession().getAttribute("piccode"))) {
			return "{\"code\":\"0\",\"result\":\"verification code is error!\"}";
		}
		String md5Password = DigestUtils.md5Hex(password);
		RcUser user = userService.findUserByAccount(account);
		if (user == null) {
			RcMerchantUser merchantUser = merchantUserService.findMerchantUserByAccount(account);
			if (merchantUser == null) {
				RcVendorUser vendorUser = vendorUserService.findVendorUserByAccount(account);
				if (vendorUser == null) {
					return "{\"code\":\"0\",\"result\":\"password or account error!\"}";
				} else {
					if(vendorUser.getDelFlag() == YesOrNoEnum.YES.getValue()) {
						return "{\"code\":\"0\",\"result\":\"account disabled!\"}";
					}
					if (vendorUser.getPassword().equals(md5Password)) {
						// 存入session
						httpServletRequest.getSession().setAttribute(Constants.VENDOR_USER_ID_IN_SESSION_KEY,
								vendorUser.getMemberId());
						httpServletRequest.getSession().setAttribute(Constants.MEMBER_INFO_IN_SESSION_KEY,
								vendorUser);
						httpServletRequest.getSession().setAttribute(Constants.VENDOR_INFO_IN_SESSION_KEY,
								vendorService.findVendorByVendorUserId(vendorUser.getMemberId()));
						// 跟新登录时间
						vendorUser.setLastLoginTime(new Date());
						vendorUserService.modifyVendorUser(vendorUser);
						List<RcRole> roles = Arrays.asList(roleService.findRoleById(RoleIdEnum.PRINCIPAL_MANAGER.getValue()));
						if (!CollectionUtils.isEmpty(roles)) {
							vendorUser.setRoles(roles);
							// 当前角色包含菜单
							vendorUser.setMenus(menuService.findMenusByRoleId(roles.get(0).getRoleId()));
						}
						Result<RcVendorUser> result = new Result<RcVendorUser>();
						result.setData(vendorUser);

						return JsonUtil.toJson(result);
					} else {
						return "{\"code\":\"0\",\"result\":\"password or account error!\"}";
					}
				}
			} else {
				if(merchantUser.getDelFlag() == YesOrNoEnum.YES.getValue()) {
					return "{\"code\":\"0\",\"result\":\"account disabled!\"}";
				}
				if (merchantUser.getPassword().equals(md5Password)) {
					// 存入session
					httpServletRequest.getSession().setAttribute(Constants.MERCHANT_USER_ID_IN_SESSION_KEY,
							merchantUser.getMemberId());
					httpServletRequest.getSession().setAttribute(Constants.MEMBER_INFO_IN_SESSION_KEY,
							merchantUser);
					httpServletRequest.getSession().setAttribute(Constants.MERCHANT_INFO_IN_SESSION_KEY,
							merchantService.findMerchantByMerchantUserId(merchantUser.getMemberId()));
					// 跟新登录时间
					merchantUser.setLastLoginTime(new Date());
					merchantUserService.modifyMerchantUser(merchantUser);
					List<RcRole> roles = Arrays.asList(roleService.findRoleById(RoleIdEnum.STORE_SELLER.getValue()));
					if (!CollectionUtils.isEmpty(roles)) {
						merchantUser.setRoles(roles);
						// 当前角色包含菜单
						merchantUser.setMenus(menuService.findMenusByRoleId(roles.get(0).getRoleId()));
					}
					Result<RcMerchantUser> result = new Result<RcMerchantUser>();
					result.setData(merchantUser);

					return JsonUtil.toJson(result);
				} else {
					return "{\"code\":\"0\",\"result\":\"password or account error!\"}";
				}
			}
		} else {
			if(user.getDelFlag() == YesOrNoEnum.YES.getValue()) {
				return "{\"code\":\"0\",\"result\":\"account disabled!\"}";
			}
			if (user.getPassword().equals(md5Password)) {
				// 存入session
				httpServletRequest.getSession().setAttribute(Constants.USER_ID_IN_SESSION_KEY,
						user.getMemberId());
				httpServletRequest.getSession().setAttribute(Constants.MEMBER_INFO_IN_SESSION_KEY,
						user);
				// 跟新登录时间
				user.setLastLoginTime(new Date());
				userService.modifyUser(user);
				if (user.getIsSuper() == YesOrNoEnum.YES.getValue()) {
					user.setMenus(menuService.findAllMenus());
				} else {
					List<RcRole> roles = roleService.findRolesByUserId(user.getMemberId());
					if (!CollectionUtils.isEmpty(roles)) {
						user.setRoles(roles);
						// 当前角色包含菜单
						user.setMenus(menuService.findMenusByRoleId(roles.get(0).getRoleId()));
					}
				}
				Result<RcUser> result = new Result<RcUser>();
				result.setData(user);

				return JsonUtil.toJson(result);
			} else {
				return "{\"code\":\"0\",\"result\":\"password or account error!\"}";
			}
		}
	}
	
	@RequestMapping(value = "merchantlogin")
	@ResponseBody
	public String merchantLogin(String account, String password, String verificationCode, HttpServletRequest httpServletRequest)
			throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
		if (!verificationCode.equals(httpServletRequest.getSession().getAttribute("piccode"))) {
			return "{\"code\":\"0\",\"result\":\"verification code is error!\"}";
		}
		String md5Password = DigestUtils.md5Hex(password);
		RcMerchantUser merchantUser = merchantUserService.findMerchantUserByAccount(account);
		if (merchantUser == null) {
			return "{\"code\":\"0\",\"result\":\"password or account error!\"}";
		} else {
			if (merchantUser.getPassword().equals(md5Password)) {
				// 存入session
				httpServletRequest.getSession().setAttribute(Constants.MERCHANT_USER_ID_IN_SESSION_KEY,
						merchantUser.getMemberId());
				httpServletRequest.getSession().setAttribute(Constants.MEMBER_INFO_IN_SESSION_KEY,
						merchantUser);
				httpServletRequest.getSession().setAttribute(Constants.MERCHANT_INFO_IN_SESSION_KEY,
						merchantService.findMerchantByMerchantUserId(merchantUser.getMemberId()));
				// 跟新登录时间
				merchantUser.setLastLoginTime(new Date());
				merchantUserService.modifyMerchantUser(merchantUser);
				List<RcRole> roles = Arrays.asList(roleService.findRoleById(RoleIdEnum.STORE_SELLER.getValue()));
				if (!CollectionUtils.isEmpty(roles)) {
					merchantUser.setRoles(roles);
					// 当前角色包含菜单
					merchantUser.setMenus(menuService.findMenusByRoleId(roles.get(0).getRoleId()));
				}
				Result<RcMerchantUser> result = new Result<RcMerchantUser>();
				result.setData(merchantUser);

				return JsonUtil.toJson(result);
			} else {
				return "{\"code\":\"0\",\"result\":\"password or account error!\"}";
			}
		}
	}
	
	@RequestMapping(value = "vendorlogin")
	@ResponseBody
	public String vendorLogin(String account, String password, String verificationCode, HttpServletRequest httpServletRequest)
			throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
		if (!verificationCode.equals(httpServletRequest.getSession().getAttribute("piccode"))) {
			return "{\"code\":\"0\",\"result\":\"verification code is error!\"}";
		}
		String md5Password = DigestUtils.md5Hex(password);
		RcVendorUser vendorUser = vendorUserService.findVendorUserByAccount(account);
		if (vendorUser == null) {
			return "{\"code\":\"0\",\"result\":\"password or account error!\"}";
		} else {
			if (vendorUser.getPassword().equals(md5Password)) {
				// 存入session
				httpServletRequest.getSession().setAttribute(Constants.VENDOR_USER_ID_IN_SESSION_KEY,
						vendorUser.getMemberId());
				httpServletRequest.getSession().setAttribute(Constants.MEMBER_INFO_IN_SESSION_KEY,
						vendorUser);
				httpServletRequest.getSession().setAttribute(Constants.VENDOR_INFO_IN_SESSION_KEY,
						vendorService.findVendorByVendorUserId(vendorUser.getMemberId()));
				// 跟新登录时间
				vendorUser.setLastLoginTime(new Date());
				vendorUserService.modifyVendorUser(vendorUser);
				List<RcRole> roles = Arrays.asList(roleService.findRoleById(RoleIdEnum.PRINCIPAL_MANAGER.getValue()));
				if (!CollectionUtils.isEmpty(roles)) {
					vendorUser.setRoles(roles);
					// 当前角色包含菜单
					vendorUser.setMenus(menuService.findMenusByRoleId(roles.get(0).getRoleId()));
				}
				Result<RcVendorUser> result = new Result<RcVendorUser>();
				result.setData(vendorUser);

				return JsonUtil.toJson(result);
			} else {
				return "{\"code\":\"0\",\"result\":\"password or account error!\"}";
			}
		}
	}

//	/**
//	 * 验证用户名
//	 * 
//	 * @param memberAccount
//	 * @return
//	 */
//	@RequestMapping(value = "validateAccount")
//	@ResponseBody
//	public String validateAccount(RcMember rcMember) {
//		RcMember member = memberService.findMember(rcMember);
//		if (member != null) {
//			return "{\"code\":\"0\"}";
//		}
//		return "{\"code\":\"1\"}";
//	}

	/**
	 * 获取头像
	 */
	@RequestMapping("getmyhead")
	@ResponseBody
	public Result<RcBuyer> getMyhead(String data, String timestamp, String nonceStr, String product, String signature,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		Result<RcBuyer> result = new Result<RcBuyer>();
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}
		int memberId = json.getInt("memberId");
		RcBuyer buyer = buyerService.findBuyerById(memberId);
		VOConversionUtil.Entity2VO(buyer, new String[] { "imgUrl" }, null);
		result.setData(buyer);
		return result;
	}

	/**
	 * 手机上传头像
	 * 
	 * @param data
	 * @param timestamp
	 * @param nonceStr
	 * @param product
	 * @param signature
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@RequestMapping("myhead")
	@ResponseBody
	public String myhead(String data, String timestamp, String nonceStr, String product, String signature,
			HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		int buyerId = json.getInt("memberId");

		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multipartRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile multipartFile = multipartRequest.getFile(iter.next());
				if (multipartFile != null) {
					String fileName = multipartFile.getOriginalFilename();
					if (StringUtils.isNotBlank(fileName)) {
						if (multipartFile.getSize() > 5 * 1024 * 1024) {
							return "{\"code\":\"-1\",\"result\":\"图片太大！\"}";
						} else if (multipartFile.getSize() == 0) {
							return "{\"code\":\"-1\",\"result\":\"未获取到图片尺寸！\"}";
						} else {
							String fileInfoId = UUID.randomUUID().toString().replaceAll("-", "");
							int pos = fileName.lastIndexOf(".");
							String originalName = fileName.substring(0, pos);
							String suffixName = fileName.substring(pos + 1, fileName.length());
							Pattern pattern = Pattern.compile("(png|PNG|GIF|gif|jpg|JPG|bmp|BMP)");
							Matcher matcher = pattern.matcher(suffixName);
							if (!matcher.matches()) {
								return "{\"code\":\"0\",\"result\":\"图片格式不正确（必须为.jpg/.gif/.bmp/.png图片）！\"}";
							}
							String realFileName = fileInfoId;
							Calendar cal = Calendar.getInstance();
							int year = cal.get(Calendar.YEAR);
							int month = cal.get(Calendar.MONTH) + 1;
							int day = cal.get(Calendar.DATE);
							String relativePath = year + File.separator + month + File.separator + day
									+ File.separator;
							String dirPath = propertyConstants.systemFileServerRoot + relativePath;
							File dir = new File(dirPath);
							if (!dir.exists()) {
								dir.mkdirs();
							}
							File file = new File(dirPath + realFileName + "." + suffixName);
							multipartFile.transferTo(file);
							RcFileInfo fileInfo = new RcFileInfo();
							fileInfo.setFileInfoId(fileInfoId);
							fileInfo.setOriginalName(originalName);
							fileInfo.setRealName(realFileName);
							fileInfo.setSuffixName(suffixName);
							fileInfo.setSize(multipartFile.getSize());
							fileInfo.setRelativePath(relativePath);
							fileInfo.setMemberId(buyerId);
							fileInfoService.addFileInfo(fileInfo);

							RcBuyer buyer = buyerService.findBuyerById(buyerId);
							buyer.setImgUrl(propertyConstants.systemFileServerUrl + fileInfo.getPath());
							buyerService.modifyBuyer(buyer);
							return "{\"code\":\"1\",\"result\":\"上传成功！\"}";
						}
					}
				}
			}
		}
		return "{\"code\":\"-1\",\"result\":\"未获取到图片！\"}";
	}

	/**
	 * 获取地址
	 * 
	 * @param data
	 * @param timestamp
	 * @param nonceStr
	 * @param product
	 * @param signature
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("myconsignee")
	@ResponseBody
	public Result<List<RcBuyerConsignee>> myconsignee(String data, String timestamp, String nonceStr, String product,
			String signature, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		Result<List<RcBuyerConsignee>> result = new Result<List<RcBuyerConsignee>>();
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}
		int buyerId = json.getInt("memberId");
		List<RcBuyerConsignee> consignees = buyerConsigneeService.findBuyerConsigneesByBuyerIdForAndroid(buyerId);
		result.setData(consignees);
		return result;
	}

	/**
	 * 修改昵称
	 * 
	 * @param data
	 * @param timestamp
	 * @param nonceStr
	 * @param product
	 * @param signature
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("mynickname")
	@ResponseBody
	public String myNickname(String data, String timestamp, String nonceStr, String product, String signature,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		int buyerId = json.getInt("memberId");
		String nickName = json.getString("name");
		RcBuyer buyer = buyerService.findBuyerById(buyerId);
		buyer.setName(nickName);
		buyerService.modifyBuyer(buyer);
		return "{\"code\":\"1\",\"result\":\"昵称修改成功！\"}";
	}

	/**
	 * 设置默认地址
	 * 
	 * @param data
	 * @param timestamp
	 * @param nonceStr
	 * @param product
	 * @param signature
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("settodefault")
	@ResponseBody
	public String setToDefault(String data, String timestamp, String nonceStr, String product, String signature,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		int buyerConsigneeId = json.getInt("consigneeId");
		buyerConsigneeService.setDefault(buyerConsigneeId);
		return "{\"code\":\"1\",\"result\":\"默认地址修改成功！\"}";

	}

	/**
	 * 获取citys
	 * 
	 * @param data
	 * @param timestamp
	 * @param nonceStr
	 * @param product
	 * @param signature
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getCity")
	@ResponseBody
	public Result<List<RcCity>> getCity(String data, String timestamp, String nonceStr, String product,
			String signature, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		Result<List<RcCity>> result = new Result<List<RcCity>>();
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}
		List<RcCity> cities = cityService.findAllCities();
		for (RcCity city : cities) {
			VOConversionUtil.Entity2VO(city, new String[] { "cityId", "name", "provinceName", "isDredged" }, null);
		}
		result.setData(cities);
		return result;
	}

	@RequestMapping("citys")
	@ResponseBody
	public List<RcCity> citys(String provinceName) {
		List<RcCity> cities = cityService.findCitiesByProvinceName(provinceName);
		return cities;
	}

	@RequestMapping("provinces")
	@ResponseBody
	public List<String> provinces() {
		List<String> provinces = cityService.findProvinces();
		return provinces;
	}

	/**
	 * 修改绑定手机号
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SystemException 
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */

	@RequestMapping("myphone")
	@ResponseBody
	public Result<String> myPhone(String data, String timestamp, String nonceStr, String product, String signature,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException, SystemException {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		Result<String> result = new Result<String>();
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}
		int buyerId = json.getInt("memberId");
		RcBuyer buyer = buyerService.findBuyerById(buyerId);
		String mobile = json.getString("newMobile");
		String code = json.getString("code");
		String string = propertyConstants.whiteListMobile;
		String[] whiteList = string.split(",");
		int status = 0;
		for(int i = 0; i < whiteList.length; i++){
			if(buyer.getMobile().equals(whiteList[i])){
				status = 1;
				break;
			}
		}
		if(status == 0){
			// 验证码逻辑
			Code codeRedis = (Code) redisService.hgetObject(Constants.REDIS_MOBILE_VERIFICATION_CODE, mobile);
			if (codeRedis != null) {
				if (new Date().getTime() - codeRedis.getCreateDate().getTime() > 3 * 60 * 1000) {
					result.setCode(ErrorValue.VERIFICATION_CODE_FAILURE.getStr());
					result.setResult("Verification code has expired");
					return result;
				} else if (!codeRedis.getCode().equals(code)) {
					result.setCode(ErrorValue.ERROR_VERIFICATION_CODE.getStr());
					result.setResult("Incorrect verification code");
					return result;
				}
				// 验证成功销毁验证码
				redisService.hdelObject(Constants.REDIS_MOBILE_VERIFICATION_CODE, mobile);
			}
		}

		RcMember member = memberService.findMemberByMobile(mobile);
		if (member != null && member.getMemberId() != buyerId) {
			result.setCode(ErrorValue.ACCOUNT_ALREADY_EXISTS.getStr());
			result.setResult("手机号已经被使用！");
			return result;
		}

		buyer.setMobile(mobile);
		buyerService.modifyBuyer(buyer);
		return result;
	}

	/**
	 * 添加或者修改收货地址
	 * 
	 * @param data
	 * @param timestamp
	 * @param nonceStr
	 * @param product
	 * @param signature
	 * @param request
	 * @param response
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */

	@RequestMapping("addconsignee")
	@ResponseBody
	public Result<RcBuyerConsignee> addConsignee(String data, String timestamp, String nonceStr, String product,
			String signature, HttpServletRequest request, HttpServletResponse response)
			throws IllegalAccessException, InvocationTargetException {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		Result<RcBuyerConsignee> result = new Result<RcBuyerConsignee>();
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}
		int buyerId = json.getInt("memberId");
		Integer buyerConsigneeId = null;
		try {
			buyerConsigneeId = json.getInt("consigneeId");
		} catch (Exception e) {
		}
		String consigneeName = json.getString("name");
		String mobile = json.getString("mobile");
		String consigneeDetail = json.getString("address");
		int cityId = json.getInt("cityId");
		RcBuyerConsignee buyerConsignee = null;
		if(buyerConsigneeId == null) {
			buyerConsignee = new RcBuyerConsignee();
		} else {
			buyerConsignee = buyerConsigneeService.findBuyerConsigneeById(buyerConsigneeId);
		}
		buyerConsignee.setMemberId(buyerId);
		buyerConsignee.setName(consigneeName);
		buyerConsignee.setCityId(cityId);
		buyerConsignee.setAddress(consigneeDetail);
		buyerConsignee.setMobile(mobile);

	/*	consignee.setIsDefault((byte) 0);
		if (consigneeId == null) {
			List<RcConsignee> list = consigneeService.findConsigneesByMemberId(memberId);
			if (CollectionUtils.isEmpty(list)) {
				consignee.setIsDefault(YesOrNoEnum.YES.getValue());
			}
			consigneeService.addConsignee(consignee);
		} else {
			consignee.setConsigneeId(consigneeId);
			consigneeService.modifyConsignee(consignee);
		}*/

		buyerConsigneeService.saveBuyerConsignee(buyerConsignee);

		RcCity city = cityService.findCityById(buyerConsignee.getCityId());
		buyerConsignee.setCityName(city.getName());
		buyerConsignee.setProvinceName(city.getProvinceName());
		VOConversionUtil.Entity2VO(buyerConsignee, null, new String[] { "createTime", "updateTime", "memberId"});
		result.setData(buyerConsignee);
		return result;
	}

	/**
	 * 删除地址
	 * 
	 * @param data
	 * @param timestamp
	 * @param nonceStr
	 * @param product
	 * @param signature
	 * @param request
	 * @param response
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("deleteConsignee")
	@ResponseBody
	public Result<RcBuyerConsignee> deleteConsignee(String data, String timestamp, String nonceStr, String product,
			String signature, HttpServletRequest request, HttpServletResponse response)
			throws IllegalAccessException, InvocationTargetException {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		Result<RcBuyerConsignee> result = new Result<RcBuyerConsignee>();
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}
		JSONArray consigneeIdArray = json.getJSONArray("consigneeIds");

		@SuppressWarnings("unchecked")
		Collection<String> consigneeIds = (Collection<String>) JSONArray.toCollection(consigneeIdArray);
		for (String consigneeId : consigneeIds) {
			buyerConsigneeService.removeBuyerConsignee(Integer.valueOf(consigneeId));
		}

		return result;
	}

	/**
	 * 查询钱包
	 * 
	 * @param data
	 * @param timestamp
	 * @param nonceStr
	 * @param product
	 * @param signature
	 * @param request
	 * @param response
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@RequestMapping("mywallet")
	@ResponseBody
	public Result<RcBuyer> myWallet(String data, String timestamp, String nonceStr, String product, String signature,
			HttpServletRequest request, HttpServletResponse response)
			throws IllegalAccessException, InvocationTargetException {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		Result<RcBuyer> result = new Result<RcBuyer>();
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}
		int buyerId = json.getInt("memberId");
		RcBuyer buyer = buyerService.findBuyerById(buyerId);
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(8);
		page.setCurrentPage(1);
		List<RcBuyerTrade> buyerTrades = buyerTradeService.findPagedBuyerTradesByBuyerIdForAndroid(buyerId, page);
		VOConversionUtil.Entity2VO(buyer, new String[] { "walletAmount" }, null);
		buyer.setMemberTrades(buyerTrades);
		result.setData(buyer);
		return result;
	}

	/**
	 * 查询消费记录
	 */

	@RequestMapping("mytrade")
	@ResponseBody
	public Result<List<RcBuyerTrade>> myTrade(String data, String timestamp, String nonceStr, String product,
			String signature, HttpServletRequest request, HttpServletResponse response) {
		Result<List<RcBuyerTrade>> result = new Result<List<RcBuyerTrade>>();
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}
		int buyerId = json.getInt("memberId");
		Integer page = null;
		try {
			page = json.getInt("currentPage");
		} catch (Exception e) {
			page = 1;
		}
		Page pager = new Page();
		// 初始化时第一页
		pager.setPagination(true);
		pager.setPageSize(15);
		pager.setCurrentPage(page);
		List<RcBuyerTrade> buyerTrades = buyerTradeService.findPagedBuyerTradesByBuyerIdForAndroid(buyerId, pager);
		
		result.setData(buyerTrades);
		return result;
	}

	/**
	 * 获取订单数量
	 * 
	 * @throws IOException
	 */
	@RequestMapping("getordernum")
	@ResponseBody
	public Result<OrderNumVO> getOrderNum(String data, String timestamp, String nonceStr, String product,
			String signature, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Result<OrderNumVO> result = new Result<OrderNumVO>();
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
		}
		int buyerId = json.getInt("memberId");
		OrderNumVO orderNum = orderService.findOrderNumByBuyerId(buyerId);
		result.setData(orderNum);
		return result;
	}

	/**
	 * 获取验证码
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "code")
	public void code(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BufferedImage bi = new BufferedImage(80, 40, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		Color c = new Color(255, 255, 255);
		g.setColor(c);
		g.fillRect(0, 0, 80, 40);
		String randomAlphanumeric = RandomStringUtils.randomNumeric(4);

		Random r = new Random();
		for (int i = 0; i < 4; i++) {
			g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt(255)));
			Font font = new Font("楷体", Font.BOLD, 20);
			g.setFont(font);
			g.drawString(randomAlphanumeric.charAt(i) + "", (i * 18) + 10, 28);
			g.drawLine(r.nextInt(80), r.nextInt(40), r.nextInt(80), r.nextInt(40));
			g.drawLine(r.nextInt(80), r.nextInt(40), r.nextInt(80), r.nextInt(40));
		}
		request.getSession().setAttribute("piccode", randomAlphanumeric.toString());
		ImageIO.write(bi, "JPG", response.getOutputStream());
		IOUtils.closeQuietly(response.getOutputStream());
	}

//	@RequestMapping(value = "/flashlogintime")
//	@ResponseBody
//	public String flashLoginTime(String data, String timestamp, String nonceStr, String product, String signature)
//			throws Exception {
//		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
//		if (json == null) {
//			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
//		}
//		int memberId = json.getInt("memberId");
//		RcMember member = buyerService.findMemberByMemberId(memberId);
//		if (member == null) {
//			return "{\"code\":\"0\",\"result\":\"fail!\"}";
//		}
//		member.setLastLoginTime(new Date());
//		buyerService.modifyMember(member);
//		return "{\"code\":\"1\",\"result\":\"success!\"}";
//	}

	/**
	 * 退出登录
	 * 
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/logout")
	public void logout(Model model, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest)
			throws IOException {
		httpServletRequest.getSession().invalidate();
		httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login.jsp");
	}
	
	@RequestMapping(value = "/merchantlogout")
	public void merchantLogout(Model model, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest)
			throws IOException {
		httpServletRequest.getSession().invalidate();
		httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/m_login.jsp");
	}
	
	@RequestMapping(value = "/vendorlogout")
	public void vendorLogout(Model model, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest)
			throws IOException {
		httpServletRequest.getSession().invalidate();
		httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/v_login.jsp");
	}
	
	@RequestMapping(value = "infolist")
	@ResponseBody
	public String infolist(String data, String timestamp, String nonceStr, String product, String signature,
			HttpServletRequest httpServletRequest){
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		JSONArray idArray = json.getJSONArray("ids");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for(Object id : idArray){
			Map<String, String> map = new HashMap<String, String>();
			map.put("huanxinId", id.toString());
			String[] param = id.toString().split("_");
			if(propertyConstants.huanxinBuyerAccountPrefix.equals(param[0])){
				RcBuyer buyer = buyerService.findBuyerById(Integer.parseInt(param[1]));
				map.put("nickName", buyer.getName());
				map.put("userIcon", buyer.getImgUrl());
			} else if(propertyConstants.huanxinSellerAccountPrefix.equals(param[0])){
				RcMerchantUser merchantUser = merchantUserService.findMerchantUserById(Integer.parseInt(param[2]));
				RcMerchant merchant = merchantService.findMerchantById(merchantUser.getMerchantId());
				map.put("nickName", merchant.getName());
				map.put("userIcon", merchant.getLogoUrl());
			}
			list.add(map);
		}
		return "{\"code\":\"1\",\"result\":\"success\",\"data\":"+JsonUtil.toJson(list)+"}";
	}
	
	@RequestMapping(value = "/test")
	@ResponseBody
	public void test() throws Exception{
		RcBuyer param = new RcBuyer();
		List<RcBuyer> list = buyerService.findBuyers(param);
		for(RcBuyer member:list){
			imService.regist(propertyConstants.huanxinBuyerAccountPrefix+"_"+member.getMemberId(), member.getName());
		}
	}


	private Logger log = Logger.getLogger(getClass());
    /**
     * 买家,卖家端绑定个推cid
     * @return
     */
    @RequestMapping(value = "/getui/bind")
    @ResponseBody
    public Result<Object> userGetuiBind(String data, String timestamp,String nonceStr,
                                String product, String signature) throws SystemException {
        JSONObject json = parseParam(data, timestamp, nonceStr, product,signature);
//        JSONObject json = JSONObject.fromObject(data);
        if (json == null) {
//            return new Result<Object>("-1", "token验证失败！");
            throw new SystemException(ErrorCode.PARAMETER_ERROR, "token error");
        }
        log.info("bind->json: " + json.toString());

        Integer memberId = Integer.valueOf(json.getString("memberId"));
        String cid = json.getString("cid");
        log.info("cid: " + cid + "  memberId: " + memberId);

        // 商户-个推cid绑定
        userGetuiService.userBind(memberId, cid);
        return new Result<Object>();
    }

}
