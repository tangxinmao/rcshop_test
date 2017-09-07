package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcProdSkuPropInfo;

public interface RcProdSkuPropInfoMapper {

//	int deleteByPrimaryKey(int prodSkuPropInfoId);

	int insert(RcProdSkuPropInfo record);

//	RcProdSkuPropInfo selectByPrimaryKey(int prodSkuPropInfoId);

//	int updateByPrimaryKey(RcProdSkuPropInfo record);

	List<RcProdSkuPropInfo> select(RcProdSkuPropInfo record);

	RcProdSkuPropInfo selectOne(RcProdSkuPropInfo record);

	int delete(RcProdSkuPropInfo record);
}