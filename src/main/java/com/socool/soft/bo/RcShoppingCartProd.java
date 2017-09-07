package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socool.soft.jsonSerializer.BigDecimalSerializer;

public class RcShoppingCartProd implements Serializable {

	private static final long serialVersionUID = 2983656808615290860L;
	
	// vo
	private Integer prodId;
	private String name;
	private Integer type;
	private Integer status;
	private Integer quantity;
	private Integer storage;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal price;
	private String prodImgUrl;
	private String prodSkuId;
	private List<String> prodSkuInfos;

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getStorage() {
		return storage;
	}

	public void setStorage(Integer storage) {
		this.storage = storage;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getProdImgUrl() {
		return prodImgUrl;
	}

	public void setProdImgUrl(String prodImgUrl) {
		this.prodImgUrl = prodImgUrl;
	}

	public String getProdSkuId() {
		return prodSkuId;
	}

	public void setProdSkuId(String prodSkuId) {
		this.prodSkuId = prodSkuId;
	}

	public List<String> getProdSkuInfos() {
		return prodSkuInfos;
	}

	public void setProdSkuInfos(List<String> prodSkuInfos) {
		this.prodSkuInfos = prodSkuInfos;
	}
}
