package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcProdBrand;

public interface RcProdBrandMapper {

//	int deleteByPrimaryKey(int prodBrandId);

	int insert(RcProdBrand record);

	RcProdBrand selectByPrimaryKey(int prodBrandId);

	int updateByPrimaryKey(RcProdBrand record);

	List<RcProdBrand> select(RcProdBrand record);

//	RcProdBrand selectOne(RcProdBrand record);

//	int delete(RcProdBrand record);
}