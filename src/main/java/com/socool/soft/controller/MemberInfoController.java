package com.socool.soft.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.socool.soft.bo.RcBuyer;
import com.socool.soft.bo.RcBuyerTrade;
import com.socool.soft.bo.RcFileInfo;
import com.socool.soft.bo.RcOrder;
import com.socool.soft.bo.RcRechargeRecord;
import com.socool.soft.bo.RcRechargeStation;
import com.socool.soft.bo.RcUser;
import com.socool.soft.bo.constant.BuyerTradeTypeEnum;
import com.socool.soft.bo.constant.PaymentInterfaceEnum;
import com.socool.soft.bo.constant.PaymentTypeEnum;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.service.IBuyerService;
import com.socool.soft.service.IBuyerTradeService;
import com.socool.soft.service.ICityService;
import com.socool.soft.service.IFileInfoService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IMerchantUserService;
import com.socool.soft.service.IOrderService;
import com.socool.soft.service.IRechargeRecordService;
import com.socool.soft.service.IRechargeStationService;
import com.socool.soft.service.IUserService;
import com.socool.soft.service.IVendorUserService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.Result;

import net.sf.json.JSONObject;

@RequestMapping(value = "memberinfo")
@Controller
public class MemberInfoController extends BaseController {
	@Autowired
	private IBuyerService buyerService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IMerchantUserService merchantUserService;
	@Autowired
	private IVendorUserService vendorUserService;
	@Autowired
	private IBuyerTradeService buyerTradeService;

	@Autowired
	private IRechargeStationService rechargeStationService;

	@Autowired
	private IRechargeRecordService rechargeRecordService;

	@Autowired
	private ICityService cityService;

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IFileInfoService fileInfoService;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private PropertyConstants propertyConstants;

	/**
	 * 删除充装站
	 * 
	 * @param stationIds
	 * @return
	 */
	@RequestMapping(value = "removeStations")
	@ResponseBody
	public Result<String> removeStations(String rechargeStationIds) {
		Result<String> result = new Result<String>();
		String[] rechargeStationIdArray = StringUtils.split(rechargeStationIds, ",");
		for (String rechargeStationId : rechargeStationIdArray) {
			rechargeStationService.removeRechargeStation(Integer.parseInt(rechargeStationId));
		}
		result.setResult("Done!");
		return result;
	}

	/**
	 * 充装站列表
	 * 
	 * @param request
	 * @return
	 */

	@RequestMapping("rechargeStationList")
	public String rechargeStationList(HttpServletRequest request, Model model, Integer currentPage,
			String stationName,String cityName) {
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcRechargeStation> rechargeStations = rechargeStationService.findPagedRechargeStations(stationName, cityName, page);
		for (RcRechargeStation rechargeStation : rechargeStations) {
			rechargeStation.setMember(userService.findUserById(rechargeStation.getMemberId()));
			rechargeStation.setCity(cityService.findCityById(rechargeStation.getCityId()));
		}
		List<String> provinces = cityService.findProvinces();
		model.addAttribute("provinceList", provinces);
		model.addAttribute("list", rechargeStations);
		if (flag) {
			return "rechargeStationList";
		} else {
			return "rechargeStationList_inner";
		}
	}

	/**
	 * 获取站点操作人员
	 * 
	 * @param request
	 * @return
	 */

	@RequestMapping("getOperator")
	@ResponseBody
	public List<RcUser> getOperator(HttpServletRequest request, Integer rechargeStationId) {
		// 当前登录人员
		List<RcUser> rechargeOperators = userService.findUnboundRechargeOperators(rechargeStationId);
		return rechargeOperators;
	}

	/**
	 * 通过城市获取充值站
	 * 
	 * @param data
	 * @param timestamp
	 * @param nonceStr
	 * @param product
	 * @param signature
	 * @param httpServletRequest
	 * @param response
	 * @return
	 */

	@RequestMapping("rechargestationbycity")
	@ResponseBody
	public Result<List<RcRechargeStation>> rechargeStationByCity(String data, String timestamp, String nonceStr,
			String product, String signature, HttpServletRequest httpServletRequest, HttpServletResponse response) {
		Result<List<RcRechargeStation>> result = new Result<List<RcRechargeStation>>();
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
		}
		int cityId = json.getInt("cityId");
		List<RcRechargeStation> rechargeStations = rechargeStationService.findRechargeStationsByCityId(cityId);
		result.setData(rechargeStations);
		return result;

	}

	@RequestMapping("saveRechargeStation")
	@ResponseBody
	public Result<String> saveRechargeStation(RcRechargeStation rechargeStation) {
		Result<String> result = new Result<String>();
		if (rechargeStation.getRechargeStationId() == null) {
			rechargeStationService.addRechargeStation(rechargeStation);
		} else {
			rechargeStationService.modifyRechargeStation(rechargeStation);
		}
		result.setResult("Done.");
		return result;
	}

	@RequestMapping(value = { "validateMobile" })
	@ResponseBody
	public Result<RcBuyer> validateMobile(RcBuyer memberParam) {
		Result<RcBuyer> result = new Result<RcBuyer>();
		if (memberParam.getMemberId() == null && StringUtils.isBlank(memberParam.getMobile())) {
			result.setResult("-1");
			return result;
		}
		memberParam.setDelFlag(YesOrNoEnum.NO.getValue());
		if(memberParam.getMemberId() != null) {
			RcBuyer buyer = buyerService.findBuyerById(memberParam.getMemberId());
			if (buyer == null) {
				result.setCode("-1");
			}
			result.setData(buyer);
		} else if(StringUtils.isNotBlank(memberParam.getMobile())) {
			RcBuyer buyer = buyerService.findBuyerByMobile(memberParam.getMobile());
			if (buyer == null) {
				result.setCode("-1");
			}
			result.setData(buyer);
		}
		return result;
	}

	/**
	 * 查询买家信息
	 * 
	 * @param request
	 * @param model
	 * @param currentPage
	 * @param memberParam
	 * @param isSuccess
	 * @param upDown
	 * @return
	 */
	@RequestMapping(value = { "member_list" })
	public String memberList(HttpServletRequest request, Model model, Integer currentPage, RcBuyer member,Integer signUpDesc,Integer lastLoginDesc) {
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		if(lastLoginDesc !=null && lastLoginDesc==0){
			if(signUpDesc == 1){
				member.setOrderBy("SIGN_UP_TIME DESC");
			} else if(signUpDesc == 2){
				member.setOrderBy("SIGN_UP_TIME ASC");
			}
		} else if(lastLoginDesc !=null && lastLoginDesc!=0){
			if(lastLoginDesc == 1){
				member.setOrderBy("LAST_LOGIN_TIME DESC");
			} else if(lastLoginDesc == 2){
				member.setOrderBy("LAST_LOGIN_TIME ASC");
			}
		} else{
			member.setOrderBy("BUYER_ID DESC");
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(6);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcBuyer> buyers = buyerService.findPagedBuyers(member, page);
		model.addAttribute("list", buyers);
		model.addAttribute("rcMemberInfo", member);

		if (flag) {
			return "memberList";
		} else {
			return "memberList_inner";
		}
	}

	/**
	 * 删除用户
	 * 
	 * @param rcMember
	 * @return
	 */
	@RequestMapping("deleteMember")
	@ResponseBody
	public Result<String> deleteMember(int memberId) {
		Result<String> result = new Result<String>();
		userService.removeUser(memberId);
		merchantUserService.removeMerchantUser(memberId);
		vendorUserService.removeVendorUser(memberId);
		return result;
	}

	@RequestMapping(value = "uploadcover")
	public String uploadCover(HttpServletRequest request, Model model, int memberId) throws IllegalStateException, IOException {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if(multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
            Iterator<String> iter = multipartRequest.getFileNames();
            while(iter.hasNext()) {
                MultipartFile multipartFile = multipartRequest.getFile(iter.next());
                if(multipartFile != null) {
                    String fileName = multipartFile.getOriginalFilename();
                    if(StringUtils.isNotBlank(fileName)) {
                    	String imgUrl = "";
                    	if(multipartFile.getSize() > 5 * 1024 * 1024){
                			imgUrl = "001";
                			model.addAttribute("success",false);
                		} else if(multipartFile.getSize() == 0){
                			imgUrl = "002";
                			model.addAttribute("success",false);
                		} else {
                			String fileInfoId = UUID.randomUUID().toString().replaceAll("-", "");
                			int pos = fileName.lastIndexOf(".");
                			String originalName = fileName.substring(0, pos);
                			String suffixName = fileName.substring(pos + 1, fileName.length());
                			Pattern pattern = Pattern.compile("(png|PNG|GIF|gif|jpg|JPG|bmp|BMP)");
                			Matcher matcher = pattern.matcher(suffixName);
                			if(!matcher.matches()){
                				return "{\"code\":\"0\",\"result\":\"图片格式不正确（必须为.jpg/.gif/.bmp/.png图片）！\"}";
                			}
                			String realFileName = fileInfoId;
                			Calendar cal = Calendar.getInstance();
                			int year = cal.get(Calendar.YEAR);
                			int month = cal.get(Calendar.MONTH) + 1;
                			int day = cal.get(Calendar.DATE);
                			String relativePath = year + File.separator + month + File.separator + day + File.separator;
                			String dirPath = propertyConstants.systemFileServerRoot + relativePath;
                			File dir = new File(dirPath);
                			if(!dir.exists()) {
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
                			fileInfo.setMemberId(memberId);
                			fileInfoService.addFileInfo(fileInfo);
                			imgUrl = propertyConstants.systemFileServerUrl + fileInfo.getPath();
                			model.addAttribute("imgUrl", imgUrl);
                			model.addAttribute("fileid", realFileName);
                			model.addAttribute("success", true);
                		}
                    }
                }
            }
        }
		return "subpage";
	}

	@RequestMapping(value = "walletRecharge")
	public String memberWalletList(HttpServletRequest httpServletRequest, Model model) {
		int userId = getUserId(httpServletRequest);
		RcUser rechargeOperator = userService.findUserById(userId);
		model.addAttribute("operator", rechargeOperator);
		RcRechargeStation rechargeStation = rechargeStationService
				.findRechargeStationByMemberId(rechargeOperator.getMemberId());
		model.addAttribute("rcRechargeStation", rechargeStation);
		return "walletRecharge";
	}

	@RequestMapping(value = "rechargeIndex")
	public String rechargeIndex(HttpServletRequest httpServletRequest, Model model) {
		int userId = getUserId(httpServletRequest);
		RcUser rechargeOperator = userService.findUserById(userId);
		model.addAttribute("operator", rechargeOperator);
		RcRechargeStation rechargeStation = rechargeStationService
				.findRechargeStationByMemberId(rechargeOperator.getMemberId());
		model.addAttribute("rcRechargeStation", rechargeStation);
		return "rechargeIndex";
	}

	@RequestMapping(value = "rechargeWallet")
	@ResponseBody
	public Result<String> rechargeWallet(RcRechargeRecord rechargeRecord, HttpServletRequest request) {
		Result<String> result = new Result<String>();
		int userId = getUserId(request);
		RcRechargeStation rechargeStation = rechargeStationService
				.findRechargeStationByMemberId(userId);
		rechargeRecord.setRechargeMemberId(userId);
		rechargeRecord.setRechargeStationId(rechargeStation.getRechargeStationId());
//		if (iLockService.lock(redisTemplate, rechargeRecord.getMemberId())) {
//			try {
				RcBuyer buyer = buyerService.findBuyerById(rechargeRecord.getMemberId());
				buyer.setWalletAmount(buyer.getWalletAmount().add(rechargeRecord.getAmount()));
				RcBuyerTrade buyerTrade = new RcBuyerTrade();
				buyerTrade.setBuyerId(rechargeRecord.getMemberId());
				buyerTrade.setType(BuyerTradeTypeEnum.RECHARGE.getValue());
				buyerTrade.setPaymentType(PaymentTypeEnum.BALANCE.getValue());
				buyerTrade.setPaymentInterface(PaymentInterfaceEnum.WALLET.getValue());
				buyerTrade.setAmount(rechargeRecord.getAmount());
				buyerTrade.setBalance(buyer.getWalletAmount());
				buyerTradeService.addBuyerTrade(buyerTrade);
				buyerService.modifyBuyer(buyer);
				rechargeRecordService.addRechargeRecord(rechargeRecord);

				result.setResult("Done!");
				return result;
//			} finally {
//				iLockService.unlock(redisTemplate, rechargeRecord.getRechargeMemberId());
//			}
//		} else {
//			result.setCode("-1");
//			result.setResult("System is busy.");
//			return result;
//		}
	}

	@RequestMapping(value = "rechargeRecord")
	public String rechargeRecord() {
		return "rechargeRecord";
	}

	@RequestMapping(value = "rechargeRecordList")
	public String rechargeRecordList(HttpServletRequest request, Model model, Integer currentPage,
			RcRechargeRecord rechargeRecord) {
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		if(rechargeRecord.getCreateTimeTo() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(rechargeRecord.getCreateTimeTo());
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.SECOND, -1);
			rechargeRecord.setCreateTimeTo(cal.getTime());
		}
		// 分页
		Page page = new Page(); // 初始化时第一页 page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcRechargeRecord> rechargeRecords = rechargeRecordService.findPagedRechargeRecords(rechargeRecord, page);
		model.addAttribute("list", rechargeRecords);
		if (flag) {
			return "rechargeRecordList";
		} else {
			return "rechargeRecordList_inner";
		}
	}

	@RequestMapping("/orderList/{memberId}")
	public String orderList(Integer currentPage, Model model, @PathVariable Integer memberId)
			throws UnsupportedEncodingException {
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcOrder> orders = orderService.findPagedOrdersByBuyerId(memberId, page);
		for (RcOrder order : orders) {
			order.setMember(buyerService.findBuyerById(order.getMemberId()));
			order.setMerchant(merchantService.findMerchantById(order.getMerchantId()));
		}
		model.addAttribute("userId", memberId);
		model.addAttribute("list", orders);
		if (flag) {
			return "memberOrder";
		} else {
			return "memberOrder_inner";
		}
	}

	@RequestMapping(value = "/rechargeRecord/{memberId}")
	public String rechargeRecord(Integer currentPage, Model model, @PathVariable Integer memberId) {
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		// 分页
		Page page = new Page(); // 初始化时第一页
		page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcRechargeRecord> rechargeRecords = rechargeRecordService
				.findPagedRechargeRecordsByMemberId(memberId, page);
		for (RcRechargeRecord rechargeRecord : rechargeRecords) {
			rechargeRecord.setRechargeMember(userService.findUserById(rechargeRecord.getRechargeMemberId()));
			rechargeRecord.setRechargeStation(rechargeStationService.findRechargeStationByMemberId(rechargeRecord.getRechargeMemberId()));
		}
		
		model.addAttribute("list", rechargeRecords);
		if (flag) {
			return "memberRecharge";
		} else {
			return "memberRecharge_inner";
		}
	}
}
