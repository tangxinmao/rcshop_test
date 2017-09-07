package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcBuyerCoupon;
import com.socool.soft.vo.Page;

public interface IBuyerCouponService {
	/**
	 * 根据memberid和couponId查询
	 * 
	 * @param rcCouponMemberParam
	 * @return
	 */

	RcBuyerCoupon findBuyerCouponByBuyerIdAndCouponId(int buyerId, int couponId);
	
	List<RcBuyerCoupon> findBuyerCoupons(int buyerId, List<Integer> couponIds);

	/**
	 * 新增couponmember
	 * @param rcCouponMember
	 * @return
	 */
	int addBuyerCoupon(RcBuyerCoupon buyerCoupon);
	
	int modifyBuyerCoupon(RcBuyerCoupon buyerCoupon);

//	List<RcBuyerCoupon> findCouponsByMemberId(int buyerId);
	
//	List<RcBuyerCoupon> findUsableCouponsByMemberId(int buyerId);

	/**
	 * 根据couponId和memberId跟新用户卡券状态
	 * @param rcCouponMember
	 * @return
	 */
	int modifyBuyerCouponStatus(int buyerId, int couponId, int status);
	
	List<RcBuyerCoupon> findBuyerCouponsByBuyerIdAndStatus(int buyerId, Integer status);
	
	List<RcBuyerCoupon> findPagedBuyerCouponsByBuyerIdAndStatus(int buyerId, Integer status, Page page);
	
	/**
	 * 获取当前用户金额最优卡券（满减）
	 * 
	 * @param merchantId
	 *            商家
	 * @param buyerId
	 *            用户id
	 * @param amount
	 *            金额
	 * @return
	 */
//	RcCoupon findCouponByMerchantIdAndMemberId(int merchantId, int buyerId);
}
