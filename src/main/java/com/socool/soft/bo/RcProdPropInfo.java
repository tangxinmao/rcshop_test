package com.socool.soft.bo;

import java.io.Serializable;

public class RcProdPropInfo extends QueryParam implements Serializable {

	private static final long serialVersionUID = -2408699264110366422L;
	
	// db
	private Integer prodPropInfoId;
	private Integer prodId;
	private String prodPropName;
	private String prodPropVal;
	private Integer prodPropId;
	
	// rel
	private RcProdProp prodProp;

	public Integer getProdPropInfoId() {
		return prodPropInfoId;
	}

	public void setProdPropInfoId(Integer prodPropInfoId) {
		this.prodPropInfoId = prodPropInfoId;
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
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
}