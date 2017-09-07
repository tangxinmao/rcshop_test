package com.socool.soft.bo;

import java.io.Serializable;
import java.util.Date;

public class RcBuyerConsignee extends QueryParam implements Serializable {

	private static final long serialVersionUID = -5169079459922835815L;
	
	// db
	private Integer consigneeId;
	private String name;
	private String mobile;
	private Integer cityId;
	private String address;
	private Integer memberId;
	private Byte isDefault;
	private Date createTime;

	// rel
	private RcCity city;
	private RcBuyer member;
	
	// vo
	private String provinceName;
	private String cityName;

	public Integer getConsigneeId() {
		return consigneeId;
	}

	public void setConsigneeId(Integer consigneeId) {
		this.consigneeId = consigneeId;
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

	public Byte getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Byte isDefault) {
		this.isDefault = isDefault;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public RcBuyer getMember() {
		return member;
	}

	public void setMember(RcBuyer member) {
		this.member = member;
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