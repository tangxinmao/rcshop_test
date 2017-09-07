package com.socool.soft.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcOrderOperation;
import com.socool.soft.dao.RcOrderOperationMapper;
import com.socool.soft.service.IOrderOperationService;

@Service
public class OrderOperationServiceImpl implements IOrderOperationService {

	@Autowired
	private RcOrderOperationMapper orderOperationMapper;

//	@Override
//	public void addOrderOperationByOrderAndOrderOperationType(
//			RcOrderOperationTypeEnum rcOrderOperationTypeEnum, RcOrder rcOrder,
//			String... operationMemo) {
//
//	}

	@Override
	public long addOrderOperation(RcOrderOperation orderOperation) {
		orderOperation.setOperationTime(new Date());
		if(orderOperationMapper.insert(orderOperation) > 0) {
			return orderOperation.getOrderOperationId();
		}
		return 0;
	}

	@Override
	public List<RcOrderOperation> findOrderOperations(RcOrderOperation param) {
		return orderOperationMapper.select(param);
	}

	@Override
	public List<RcOrderOperation> findOrderOperationsByOrderId(long orderId) {
		RcOrderOperation param = new RcOrderOperation();
		param.setOrderId(orderId);
		return orderOperationMapper.select(param);
	}
}
