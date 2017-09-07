package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcProd;
import com.socool.soft.bo.RcProdCatProp;
import com.socool.soft.bo.RcProdCatSkuProp;
import com.socool.soft.bo.RcProdImg;
import com.socool.soft.bo.RcProdProp;
import com.socool.soft.bo.RcProdPropEnum;
import com.socool.soft.bo.RcProdPropInfo;
import com.socool.soft.bo.RcProdSku;
import com.socool.soft.bo.RcProdSkuProp;
import com.socool.soft.bo.RcProdSkuPropEnum;
import com.socool.soft.bo.RcProdSkuPropInfo;
import com.socool.soft.bo.constant.ProdStatusEnum;
import com.socool.soft.bo.constant.ProdTypeEnum;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IProdImgService;
import com.socool.soft.service.IProdPropInfoService;
import com.socool.soft.service.IProdPropService;
import com.socool.soft.service.IProdService;
import com.socool.soft.service.IProdSkuPropInfoService;
import com.socool.soft.service.IProdSkuPropService;
import com.socool.soft.service.IProdSkuService;
import com.socool.soft.service.IProductManagerService;
import com.socool.soft.service.ISearchService;
import com.socool.soft.vo.ProdPropListDTO;
import com.socool.soft.vo.ProductPublishedDTO;

@Service
public class ProductManagerServiceImpl implements IProductManagerService {
	@Autowired
	private IProdPropService prodPropService;
	@Autowired
	private IProdSkuPropService prodSkuPropService;
	@Autowired
	private IProdPropInfoService prodPropInfoService;
	@Autowired
	private IProdService prodService;
	@Autowired
	private IProdImgService prodImgService;
	@Autowired
	private IProdSkuService prodSkuService;
	@Autowired
	private IProdSkuPropInfoService prodSkuPropInfoService;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private ISearchService searchService;

	@Override
	public List<ProdPropListDTO> findProdPropsByProdCatId(int prodCatId) {
		RcProdProp param = new RcProdProp();
		List<RcProdCatProp> prodCatProps = prodPropService.findProdCatPropsByProdCatId(prodCatId);
		if(CollectionUtils.isEmpty(prodCatProps)) {
			return new ArrayList<ProdPropListDTO>();
		}
		List<Integer> prodPropIds = new ArrayList<Integer>();
		for(RcProdCatProp prodCatProp : prodCatProps) {
			prodPropIds.add(prodCatProp.getProdPropId());
		}
		param.setProdPropIds(prodPropIds);
		List<RcProdProp> prodProps = prodPropService.findProdProps(param);
		List<ProdPropListDTO> result = new ArrayList<ProdPropListDTO>();
		for(RcProdProp prodProp : prodProps) {
			int prodPropId = prodProp.getProdPropId();
			List<RcProdPropEnum> prodPropEnums = prodPropService.findProdPropEnumsByProdPropId(prodPropId);
			for(RcProdPropEnum prodPropEnum : prodPropEnums) {
				ProdPropListDTO prodPropListDTO = new ProdPropListDTO();
				prodPropListDTO.setProdPropEnum(prodPropEnum.getProdPropEnum());
				prodPropListDTO.setProdPropEnumId(prodPropEnum.getProdPropEnumId());
				prodPropListDTO.setProdPropId(prodPropId);
				prodPropListDTO.setProdPropName(prodProp.getName());
				result.add(prodPropListDTO);
			}
//			ProdPropListDTO prodPropListDTO = new ProdPropListDTO();
//			prodPropListDTO.setProdPropId(prodPropId);
//			prodPropListDTO.setProdPropName(prodProp.getName());
//			result.add(prodPropListDTO);
		}
		return result;
	}
	
	@Override
	public List<ProdPropListDTO> findProdSkuPropsByProdCatId(int prodCatId, int prodType) {
		RcProdSkuProp param = new RcProdSkuProp();
		List<RcProdCatSkuProp> prodCatSkuProps = prodSkuPropService.findProdCatSkuPropsByProdCatId(prodCatId);
		if(CollectionUtils.isEmpty(prodCatSkuProps)) {
			return new ArrayList<ProdPropListDTO>();
		}
		List<Integer> prodSkuPropIds = new ArrayList<Integer>();
		for(RcProdCatSkuProp prodCatSkuProp : prodCatSkuProps) {
			prodSkuPropIds.add(prodCatSkuProp.getProdPropId());
		}
		param.setProdPropIds(prodSkuPropIds);
		if(prodType != ProdTypeEnum.SERVICE.getValue()) {
			param.setIsPackService(YesOrNoEnum.NO.getValue());
		}
		List<RcProdSkuProp> prodSkuProps = prodSkuPropService.findProdSkuProps(param);
		List<ProdPropListDTO> result = new ArrayList<ProdPropListDTO>();
		for(RcProdSkuProp prodSkuProp : prodSkuProps) {
			int prodPropId = prodSkuProp.getProdPropId();
			List<RcProdSkuPropEnum> prodSkuPropEnums = prodSkuPropService.findProdSkuPropEnumsByProdSkuPropId(prodPropId);
			for(RcProdSkuPropEnum prodSkuPropEnum : prodSkuPropEnums) {
				ProdPropListDTO prodPropListDTO = new ProdPropListDTO();
				prodPropListDTO.setIsApplicator(prodSkuProp.getIsPackService());
				prodPropListDTO.setIsColor(prodSkuProp.getHasImg());
				prodPropListDTO.setProdPropEnum(prodSkuPropEnum.getProdPropEnum());
				prodPropListDTO.setProdPropEnumId(prodSkuPropEnum.getProdPropEnumId());
				prodPropListDTO.setProdPropId(prodPropId);
				prodPropListDTO.setProdPropName(prodSkuProp.getName());
				result.add(prodPropListDTO);
			}
		}
		return result;
	}
	
	public int createProd(ProductPublishedDTO productPublishedDTO) {
		RcProd prod = new RcProd();
		prod.setName(productPublishedDTO.getProdName());
		prod.setAd(productPublishedDTO.getProdAdword());
		prod.setStockWarning(productPublishedDTO.getProdStockWarning());
		prod.setMeasureUnit(productPublishedDTO.getProdMeasureUnit());
		prod.setOriginPrice(productPublishedDTO.getProdOriginPrice());
		prod.setPrice(productPublishedDTO.getProdPrice());
		prod.setProdBrandId(productPublishedDTO.getProdBrandId());
		if(productPublishedDTO.getProdBrandId() == 0) {
			prod.setProdBrandName(productPublishedDTO.getProdBrandName());
		}
		prod.setMerchantId(productPublishedDTO.getMerchantId());
		prod.setProdCatId(productPublishedDTO.getProdCatId());
		prod.setType(productPublishedDTO.getProductType());
		prod.setDetail(productPublishedDTO.getProdDetail());
		if(productPublishedDTO.getMerchantId() > 0) {
			RcMerchant merchant = merchantService.findMerchantById(productPublishedDTO.getMerchantId());
			prod.setCityId(merchant.getCityId());
			prod.setVendorId(merchant.getVendorId());
		} else {
			prod.setCityId(0);
			prod.setVendorId(0);
		}
		prod.setBaseProdId(productPublishedDTO.getBaseProdId());
		if(CollectionUtils.isEmpty(productPublishedDTO.getProdSkus())) {
			prod.setInventory(productPublishedDTO.getProdStorage());
		} else {
			int inventory = 0;
			for(RcProdSku prodSku : productPublishedDTO.getProdSkus()) {
				inventory += prodSku.getInventory();
			}
			prod.setInventory(inventory);
		}
		int prodId = prodService.addProd(prod);

		for(RcProdPropInfo prodPropInfo : productPublishedDTO.getProdPropInfos()) {
			prodPropInfo.setProdId(prodId);
			prodPropInfoService.addProdPropInfo(prodPropInfo);
		}

		int seq = 1;
		for(String prodImgUrl : productPublishedDTO.getProdImgUrls()) {
			RcProdImg prodImg = new RcProdImg();
			prodImg.setProdId(prodId);
			prodImg.setProdSkuId(String.valueOf(prodId));
			prodImg.setImgUrl(prodImgUrl);
			prodImg.setSeq(seq++);
			prodImgService.addProdImg(prodImg);
		}
		
		if(CollectionUtils.isEmpty(productPublishedDTO.getProdSkus())) {
		} else {
			int index = 0;
			for(RcProdSku prodSku : productPublishedDTO.getProdSkus()) {
				List<Integer> prodSkuPropEnumIds = new ArrayList<Integer>();
				for(RcProdSkuPropInfo prodSkuPropInfo : productPublishedDTO.getProdSkuPropInfoMap().get(index)) {
					prodSkuPropEnumIds.add(prodSkuPropInfo.getProdPropEnumId());
				}
				Collections.sort(prodSkuPropEnumIds);
				StringBuilder sb = new StringBuilder(String.valueOf(prodId));
				for(Integer prodPropEnumId : prodSkuPropEnumIds) {
					sb.append("_").append(prodPropEnumId);
				}
				String prodSkuId = sb.toString();
				prodSku.setProdSkuId(prodSkuId);
				prodSku.setProdId(prodId);
				prodSkuService.addProdSku(prodSku);
				
				if(!CollectionUtils.isEmpty(productPublishedDTO.getProdSkuImgUrlsMap())) {
					Iterator<Entry<Integer, List<String>>> iter = productPublishedDTO.getProdSkuImgUrlsMap().entrySet().iterator();
					while(iter.hasNext()) {
						Entry<Integer, List<String>> entry = iter.next();
						int prodPropEnumId = entry.getKey();
						if(!prodSkuPropEnumIds.contains(prodPropEnumId)) {
							continue;
						}
						List<String> prodSkuImgUrls = entry.getValue();
						seq = 1; 
						for(String skuImgUrl : prodSkuImgUrls) {
							RcProdImg prodImgRel = new RcProdImg();
							prodImgRel.setProdId(prodId);
							prodImgRel.setProdSkuId(prodSkuId);
							prodImgRel.setImgUrl(skuImgUrl);
							prodImgRel.setSeq(seq++);
							prodImgService.addProdImg(prodImgRel);
						}
					}
				} else if(!CollectionUtils.isEmpty(productPublishedDTO.getSkuImgUrlsMap())) {
					List<String> skuImgUrls = productPublishedDTO.getSkuImgUrlsMap().get(index);
					seq = 1; 
					for(String skuImgUrl : skuImgUrls) {
						RcProdImg prodImgRel = new RcProdImg();
						prodImgRel.setProdId(prodId);
						prodImgRel.setProdSkuId(prodSkuId);
						prodImgRel.setImgUrl(skuImgUrl);
						prodImgRel.setSeq(seq++);
						prodImgService.addProdImg(prodImgRel);
					}
				}
				
				for(RcProdSkuPropInfo prodSkuPropInfo : productPublishedDTO.getProdSkuPropInfoMap().get(index)) {
					prodSkuPropInfo.setProdId(prodId);
					prodSkuPropInfo.setProdSkuId(prodSkuId);
					RcProdSkuProp prodSkuProp = prodSkuPropService.findProdSkuPropById(prodSkuPropInfo.getProdPropId());
					prodSkuPropInfo.setHasImg(prodSkuProp.getHasImg());
					prodSkuPropInfo.setIsPackService(prodSkuProp.getIsPackService());
					prodSkuPropInfoService.addProdSkuPropInfo(prodSkuPropInfo);
				}
				index++;
			}
		}
		return prodId;
	}

	@Override
	public int publishProduct(ProductPublishedDTO productPublishedDTO) {
		if(productPublishedDTO.isIfAdded()) {
			int prodId = createProd(productPublishedDTO);
			return prodId;
		} else {
			int prodId = productPublishedDTO.getProdId();
			RcProd prod = prodService.findProdById(prodId);
			prod.setName(productPublishedDTO.getProdName());
			prod.setAd(productPublishedDTO.getProdAdword());
			prod.setStorage(productPublishedDTO.getProdStorage());
			prod.setStockWarning(productPublishedDTO.getProdStockWarning());
			prod.setMeasureUnit(productPublishedDTO.getProdMeasureUnit());
			prod.setOriginPrice(productPublishedDTO.getProdOriginPrice());
			prod.setPrice(productPublishedDTO.getProdPrice());
			prod.setProdBrandId(productPublishedDTO.getProdBrandId());
			if(productPublishedDTO.getProdBrandId() == 0) {
				prod.setProdBrandName(productPublishedDTO.getProdBrandName());
			}
			prod.setMerchantId(productPublishedDTO.getMerchantId());
			prod.setProdCatId(productPublishedDTO.getProdCatId());
			prod.setType(productPublishedDTO.getProductType());
			prod.setDetail(productPublishedDTO.getProdDetail());
			if(productPublishedDTO.getMerchantId() > 0) {
				RcMerchant merchant = merchantService.findMerchantById(productPublishedDTO.getMerchantId());
				prod.setCityId(merchant.getCityId());
				prod.setVendorId(merchant.getVendorId());
			} else {
				prod.setCityId(0);
				prod.setVendorId(0);
			}
			if(CollectionUtils.isEmpty(productPublishedDTO.getProdSkus())) {
				prod.setInventory(productPublishedDTO.getProdStorage());
			} else {
				int inventory = 0;
				for(RcProdSku prodSku : productPublishedDTO.getProdSkus()) {
					inventory += prodSku.getInventory();
				}
				prod.setInventory(inventory);
			}
			prodService.modifyProd(prod);

			prodPropInfoService.removeProdPropInfosByProdId(prodId);
			for(RcProdPropInfo prodPropInfo : productPublishedDTO.getProdPropInfos()) {
				prodPropInfo.setProdId(prodId);
				prodPropInfoService.addProdPropInfo(prodPropInfo);
			}
			
			prodImgService.removeProdImgsByProdId(prodId);
			int seq = 1;
			for(String prodImgUrl : productPublishedDTO.getProdImgUrls()) {
				RcProdImg prodImgRel = new RcProdImg();
				prodImgRel.setProdId(prodId);
				prodImgRel.setProdSkuId(String.valueOf(prodId));
				prodImgRel.setImgUrl(prodImgUrl);
				prodImgRel.setSeq(seq++);
				prodImgService.addProdImg(prodImgRel);
			}
			
			prodSkuService.removeProdSkusByProdId(prodId);
			prodSkuPropInfoService.removeProdSkuPropInfosByProdId(prodId);
			if(CollectionUtils.isEmpty(productPublishedDTO.getProdSkus())) {
			} else {
				int index = 0;
				for(RcProdSku prodSku : productPublishedDTO.getProdSkus()) {
					List<Integer> prodPropEnumIds = new ArrayList<Integer>();
					for(RcProdSkuPropInfo prodSkuPropInfo : productPublishedDTO.getProdSkuPropInfoMap().get(index)) {
						prodPropEnumIds.add(prodSkuPropInfo.getProdPropEnumId());
					}
					Collections.sort(prodPropEnumIds);
					StringBuilder sb = new StringBuilder(String.valueOf(prodId));
					for(Integer prodPropEnumId : prodPropEnumIds) {
						sb.append("_").append(prodPropEnumId);
					}
					String prodSkuId = sb.toString();
					prodSku.setProdSkuId(prodSkuId);
					prodSku.setProdId(prodId);
					prodSkuService.addProdSku(prodSku);
					
					Iterator<Entry<Integer, List<String>>> iter = productPublishedDTO.getProdSkuImgUrlsMap().entrySet().iterator();
					while(iter.hasNext()) {
						Entry<Integer, List<String>> entry = iter.next();
						int prodPropEnumId = entry.getKey();
						if(!prodPropEnumIds.contains(prodPropEnumId)) {
							continue;
						}
						List<String> prodSkuImgUrls = entry.getValue();
						seq = 1; 
						for(String skuImgUrl : prodSkuImgUrls) {
							RcProdImg prodImgRel = new RcProdImg();
							prodImgRel.setProdId(prodId);
							prodImgRel.setProdSkuId(prodSkuId);
							prodImgRel.setImgUrl(skuImgUrl);
							prodImgRel.setSeq(seq++);
							prodImgService.addProdImg(prodImgRel);
						}
					}
					
					for(RcProdSkuPropInfo prodSkuPropInfo : productPublishedDTO.getProdSkuPropInfoMap().get(index)) {
						prodSkuPropInfo.setProdId(prodId);
						prodSkuPropInfo.setProdSkuId(prodSkuId);
						RcProdSkuProp prodSkuProp = prodSkuPropService.findProdSkuPropById(prodSkuPropInfo.getProdPropId());
						prodSkuPropInfo.setHasImg(prodSkuProp.getHasImg());
						prodSkuPropInfo.setIsPackService(prodSkuProp.getIsPackService());
						prodSkuPropInfoService.addProdSkuPropInfo(prodSkuPropInfo);
					}
					index++;
				}
			}
			return prodId;
		}
	}

	@Override
	public int publishBaseProduct(ProductPublishedDTO productPublishedDTO) {
		if(productPublishedDTO.isIfAdded()) {
			int baseProdId = createProd(productPublishedDTO);
			return baseProdId;
		} else {
			int prodId = productPublishedDTO.getProdId();
			RcProd prod = prodService.findProdById(prodId);
			prod.setName(productPublishedDTO.getProdName());
			prod.setAd(productPublishedDTO.getProdAdword());
			prod.setStorage(productPublishedDTO.getProdStorage());
			prod.setStockWarning(productPublishedDTO.getProdStockWarning());
			prod.setMeasureUnit(productPublishedDTO.getProdMeasureUnit());
			prod.setOriginPrice(productPublishedDTO.getProdOriginPrice());
			prod.setPrice(productPublishedDTO.getProdPrice());
			prod.setProdBrandId(productPublishedDTO.getProdBrandId());
			if(productPublishedDTO.getProdBrandId() == 0) {
				prod.setProdBrandName(productPublishedDTO.getProdBrandName());
			} else {
				prod.setProdBrandName("");
			}
			prod.setMerchantId(0);
			prod.setVendorId(0);
			prod.setProdCatId(productPublishedDTO.getProdCatId());
			prod.setType(productPublishedDTO.getProductType());
			prod.setDetail(productPublishedDTO.getProdDetail());
			if(CollectionUtils.isEmpty(productPublishedDTO.getProdSkus())) {
				prod.setInventory(productPublishedDTO.getProdStorage());
			} else {
				int inventory = 0;
				for(RcProdSku prodSku : productPublishedDTO.getProdSkus()) {
					inventory += prodSku.getInventory();
				}
				prod.setInventory(inventory);
			}
			prodService.modifyProd(prod);

			prodPropInfoService.removeProdPropInfosByProdId(prodId);
			for(RcProdPropInfo prodPropInfo : productPublishedDTO.getProdPropInfos()) {
				prodPropInfo.setProdId(prodId);
				prodPropInfoService.addProdPropInfo(prodPropInfo);
			}
			
			prodImgService.removeProdImgsByProdId(prodId);
			int seq = 1;
			for(String prodImgUrl : productPublishedDTO.getProdImgUrls()) {
				RcProdImg prodImgRel = new RcProdImg();
				prodImgRel.setProdId(prodId);
				prodImgRel.setProdSkuId(String.valueOf(prodId));
				prodImgRel.setImgUrl(prodImgUrl);
				prodImgRel.setSeq(seq++);
				prodImgService.addProdImg(prodImgRel);
			}
			
			prodSkuService.removeProdSkusByProdId(prodId);
			prodSkuPropInfoService.removeProdSkuPropInfosByProdId(prodId);
			if(CollectionUtils.isEmpty(productPublishedDTO.getProdSkus())) {
			} else {
				int index = 0;
				for(RcProdSku prodSku : productPublishedDTO.getProdSkus()) {
					List<Integer> prodPropEnumIds = new ArrayList<Integer>();
					for(RcProdSkuPropInfo prodSkuPropInfo : productPublishedDTO.getProdSkuPropInfoMap().get(index)) {
						prodPropEnumIds.add(prodSkuPropInfo.getProdPropEnumId());
					}
					Collections.sort(prodPropEnumIds);
					StringBuilder sb = new StringBuilder(String.valueOf(prodId));
					for(Integer prodPropEnumId : prodPropEnumIds) {
						sb.append("_").append(prodPropEnumId);
					}
					String prodSkuId = sb.toString();
					prodSku.setProdSkuId(prodSkuId);
					prodSku.setProdId(prodId);
					prodSkuService.addProdSku(prodSku);
					
					Iterator<Entry<Integer, List<String>>> iter = productPublishedDTO.getProdSkuImgUrlsMap().entrySet().iterator();
					while(iter.hasNext()) {
						Entry<Integer, List<String>> entry = iter.next();
						int prodPropEnumId = entry.getKey();
						if(!prodPropEnumIds.contains(prodPropEnumId)) {
							continue;
						}
						List<String> prodSkuImgUrls = entry.getValue();
						seq = 1; 
						for(String skuImgUrl : prodSkuImgUrls) {
							RcProdImg prodImgRel = new RcProdImg();
							prodImgRel.setProdId(prodId);
							prodImgRel.setProdSkuId(prodSkuId);
							prodImgRel.setImgUrl(skuImgUrl);
							prodImgRel.setSeq(seq++);
							prodImgService.addProdImg(prodImgRel);
						}
					}
					
					for(RcProdSkuPropInfo prodSkuPropInfo : productPublishedDTO.getProdSkuPropInfoMap().get(index)) {
						prodSkuPropInfo.setProdId(prodId);
						prodSkuPropInfo.setProdSkuId(prodSkuId);
						RcProdSkuProp prodSkuProp = prodSkuPropService.findProdSkuPropById(prodSkuPropInfo.getProdPropId());
						prodSkuPropInfo.setHasImg(prodSkuProp.getHasImg());
						prodSkuPropInfo.setIsPackService(prodSkuProp.getIsPackService());
						prodSkuPropInfoService.addProdSkuPropInfo(prodSkuPropInfo);
					}
					index++;
				}
			}
			return prodId;
		}
	}

	@Override
	public void doProdPutAway(int prodId) {
		RcProd prod = prodService.findProdById(prodId);
		prod.setStatus(ProdStatusEnum.PUTAWAY.getValue());
		prod.setPutawayTime(new Date());
		prodService.modifyProd(prod);
		searchService.updateIndexForProdPutUpDown(prodId);
	}

	@Override
	public void doProdOutOfStock(int prodId) {
		RcProd prod = prodService.findProdById(prodId);
		prod.setStatus(ProdStatusEnum.SOLD_OUT.getValue());
		prod.setSoldoutTime(new Date());
		prodService.modifyProd(prod);
		searchService.updateIndexForProdPutUpDown(prodId);
	}

	@Override
	public List<Map<String, Object>> querySkuStoragesBySkuId(int prodId) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<RcProdSku> prodSkus = prodSkuService.findProdSkusByProdId(prodId);
		for(RcProdSku prodSku : prodSkus) {
			String prodSkuId = prodSku.getProdSkuId();
			List<RcProdSkuPropInfo> prodSkuPropInfos = prodSkuPropInfoService.findProdSkuPropInfosByProdSkuId(prodSkuId);
			StringBuilder sb = new StringBuilder("");
			int index = 0;
			for(RcProdSkuPropInfo prodSkuPropInfo : prodSkuPropInfos) {
				if(index > 0) {
					sb.append(" * ");
				}
				sb.append(prodSkuPropInfo.getProdPropVal());
				index++;
			}
			Map<String, Object> inventoryInfo = new HashMap<String, Object>();
			inventoryInfo.put("PROD_ID", prodSkuId);
			inventoryInfo.put("PROD_PROP_VAL", sb.toString());
			inventoryInfo.put("PROD_STORAGE", prodSku.getInventory());
			inventoryInfo.put("SKU_ORIGIN_PRICE", prodSku.getOriginPrice());
			inventoryInfo.put("SKU_PRICE", prodSku.getPrice());
			
			result.add(inventoryInfo);
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> queryProdStorageByProdId(int prodId) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		RcProd prod = prodService.findProdById(prodId);
		Map<String, Object> inventoryInfo = new HashMap<String, Object>();
		inventoryInfo.put("PROD_ID", prodId);
		inventoryInfo.put("PROD_PROP_VAL", prod.getName());
		inventoryInfo.put("PROD_STORAGE", prod.getInventory());
		inventoryInfo.put("SKU_ORIGIN_PRICE", prod.getOriginPrice());
		inventoryInfo.put("SKU_PRICE", prod.getPrice());
		result.add(inventoryInfo);
		return result;
	}

//	@Override
//	public RcProdSku findProdSkuById(String prodSkuId) {
//		return prodSkuService.findProdSkuByProdSkuId(prodSkuId);
//	}

//	@Override
//	public int modifyProdSku(RcProdSku rcProdSku) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	@Override
	public ProductPublishedDTO getProductDetail(int prodId) {
		ProductPublishedDTO productPublishedDTO = new ProductPublishedDTO();
		
		RcProd prod = prodService.findProdById(prodId);
		productPublishedDTO.setProdId(prodId);
		productPublishedDTO.setBaseProdId(prod.getBaseProdId());
		productPublishedDTO.setProdName(prod.getName());
		productPublishedDTO.setProdAdword(prod.getAd());
		productPublishedDTO.setProdStorage(prod.getInventory());
		productPublishedDTO.setProdStockWarning(prod.getStockWarning());
		productPublishedDTO.setProdMeasureUnit(prod.getMeasureUnit());
		productPublishedDTO.setProdOriginPrice(prod.getOriginPrice());
		productPublishedDTO.setProdPrice(prod.getPrice());
		productPublishedDTO.setProdBrandId(prod.getProdBrandId());
		productPublishedDTO.setProdBrandName(prod.getProdBrandName());
		productPublishedDTO.setMerchantId(prod.getMerchantId());
		productPublishedDTO.setProdCatId(prod.getProdCatId());
		productPublishedDTO.setProductType(prod.getType());
		productPublishedDTO.setProdDetail(prod.getDetail());

		List<RcProdImg> prodImgs = prodImgService.findProdImgsByProdSkuId(String.valueOf(prodId));
		List<String> prodImgUrls = new ArrayList<String>();
		for(RcProdImg prodImg : prodImgs) {
			prodImgUrls.add(prodImg.getImgUrl());
		}
		List<RcProdPropInfo> prodPropInfos = prodPropInfoService.findProdPropInfosByProdId(prodId);
		
		List<RcProdSku> prodSkus = prodSkuService.findProdSkusByProdId(prodId);
		Map<Integer, List<String>> skuImgUrlsMap = new HashMap<Integer, List<String>>();
		Map<Integer, List<RcProdSkuPropInfo>> prodSkuPropInfoMap = new HashMap<Integer, List<RcProdSkuPropInfo>>();
		int seq = 0;
		for(RcProdSku prodSku : prodSkus) {
			String prodSkuId = prodSku.getProdSkuId();
			
			prodImgs = prodImgService.findProdImgsByProdSkuId(prodSkuId);
			List<String> skuImgUrls = new ArrayList<String>();
			for(RcProdImg prodImgRel : prodImgs) {
				skuImgUrls.add(prodImgRel.getImgUrl());
			}
			skuImgUrlsMap.put(seq, skuImgUrls);
			
			List<RcProdSkuPropInfo> prodSkuPropInfos = prodSkuPropInfoService.findProdSkuPropInfosByProdSkuId(prodSkuId);
			prodSkuPropInfoMap.put(seq, prodSkuPropInfos);
			
			seq++;
		}
		
		productPublishedDTO.setProdImgUrls(prodImgUrls);
		productPublishedDTO.setSkuImgUrlsMap(skuImgUrlsMap);
		productPublishedDTO.setProdPropInfos(prodPropInfos);
		productPublishedDTO.setProdSkus(prodSkus);
		productPublishedDTO.setProdSkuPropInfoMap(prodSkuPropInfoMap);
		
		return productPublishedDTO;
	}

	@Override
	public void copyProduct(int prodId, int merchantId) {
		ProductPublishedDTO productPublishedDTO = getProductDetail(prodId);
		productPublishedDTO.setIfAdded(true);
		productPublishedDTO.setMerchantId(merchantId);
		productPublishedDTO.setBaseProdId(prodId);
		productPublishedDTO.setProdId(null);
		for(RcProdPropInfo prodPropInfo : productPublishedDTO.getProdPropInfos()) {
			prodPropInfo.setProdPropInfoId(null);
			prodPropInfo.setProdId(null);
		}
		for(RcProdSku prodSku : productPublishedDTO.getProdSkus()) {
			prodSku.setProdId(null);
			prodSku.setProdSkuId(null);
		}
		Iterator<Entry<Integer, List<RcProdSkuPropInfo>>> iter = productPublishedDTO.getProdSkuPropInfoMap().entrySet().iterator();
		while(iter.hasNext()) {
			Entry<Integer, List<RcProdSkuPropInfo>> entry = iter.next();
			for(RcProdSkuPropInfo prodSkuPropInfo : entry.getValue()) {
				prodSkuPropInfo.setProdSkuPropInfoId(null);
				prodSkuPropInfo.setProdId(null);
				prodSkuPropInfo.setProdSkuId(null);
			}
		}
		publishProduct(productPublishedDTO);
	}
}
