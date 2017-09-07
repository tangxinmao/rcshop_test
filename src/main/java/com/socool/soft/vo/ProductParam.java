package com.socool.soft.vo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductParam {
	private Integer merchantId;
	private String prodName;
	private Integer prodId;
	private Integer prodStatus;
	private Date rackOnStartTime;
	private Date rackOnEndTime;
	private Date rackDownStartTime;
	private Date rackDownEndTime;
	private String rackOnStartTimeStr;
	private String rackOnEndTimeStr;
	private String rackDownStartTimeStr;
	private String rackDownEndTimeStr;
	private Integer prodCatId;
	private Boolean isNotEnough = null;
	private Integer prodBrandId;
	private Integer productType;
	private String city;
	private String merchantName;
	private String isDesc;
	private Integer vendorId;
	//for promotion, query products which not include specific prod ids
	private String notLikeProdIds;

	public Boolean getIsNotEnough() {
		return isNotEnough;
	}

	public void setIsNotEnough(Boolean isNotEnough) {
		this.isNotEnough = isNotEnough;
	}

	public String getNotLikeProdIds() {
		return notLikeProdIds;
	}

	public void setNotLikeProdIds(String notLikeProdIds) {
		this.notLikeProdIds = notLikeProdIds;
	}

	public String getRackOnStartTimeStr() {
		return rackOnStartTimeStr;
	}

	public void setRackOnStartTimeStr(String rackOnStartTimeStr)
			throws Exception {
		this.rackOnStartTimeStr = rackOnStartTimeStr;
		if (rackOnStartTimeStr != null && !"".equals(rackOnStartTimeStr)) {
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			rackOnStartTime = format1.parse(rackOnStartTimeStr);
		}
	}

	public String getRackOnEndTimeStr() {
		return rackOnEndTimeStr;
	}

	public void setRackOnEndTimeStr(String rackOnEndTimeStr) throws Exception {
		this.rackOnEndTimeStr = rackOnEndTimeStr;
		if (rackOnEndTimeStr != null && !"".equals(rackOnEndTimeStr)) {
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			rackOnEndTime = format1.parse(rackOnEndTimeStr);
		}
	}

	public String getRackDownStartTimeStr() {
		return rackDownStartTimeStr;
	}

	public void setRackDownStartTimeStr(String rackDownStartTimeStr)
			throws Exception {
		this.rackDownStartTimeStr = rackDownStartTimeStr;
		if (rackDownStartTimeStr != null && !"".equals(rackDownStartTimeStr)) {
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			rackDownStartTime = format1.parse(rackDownStartTimeStr);
		}
	}

	public String getRackDownEndTimeStr() {
		return rackDownEndTimeStr;
	}

	public void setRackDownEndTimeStr(String rackDownEndTimeStr)
			throws Exception {
		this.rackDownEndTimeStr = rackDownEndTimeStr;
		if (rackDownEndTimeStr != null && !"".equals(rackDownEndTimeStr)) {
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			rackDownEndTime = format1.parse(rackDownEndTimeStr);
		}
	}



	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}


	public Integer getProdStatus() {
		return prodStatus;
	}

	public void setProdStatus(Integer prodStatus) {
		this.prodStatus = prodStatus;
	}

	public Date getRackOnStartTime() {
		return rackOnStartTime;
	}

	public void setRackOnStartTime(Date rackOnStartTime) {
		this.rackOnStartTime = rackOnStartTime;
	}

	public Date getRackOnEndTime() {
		return rackOnEndTime;
	}

	public void setRackOnEndTime(Date rackOnEndTime) {
		this.rackOnEndTime = rackOnEndTime;
	}

	public Date getRackDownStartTime() {
		return rackDownStartTime;
	}

	public void setRackDownStartTime(Date rackDownStartTime) {
		this.rackDownStartTime = rackDownStartTime;
	}

	public Date getRackDownEndTime() {
		return rackDownEndTime;
	}

	public void setRackDownEndTime(Date rackDownEndTime) {
		this.rackDownEndTime = rackDownEndTime;
	}


	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public Integer getProdCatId() {
		return prodCatId;
	}

	public void setProdCatId(Integer prodCatId) {
		this.prodCatId = prodCatId;
	}

	public Integer getProdBrandId() {
		return prodBrandId;
	}

	public void setProdBrandId(Integer prodBrandId) {
		this.prodBrandId = prodBrandId;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getIsDesc() {
		return isDesc;
	}

	public void setIsDesc(String isDesc) {
		this.isDesc = isDesc;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
}
