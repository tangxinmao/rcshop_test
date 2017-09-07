package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcAppBrandHot;
import com.socool.soft.bo.RcProdBrand;
import com.socool.soft.dao.RcAppBrandHotMapper;
import com.socool.soft.service.IAppBrandHotService;
import com.socool.soft.service.IProdBrandService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class AppBrandHotServiceImpl implements IAppBrandHotService {
	@Autowired
	private RcAppBrandHotMapper appBrandHotMapper;
	@Autowired
	private IProdBrandService prodBrandService;

	@Override
	public int addAppBrandHot(RcAppBrandHot appBrandHot) {
		if(appBrandHotMapper.insert(appBrandHot) > 0) {
			return appBrandHot.getAppBrandHotId();
		}
		return 0;
	}

	@Override
	public int modifyAppBrandHot(RcAppBrandHot appBrandHot) {
		if(appBrandHotMapper.updateByPrimaryKey(appBrandHot) > 0) {
			return appBrandHot.getAppBrandHotId();
		}
		return 0;
	}

	@Override
	public int removeAppBrandHot(int appBrandHotId) {
		return appBrandHotMapper.deleteByPrimaryKey(appBrandHotId);
	}

	@Override
	public List<RcAppBrandHot> findAllAppBrandHots() {
		RcAppBrandHot param = new RcAppBrandHot();
		param.setOrderBy("SEQ ASC");
		return appBrandHotMapper.select(param);
	}

	@Override
	public List<RcAppBrandHot> findAllPagedAppBrandHots(String prodBrandName, Page page) {
		List<RcProdBrand> prodBrands = prodBrandService.findProdBrandsByName(prodBrandName);
		if(CollectionUtils.isEmpty(prodBrands)) {
			return new ArrayList<RcAppBrandHot>();
		}
		RcAppBrandHot param = new RcAppBrandHot();
		List<Integer> prodBrandIds = new ArrayList<Integer>();
		for(RcProdBrand prodBrand : prodBrands) {
			prodBrandIds.add(prodBrand.getProdBrandId());
		}
		param.setProdBrandIds(prodBrandIds);
		param.setOrderBy("SEQ ASC");
		PageContext.setPage(page);
		List<RcAppBrandHot> appBrandHots = appBrandHotMapper.select(param);
		for(RcAppBrandHot appBrandHot : appBrandHots) {
			appBrandHot.setProdBrand(prodBrandService.findProdBrandById(appBrandHot.getProdBrandId()));
		}
		return appBrandHots;
	}

	@Override
	public List<RcAppBrandHot> findAppBrandHotByProdBrandId(int prodBrandId) {
		RcAppBrandHot param = new RcAppBrandHot();
		param.setProdBrandId(prodBrandId);
		return appBrandHotMapper.select(param);
	}
}
