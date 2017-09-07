package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcAppProdRec;

public interface RcAppProdRecMapper {

	int deleteByPrimaryKey(int prodRecId);

	int insert(RcAppProdRec record);

	RcAppProdRec selectByPrimaryKey(int prodRecId);

	int updateByPrimaryKey(RcAppProdRec record);

	List<RcAppProdRec> select(RcAppProdRec record);

	RcAppProdRec selectOne(RcAppProdRec record);

//	int delete(RcAppProdRec record);
}