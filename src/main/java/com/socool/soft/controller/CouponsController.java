package com.socool.soft.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socool.soft.bo.RcBuyerCoupon;
import com.socool.soft.bo.RcCoupon;
import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcMerchantUser;
import com.socool.soft.bo.RcProd;
import com.socool.soft.bo.RcProdCat;
import com.socool.soft.bo.constant.CouponStatusEnum;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.exception.ErrorValue;
import com.socool.soft.param.Coupon;
import com.socool.soft.service.IBuyerCouponService;
import com.socool.soft.service.ICouponService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IMerchantUserService;
import com.socool.soft.service.IProdCatService;
import com.socool.soft.service.IProdService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.Result;
import com.socool.soft.vo.constant.CouponBVOStatusEnum;
import com.socool.soft.vo.constant.CouponVOStatusEnum;
import com.socool.soft.vo.constant.CouponVOTypeEnum;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RequestMapping(value = "/coupons")
@Controller
public class CouponsController extends BaseController {

	@Autowired
	private ICouponService couponService;
	@Autowired
	private IBuyerCouponService buyerCouponService;
	@Autowired
	private IProdService prodService;
	@Autowired
	private IProdCatService prodCatService;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private IMerchantUserService merchantUserService;

	@RequestMapping(value = "getCouponsPage")
	@ResponseBody
	public Result<List<RcCoupon>> placingOrder(String data, String timestamp, String nonceStr, String product, String signature,
			HttpServletRequest request, HttpServletResponse response) throws ParseException {
		Result<List<RcCoupon>> result = new Result<List<RcCoupon>>();
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			result.setCode("-1");
			result.setResult("token fail");
			return result;
		}

		int currentPage = json.getInt("currentPage");
		int buyerId = json.getInt("memberId");

		Page pager = new Page();
		// 初始化时第一页
		pager.setPagination(true);
		pager.setPageSize(5);
		pager.setCurrentPage(currentPage);
		List<RcCoupon> coupons = couponService.findAllPagedCouponsByBuyerId(buyerId, pager);
		result.setData(coupons);
		return result;
	}

	@RequestMapping(value = "getmycouponpage")
	@ResponseBody
	public Result<List<RcBuyerCoupon>> getMyCouponPage(String data, String timestamp, String nonceStr, String product, String signature,
			HttpServletRequest request, HttpServletResponse response) throws ParseException {
		Result<List<RcBuyerCoupon>> result = new Result<List<RcBuyerCoupon>>();
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			result.setCode("-1");
			result.setResult("token fail");
			return result;
		}
		int currentPage = json.getInt("currentPage");
		int buyerId = json.getInt("memberId");
		int status = json.getInt("status");

		Page pager = new Page();
		// 初始化时第一页
		pager.setPagination(true);
		pager.setPageSize(5);
		pager.setCurrentPage(currentPage);
		List<RcBuyerCoupon> buyerCoupons = buyerCouponService.findPagedBuyerCouponsByBuyerIdAndStatus(buyerId, status, pager);
		for (RcBuyerCoupon buyerCoupon : buyerCoupons) {
			buyerCoupon.setCouponMemberStatus(buyerCoupon.getStatus());
		}
		result.setData(buyerCoupons);
		return result;
	}

	@RequestMapping(value = "couponDetail")
	public String couponDetail(Model model, Coupon couponParam, HttpServletRequest httpServletRequest)
			throws JsonProcessingException {
		if (couponParam.getCouponId() != null) {
			RcCoupon coupon = couponService.findCouponByCouponId(couponParam.getCouponId());
			model.addAttribute("coupon", coupon);
		} else {
			Integer merchantUserId = getMerchantUserId(httpServletRequest);
			if(merchantUserId != null) {
				RcMerchantUser merchantUser = merchantUserService.findMerchantUserById(merchantUserId);
				if (merchantUser != null) {
					RcMerchant merchant = merchantService.findMerchantById(merchantUser.getMerchantId());
					couponParam.setMerchantId(merchant.getMerchantId());
					couponParam.setMerchantName(merchant.getName());
					model.addAttribute("coupon", couponParam);
				}
			}
		}
		// 查询商品分类
		List<RcProdCat> prodCats = prodCatService.findTopProdCats();
		model.addAttribute("categoryList", prodCats);
		List<RcMerchant> merchants = merchantService.findAllEnabledMerchants();
		model.addAttribute("list", merchants);
		return "couponDetail";
	}

	/*
	 * @ModelAttribute public CouponsListVO setMerchantId(CouponsListVO
	 * rcOrderVo,HttpServletRequest httpServletRequest){
	 * if(httpServletRequest.getAttribute("merchantId")!=null)
	 * rcOrderVo.setMerchantId(httpServletRequest.getAttribute("merchantId").
	 * toString()); return rcOrderVo; }
	 */
	/**
	 * Coupon 列表
	 * @param model
	 * @param currentPage
	 * @param coupon
	 * @param httpSession
	 * @param httpServletRequest
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "couponIndex")
	public String couponIndex(HttpServletRequest httpServletRequest,HttpSession httpSession, Model model, Integer currentPage, RcCoupon coupon)
			throws UnsupportedEncodingException {
		if(coupon.getStatus() != null) {
			Date now = new Date();
			if(coupon.getStatus() != CouponBVOStatusEnum.RECEIVE_OVERDUE.getValue()) {
				coupon.setReceiveEndTimeFrom(now);
			} else {
				coupon.setStatus(null);
				coupon.setReceiveEndTimeTo(now);
			}
		}
		Integer merchantUserId = getMerchantUserId(httpServletRequest);
		if(merchantUserId != null) {
			RcMerchantUser merchantUser = merchantUserService.findMerchantUserById(merchantUserId);
			coupon.setMerchantId(merchantUser.getMerchantId());
		}
		if(coupon.getCreateTimeTo() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(coupon.getCreateTimeTo());
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.SECOND, -1);
			coupon.setCreateTimeTo(cal.getTime());
		}
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
		List<RcCoupon> coupons = couponService.findPagedCoupons(coupon, page);
		model.addAttribute("list", coupons);
		if (flag) {
			return "couponIndex";
		} else {
			return "couponIndex_inner";
		}
	}

	@RequestMapping(value = "merchantList")
	@ResponseBody
	public List<RcMerchant> merchantList() {
		List<RcMerchant> merchants = merchantService.findAllEnabledMerchants();
		return merchants;
	}

	/**
	 * 撤销
	 * 
	 * @param couponIds
	 * @return
	 */
	@RequestMapping(value = "revertCoupons")
	@ResponseBody
	public String revertCoupons(String couponIds) {
		String[] couponIdArray = StringUtils.split(couponIds, ",,,");
		for (String couponId : couponIdArray) {
			RcCoupon coupon = couponService.findCouponByCouponId(Integer.parseInt(couponId));
			if (coupon.getStatus() != CouponStatusEnum.UNDID.getValue() && coupon.getReceiveEndTime().after(new Date())) {
				coupon.setStatus(CouponStatusEnum.UNDID.getValue());
				couponService.modifyCoupon(coupon);
			}
		}
		return "{\"code\":\"1\",\"result\":\"Done.\"}";
	}

	/**
	 * 
	 * @param couponId
	 * @return
	 */
	@RequestMapping(value = "publishCoupon")
	@ResponseBody
	public String publishCoupon(int couponId) {
		RcCoupon coupon = couponService.findCouponByCouponId(couponId);
		if (coupon.getStatus() != CouponStatusEnum.PUBLISHED.getValue() && coupon.getReceiveEndTime().after(new Date())) {
			coupon.setStatus(CouponStatusEnum.PUBLISHED.getValue());
			couponService.modifyCoupon(coupon);
			return "{\"code\":\"1\",\"result\":\"Done.\"}";
		}else{
			return "{\"code\":\"0\",\"result\":\"Activity End.\"}";
		}
	}

	@RequestMapping(value = "publishCoupons")
	@ResponseBody
	public String publishCoupons(String couponIds) {
		String[] couponIdArray = StringUtils.split(couponIds, ",,,");
		for (String couponId : couponIdArray) {
			RcCoupon coupon = couponService.findCouponByCouponId(Integer.parseInt(couponId));
			if (coupon.getStatus() != CouponStatusEnum.PUBLISHED.getValue() && coupon.getReceiveEndTime().after(new Date())) {
				coupon.setStatus(CouponStatusEnum.PUBLISHED.getValue());
				couponService.modifyCoupon(coupon);
			}
		}
		return "{\"code\":\"1\",\"result\":\"Done.\"}";
	}

	@RequestMapping(value = "removeCoupon")
	@ResponseBody
	public String removeCoupon(int couponId) {
		RcCoupon coupon = couponService.findCouponByCouponId(couponId);
		coupon.setDelFlag(YesOrNoEnum.YES.getValue());
		couponService.modifyCoupon(coupon);
		return "{\"code\":\"1\",\"result\":\"Done.\"}";
	}

	@RequestMapping(value = "deleteCoupons")
	@ResponseBody
	public String deleteCoupons(String couponIds) {
		String[] couponIdArray = StringUtils.split(couponIds, ",,,");
		for (String couponId : couponIdArray) {
			RcCoupon coupon = couponService.findCouponByCouponId(Integer.parseInt(couponId));
			coupon.setDelFlag(YesOrNoEnum.YES.getValue());
			couponService.modifyCoupon(coupon);
		}
		return "{\"code\":\"1\",\"result\":\"Done.\"}";
	}

	/**
	 * 修改和新增卡券
	 * 
	 * @param coupon
	 * @return
	 */
	@RequestMapping(value = "addCoupon")
	@ResponseBody
	public String addCoupon(RcCoupon coupon) {
		if(coupon.getEffectTime().compareTo(coupon.getExpireTime())>0){
			return "{\"code\":\"0\",\"result\":\"The voucher effect time must more than effect time.\"}";
		}
		if(coupon.getExpireTime() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(coupon.getExpireTime());
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.SECOND, -1);
			coupon.setExpireTime(cal.getTime());
		}
		if(coupon.getExpireTime().compareTo(new Date()) < 0){
			return "{\"code\":\"0\",\"result\":\"The voucher can not be overdue.\"}";
		}
		couponService.saveCoupon(coupon);
		return "{\"code\":\"1\",\"result\":\"Done.\"}";
	}

	@RequestMapping(value = "accessCoupon")
	@ResponseBody
	public String accessCoupon(String data, String timestamp, String nonceStr, String product, String signature,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		int couponId = json.getInt("couponId");
		int buyerId = json.getInt("memberId");
//		if (iLockService.lockMemberAndCoupon(redisTemplate, memberId, couponId)) {
//			try {
				RcCoupon coupon = couponService.findCouponByCouponId(couponId);
				if (coupon.getStatus() == CouponStatusEnum.UNDID.getValue()) {
					return "{\"code\":\"" + ErrorValue.COUPON_DOES_NOT_EXIST.getStr()
							+ "\",\"result\":\"Coupon already get.\"}";
				}
				if (coupon.getLeftCount() == 0) {
					return "{\"code\":\"" + ErrorValue.COUPON_HAS_BEEN_COMPLETED.getStr()
							+ "\",\"result\":\"Coupon shortage.\"}";
				}
				if (!coupon.getReceiveEndTime().after(new Date())) {
					return "{\"code\":\"" + ErrorValue.COUPON_HAS_BEEN_COMPLETED.getStr()
							+ "\",\"result\":\"Coupon shortage.\"}";
				}
				RcBuyerCoupon buyerCoupon = buyerCouponService.findBuyerCouponByBuyerIdAndCouponId(buyerId, couponId);
				if (buyerCoupon != null) {
					return "{\"code\":\"1\",\"result\":\"success！\"}";
					/*return "{\"code\":\"" + ErrorValue.COUPON_ALREADY_RECEIVE.getStr()
							+ "\",\"result\":\"Coupon already get.\"}";*/
				}
				coupon.setLeftCount(coupon.getLeftCount() - 1);
				buyerCoupon = new RcBuyerCoupon();
				buyerCoupon.setCouponId(couponId);
				buyerCoupon.setBuyerId(buyerId);
				buyerCoupon.setEffectTime(coupon.getEffectTime());
				buyerCoupon.setExpireTime(coupon.getExpireTime());
				couponService.modifyCoupon(coupon);
				buyerCouponService.addBuyerCoupon(buyerCoupon);

				return "{\"code\":\"1\",\"result\":\"success！\"}";
//			} finally {
//				iLockService.unlock(redisTemplate, memberId);
//				iLockService.unLockCoupon(redisTemplate, couponId);
//			}
//		} else {
//			return "{\"code\":\"" + ErrorValue.SYSTEM_BUSY.getStr() + "\",\"result\":\"System is busy.\"}";
//		}
	}


	@RequestMapping(value = "usableCoupon")
	@ResponseBody
	public String usableCoupon(String data, String timestamp, String nonceStr,
			String product, String signature, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		Result<Map<Integer, List<RcBuyerCoupon>>> result = new Result<Map<Integer, List<RcBuyerCoupon>>>();
  		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
		}
		JSONArray orders = json.getJSONArray("merchant");
		int buyerId = json.getInt("memberId");
//		Map<Integer, List<RcBuyerCoupon>> map = new HashMap<Integer, List<RcBuyerCoupon>>();
		List<RcBuyerCoupon> buyerCoupons = buyerCouponService.findBuyerCouponsByBuyerIdAndStatus(buyerId, CouponVOStatusEnum.UNUSED.getValue());
		StringBuffer sb = new StringBuffer();
		for (Object order : orders) {
			List<RcBuyerCoupon> platformBuyerCoupons = new ArrayList<RcBuyerCoupon>();
			List<RcBuyerCoupon> merchantBuyerCoupons = new ArrayList<RcBuyerCoupon>();
			
			JSONObject orderObj = (JSONObject) order;
			Integer merchantId = orderObj.getInt("merchantId");
			double amount = orderObj.getDouble("amount");
			JSONArray prodIdArray = orderObj.getJSONArray("prodAmounts");
		//	map.put(merchantId, merchantCouponMembers);
			BigDecimal orderAmount = new BigDecimal(String.valueOf(amount));
			Map<Integer, BigDecimal> prodAmountMap = new HashMap<Integer, BigDecimal>();
			List<Integer> prods = new ArrayList<Integer>();
			for(Object obj : prodIdArray) {
				JSONObject jo = (JSONObject)obj;
				int prodId = jo.getInt("prodId");
				double prodAmount = jo.getDouble("amount");
				if(!prodAmountMap.containsKey(prodId)) {
					prodAmountMap.put(prodId, new BigDecimal(0));
				}
				prodAmountMap.put(prodId, prodAmountMap.get(prodId).add(new BigDecimal(String.valueOf(prodAmount))));
				prods.add(prodId);
			}
			
//			@SuppressWarnings("unchecked")
//			Collection<String> prodIds = (Collection<String>)JSONArray.toCollection(prodIdArray, String.class);
//			List<Integer> prods = new ArrayList<Integer>();
//		    for(String prodId:prodIds){
//		    	prods.add(Integer.valueOf(prodId));
//		    }
			
//			List<Integer> prodCatIds = prodCatService.findProdCatIdsByProdIds(prods);
//			List<Integer> parentProdCatIds = new ArrayList<Integer>();
//			for(Integer prodCatId: prodCatIds) {
//				RcProdCat procCat = prodCatService.findProdCatById(prodCatId);
//				while(procCat.getParentId() > 0) {
//					if(!parentProdCatIds.contains(procCat.getParentId())) {
//						parentProdCatIds.add(procCat.getParentId());
//					}
//					procCat = prodCatService.findProdCatById(procCat.getParentId());
//				}
//			}
			
			for (RcBuyerCoupon buyerCoupon : buyerCoupons) {
				if(buyerCoupon.getType() == CouponVOTypeEnum.MERCHANT.getValue()) {
					if(buyerCoupon.getMerchantId().equals(merchantId) && buyerCoupon.getBaseAmount().compareTo(orderAmount) <= 0) {
						merchantBuyerCoupons.add(buyerCoupon);
					}
				} else {
//					if((buyerCoupon.getProdCatId() == 0 || prodCatIds.contains(buyerCoupon.getProdCatId()) || parentProdCatIds.contains(buyerCoupon.getProdCatId())) && buyerCoupon.getBaseAmount().compareTo(orderAmount) <= 0) {
//						platformBuyerCoupons.add(buyerCoupon);
//					}
					if(buyerCoupon.getProdCatId() == 0) {
						if(buyerCoupon.getBaseAmount().compareTo(orderAmount) <= 0) {
							platformBuyerCoupons.add(buyerCoupon);
						}
					} else {
						Iterator<Entry<Integer, BigDecimal>> iter = prodAmountMap.entrySet().iterator();
						BigDecimal catAmount = new BigDecimal(0);
						while(iter.hasNext()) {
							Entry<Integer, BigDecimal> entry = iter.next();
							int prodId = entry.getKey();
							BigDecimal prodAmount = entry.getValue();
							RcProd prod = prodService.findProdById(prodId);
							if(prod.getProdCatId().equals(buyerCoupon.getProdCatId())) {
								catAmount = catAmount.add(prodAmount);
								continue;
							}
							RcProdCat procCat = prodCatService.findProdCatById(prod.getProdCatId());
							boolean isFound = false;
							while(procCat.getParentId() > 0) {
								if(procCat.getParentId().equals(buyerCoupon.getProdCatId())) {
									isFound = true;
									break;
								}
								procCat = prodCatService.findProdCatById(procCat.getParentId());
							}
							if(isFound) {
								catAmount = catAmount.add(prodAmount);
								continue;
							}
						}
						if(buyerCoupon.getBaseAmount().compareTo(catAmount) <= 0) {
							platformBuyerCoupons.add(buyerCoupon);
						}
					}
				}
			}
	            
			String coupons = new ObjectMapper().writeValueAsString(merchantBuyerCoupons);
	        String plate = new ObjectMapper().writeValueAsString(platformBuyerCoupons);
	        sb.append(",{merchantId:"+merchantId+",merchantCoupons:" +coupons+",couponList:"+plate+"}");
		}
		
		String s = "{code:1,data:[" + (sb.length() > 0 ? sb.substring(1) : "") + "]}";
		System.out.println(s);
        return s;
	}
}
