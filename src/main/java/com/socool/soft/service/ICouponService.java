package com.socool.soft.service;

import java.util.List;
import java.util.Map;

import com.socool.soft.bo.RcCoupon;
import com.socool.soft.vo.Page;

public interface ICouponService {
	/**
	 * 根据卡券状态查询卡券（app接口）
	 * 
	 * @param couponsListVO
	 *            领取优惠券页面 (查询所有情况优惠券 ) 没有得到商家优惠券 4 商家优惠券已领完 5 商家优惠券已过期 3
	 *            商家优惠券已领取可用 1 买家优惠券个人中心(根据状态查询优惠券) 优惠券可使用 1 优惠券已使用 2 优惠券已过期 3
	 *            memberId
	 * @return
	 */
	List<RcCoupon> findAllPagedCouponsByBuyerId(int buyerId, Page page);

	Map<Integer, List<RcCoupon>> findAllCouponsForShoppingCartByBuyerId(int buyerId);

	/**
	 * 通过couponId查询coupon
	 * 
	 * @param couponId
	 * @return
	 */
	RcCoupon findCouponByCouponId(int couponId);

	/**
	 * 修改coupon
	 * 
	 * @param rcCoupon
	 */

	int modifyCoupon(RcCoupon coupon);

	/**
	 * 新增卡券
	 * 
	 * @param rcCoupon
	 * @return
	 */
	int addCoupon(RcCoupon coupon);
	
	int saveCoupon(RcCoupon coupon);

	/**
	 * 通过memberId和prodIds来查询coupon 卡券分类进行分组 取每组卡券金额最大的卡券
	 * 
	 * @param couponsListVO
	 * @return
	 */
//	List<RcCoupon> findCouponsByProdIdsAndMemberId(int memberId, List<Integer> prodIds);

	/**
	 * 删除coupon
	 * 
	 * @param couponId
	 * @return
	 */
//	int removeCoupon(int couponId);

	/**
	 * 获取当前用户金额最优卡券（满减）
	 * 
	 * @param merchantId
	 *            商家
	 * @param memberId
	 *            用户id
	 * @param amount
	 *            金额
	 * @return
	 */
//	RcCoupon findCouponByMerchantIdAndMemberIdAndAmount(int merchantId, int memberId, BigDecimal amount);

	/**
	 * app接口需要， 查询所有卡券 包括 1按分类，2按商家 3平台
	 * 
	 * @param prodCatId
	 * @param merchantId
	 * @return
	 */

	List<RcCoupon> findPagedCoupons(RcCoupon coupon, Page page);

	/**
	 * 商品详情app接口
	 * @param prodCatId
	 * @param merchantId
	 * @return
	 */
	List<RcCoupon> findCouponsForProd(int buyerId, int prodId);
}
