package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcAppBrandHot;
import com.socool.soft.bo.RcAppProdRec;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.newvo.android.HomePageVO;

public interface IHomePageService {

	/**
	 * 获取APP首页数据
	 * @param cityName 城市名称
	 * @return
	 */
	HomePageVO getHomePageData(int cityId);
	
	List<RcAppBrandHot> findAllPagedAppBrandHots(Page page);
	
	List<RcAppProdRec> findPagedAppProdRecsByCityId(int cityId, Page page);
}
