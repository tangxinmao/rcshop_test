package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcMerchantWid;

public interface RcMerchantWidMapper {

//	int deleteByPrimaryKey(long merchantWidId);

	int insert(RcMerchantWid record);

	RcMerchantWid selectByPrimaryKey(long merchantWidId);

	int updateByPrimaryKey(RcMerchantWid record);

	List<RcMerchantWid> select(RcMerchantWid record);
	
	RcMerchantWid selectOne(RcMerchantWid record);

//	int delete(RcMerchantWid record);
}