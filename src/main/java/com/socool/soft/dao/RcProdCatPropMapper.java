package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcProdCatProp;

public interface RcProdCatPropMapper {

//	int deleteByPrimaryKey(int prodPropRelId);

	int insert(RcProdCatProp record);

//	RcProdCatProp selectByPrimaryKey(int prodPropRelId);

//	int updateByPrimaryKey(RcProdCatProp record);

	List<RcProdCatProp> select(RcProdCatProp record);

	RcProdCatProp selectOne(RcProdCatProp record);

	int delete(RcProdCatProp record);
}