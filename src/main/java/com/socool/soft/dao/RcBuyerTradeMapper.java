package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcBuyerTrade;

public interface RcBuyerTradeMapper {

//	int deleteByPrimaryKey(long memberTradeId);

	int insert(RcBuyerTrade record);

//	RcBuyerTrade selectByPrimaryKey(long memberTradeId);

//	int updateByPrimaryKey(RcBuyerTrade record);

	List<RcBuyerTrade> select(RcBuyerTrade record);

//	RcBuyerTrade selectOne(RcBuyerTrade record);

//	int delete(RcBuyerTrade record);
}