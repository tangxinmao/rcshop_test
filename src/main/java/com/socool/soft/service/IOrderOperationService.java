package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcOrderOperation;

public interface IOrderOperationService {
	
	long addOrderOperation(RcOrderOperation orderOperation);

	/**
	 * 查询操作对象
	 * 
	 * @param rcOrderOperation 
	 * @return
	 */
	List<RcOrderOperation> findOrderOperations(RcOrderOperation param);
	
	List<RcOrderOperation> findOrderOperationsByOrderId(long orderId);
	/**
	 * 通过订单和操作类型 新增orderOperation
	 * @param deliver
	 * @param rcOrder
	 */

//	void addOrderOperationByOrderAndOrderOperationType(RcOrderOperationTypeEnum rcOrderOperationTypeEnum, RcOrder rcOrder,String ... operationMemo);
}
