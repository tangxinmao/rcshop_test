package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcOrderProdEvaluation;
import com.socool.soft.bo.RcProd;
import com.socool.soft.bo.RcProdSkuPropEnum;
import com.socool.soft.bo.RcProdPropInfo;
import com.socool.soft.bo.RcProdSku;
import com.socool.soft.vo.Page;

public interface IProductAppService {

	RcProd findProdForAndroid(int prodId,int cityId, int memberId);
	
//	RcProd findProductById(int prodId);
	
//	ProdSkuSelectedVO findProdSkuSelectedInfo(Map<String, Object> param);
	
	RcProdSku findSelectedProdSku(int prodId, List<Integer> prodPropEnumIds);
	
	List<RcProdPropInfo> findProdPropInfosByProdId(int prodId);
	
	List<RcOrderProdEvaluation> findPagedOrderProdEvaluationsByProdIdAndPackageServiceSkuPropEnumId(int prodId, Integer prodPropEnumId, Page page);
	
//	RcMerchant findMerchantByProdId(int prodId);
	
//	List<RcProdSkuPropInfo> findApplicatorSku(RcProd rcProd);
	
	List<RcProdSkuPropEnum> findServiceSkuPropEnums(int prodId);
}
