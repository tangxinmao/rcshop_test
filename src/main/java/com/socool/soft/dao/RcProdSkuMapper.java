package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcProdSku;

public interface RcProdSkuMapper {

//	int deleteByPrimaryKey(String prodSkuId);

	int insert(RcProdSku record);

	RcProdSku selectByPrimaryKey(String prodSkuId);

	int updateByPrimaryKey(RcProdSku record);

	List<RcProdSku> select(RcProdSku record);

//	RcProdSku selectOne(RcProdSku record);

	int delete(RcProdSku record);
}