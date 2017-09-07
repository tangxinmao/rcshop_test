package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcCity;
import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcProd;
import com.socool.soft.bo.RcProdBrand;
import com.socool.soft.bo.RcProdCat;
import com.socool.soft.bo.RcProdSku;
import com.socool.soft.bo.constant.ProdPriceMannerEnum;
import com.socool.soft.bo.constant.ProdStatusEnum;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.dao.RcProdMapper;
import com.socool.soft.service.ICityService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IProdBrandService;
import com.socool.soft.service.IProdCatService;
import com.socool.soft.service.IProdImgService;
import com.socool.soft.service.IProdService;
import com.socool.soft.service.IProdSkuService;
import com.socool.soft.service.IVendorService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class ProdServiceImpl implements IProdService {
	@Autowired
	private RcProdMapper prodMapper;
	@Autowired
	private IProdBrandService prodBrandService;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private IProdCatService prodCatService;
	@Autowired
	private IProdSkuService prodSkuService;
	@Autowired
	private ICityService cityService;
	@Autowired
	private IProdImgService prodImgService;
	@Autowired
	private IVendorService vendorService;

	
	@Override
	public RcProd findProdById(int prodId) {
		return prodMapper.selectByPrimaryKey(prodId);
	}

//	@Override
//	public RcProdCat findProdCatById(int prodCatId) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public RcProdImgRel findFirstImgByProdId(int prodId) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public List<RcProd> findProdByProdIds(List<Integer> prodIds) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public int modifyProd(RcProd prod) {
		prod.setUpdateTime(new Date());
		if(prodMapper.updateByPrimaryKey(prod) > 0) {
			return prod.getProdId();
		}
		return 0;
	}

	@Override
	public int removeProd(int prodId) {
		RcProd param = new RcProd();
		param.setProdId(prodId);
		param.setDelFlag(YesOrNoEnum.YES.getValue());
		if(modifyProd(param) > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public List<RcProd> findProds(RcProd param) {
		
		return prodMapper.select(param);
	}

	@Override
	public List<RcProd> findPagedProds(RcProd param, Page page) {
		if(StringUtils.isNotBlank(param.getMerchantName()) || param.getVendorId() != null) {
			RcMerchant merchantParam = new RcMerchant();
			merchantParam.setName(param.getMerchantName());
			merchantParam.setVendorId(param.getVendorId());
			List<RcMerchant> merchants = merchantService.findMerchants(merchantParam);
			if(CollectionUtils.isEmpty(merchants)) {
				return new ArrayList<RcProd>();
			}
			List<Integer> merchantIds = new ArrayList<Integer>();
			for(RcMerchant merchant : merchants) {
				merchantIds.add(merchant.getMerchantId());
			}
			param.setMerchantIds(merchantIds);
		}
		if(StringUtils.isNotBlank(param.getCityName())) {
			List<RcCity> cities = cityService.findCitiesByName(param.getCityName());
			if(CollectionUtils.isEmpty(cities)) {
				return new ArrayList<RcProd>();
			}
			List<Integer> cityIds = new ArrayList<Integer>();
			for(RcCity city : cities) {
				cityIds.add(city.getCityId());
			}
			param.setCityIds(cityIds);
		}
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		PageContext.setPage(page);
		List<RcProd> prods = null;
		if(param.getProdId() != null) {
			RcProd prod = findProdById(param.getProdId());
			if(prod == null) {
				prods = new ArrayList<RcProd>();
			} else {
				prods = Arrays.asList(prod);
			}
		} else {
			prods = prodMapper.select(param);
		}
		for(RcProd prod : prods) {
			prod.setMerchant(merchantService.findMerchantById(prod.getMerchantId()));
			prod.setCity(cityService.findCityById(prod.getCityId()));
			prod.setVendor(vendorService.findVendorById(prod.getVendorId()));
			prod.setProdImgUrl(prodImgService.findFirstProdImgByProdSkuId(String.valueOf(prod.getProdId())).getImgUrl());
			prod.setProdSkus(prodSkuService.findProdSkusByProdId(prod.getProdId()));
			prod.setProdBrand(prodBrandService.findProdBrandById(prod.getProdBrandId()));
		}
		return prods;
	}

	@Override
	public int addProd(RcProd prod) {
		prod.setPriceManner(ProdPriceMannerEnum.BY_QUANTITY.getValue());
		prod.setMeasureUnitCount(1);
		prod.setCreateTime(new Date());
		if(prodMapper.insert(prod) > 0) {
			return prod.getProdId();
		}
		return 0;
	}

	@Override
	public RcProd findProdForSolr(int prodId) {
		RcProd prod = findProdById(prodId);
		if(prod.getProdBrandId() > 0) {
			RcProdBrand prodBrand = prodBrandService.findProdBrandById(prod.getProdBrandId());
			prod.setProdBrand(prodBrand);
		}
		RcMerchant merchant = merchantService.findMerchantById(prod.getMerchantId());
		prod.setMerchant(merchant);
		RcProdCat prodCat = prodCatService.findProdCatById(prod.getProdCatId());
		prod.setProdCat(prodCat);
		List<RcProdSku> prodSkus = prodSkuService.findProdSkusByProdId(prodId);
		prod.setSkuCount(prodSkus.size());
		RcCity city = cityService.findCityById(prod.getCityId());
		prod.setCity(city);
		prod.setProdImgUrl(prodImgService.findFirstProdImgByProdSkuId(String.valueOf(prod.getProdId())).getImgUrl());
		return prod;
	}

	@Override
	public List<RcProd> findProdsForSolr() {
		RcProd param = new RcProd();
		param.setStatus(ProdStatusEnum.PUTAWAY.getValue());
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		List<RcProd> prods = prodMapper.select(param);
		for(RcProd prod: prods){
			if(prod.getProdBrandId() > 0) {
				RcProdBrand prodBrand = prodBrandService.findProdBrandById(prod.getProdBrandId());
				prod.setProdBrand(prodBrand);
			}
			RcMerchant merchant = merchantService.findMerchantById(prod.getMerchantId());
			prod.setMerchant(merchant);
			RcProdCat prodCat = prodCatService.findProdCatById(prod.getProdCatId());
			prod.setProdCat(prodCat);
			List<RcProdSku> prodSkus = prodSkuService.findProdSkusByProdId(prod.getProdId());
			prod.setSkuCount(prodSkus.size());
			RcCity city = cityService.findCityById(prod.getCityId());
			prod.setCity(city);
			prod.setProdImgUrl(prodImgService.findFirstProdImgByProdSkuId(String.valueOf(prod.getProdId())).getImgUrl());
		}
		return prods;
	}
}
