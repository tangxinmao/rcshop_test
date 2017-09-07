package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcProdPropEnum;

public interface RcProdPropEnumMapper {

	int deleteByPrimaryKey(int prodPropEnumId);

	int insert(RcProdPropEnum record);

//	RcProdPropEnum selectByPrimaryKey(int prodPropEnumId);

	int updateByPrimaryKey(RcProdPropEnum record);

	List<RcProdPropEnum> select(RcProdPropEnum record);

//	RcProdPropEnum selectOne(RcProdPropEnum record);

	int delete(RcProdPropEnum record);
}