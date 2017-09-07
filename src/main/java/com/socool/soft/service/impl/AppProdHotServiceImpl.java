package com.socool.soft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcAppProdHot;
import com.socool.soft.bo.RcCity;
import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcProd;
import com.socool.soft.dao.RcAppProdHotMapper;
import com.socool.soft.service.IAppProdHotService;
import com.socool.soft.service.ICityService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IProdImgService;
import com.socool.soft.service.IProdService;
import com.socool.soft.service.ISearchService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class AppProdHotServiceImpl implements IAppProdHotService {
    @Autowired
    private RcAppProdHotMapper appProdHotMapper;
    @Autowired
    private IProdService prodService;
    @Autowired
    private IMerchantService merchantService;
    @Autowired
    private ICityService cityService;
    @Autowired
    private IProdImgService prodImgService;
    @Autowired
    private ISearchService searchService;

    @Override
    public List<RcAppProdHot> findAppProdHotsByCityId(int cityId) {
        RcAppProdHot param = new RcAppProdHot();
        param.setCityId(cityId);
        param.setOrderBy("SEQ ASC");
        return appProdHotMapper.select(param);
    }

    @Override
    public List<RcAppProdHot> findAppProdHotsByCityId(int cityId, int limit) {
        RcAppProdHot param = new RcAppProdHot();
        param.setCityId(cityId);
        param.setOrderBy("SEQ ASC");
        param.setLimit(limit);
        return appProdHotMapper.select(param);
    }

    @Override
    public int addAppProdHot(RcAppProdHot appProdHot) {
        if (appProdHotMapper.insert(appProdHot) > 0) {
        	searchService.updateIndexForProdPutUpDown(appProdHot.getProdId());
            return appProdHot.getAppProdHotId();
        }
        return 0;
    }

    @Override
    public int modifyAppProdHot(RcAppProdHot appProdHot) {
        if (appProdHotMapper.updateByPrimaryKey(appProdHot) > 0) {
            return appProdHot.getAppProdHotId();
        }
        return 0;
    }

    @Override
    public int removeAppProdHot(int appProdHotId) {
    	RcAppProdHot appProdHot = findAppProdHotById(appProdHotId);
        if(appProdHotMapper.deleteByPrimaryKey(appProdHotId) > 0) {
        	searchService.updateIndexForProdPutUpDown(appProdHot.getProdId());
        	return 1;
        }
        return 0;
    }

    @Override
    public List<RcAppProdHot> findPagedAppProdHotsByCityId(Integer cityId, Page page) {
        PageContext.setPage(page);
        RcAppProdHot param = new RcAppProdHot();
        param.setCityId(cityId);
        param.setOrderBy("CITY_ID ASC, SEQ ASC");
        List<RcAppProdHot> appProdHots = appProdHotMapper.select(param);
        for (RcAppProdHot appProdHot : appProdHots) {
            RcProd prod = prodService.findProdById(appProdHot.getProdId());
            prod.setProdImgUrl(prodImgService.findFirstProdImgByProdSkuId(String.valueOf(prod.getProdId())).getImgUrl());
            RcMerchant merchant = merchantService.findMerchantById(prod.getMerchantId());
            prod.setMerchant(merchant);
            appProdHot.setProd(prod);
            RcCity city = cityService.findCityById(appProdHot.getCityId());
            appProdHot.setCity(city);
        }
        return appProdHots;
    }

    @Override
    public RcAppProdHot findAppProdHotByProdId(int prodId) {
        RcAppProdHot param = new RcAppProdHot();
        param.setOrderBy("SEQ ASC");
        param.setProdId(prodId);
        return appProdHotMapper.selectOne(param);
    }

	@Override
	public RcAppProdHot findAppProdHotById(int appProdHotId) {
		return appProdHotMapper.selectByPrimaryKey(appProdHotId);
	}
}
