package com.socool.soft.dao;

import com.socool.soft.bo.RcOrderDelivery;

public interface RcOrderDeliveryMapper {

	int deleteByPrimaryKey(long orderId);

	int insert(RcOrderDelivery record);

	RcOrderDelivery selectByPrimaryKey(long orderId);

	int updateByPrimaryKey(RcOrderDelivery record);

//	List<RcOrderDelivery> select(RcOrderDelivery record);

//	RcOrderDelivery selectOne(RcOrderDelivery record);

//	int delete(RcOrderDelivery record);
}