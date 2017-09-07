package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcOrderPayment;

public interface IOrderPaymentService {
	
	long addOrderPayment(RcOrderPayment orderPayment);
	
	long modifyOrderPayment(RcOrderPayment orderPayment);
	
	RcOrderPayment findOrderPaymentByOrderId(long orderId);
	
	int removeOrderPaymentByOrderId(long orderId);

	List<RcOrderPayment> findOrderPayments(RcOrderPayment param);

	RcOrderPayment findOrderPayment(RcOrderPayment param);
}
