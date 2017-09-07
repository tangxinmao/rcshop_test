package com.socool.soft.bo;

import java.io.Serializable;
import java.util.List;

public class RcAppBrandHot extends QueryParam implements Serializable {

	private static final long serialVersionUID = -61134686189251130L;
	
	// db
	private Integer appBrandHotId;
	private Integer prodBrandId;
	private Integer seq;
	private String imgUrl;

	// rel
	private RcProdBrand prodBrand;
	
	// vo
	private String prodBrandName;
	private String prodBrandLogoUrl;
	
	// se
	private List<Integer> prodBrandIds;

	public Integer getAppBrandHotId() {
		return appBrandHotId;
	}

	public void setAppBrandHotId(Integer appBrandHotId) {
		this.appBrandHotId = appBrandHotId;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getProdBrandId() {
		return prodBrandId;
	}

	public void setProdBrandId(Integer prodBrandId) {
		this.prodBrandId = prodBrandId;
	}

	public RcProdBrand getProdBrand() {
		return prodBrand;
	}

	public void setProdBrand(RcProdBrand prodBrand) {
		this.prodBrand = prodBrand;
	}

	public String getProdBrandName() {
		return prodBrandName;
	}

	public void setProdBrandName(String prodBrandName) {
		this.prodBrandName = prodBrandName;
	}

	public String getProdBrandLogoUrl() {
		return prodBrandLogoUrl;
	}

	public void setProdBrandLogoUrl(String prodBrandLogoUrl) {
		this.prodBrandLogoUrl = prodBrandLogoUrl;
	}

	public List<Integer> getProdBrandIds() {
		return prodBrandIds;
	}

	public void setProdBrandIds(List<Integer> prodBrandIds) {
		this.prodBrandIds = prodBrandIds;
	}
}