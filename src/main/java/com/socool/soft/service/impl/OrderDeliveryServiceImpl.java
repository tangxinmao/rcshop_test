package com.socool.soft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcOrderDelivery;
import com.socool.soft.dao.RcOrderDeliveryMapper;
import com.socool.soft.service.IOrderDeliveryService;

@Service
public class OrderDeliveryServiceImpl implements IOrderDeliveryService {
	@Autowired
	private RcOrderDeliveryMapper orderDeliveryMapper;

	@Override
	public long addOrderDelivery(RcOrderDelivery orderDelivery) {
		if(orderDeliveryMapper.insert(orderDelivery) > 0) {
			return orderDelivery.getOrderId();
		}
		return 0;
	}

	@Override
	public RcOrderDelivery findOrderDeliveryById(long orderId) {
		return orderDeliveryMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public long modifyOrderDelivery(RcOrderDelivery orderDelivery) {
		if(orderDeliveryMapper.updateByPrimaryKey(orderDelivery) > 0) {
			return orderDelivery.getOrderId();
		}
		return 0;
	}
}
