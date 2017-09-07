package com.socool.soft.bo;

import java.io.Serializable;

public class RcProdBrand extends QueryParam implements Serializable {

	private static final long serialVersionUID = 1450354260786536969L;
	
	// db
	private Integer prodBrandId;
	private String name;
	private String description;
	private String logoUrl;
	private Byte delFlag;
	
	// rel
	private RcVendor vendor;

	public Integer getProdBrandId() {
		return prodBrandId;
	}

	public void setProdBrandId(Integer prodBrandId) {
		this.prodBrandId = prodBrandId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public RcVendor getVendor() {
		return vendor;
	}

	public void setVendor(RcVendor vendor) {
		this.vendor = vendor;
	}

	public Byte getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Byte delFlag) {
		this.delFlag = delFlag;
	}
}