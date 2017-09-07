package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcAppProdRec;
import com.socool.soft.vo.Page;

public interface IAppProdRecService {

	List<RcAppProdRec> findAppProdRecsByCityId(int cityId);

	List<RcAppProdRec> findAppProdRecsByCityId(int cityId, int limit);
	
	List<RcAppProdRec> findPagedAppProdRecsByCityId(Integer cityId, Page page);
	
	int addAppProdRec(RcAppProdRec appProdRec);
	
	int modifyAppProdRec(RcAppProdRec appProdRec);
	
	int removeAppProdRec(int appProdRecId);

    RcAppProdRec findAppProdRecByCityIdAndProdId(int cityId, int prodId);

    RcAppProdRec findAppProdRecById(int appProdRecId);
}
