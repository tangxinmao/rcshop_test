package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcOrderFee;

public interface IOrderFeeService {

	List<RcOrderFee> findOrderFeesByOrderId(long orderId);
	
	long addOrderFee(RcOrderFee orderFee);
	
	long modifyOrderFee(RcOrderFee orderFee);
	
	int removeOrderFee(long orderFeeId);
	
	int removeOrderFeesByOrderId(long orderId);
	
	RcOrderFee findOrderFeeByType(long orderId, int type);
	
	List<RcOrderFee> findOrderFees(RcOrderFee param);
}
