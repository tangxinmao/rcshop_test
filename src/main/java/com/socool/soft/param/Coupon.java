package com.socool.soft.param;

import java.math.BigDecimal;
import java.util.Date;

public class Coupon {
	
	private Date createTimeStart;
	private Date createTimeEnd;
	private Integer Status;
	private BigDecimal amountStart;
	private BigDecimal amountEnd;
	private String merchantName;
	private Integer merchantId;
	private Integer couponId;
	private Integer initCount;
	private Integer leftCount;
	private BigDecimal amount;
	private Date effectTime;
	private Date expireTime;
	private BigDecimal baseAmount;
	private String description;
	private String name;
	private Integer prodCatId;
	private Integer type;
	
	public Date getCreateTimeStart() {
		return createTimeStart;
	}
	public void setCreateTimeStart(Date createTimeStart) {
		this.createTimeStart = createTimeStart;
	}
	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	public Integer getStatus() {
		return Status;
	}
	public void setStatus(Integer status) {
		Status = status;
	}
	public BigDecimal getAmountStart() {
		return amountStart;
	}
	public void setAmountStart(BigDecimal amountStart) {
		this.amountStart = amountStart;
	}
	public BigDecimal getAmountEnd() {
		return amountEnd;
	}
	public void setAmountEnd(BigDecimal amountEnd) {
		this.amountEnd = amountEnd;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Integer getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	public Integer getCouponId() {
		return couponId;
	}
	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}
	public Integer getInitCount() {
		return initCount;
	}
	public void setInitCount(Integer initCount) {
		this.initCount = initCount;
	}
	public Integer getLeftCount() {
		return leftCount;
	}
	public void setLeftCount(Integer leftCount) {
		this.leftCount = leftCount;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Date getEffectTime() {
		return effectTime;
	}
	public void setEffectTime(Date effectTime) {
		this.effectTime = effectTime;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	public BigDecimal getBaseAmount() {
		return baseAmount;
	}
	public void setBaseAmount(BigDecimal baseAmount) {
		this.baseAmount = baseAmount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getProdCatId() {
		return prodCatId;
	}
	public void setProdCatId(Integer prodCatId) {
		this.prodCatId = prodCatId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
