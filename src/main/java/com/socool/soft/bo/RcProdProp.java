package com.socool.soft.bo;

import java.io.Serializable;
import java.util.List;

public class RcProdProp extends QueryParam implements Serializable {

	private static final long serialVersionUID = 6553248516631590676L;
	
	// db
	private Integer prodPropId;
	private String name;
	
	// rel
	private List<RcProdPropEnum> prodPropEnums;
	
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

	public List<RcProdPropEnum> getProdPropEnums() {
		return prodPropEnums;
	}

	public void setProdPropEnums(List<RcProdPropEnum> prodPropEnums) {
		this.prodPropEnums = prodPropEnums;
	}

	public List<Integer> getProdPropIds() {
		return prodPropIds;
	}

	public void setProdPropIds(List<Integer> prodPropIds) {
		this.prodPropIds = prodPropIds;
	}
}