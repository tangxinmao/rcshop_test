package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcOrderProdSkuPropInfo;

public interface RcOrderProdSkuPropInfoMapper {

//	int deleteByPrimaryKey(long orderProdSkuPropInfoId);

	int insert(RcOrderProdSkuPropInfo record);

//	RcOrderProdSkuPropInfo selectByPrimaryKey(long orderProdSkuPropInfoId);

//	int updateByPrimaryKey(RcOrderProdSkuPropInfo record);

	List<RcOrderProdSkuPropInfo> select(RcOrderProdSkuPropInfo record);

//	RcOrderProdSkuPropInfo selectOne(RcOrderProdSkuPropInfo record);

//	int delete(RcOrderProdSkuPropInfo record);
}