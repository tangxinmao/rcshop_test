package com.socool.soft.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcOrderFee;
import com.socool.soft.dao.RcOrderFeeMapper;
import com.socool.soft.service.IOrderFeeService;

@Service
public class OrderFeeService implements IOrderFeeService {
	
	@Autowired
	private RcOrderFeeMapper orderFeeMapper;

	@Override
	public List<RcOrderFee> findOrderFeesByOrderId(long orderId) {
		RcOrderFee param = new RcOrderFee();
		param.setOrderId(orderId);
		return orderFeeMapper.select(param);
	}

	@Override
	public long addOrderFee(RcOrderFee orderFee) {
		if(orderFee.getAmount().compareTo(new BigDecimal(0)) != 0) {
			if(orderFeeMapper.insert(orderFee) > 0) {
				return orderFee.getOrderFeeId();
			}
		}
		return 0;
	}

	@Override
	public long modifyOrderFee(RcOrderFee orderFee) {
		if(orderFee.getAmount().compareTo(new BigDecimal(0)) != 0) {
			if(orderFeeMapper.updateByPrimaryKey(orderFee) > 0) {
				return orderFee.getOrderFeeId();
			}
		}
		return 0;
	}

	@Override
	public int removeOrderFee(long orderFeeId) {
		return orderFeeMapper.deleteByPrimaryKey(orderFeeId);
	}

	@Override
	public int removeOrderFeesByOrderId(long orderId) {
		RcOrderFee param = new RcOrderFee();
		param.setOrderId(orderId);
		return orderFeeMapper.delete(param);
	}

	@Override
	public RcOrderFee findOrderFeeByType(long orderId, int type) {
		RcOrderFee param = new RcOrderFee();
		param.setOrderId(orderId);
		param.setType(type);
		return orderFeeMapper.selectOne(param);
	}

	@Override
	public List<RcOrderFee> findOrderFees(RcOrderFee param) {
		return orderFeeMapper.select(param);
	}

}
