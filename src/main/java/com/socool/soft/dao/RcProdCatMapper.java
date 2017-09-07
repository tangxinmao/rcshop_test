package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcProdCat;

public interface RcProdCatMapper {

	int deleteByPrimaryKey(int prodCatId);

	int insert(RcProdCat record);

	RcProdCat selectByPrimaryKey(int prodCatId);

	int updateByPrimaryKey(RcProdCat record);

	List<RcProdCat> select(RcProdCat record);

//	RcProdCat selectOne(RcProdCat record);

	int delete(RcProdCat record);
}