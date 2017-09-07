package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcProdCatSkuProp;

public interface RcProdCatSkuPropMapper {

//	int deleteByPrimaryKey(int prodPropRelId);

	int insert(RcProdCatSkuProp record);

//	RcProdCatSkuProp selectByPrimaryKey(int prodPropRelId);

//	int updateByPrimaryKey(RcProdCatSkuProp record);

	List<RcProdCatSkuProp> select(RcProdCatSkuProp record);

	RcProdCatSkuProp selectOne(RcProdCatSkuProp record);

	int delete(RcProdCatSkuProp record);
}