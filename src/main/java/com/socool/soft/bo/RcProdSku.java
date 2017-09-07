package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socool.soft.jsonSerializer.BigDecimalSerializer;

public class RcProdSku extends QueryParam implements Serializable {

	private static final long serialVersionUID = 7202020286951809032L;
	
	// db
	private String prodSkuId;
	private Integer prodId;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal originPrice;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal price;
	private Integer inventory;

	// rel
	private List<RcProdImg> prodImgs;

	// vo
	private Integer storage;
	private Integer status;

	public String getProdSkuId() {
		return prodSkuId;
	}

	public void setProdSkuId(String prodSkuId) {
		this.prodSkuId = prodSkuId;
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public BigDecimal getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(BigDecimal originPrice) {
		this.originPrice = originPrice;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public List<RcProdImg> getProdImgs() {
		return prodImgs;
	}

	public void setProdImgs(List<RcProdImg> prodImgs) {
		this.prodImgs = prodImgs;
	}

	public Integer getStorage() {
		return storage;
	}

	public void setStorage(Integer storage) {
		this.storage = storage;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}