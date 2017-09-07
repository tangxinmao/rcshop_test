package com.socool.soft.bo;

import java.io.Serializable;

public class RcProdCatProp extends QueryParam implements Serializable {

	private static final long serialVersionUID = -1329271248020042895L;
	
	// db
	private Integer prodCatPropId;
	private Integer prodPropId;
	private Integer prodCatId;

	public Integer getProdCatPropId() {
		return prodCatPropId;
	}

	public void setProdCatPropId(Integer prodCatPropId) {
		this.prodCatPropId = prodCatPropId;
	}

	public Integer getProdPropId() {
		return prodPropId;
	}

	public void setProdPropId(Integer prodPropId) {
		this.prodPropId = prodPropId;
	}

	public Integer getProdCatId() {
		return prodCatId;
	}

	public void setProdCatId(Integer prodCatId) {
		this.prodCatId = prodCatId;
	}
}