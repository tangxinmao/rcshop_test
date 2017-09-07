package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcOrderPayment;

public interface RcOrderPaymentMapper {

//	int deleteByPrimaryKey(long orderOperationId);

	int insert(RcOrderPayment record);

//	RcOrderPayment selectByPrimaryKey(long orderOperationId);

	int updateByPrimaryKey(RcOrderPayment record);

	List<RcOrderPayment> select(RcOrderPayment record);

	RcOrderPayment selectOne(RcOrderPayment record);

	int delete(RcOrderPayment record);
}