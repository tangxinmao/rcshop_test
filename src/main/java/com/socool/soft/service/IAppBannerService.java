package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcAppBanner;
import com.socool.soft.vo.Page;

public interface IAppBannerService {

	List<RcAppBanner> findAppBannersByCityId(int cityId);
	
	List<RcAppBanner> findPagedAppBanners(Integer cityId, Page page);
	
	int addAppBanner(RcAppBanner appBanner);
	
	int modifyAppBanner(RcAppBanner appBanner);
	
	int removeAppBanner(int appBannerId);

	RcAppBanner findAppBannerById(int appBannerId);
}
