package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcProdProp;

public interface RcProdPropMapper {

	int deleteByPrimaryKey(int prodPropId);

	int insert(RcProdProp record);

	RcProdProp selectByPrimaryKey(int prodPropId);

	int updateByPrimaryKey(RcProdProp record);

	List<RcProdProp> select(RcProdProp record);

//	RcProdProp selectOne(RcProdProp record);

//	int delete(RcProdProp record);
}