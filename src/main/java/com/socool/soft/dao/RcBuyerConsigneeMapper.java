package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcBuyerConsignee;

public interface RcBuyerConsigneeMapper {

	int deleteByPrimaryKey(int consigneeId);

	int insert(RcBuyerConsignee record);

	RcBuyerConsignee selectByPrimaryKey(int consigneeId);

	int updateByPrimaryKey(RcBuyerConsignee record);

	List<RcBuyerConsignee> select(RcBuyerConsignee record);

//	RcBuyerConsignee selectOne(RcBuyerConsignee record);

//	int delete(RcBuyerConsignee record);
}