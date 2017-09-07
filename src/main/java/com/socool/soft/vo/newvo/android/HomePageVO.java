package com.socool.soft.vo.newvo.android;

import java.io.Serializable;
import java.util.List;

import com.socool.soft.bo.RcAppBanner;
import com.socool.soft.bo.RcAppMenu;
import com.socool.soft.bo.RcAppProdHot;
import com.socool.soft.bo.RcAppProdRec;

public class HomePageVO implements Serializable {

	private static final long serialVersionUID = -4917609807177777024L;
	
	private Integer cityId;
	private String cityName;
	private List<RcAppBanner> banners;
	private List<RcAppMenu> menus;
	private List<RcAppProdHot> hots;
	private List<RcAppProdRec> recommends;

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public List<RcAppBanner> getBanners() {
		return banners;
	}

	public void setBanners(List<RcAppBanner> banners) {
		this.banners = banners;
	}

	public List<RcAppMenu> getMenus() {
		return menus;
	}

	public void setMenus(List<RcAppMenu> menus) {
		this.menus = menus;
	}

	public List<RcAppProdHot> getHots() {
		return hots;
	}

	public void setHots(List<RcAppProdHot> hots) {
		this.hots = hots;
	}

	public List<RcAppProdRec> getRecommends() {
		return recommends;
	}

	public void setRecommends(List<RcAppProdRec> recommends) {
		this.recommends = recommends;
	}
}
