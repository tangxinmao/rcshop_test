package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcProdPropInfo;

public interface RcProdPropInfoMapper {

//	int deleteByPrimaryKey(int prodPropInfoId);

	int insert(RcProdPropInfo record);

//	RcProdPropInfo selectByPrimaryKey(int prodPropInfoId);

//	int updateByPrimaryKey(RcProdPropInfo record);

	List<RcProdPropInfo> select(RcProdPropInfo record);

//	RcProdPropInfo selectOne(RcProdPropInfo record);

	int delete(RcProdPropInfo record);
}