package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcCoupon;

public interface RcCouponMapper {

	int deleteByPrimaryKey(int couponId);

	int insert(RcCoupon record);

	RcCoupon selectByPrimaryKey(int couponId);

	int updateByPrimaryKey(RcCoupon record);

	List<RcCoupon> select(RcCoupon record);

//	RcCoupon selectOne(RcCoupon record);

//	int delete(RcCoupon record);
}