package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcProdSkuProp;

public interface RcProdSkuPropMapper {

	int deleteByPrimaryKey(int prodPropId);

	int insert(RcProdSkuProp record);

	RcProdSkuProp selectByPrimaryKey(int prodPropId);

	int updateByPrimaryKey(RcProdSkuProp record);

	List<RcProdSkuProp> select(RcProdSkuProp record);

//	RcProdSkuProp selectOne(RcProdSkuProp record);

//	int delete(RcProdSkuProp record);
}