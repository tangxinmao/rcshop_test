package com.socool.soft.bo;

import java.io.Serializable;

public class RcProdImg extends QueryParam implements Serializable {

	private static final long serialVersionUID = 6091498563611172639L;
	
	// db
	private Integer prodImgId;
	private Integer prodId;
	private String prodSkuId;
	private String imgUrl;
	private Integer seq;

	public Integer getProdImgId() {
		return prodImgId;
	}

	public void setProdImgId(Integer prodImgId) {
		this.prodImgId = prodImgId;
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

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}