package com.socool.soft.bo;

import java.io.Serializable;
import java.util.List;

public class RcProdCat extends QueryParam implements Serializable {

	private static final long serialVersionUID = -6901921481061091277L;
	
	// db
	private Integer prodCatId;
	private String name;
	private Integer parentId;
	private Integer seq;
	private String icon;

	// rel
	private RcProdCat parentProdCat;
	private List<RcProdCat> childProdCats;
	private List<RcProdProp> prodProps;
	private List<RcProdSkuProp> prodSkuProps;

	public Integer getProdCatId() {
		return prodCatId;
	}

	public void setProdCatId(Integer prodCatId) {
		this.prodCatId = prodCatId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public RcProdCat getParentProdCat() {
		return parentProdCat;
	}

	public void setParentProdCat(RcProdCat parentProdCat) {
		this.parentProdCat = parentProdCat;
	}

	public List<RcProdCat> getChildProdCats() {
		return childProdCats;
	}

	public void setChildProdCats(List<RcProdCat> childProdCats) {
		this.childProdCats = childProdCats;
	}

	public List<RcProdProp> getProdProps() {
		return prodProps;
	}

	public void setProdProps(List<RcProdProp> prodProps) {
		this.prodProps = prodProps;
	}

	public List<RcProdSkuProp> getProdSkuProps() {
		return prodSkuProps;
	}

	public void setProdSkuProps(List<RcProdSkuProp> prodSkuProps) {
		this.prodSkuProps = prodSkuProps;
	}
}