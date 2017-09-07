package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcOrderProdSkuPropInfo;

public interface IOrderProdSkuPropInfoService {
	
	long addOrderProdSkuPropInfo(RcOrderProdSkuPropInfo orderProdSkuPropInfo);
	
	List<RcOrderProdSkuPropInfo> findOrderProdSkuPropInfosByOrderProdId(long orderProdId);
}
