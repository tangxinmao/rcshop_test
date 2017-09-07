package com.socool.soft.vo.newvo;

import java.math.BigDecimal;

public class ReportMerchantVO {
	
	private Integer merchantId;
	private String merchantName;
	private BigDecimal merchantAmount;

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

	public BigDecimal getMerchantAmount() {
		return merchantAmount;
	}

	public void setMerchantAmount(BigDecimal merchantAmount) {
		this.merchantAmount = merchantAmount;
	}
}
