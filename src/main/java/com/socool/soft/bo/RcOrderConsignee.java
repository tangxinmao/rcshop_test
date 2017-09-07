package com.socool.soft.bo;

import java.io.Serializable;

public class RcOrderConsignee extends QueryParam implements Serializable {

	private static final long serialVersionUID = 1642157193263932159L;
	
	// db
	private Long orderId;
	private String name;
	private String mobile;
	private Integer cityId;
	private String address;

	// rel
	private RcCity city;
	
	// vo
	private String provinceName;
	private String cityName;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}