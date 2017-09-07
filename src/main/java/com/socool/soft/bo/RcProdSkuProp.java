package com.socool.soft.bo;

import java.io.Serializable;
import java.util.List;

public class RcProdSkuProp extends QueryParam implements Serializable {

	private static final long serialVersionUID = -1810976371534850615L;
	
	// db
	private Integer prodPropId;
	private String name;
	private Byte hasImg;
	private Byte isPackService;
	
	// rel
	private List<RcProdSkuPropEnum> prodPropEnums;
	
	// se
	private List<Integer> prodPropIds;

	public Integer getProdPropId() {
		return prodPropId;
	}

	public void setProdPropId(Integer prodPropId) {
		this.prodPropId = prodPropId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getHasImg() {
		return hasImg;
	}

	public void setHasImg(Byte hasImg) {
		this.hasImg = hasImg;
	}

	public Byte getIsPackService() {
		return isPackService;
	}

	public void setIsPackService(Byte isPackService) {
		this.isPackService = isPackService;
	}

	public List<RcProdSkuPropEnum> getProdPropEnums() {
		return prodPropEnums;
	}

	public void setProdPropEnums(List<RcProdSkuPropEnum> prodPropEnums) {
		this.prodPropEnums = prodPropEnums;
	}

	public List<Integer> getProdPropIds() {
		return prodPropIds;
	}

	public void setProdPropIds(List<Integer> prodPropIds) {
		this.prodPropIds = prodPropIds;
	}
}