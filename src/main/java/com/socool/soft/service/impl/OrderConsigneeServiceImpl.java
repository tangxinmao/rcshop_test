package com.socool.soft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcOrderConsignee;
import com.socool.soft.dao.RcOrderConsigneeMapper;
import com.socool.soft.service.IOrderConsigneeService;

@Service
public class OrderConsigneeServiceImpl implements IOrderConsigneeService {
	@Autowired
	private RcOrderConsigneeMapper orderConsigneeMapper;

//	@Override
//	public void addOrderConsignees(List<RcOrderConsignee> orderConsignees) {
//		for(RcOrderConsignee orderConsignee : orderConsignees) {
//			orderConsigneeMapper.insert(orderConsignee);
//		}
//	}
	
	@Override
	public long addOrderConsignee(RcOrderConsignee orderConsignee) {
		if(orderConsigneeMapper.insert(orderConsignee) > 0) {
			return orderConsignee.getOrderId();
		}
		return 0;
	}

	@Override
	public RcOrderConsignee findOrderConsigneeById(long orderId) {
		return orderConsigneeMapper.selectByPrimaryKey(orderId);
	}
}
