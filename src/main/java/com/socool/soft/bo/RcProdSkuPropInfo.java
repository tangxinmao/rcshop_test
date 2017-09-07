package com.socool.soft.bo;

import java.io.Serializable;

public class RcProdSkuPropInfo extends QueryParam implements Serializable {

	private static final long serialVersionUID = 838977553237901329L;
	
	// db
	private Integer prodSkuPropInfoId;
	private Integer prodId;
	private Integer prodPropId;
	private String prodSkuId;
	private String prodPropName;
	private String prodPropVal;
	private Integer prodPropEnumId;
	private Byte hasImg;
	private Byte isPackService;

	// vo
	private RcProdProp prodProp;
	private RcProdSkuPropEnum prodPropEnum;

	public Integer getProdSkuPropInfoId() {
		return prodSkuPropInfoId;
	}

	public void setProdSkuPropInfoId(Integer prodSkuPropInfoId) {
		this.prodSkuPropInfoId = prodSkuPropInfoId;
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public String getProdSkuId() {
		return prodSkuId;
	}

	public void setProdSkuId(String prodSkuId) {
		this.prodSkuId = prodSkuId;
	}

	public String getProdPropName() {
		return prodPropName;
	}

	public void setProdPropName(String prodPropName) {
		this.prodPropName = prodPropName;
	}

	public String getProdPropVal() {
		return prodPropVal;
	}

	public void setProdPropVal(String prodPropVal) {
		this.prodPropVal = prodPropVal;
	}

	public Byte getHasImg() {
		return hasImg;
	}

	public void setHasImg(Byte hasImg) {
		this.hasImg = hasImg;
	}

	public Integer getProdPropId() {
		return prodPropId;
	}

	public void setProdPropId(Integer prodPropId) {
		this.prodPropId = prodPropId;
	}

	public RcProdProp getProdProp() {
		return prodProp;
	}

	public void setProdProp(RcProdProp prodProp) {
		this.prodProp = prodProp;
	}

	public Integer getProdPropEnumId() {
		return prodPropEnumId;
	}

	public void setProdPropEnumId(Integer prodPropEnumId) {
		this.prodPropEnumId = prodPropEnumId;
	}

	public RcProdSkuPropEnum getProdPropEnum() {
		return prodPropEnum;
	}

	public void setProdPropEnum(RcProdSkuPropEnum prodPropEnum) {
		this.prodPropEnum = prodPropEnum;
	}

	public Byte getIsPackService() {
		return isPackService;
	}

	public void setIsPackService(Byte isPackService) {
		this.isPackService = isPackService;
	}
}