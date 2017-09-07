package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socool.soft.jsonSerializer.BigDecimalSerializer;

public class RcMerchantWid extends QueryParam implements Serializable {

	private static final long serialVersionUID = -7210658015134700754L;
	
	// db
	private Long merchantWidId;
	private Integer merchantId;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal applyAmount;
	private Date applyTime;
	private Date processTime;
	private Integer status;
	private Date payTime;
	private String balanceMonth;
	private String bankName;
	private String bankAccount;
	private String bankAccountName;

	// rel
	private RcMerchant merchant;
	
	// vo
	private String merchantName;
	
	// se
	private List<Integer> merchantIds;

	public Long getMerchantWidId() {
		return merchantWidId;
	}

	public void setMerchantWidId(Long merchantWidId) {
		this.merchantWidId = merchantWidId;
	}

	public BigDecimal getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getBalanceMonth() {
		return balanceMonth;
	}

	public void setBalanceMonth(String balanceMonth) {
		this.balanceMonth = balanceMonth;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public RcMerchant getMerchant() {
		return merchant;
	}

	public void setMerchant(RcMerchant merchant) {
		this.merchant = merchant;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public List<Integer> getMerchantIds() {
		return merchantIds;
	}

	public void setMerchantIds(List<Integer> merchantIds) {
		this.merchantIds = merchantIds;
	}
}