package com.socool.soft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcOrderProdSkuPropInfo;
import com.socool.soft.dao.RcOrderProdSkuPropInfoMapper;
import com.socool.soft.service.IOrderProdSkuPropInfoService;

@Service
public class OrderProdSkuPropInfoServiceImpl implements IOrderProdSkuPropInfoService {

	@Autowired
	private RcOrderProdSkuPropInfoMapper orderProdSkuPropInfoMapper;
	
	@Override
	public long addOrderProdSkuPropInfo(RcOrderProdSkuPropInfo orderProdSkuPropInfo) {
		if(orderProdSkuPropInfoMapper.insert(orderProdSkuPropInfo) > 0) {
			orderProdSkuPropInfo.getOrderProdSkuPropInfoId();
		}
		return 0;
	}

	@Override
	public List<RcOrderProdSkuPropInfo> findOrderProdSkuPropInfosByOrderProdId(
			long orderProdId) {
		RcOrderProdSkuPropInfo param = new RcOrderProdSkuPropInfo();
		param.setOrderProdId(orderProdId);
		return orderProdSkuPropInfoMapper.select(param);
	}
}
