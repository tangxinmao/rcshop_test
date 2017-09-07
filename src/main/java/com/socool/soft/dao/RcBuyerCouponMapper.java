package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcBuyerCoupon;

public interface RcBuyerCouponMapper {

//	int deleteByPrimaryKey(int couponMemberId);

	int insert(RcBuyerCoupon record);

	RcBuyerCoupon selectByPrimaryKey(int couponMemberId);

	int updateByPrimaryKey(RcBuyerCoupon record);

	List<RcBuyerCoupon> select(RcBuyerCoupon record);

	RcBuyerCoupon selectOne(RcBuyerCoupon record);

//	int delete(RcBuyerCoupon record);
}