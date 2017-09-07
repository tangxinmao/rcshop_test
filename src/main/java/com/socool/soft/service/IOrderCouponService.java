package com.socool.soft.service;

import com.socool.soft.bo.RcOrderCoupon;

public interface IOrderCouponService {
	/**
	 * 批量保存Couponorder
	 * 
	 * @param rcCouponOrders
	 */
//	void addCouponOrders(List<RcOrderCoupon> couponOrders);

	/**
	 * 保存Couponorder
	 * 
	 * @param couponOrder
	 * @return
	 */
	int addOrderCoupon(RcOrderCoupon orderCoupon);

	/**
	 * @param orderId
	 * @return
	 */
//	List<RcOrderCoupon> findCouponOrdersByOrderId(long orderId);

	/**
	 * 
	 * @param orderId
	 * @return
	 */
	RcOrderCoupon findOrderCouponByOrderId(long orderId);
}
