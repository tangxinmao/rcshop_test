package com.socool.soft.dao;

import com.socool.soft.bo.RcOrderConsignee;

public interface RcOrderConsigneeMapper {

//	int deleteByPrimaryKey(long orderId);

	int insert(RcOrderConsignee record);

	RcOrderConsignee selectByPrimaryKey(long orderId);

//	int updateByPrimaryKey(RcOrderConsignee record);

//	List<RcOrderConsignee> select(RcOrderConsignee record);

//	RcOrderConsignee selectOne(RcOrderConsignee record);

//	int delete(RcOrderConsignee record);
}