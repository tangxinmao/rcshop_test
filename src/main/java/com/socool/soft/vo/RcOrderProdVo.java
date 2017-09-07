package com.socool.soft.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.socool.soft.bo.RcOrderProd;
public class RcOrderProdVo extends RcOrderProd{
	private String prodImg;
	private String prodName;
	private Integer prodBuyAmt;
	private BigDecimal ProdTotelAmt;
	private List<String> prodProp=new ArrayList<String>();
	public String getProdImg() {
		return prodImg;
	}
	public void setProdImg(String prodImg) {
		this.prodImg = prodImg;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public List<String> getProdProp() {
		return prodProp;
	}
	public void setProdProp(List<String> prodProp) {
		this.prodProp = prodProp;
	}
	public Integer getProdBuyAmt() {
		return prodBuyAmt;
	}
	public void setProdBuyAmt(Integer prodBuyAmt) {
		this.prodBuyAmt = prodBuyAmt;
	}
	public BigDecimal getProdTotelAmt() {
		return ProdTotelAmt;
	}
	public void setProdTotelAmt(BigDecimal prodTotelAmt) {
		ProdTotelAmt = prodTotelAmt;
	}

	
}
