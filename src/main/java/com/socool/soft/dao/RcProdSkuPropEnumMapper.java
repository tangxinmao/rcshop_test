package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcProdSkuPropEnum;

public interface RcProdSkuPropEnumMapper {

	int deleteByPrimaryKey(int prodPropEnumId);

	int insert(RcProdSkuPropEnum record);

	RcProdSkuPropEnum selectByPrimaryKey(int prodPropEnumId);

	int updateByPrimaryKey(RcProdSkuPropEnum record);

	List<RcProdSkuPropEnum> select(RcProdSkuPropEnum record);

//	RcProdSkuPropEnum selectOne(RcProdSkuPropEnum record);

	int delete(RcProdSkuPropEnum record);
}