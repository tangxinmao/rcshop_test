package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcOrderProd;

public interface RcOrderProdMapper {

//	int deleteByPrimaryKey(long orderPropId);

	int insert(RcOrderProd record);

	RcOrderProd selectByPrimaryKey(long orderPropId);

//	int updateByPrimaryKey(RcOrderProd record);

	List<RcOrderProd> select(RcOrderProd record);

//	RcOrderProd selectOne(RcOrderProd record);

//	int delete(RcOrderProd record);
}