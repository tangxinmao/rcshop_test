package com.socool.soft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcAppBanner;
import com.socool.soft.bo.RcAppBrandHot;
import com.socool.soft.bo.RcAppProdHot;
import com.socool.soft.bo.RcAppProdRec;
import com.socool.soft.bo.RcCity;
import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcProd;
import com.socool.soft.bo.RcProdBrand;
import com.socool.soft.bo.RcProdCat;
import com.socool.soft.bo.constant.AppBannerTypeEnum;
import com.socool.soft.bo.constant.ProdStatusEnum;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.service.IAppBannerService;
import com.socool.soft.service.IAppBrandHotService;
import com.socool.soft.service.IAppProdHotService;
import com.socool.soft.service.IAppProdRecService;
import com.socool.soft.service.ICityService;
import com.socool.soft.service.IHomePageService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IProdBrandService;
import com.socool.soft.service.IProdCatService;
import com.socool.soft.service.IProdImgService;
import com.socool.soft.service.IProdService;
import com.socool.soft.util.VOConversionUtil;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;
import com.socool.soft.vo.newvo.android.HomePageVO;

@Service
public class HomePageServiceImpl implements IHomePageService {

	@Autowired
	private IAppBannerService appBannerService;
	
//	@Autowired
//	private IAppMenuService appMenuService;
	
	@Autowired
	private IProdService prodService;
	
	@Autowired
	private IAppProdHotService appProdHotService;
	
	@Autowired
	private IAppProdRecService appProdRecService;
	
	@Autowired
	private IProdCatService prodCatService;
	
	@Autowired
	private IProdBrandService prodBrandService;
	@Autowired
	private IAppBrandHotService appBrandHotService;
	@Autowired
	private IProdImgService prodImgService;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private ICityService cityService;
	
	@Override
	public HomePageVO getHomePageData(int cityId) {
		HomePageVO homePageVO = new HomePageVO();
		
		RcCity city = cityService.findCityById(cityId);
		homePageVO.setCityId(city.getCityId());
		homePageVO.setCityName(city.getName());

		List<RcAppBanner> appBanners = appBannerService.findAppBannersByCityId(cityId);
		for(RcAppBanner appBanner : appBanners) {
			if(appBanner.getType() == AppBannerTypeEnum.PRODUCT_DETAIL.getValue()) {
				RcProd prod = prodService.findProdById(Integer.parseInt(appBanner.getTarget()));
				appBanner.setStatus(prod.getStatus());
				if(prod.getDelFlag() == YesOrNoEnum.YES.getValue()) {
					appBanner.setStatus(ProdStatusEnum.SOLD_OUT.getValue());
				}
			}
			VOConversionUtil.Entity2VO(appBanner, null, new String[] {"seq", "cityId"});
		}

//		List<RcAppMenu> appMenus = appMenuService.findAllAppMenus();
//		for(RcAppMenu appMenu : appMenus) {
//			VOConversionUtil.Entity2VO(appMenu, null, new String[] {"seq"});
//		}

		List<RcAppProdHot> appProdHots = appProdHotService.findAppProdHotsByCityId(cityId, 5);
		for(RcAppProdHot appProdHot : appProdHots) {
			RcProd prod = prodService.findProdById(appProdHot.getProdId());
			RcProdCat prodCat = prodCatService.findProdCatById(prod.getProdCatId());
			RcMerchant merchant = merchantService.findMerchantById(prod.getMerchantId());
			appProdHot.setMerchantName(merchant.getName());
			appProdHot.setProdCatName(prodCat.getName());
			appProdHot.setProdImgUrl(prodImgService.findFirstProdImgByProdSkuId(String.valueOf(prod.getProdId())).getImgUrl());
			if(prod.getProdBrandId() > 0) {
				RcProdBrand prodBrand = prodBrandService.findProdBrandById(prod.getProdBrandId());
				appProdHot.setProdBrandLogoUrl(prodBrand.getLogoUrl());
				appProdHot.setProdBrandName(prodBrand.getName());
			} else {
				appProdHot.setProdBrandName(prod.getProdBrandName());
			}
			appProdHot.setProdName(prod.getName());
			appProdHot.setOriginPrice(prod.getOriginPrice());
			appProdHot.setPrice(prod.getPrice());
			appProdHot.setType(prod.getType());
			appProdHot.setScore(prod.getScore());
			appProdHot.setStatus(prod.getStatus());
			if(prod.getDelFlag() == YesOrNoEnum.YES.getValue()) {
				appProdHot.setStatus(ProdStatusEnum.SOLD_OUT.getValue());
			}
			VOConversionUtil.Entity2VO(appProdHot, null, new String[] {"appProdHotId", "cityId"});
		}

		List<RcAppProdRec> appProdRecs = appProdRecService.findAppProdRecsByCityId(cityId, 8);
		for(RcAppProdRec appProdRec : appProdRecs) {
			RcProd prod = prodService.findProdById(appProdRec.getProdId());
			RcProdCat prodCat = prodCatService.findProdCatById(prod.getProdCatId());
			RcMerchant merchant = merchantService.findMerchantById(prod.getMerchantId());
			appProdRec.setMerchantName(merchant.getName());
			appProdRec.setProdCatName(prodCat.getName());
			appProdRec.setProdImgUrl(prodImgService.findFirstProdImgByProdSkuId(String.valueOf(prod.getProdId())).getImgUrl());
			if(prod.getProdBrandId() > 0) {
				RcProdBrand prodBrand = prodBrandService.findProdBrandById(prod.getProdBrandId());
				appProdRec.setProdBrandLogoUrl(prodBrand.getLogoUrl());
				appProdRec.setProdBrandName(prodBrand.getName());
			} else {
				appProdRec.setProdBrandName(prod.getProdBrandName());
			}
			appProdRec.setProdName(prod.getName());
			appProdRec.setOriginPrice(prod.getOriginPrice());
			appProdRec.setPrice(prod.getPrice());
			appProdRec.setType(prod.getType());
			appProdRec.setScore(prod.getScore());
			appProdRec.setStatus(prod.getStatus());
			if(prod.getDelFlag() == YesOrNoEnum.YES.getValue()) {
				appProdRec.setStatus(ProdStatusEnum.SOLD_OUT.getValue());
			}
			VOConversionUtil.Entity2VO(appProdRec, null, new String[] {"appProdRecId", "cityId"});
		}
		
		homePageVO.setBanners(appBanners);
//		homePageVO.setMenus(appMenus);
		homePageVO.setHots(appProdHots);
		homePageVO.setRecommends(appProdRecs);
		
		return homePageVO;
	}

	@Override
	public List<RcAppBrandHot> findAllPagedAppBrandHots(Page page) {
		PageContext.setPage(page);
		List<RcAppBrandHot> appPrandHots = appBrandHotService.findAllAppBrandHots();
		for(RcAppBrandHot appBrandHot : appPrandHots) {
			RcProdBrand prodBrand = prodBrandService.findProdBrandById(appBrandHot.getProdBrandId());
			appBrandHot.setProdBrandName(prodBrand.getName());
			appBrandHot.setProdBrandLogoUrl(prodBrand.getLogoUrl());
			VOConversionUtil.Entity2VO(appBrandHot, null, new String[] {"appBrandHotId", "seq"});
		}
		return appPrandHots;
	}

	@Override
	public List<RcAppProdRec> findPagedAppProdRecsByCityId(int cityId, Page page) {
		PageContext.setPage(page);
		List<RcAppProdRec> appProdRecs = appProdRecService.findAppProdRecsByCityId(cityId);
		for(RcAppProdRec appProdRec : appProdRecs) {
			RcProd prod = prodService.findProdById(appProdRec.getProdId());
			RcProdCat prodCat = prodCatService.findProdCatById(prod.getProdCatId());
			RcMerchant merchant = merchantService.findMerchantById(prod.getMerchantId());
			appProdRec.setMerchantName(merchant.getName());
			appProdRec.setProdCatName(prodCat.getName());
			appProdRec.setProdImgUrl(prodImgService.findFirstProdImgByProdSkuId(String.valueOf(prod.getProdId())).getImgUrl());
			if(prod.getProdBrandId() > 0) {
				RcProdBrand prodBrand = prodBrandService.findProdBrandById(prod.getProdBrandId());
				appProdRec.setProdBrandLogoUrl(prodBrand.getLogoUrl());
				appProdRec.setProdBrandName(prodBrand.getName());
			} else {
				appProdRec.setProdBrandName(prod.getProdBrandName());
			}
			appProdRec.setProdName(prod.getName());
			appProdRec.setOriginPrice(prod.getOriginPrice());
			appProdRec.setPrice(prod.getPrice());
			appProdRec.setType(prod.getType());
			appProdRec.setScore(prod.getScore());
			appProdRec.setStatus(prod.getStatus());
			if(prod.getDelFlag() == YesOrNoEnum.YES.getValue()) {
				appProdRec.setStatus(ProdStatusEnum.SOLD_OUT.getValue());
			}
			VOConversionUtil.Entity2VO(appProdRec, null, new String[] {"appProdRecId", "cityId"});
		}
		return appProdRecs;
	}
}
