package com.socool.soft.bo;

import java.io.Serializable;

public class RcOrderProdSkuPropInfo extends QueryParam implements Serializable {

	private static final long serialVersionUID = 2598904700655235923L;
	
	// db
	private Long orderProdSkuPropInfoId;
	private Long orderProdId;
	private String prodPropName;
	private String prodPropVal;

	public Long getOrderProdSkuPropInfoId() {
		return orderProdSkuPropInfoId;
	}

	public void setOrderProdSkuPropInfoId(Long orderProdSkuPropInfoId) {
		this.orderProdSkuPropInfoId = orderProdSkuPropInfoId;
	}

	public Long getOrderProdId() {
		return orderProdId;
	}

	public void setOrderProdId(Long orderProdId) {
		this.orderProdId = orderProdId;
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
}