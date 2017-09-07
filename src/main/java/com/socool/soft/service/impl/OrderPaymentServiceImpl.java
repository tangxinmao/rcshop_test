package com.socool.soft.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcOrderPayment;
import com.socool.soft.bo.constant.OrderPaymentStatusEnum;
import com.socool.soft.dao.RcOrderPaymentMapper;
import com.socool.soft.service.IOrderPaymentService;

@Service
public class OrderPaymentServiceImpl implements IOrderPaymentService {
	
	@Autowired
	private RcOrderPaymentMapper orderPaymentMapper;

	@Override
	public long addOrderPayment(RcOrderPayment orderPayment) {
		orderPayment.setCreateTime(new Date());
		if(orderPaymentMapper.insert(orderPayment) > 0) {
			return orderPayment.getOrderPaymentId();
		}
		return 0;
	}

	@Override
	public long modifyOrderPayment(RcOrderPayment orderPayment) {
		if(orderPaymentMapper.updateByPrimaryKey(orderPayment) > 0) {
			return orderPayment.getOrderPaymentId();
		}
		return 0;
	}

	@Override
	public RcOrderPayment findOrderPaymentByOrderId(long orderId) {
		RcOrderPayment param = new RcOrderPayment();
		param.setOrderId(orderId);
		param.setStatus(OrderPaymentStatusEnum.PENDING.getValue());
		return orderPaymentMapper.selectOne(param);
	}

	@Override
	public int removeOrderPaymentByOrderId(long orderId) {
		RcOrderPayment param = new RcOrderPayment();
		param.setOrderId(orderId);
		param.setStatus(OrderPaymentStatusEnum.PENDING.getValue());
		List<RcOrderPayment> orderPayments = findOrderPayments(param);
		for(RcOrderPayment orderPayment : orderPayments) {
			orderPayment.setStatus(OrderPaymentStatusEnum.INVALID.getValue());
			orderPaymentMapper.updateByPrimaryKey(orderPayment);
		}
		return 1;
	}

	@Override
	public List<RcOrderPayment> findOrderPayments(RcOrderPayment param) {
		return orderPaymentMapper.select(param);
	}

	@Override
	public RcOrderPayment findOrderPayment(RcOrderPayment param) {
		return orderPaymentMapper.selectOne(param);
	}

}
