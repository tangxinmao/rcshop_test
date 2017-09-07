package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcProdPropInfo;

public interface IProdPropInfoService {
/**
 * 通过prodId查询
 * @param prodId
 * @return
 */
	List<RcProdPropInfo> findProdPropInfosByProdId(int prodId);
	
	int addProdPropInfo(RcProdPropInfo prodPropInfo);
	
	int removeProdPropInfosByProdId(int prodId);
}
