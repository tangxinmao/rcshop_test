package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcProdSkuPropInfo;

public interface IProdSkuPropInfoService {
	/**
	 * skuId 查询skupropINfo
	 * 
	 * @param prodSkuId
	 * @return
	 */
	List<RcProdSkuPropInfo> findProdSkuPropInfosByProdSkuId(String prodSkuId);
	
	List<RcProdSkuPropInfo> findProdSkuPropInfosByProdId(int prodId);
	
	List<RcProdSkuPropInfo> findProdSkuPropInfoByProdSkuPropEnumId(int prodSkuPropEnumId);
	
	RcProdSkuPropInfo findProdSkuPropInfoByProdIdAndProdSkuPropEnumId(int prodId, int prodSkuPropEnumId);
	
	int addProdSkuPropInfo(RcProdSkuPropInfo prodSkuPropInfo);
	
	int removeProdSkuPropInfosByProdId(int prodId);
}
