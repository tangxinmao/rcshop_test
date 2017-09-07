package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcProdSku;

public interface IProdSkuService {
/**
 * 通过skuId查询prodSku
 * @param prodSkuId
 * @return
 */
	RcProdSku findProdSkuByProdSkuId(String prodSkuId);

	List<RcProdSku> findProdSkusByProdId(int prodId);
	
	String addProdSku(RcProdSku prodSku);
	
	int removeProdSkusByProdId(int prodId);
	
	String modifyProdSku(RcProdSku prodSku);
}
