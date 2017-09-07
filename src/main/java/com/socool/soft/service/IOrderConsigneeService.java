package com.socool.soft.service;

import com.socool.soft.bo.RcOrderConsignee;

public interface IOrderConsigneeService {
	/**
	 * 批量保存订单收货信息
	 * 
	 * @param rcOrderConsignees
	 */
//	void addOrderConsignees(List<RcOrderConsignee> orderConsignees);

	/**
	 * 查询订单收货信息
	 * 
	 * @param orderId
	 * @return
	 */
	RcOrderConsignee findOrderConsigneeById(long orderId);

	/**
	 * 
	 * @param rcOrderConsignee
	 * @return
	 */
	long addOrderConsignee(RcOrderConsignee orderConsignee);

}
