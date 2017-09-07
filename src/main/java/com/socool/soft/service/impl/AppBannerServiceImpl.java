package com.socool.soft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcAppBanner;
import com.socool.soft.bo.RcCity;
import com.socool.soft.dao.RcAppBannerMapper;
import com.socool.soft.service.IAppBannerService;
import com.socool.soft.service.ICityService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class AppBannerServiceImpl implements IAppBannerService {
	@Autowired
	private RcAppBannerMapper appBannerMapper;
	@Autowired
	private ICityService cityService;

	@Override
	public List<RcAppBanner> findAppBannersByCityId(int cityId) {
		RcAppBanner param = new RcAppBanner();
		param.setCityId(cityId);
		param.setOrderBy("SEQ ASC");
		return appBannerMapper.select(param);
	}
	
	@Override
	public int addAppBanner(RcAppBanner appBanner) {
		if(appBannerMapper.insert(appBanner) > 0) {
			return appBanner.getAppBannerId();
		}
		return 0;
	}

	@Override
	public int modifyAppBanner(RcAppBanner appBanner) {
		if(appBannerMapper.updateByPrimaryKey(appBanner) > 0) {
			return appBanner.getAppBannerId();
		}
		return 0;
	}

	@Override
	public int removeAppBanner(int appBannerId) {
		return appBannerMapper.deleteByPrimaryKey(appBannerId);
	}

	@Override
	public List<RcAppBanner> findPagedAppBanners(Integer cityId, Page page) {
		PageContext.setPage(page);
		RcAppBanner param = new RcAppBanner();
		param.setCityId(cityId);
		param.setOrderBy("CITY_ID ASC, SEQ ASC");
		List<RcAppBanner> appBanners = appBannerMapper.select(param);
		for(RcAppBanner appBanner : appBanners){
			RcCity city = cityService.findCityById(appBanner.getCityId());
			appBanner.setCity(city);
		}
		return appBanners;
	}

	@Override
	public RcAppBanner findAppBannerById(int appBannerId) {
		return appBannerMapper.selectByPrimaryKey(appBannerId);
	}
}
