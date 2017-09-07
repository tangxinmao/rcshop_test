package com.socool.soft.bo;

import java.io.Serializable;

public class RcProdCatSkuProp extends QueryParam implements Serializable {

	private static final long serialVersionUID = -7871344087499410620L;
	
	// db
	private Integer prodCatSkuPropId;
	private Integer prodPropId;
	private Integer prodCatId;

	public Integer getProdCatSkuPropId() {
		return prodCatSkuPropId;
	}

	public void setProdCatSkuPropId(Integer prodCatSkuPropId) {
		this.prodCatSkuPropId = prodCatSkuPropId;
	}

	public Integer getProdPropId() {
		return prodPropId;
	}

	public void setProdPropId(Integer prodSkuPropId) {
		this.prodPropId = prodSkuPropId;
	}

	public Integer getProdCatId() {
		return prodCatId;
	}

	public void setProdCatId(Integer prodCatId) {
		this.prodCatId = prodCatId;
	}
}