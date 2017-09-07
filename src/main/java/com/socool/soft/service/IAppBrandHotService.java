package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcAppBrandHot;
import com.socool.soft.vo.Page;

public interface IAppBrandHotService {

	int addAppBrandHot(RcAppBrandHot appBrandHot);
	
	int modifyAppBrandHot(RcAppBrandHot appBrandHot);
	
	int removeAppBrandHot(int appBrandHotId);
	
	List<RcAppBrandHot> findAllAppBrandHots();
	
	List<RcAppBrandHot> findAllPagedAppBrandHots(String prodBrandName, Page page);
	
	List<RcAppBrandHot> findAppBrandHotByProdBrandId(int prodBrandId);
}
