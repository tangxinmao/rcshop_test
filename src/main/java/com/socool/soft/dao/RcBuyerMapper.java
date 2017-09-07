package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcBuyer;

public interface RcBuyerMapper {

//	int deleteByPrimaryKey(int memberId);

	int insert(RcBuyer record);

	RcBuyer selectByPrimaryKey(int memberId);

	int updateByPrimaryKey(RcBuyer record);

	List<RcBuyer> select(RcBuyer record);

	RcBuyer selectOne(RcBuyer record);

//	int delete(RcBuyer record);
}