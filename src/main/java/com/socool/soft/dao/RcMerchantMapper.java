package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcMerchant;

public interface RcMerchantMapper {

//	int deleteByPrimaryKey(int merchantId);

	int insert(RcMerchant record);

	RcMerchant selectByPrimaryKey(int merchantId);

	int updateByPrimaryKey(RcMerchant record);

	List<RcMerchant> select(RcMerchant record);

//	RcMerchant selectOne(RcMerchant record);

//	int delete(RcMerchant record);
}