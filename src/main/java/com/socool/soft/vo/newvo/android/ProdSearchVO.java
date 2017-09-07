package com.socool.soft.vo.newvo.android;

import java.util.List;

import com.socool.soft.bo.RcProd;

public class ProdSearchVO {

	private int count;
	private List<RcProd> prods;
	private List<String> brands;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<RcProd> getProds() {
		return prods;
	}

	public void setProds(List<RcProd> prods) {
		this.prods = prods;
	}

	public List<String> getBrands() {
		return brands;
	}

	public void setBrands(List<String> brands) {
		this.brands = brands;
	}
}
