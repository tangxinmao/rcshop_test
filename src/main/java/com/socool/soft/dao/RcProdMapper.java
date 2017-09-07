package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcProd;

public interface RcProdMapper {

//	int deleteByPrimaryKey(int prodId);

	int insert(RcProd record);

	RcProd selectByPrimaryKey(int prodId);

	int updateByPrimaryKey(RcProd record);

	List<RcProd> select(RcProd record);

//	RcProd selectOne(RcProd record);

//	int delete(RcProd record);
}