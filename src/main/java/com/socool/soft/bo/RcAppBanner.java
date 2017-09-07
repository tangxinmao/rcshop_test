package com.socool.soft.bo;

import java.io.Serializable;

public class RcAppBanner extends QueryParam implements Serializable {

	private static final long serialVersionUID = -3152046419841123656L;
	
	// db
	private Integer appBannerId;
	private String imgUrl;
	private Integer seq;
	private Integer type;
	private String target;
	private Integer cityId;

	// rel
	private RcCity city;
	
	// vo
	private Integer status;

	public Integer getAppBannerId() {
		return appBannerId;
	}

	public void setAppBannerId(Integer bannerId) {
		this.appBannerId = bannerId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public RcCity getCity() {
		return city;
	}

	public void setCity(RcCity city) {
		this.city = city;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}