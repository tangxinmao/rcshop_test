package com.socool.soft.bo;

import java.io.Serializable;
import java.util.List;

public class RcShoppingCartOrder implements Serializable {

	private static final long serialVersionUID = 5059506961528364517L;
	
	// vo
	private Integer merchantId;
	private String merchantName;
	private List<RcShoppingCartProd> prods;
	private List<RcCoupon> coupons;

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public List<RcShoppingCartProd> getProds() {
		return prods;
	}

	public void setProds(List<RcShoppingCartProd> prods) {
		this.prods = prods;
	}

	public List<RcCoupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<RcCoupon> coupons) {
		this.coupons = coupons;
	}
}
