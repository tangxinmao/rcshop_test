package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcBuyer;
import com.socool.soft.bo.RcCity;
import com.socool.soft.bo.RcCoupon;
import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcMerchantUser;
import com.socool.soft.bo.RcOrderProdEvaluation;
import com.socool.soft.bo.RcProd;
import com.socool.soft.bo.RcProdBrand;
import com.socool.soft.bo.RcProdImg;
import com.socool.soft.bo.RcProdPropInfo;
import com.socool.soft.bo.RcProdSku;
import com.socool.soft.bo.RcProdSkuProp;
import com.socool.soft.bo.RcProdSkuPropEnum;
import com.socool.soft.bo.RcProdSkuPropInfo;
import com.socool.soft.service.IBuyerService;
import com.socool.soft.service.ICityService;
import com.socool.soft.service.ICouponService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IMerchantUserService;
import com.socool.soft.service.IOrderProdEvaluationService;
import com.socool.soft.service.IProdBrandService;
import com.socool.soft.service.IProdImgService;
import com.socool.soft.service.IProdPropInfoService;
import com.socool.soft.service.IProdService;
import com.socool.soft.service.IProdSkuPropInfoService;
import com.socool.soft.service.IProdSkuService;
import com.socool.soft.service.IProductAppService;
import com.socool.soft.util.VOConversionUtil;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class ProductAppServiceImpl implements IProductAppService {
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private IMerchantUserService merchantUserService;
	@Autowired
	private IProdImgService prodImgService;
	@Autowired
	private IProdBrandService prodBrandService;
	@Autowired
	private ICityService cityService;
	@Autowired
	private IOrderProdEvaluationService orderProdEvaluationService;
	@Autowired
	private IProdSkuService prodSkuService;
	@Autowired
	private IProdSkuPropInfoService prodSkuPropInfoService;
	@Autowired
	private IProdPropInfoService prodPropInfoService;
	@Autowired
	private IBuyerService buyerService;
	@Autowired
	private IProdService prodService;
	@Autowired
	private ICouponService couponService;
	@Override
	
	public RcProd findProdForAndroid(int prodId, int cityId, int buyerId) {
		RcProd prod = prodService.findProdById(prodId);
		RcMerchant merchant = merchantService.findMerchantById(prod.getMerchantId());
		RcMerchantUser merchantUser = merchantUserService.findMerchantSuperAdmin(prod.getMerchantId());
		merchant.setMerchantUserId(merchantUser.getMemberId());
		List<RcProdImg> prodImgs = prodImgService.findProdImgsByProdSkuId(String.valueOf(prodId));
		RcProdBrand prodBrand = prodBrandService.findProdBrandById(prod.getProdBrandId());
		RcCity city = cityService.findCityById(prod.getCityId());
		List<RcOrderProdEvaluation> orderProdEvaluations = orderProdEvaluationService.findOrderProdEvaluationsByProdId(prodId);
		List<RcProdSku> prodSkus = prodSkuService.findProdSkusByProdId(prodId);
		
		List<RcProdSkuPropInfo> prodSkuPropInfos = prodSkuPropInfoService.findProdSkuPropInfosByProdId(prodId);
		Map<Integer, List<RcProdSkuPropInfo>> prodSkuPropInfoGroups = new HashMap<Integer, List<RcProdSkuPropInfo>>();
		for(RcProdSkuPropInfo prodSkuPropInfo : prodSkuPropInfos) {
			if(!prodSkuPropInfoGroups.containsKey(prodSkuPropInfo.getProdPropId())) {
				prodSkuPropInfoGroups.put(prodSkuPropInfo.getProdPropId(), new ArrayList<RcProdSkuPropInfo>());
			}
			prodSkuPropInfoGroups.get(prodSkuPropInfo.getProdPropId()).add(prodSkuPropInfo);
		}
		
		List<RcProdSkuProp> prodSkuInfos = new ArrayList<RcProdSkuProp>();
		Iterator<Entry<Integer, List<RcProdSkuPropInfo>>> iter = prodSkuPropInfoGroups.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<Integer, List<RcProdSkuPropInfo>> entry = iter.next();
			Integer prodSkuPropId = entry.getKey();
			List<RcProdSkuPropInfo> skuInfos = entry.getValue();
			if(CollectionUtils.isEmpty(skuInfos)) {
				continue;
			}
			
			RcProdSkuProp prodSkuProp = new RcProdSkuProp();
			prodSkuProp.setProdPropId(prodSkuPropId);
			prodSkuProp.setName(skuInfos.get(0).getProdPropName());
			prodSkuProp.setHasImg(skuInfos.get(0).getHasImg());
			
			List<RcProdSkuPropEnum> prodSkuPropEnums = new ArrayList<RcProdSkuPropEnum>();
			Map<Integer, String> skuPropEnums = new HashMap<Integer, String>();
			for(RcProdSkuPropInfo skuInfo : skuInfos) {
				skuPropEnums.put(skuInfo.getProdPropEnumId(), skuInfo.getProdPropVal());
			}
			Iterator<Entry<Integer, String>> skuPropEnumIter = skuPropEnums.entrySet().iterator();
			while(skuPropEnumIter.hasNext()) {
				Entry<Integer, String> skuPropEnum = skuPropEnumIter.next();
				RcProdSkuPropEnum prodSkuPropEnum = new RcProdSkuPropEnum();
				prodSkuPropEnum.setProdPropEnumId(skuPropEnum.getKey());
				prodSkuPropEnum.setProdPropEnum(skuPropEnum.getValue());
				prodSkuPropEnums.add(prodSkuPropEnum);
			}
			prodSkuProp.setProdPropEnums(prodSkuPropEnums);
			
			prodSkuInfos.add(prodSkuProp);
		}
		List<RcCoupon> coupons = couponService.findCouponsForProd(buyerId, prodId);
		prod.setCoupons(coupons);
		prod.setStorage(prod.getInventory());
		VOConversionUtil.Entity2VO(prod, null, new String[] {"measureUnit", "stockWarning", "delFlag", 
				"createTime", "updateTime", "putawayTime", "soldoutTime", "prodCatId", "cityId", "detail", "inventory"});
		prod.setProdDetailUrl("/rcshop/prod/content?prodId=" + prodId);
		if(prod.getProdBrandId()>0){
			prod.setProdBrandName(prodBrand.getName());
		}
		prod.setSkuCount(prodSkus.size());
//		if(prodSkus.size()==0){
//			RcProdStorage prodStorage = prodStorageService.findProdStorageByProdId(prodId);
//			prod.setStorage(prodStorage.getStorage());
//		} else{
//			prod.setStorage(0);
//		}
//		if(prod.getSoldNum()==null){
//			prod.setSoldNum(0);
//		}
		prod.setEvaluationCount(orderProdEvaluations.size());
		prod.setCityName(city.getName());
		
		VOConversionUtil.Entity2VO(merchant, new String[] {"productScore", "serviceScore", "deliveryScore", 
				"name", "description", "logoUrl", "mobile", "merchantUserId"}, null);
		prod.setMerchant(merchant);
		
		if(!CollectionUtils.isEmpty(orderProdEvaluations)) {
			RcOrderProdEvaluation orderProdEvaluation = orderProdEvaluations.get(0);
			VOConversionUtil.Entity2VO(orderProdEvaluation, new String[] {"content", "memberId","createTime", "score"}, null);
			RcBuyer buyer = buyerService.findBuyerById(orderProdEvaluation.getMemberId());
			orderProdEvaluation.setMemberName(buyer.getName());
			prod.setEvaluation(orderProdEvaluation);
		} else{
			RcOrderProdEvaluation orderProdEvaluation = new RcOrderProdEvaluation();
			prod.setEvaluation(orderProdEvaluation);
		}
		
		for(RcProdImg prodImg : prodImgs) {
			VOConversionUtil.Entity2VO(prodImg, new String[] {"imgUrl"}, null);
		}
		prod.setProdImgs(prodImgs);
		
		prod.setProdSkuInfos(prodSkuInfos);
		
		return prod;
	}

//	@Override
//	public RcProd findProductById(int prodId) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public ProdSkuSelectedVO findProdSkuSelectedInfo(Map<String, Object> param) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	@Override
	public RcProdSku findSelectedProdSku(int prodId, List<Integer> prodSkuPropEnumIds) {
		if(CollectionUtils.isEmpty(prodSkuPropEnumIds)) {
			return null;
		}
		
		Collections.sort(prodSkuPropEnumIds);
		StringBuilder sb = new StringBuilder(String.valueOf(prodId));
		for(Integer prodSkuPropEnumId : prodSkuPropEnumIds) {
			sb.append("_").append(prodSkuPropEnumId);
		}
		String prodSkuId = sb.toString();
		RcProdSku prodSku = prodSkuService.findProdSkuByProdSkuId(prodSkuId);
		if(prodSku != null) {
			prodSku.setStorage(prodSku.getInventory());
			VOConversionUtil.Entity2VO(prodSku, new String[] {"prodSkuId", "originPrice", "price", "storage"}, null);
			
			List<RcProdImg> prodImgs = prodImgService.findProdImgsByProdSkuId(prodSkuId);
			for(RcProdImg prodImg : prodImgs) {
				VOConversionUtil.Entity2VO(prodImg, new String[] {"imgUrl"}, null);
			}
			prodSku.setProdImgs(prodImgs);
			
//			RcProdStorage prodStorage = prodStorageService.findProdStorage(prodId, prodSkuId);
//			prodSku.setStorage(prodStorage.getStorage());
		}
		return prodSku;
	}

	@Override
	public List<RcProdPropInfo> findProdPropInfosByProdId(int prodId) {
		List<RcProdPropInfo> prodPropInfos = prodPropInfoService.findProdPropInfosByProdId(prodId);
		for(RcProdPropInfo prodPropInfo : prodPropInfos) {
			VOConversionUtil.Entity2VO(prodPropInfo, new String[] {"prodPropName", "prodPropVal"}, null);
		}
		return prodPropInfos;
	}

	@Override
	public List<RcOrderProdEvaluation> findPagedOrderProdEvaluationsByProdIdAndPackageServiceSkuPropEnumId(int prodId, Integer prodSkuPropEnumId, Page page) {
		List<RcOrderProdEvaluation> orderProdEvaluations = null;
		if(prodSkuPropEnumId == null) {
			PageContext.setPage(page);
			orderProdEvaluations = orderProdEvaluationService.findOrderProdEvaluationsByProdId(prodId);
		} else {
			List<RcProdSkuPropInfo> prodSkuPropInfos = prodSkuPropInfoService.findProdSkuPropInfoByProdSkuPropEnumId(prodSkuPropEnumId);
			List<String> prodSkuIds = new ArrayList<String>();
			for(RcProdSkuPropInfo prodSkuPropInfo : prodSkuPropInfos) {
				if(!prodSkuIds.contains(prodSkuPropInfo.getProdSkuId())) {
					prodSkuIds.add(prodSkuPropInfo.getProdSkuId());
				}
			}

			PageContext.setPage(page);
			RcOrderProdEvaluation param = new RcOrderProdEvaluation();
			param.setProdId(prodId);
			param.setProdSkuIds(prodSkuIds);
			orderProdEvaluations = orderProdEvaluationService.findOrderProdEvaluations(param);
		}
		
		for(RcOrderProdEvaluation orderProdEvaluation : orderProdEvaluations) {
			VOConversionUtil.Entity2VO(orderProdEvaluation, new String[] {"content","memberId", "createTime", "score", "merchantReply"}, null);
			RcBuyer buyer = buyerService.findBuyerById(orderProdEvaluation.getMemberId());
			orderProdEvaluation.setMemberName(buyer.getName());
		}
		return orderProdEvaluations;
	}

//	@Override
//	public RcMerchant findMerchantByProdId(int prodId) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public List<RcProdSkuPropInfo> findApplicatorSku(RcProd rcProd) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public List<RcProdSkuPropEnum> findServiceSkuPropEnums(int prodId) {
		List<RcProdSkuPropEnum> result = new ArrayList<RcProdSkuPropEnum>();
		List<RcProdSkuPropInfo> prodSkuPropInfos = prodSkuPropInfoService.findProdSkuPropInfosByProdId(prodId);
		if(!CollectionUtils.isEmpty(prodSkuPropInfos)) {
			Map<Integer, String> skuPropEnumMap = new HashMap<Integer, String>();
			for(RcProdSkuPropInfo prodSkuPropInfo : prodSkuPropInfos) {
				if(!skuPropEnumMap.containsKey(prodSkuPropInfo.getProdPropEnumId())) {
					skuPropEnumMap.put(prodSkuPropInfo.getProdPropEnumId(), prodSkuPropInfo.getProdPropVal());
					RcProdSkuPropEnum prodSkuPropEnum = new RcProdSkuPropEnum();
					prodSkuPropEnum.setProdPropEnumId(prodSkuPropInfo.getProdPropEnumId());
					prodSkuPropEnum.setProdPropEnum(prodSkuPropInfo.getProdPropVal());
					result.add(prodSkuPropEnum);
				}
			}
		}
		return result;
	}
}
