package com.socool.soft.service;

import com.socool.soft.bo.RcAppProdHot;
import com.socool.soft.vo.Page;

import java.util.List;

public interface IAppProdHotService {

	List<RcAppProdHot> findAppProdHotsByCityId(int cityId);

	List<RcAppProdHot> findAppProdHotsByCityId(int cityId, int limit);
	
	List<RcAppProdHot> findPagedAppProdHotsByCityId(Integer cityId, Page page);
	
	int addAppProdHot(RcAppProdHot appProdHot);
	
	int modifyAppProdHot(RcAppProdHot appProdHot);
	
	int removeAppProdHot(int appProdHotId);

    RcAppProdHot findAppProdHotByProdId(int prodId);

    RcAppProdHot findAppProdHotById(int appProdHotId);
}
