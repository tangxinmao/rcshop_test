package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcAppProdHot;

public interface RcAppProdHotMapper {

	int deleteByPrimaryKey(int appProdHotId);

	int insert(RcAppProdHot record);

	RcAppProdHot selectByPrimaryKey(int appProdHotId);

	int updateByPrimaryKey(RcAppProdHot record);

	List<RcAppProdHot> select(RcAppProdHot record);

	RcAppProdHot selectOne(RcAppProdHot record);

//	int delete(RcAppProdHot record);
}