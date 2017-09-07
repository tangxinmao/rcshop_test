package com.socool.soft.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RcRechargeStation extends QueryParam implements Serializable {

	private static final long serialVersionUID = 2886283093005267504L;
	
	// db
	private Integer rechargeStationId;
	private String mobile;
	private String stationName;
	private String stationLocation;
	private Date createTime;
	private String description;
	private Integer memberId;
	private Integer cityId;

	// rel
	private RcUser member;
	private RcCity city;

	// vo
	private String account;
	
	// se
	private List<Integer> cityIds;

	public Integer getRechargeStationId() {
		return rechargeStationId;
	}

	public void setRechargeStationId(Integer rechargeStationId) {
		this.rechargeStationId = rechargeStationId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationLocation() {
		return stationLocation;
	}

	public void setStationLocation(String stationLocation) {
		this.stationLocation = stationLocation;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public RcUser getMember() {
		return member;
	}

	public void setMember(RcUser member) {
		this.member = member;
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public List<Integer> getCityIds() {
		return cityIds;
	}

	public void setCityIds(List<Integer> cityIds) {
		this.cityIds = cityIds;
	}
}