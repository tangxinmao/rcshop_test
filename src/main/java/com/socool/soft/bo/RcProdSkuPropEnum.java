package com.socool.soft.bo;

import java.io.Serializable;

public class RcProdSkuPropEnum extends QueryParam implements Serializable {

	private static final long serialVersionUID = 3141437503099053415L;
	
	// db
	private Integer prodPropEnumId;
	private Integer prodPropId;
	private String prodPropEnum;
	private Integer seq;

	public Integer getProdPropEnumId() {
		return prodPropEnumId;
	}

	public void setProdPropEnumId(Integer prodPropEnumId) {
		this.prodPropEnumId = prodPropEnumId;
	}

	public Integer getProdPropId() {
		return prodPropId;
	}

	public void setProdPropId(Integer prodPropId) {
		this.prodPropId = prodPropId;
	}

	public String getProdPropEnum() {
		return prodPropEnum;
	}

	public void setProdPropEnum(String prodPropEnum) {
		this.prodPropEnum = prodPropEnum;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}