package com.socool.soft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcAppProdRec;
import com.socool.soft.bo.RcCity;
import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcProd;
import com.socool.soft.dao.RcAppProdRecMapper;
import com.socool.soft.service.IAppProdRecService;
import com.socool.soft.service.ICityService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IProdImgService;
import com.socool.soft.service.IProdService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class AppProdRecServiceImpl implements IAppProdRecService {
	@Autowired
	private RcAppProdRecMapper appProdRecMapper;
	@Autowired
	private IProdService prodService;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private ICityService cityService;
	@Autowired
	private IProdImgService prodImgService;

	@Override
	public List<RcAppProdRec> findAppProdRecsByCityId(int cityId) {
		RcAppProdRec param = new RcAppProdRec();
		param.setCityId(cityId);
		param.setOrderBy("SEQ ASC");
		return appProdRecMapper.select(param);
	}
	
	@Override
	public List<RcAppProdRec> findAppProdRecsByCityId(int cityId, int limit) {
		RcAppProdRec param = new RcAppProdRec();
		param.setCityId(cityId);
		param.setOrderBy("SEQ ASC");
		param.setLimit(limit);
		return appProdRecMapper.select(param);
	}

	@Override
	public int addAppProdRec(RcAppProdRec appProdRec) {
		if(appProdRecMapper.insert(appProdRec) > 0) {
			return appProdRec.getAppProdRecId();
		}
		return 0;
	}

	@Override
	public int modifyAppProdRec(RcAppProdRec appProdRec) {
		if(appProdRecMapper.updateByPrimaryKey(appProdRec) > 0) {
			return appProdRec.getAppProdRecId();
		}
		return 0;
	}

	@Override
	public int removeAppProdRec(int appProdRecId) {
		return appProdRecMapper.deleteByPrimaryKey(appProdRecId);
	}

	@Override
	public List<RcAppProdRec> findPagedAppProdRecsByCityId(Integer cityId, Page page) {
		PageContext.setPage(page);
		RcAppProdRec param = new RcAppProdRec();
		param.setCityId(cityId);
		param.setOrderBy("CITY_ID ASC, SEQ ASC");
		List<RcAppProdRec> appProdRecs = appProdRecMapper.select(param);
		for(RcAppProdRec appProdRec : appProdRecs){
			RcProd prod = prodService.findProdById(appProdRec.getProdId());
			prod.setProdImgUrl(prodImgService.findFirstProdImgByProdSkuId(String.valueOf(prod.getProdId())).getImgUrl());
			RcMerchant merchant = merchantService.findMerchantById(prod.getMerchantId());
			prod.setMerchant(merchant);
			appProdRec.setProd(prod);
			RcCity city = cityService.findCityById(appProdRec.getCityId());
			appProdRec.setCity(city);
		}
		return appProdRecs;
	}

	@Override
    public RcAppProdRec findAppProdRecByCityIdAndProdId(int cityId, int prodId) {
        RcAppProdRec param = new RcAppProdRec();
        param.setCityId(cityId);
        param.setOrderBy("SEQ ASC");
        param.setProdId(prodId);
        return appProdRecMapper.selectOne(param);
    }

	@Override
	public RcAppProdRec findAppProdRecById(int appProdRecId) {
		return appProdRecMapper.selectByPrimaryKey(appProdRecId);
	}
}
