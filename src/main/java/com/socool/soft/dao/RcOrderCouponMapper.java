package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcOrderCoupon;

public interface RcOrderCouponMapper {

//	int deleteByPrimaryKey(int couponOrderId);

	int insert(RcOrderCoupon record);

//	RcOrderCoupon selectByPrimaryKey(int couponOrderId);

//	int updateByPrimaryKey(RcOrderCoupon record);

	List<RcOrderCoupon> select(RcOrderCoupon record);

	RcOrderCoupon selectOne(RcOrderCoupon record);

	int delete(RcOrderCoupon record);
}