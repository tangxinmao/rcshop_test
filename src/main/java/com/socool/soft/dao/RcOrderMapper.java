package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcOrder;

public interface RcOrderMapper {

//	int deleteByPrimaryKey(long orderId);

	int insert(RcOrder record);

	RcOrder selectByPrimaryKey(long orderId);

	int updateByPrimaryKey(RcOrder record);

	List<RcOrder> select(RcOrder record);

//	RcOrder selectOne(RcOrder record);

//	int delete(RcOrder record);
}