package com.socool.soft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcOrderCoupon;
import com.socool.soft.dao.RcOrderCouponMapper;
import com.socool.soft.service.IOrderCouponService;

@Service
public class OrderCouponServiceImpl implements IOrderCouponService {
	@Autowired
	private RcOrderCouponMapper orderCouponMapper;

//	@Override
//	public void addCouponOrders(List<RcOrderCoupon> couponOrders) {
//		for(RcOrderCoupon couponOrder : couponOrders) {
//			addCouponOrder(couponOrder);
//		}
//	}

	@Override
	public int addOrderCoupon(RcOrderCoupon orderCoupon) {
		if(orderCouponMapper.insert(orderCoupon) > 0) {
			return orderCoupon.getCouponOrderId();
		}
		return 0;
	}

//	@Override
//	public List<RcOrderCoupon> findCouponOrdersByOrderId(long orderId) {
//		RcOrderCoupon param = new RcOrderCoupon();
//		param.setOrderId(orderId);
//		return couponOrderMapper.select(param);
//	}

	@Override
	public RcOrderCoupon findOrderCouponByOrderId(long orderId) {
		RcOrderCoupon param = new RcOrderCoupon();
		param.setOrderId(orderId);
		return orderCouponMapper.selectOne(param);
	}
}
