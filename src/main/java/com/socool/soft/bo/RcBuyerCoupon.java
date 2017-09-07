package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socool.soft.jsonSerializer.BigDecimalSerializer;

public class RcBuyerCoupon extends QueryParam implements Serializable {

	private static final long serialVersionUID = -487632682177931614L;
	
	// db
	private Integer buyerCouponId;
	private Integer couponId;
	private Integer buyerId;
	private Date createTime;
	private Integer status;
	private Date effectTime;
	private Date expireTime;

	// rel
	private RcCoupon coupon;
	private RcBuyer member;
	
	// vo
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal amount;
	private Integer leftCount;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal baseAmount;
	private Integer couponMemberStatus;
	private String description;
	private Integer type;
	private Integer merchantId;
	private String merchantName;
	private Integer prodCatId;
	private String prodCatName;
	
	// se
	private List<Integer> couponIds;
	private Date expireTimeFrom;
	private Date expireTimeTo;

	public Integer getBuyerCouponId() {
		return buyerCouponId;
	}

	public void setBuyerCouponId(Integer buyerCouponId) {
		this.buyerCouponId = buyerCouponId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public RcCoupon getCoupon() {
		return coupon;
	}

	public void setCoupon(RcCoupon coupon) {
		this.coupon = coupon;
	}

	public Integer getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}

	public RcBuyer getMember() {
		return member;
	}

	public void setMember(RcBuyer member) {
		this.member = member;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getLeftCount() {
		return leftCount;
	}

	public void setLeftCount(Integer leftCount) {
		this.leftCount = leftCount;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

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

	public Integer getProdCatId() {
		return prodCatId;
	}

	public void setProdCatId(Integer prodCatId) {
		this.prodCatId = prodCatId;
	}

	public String getProdCatName() {
		return prodCatName;
	}

	public void setProdCatName(String prodCatName) {
		this.prodCatName = prodCatName;
	}

	public List<Integer> getCouponIds() {
		return couponIds;
	}

	public void setCouponIds(List<Integer> couponIds) {
		this.couponIds = couponIds;
	}

	public Date getExpireTimeFrom() {
		return expireTimeFrom;
	}

	public void setExpireTimeFrom(Date expireTimeFrom) {
		this.expireTimeFrom = expireTimeFrom;
	}

	public Date getExpireTimeTo() {
		return expireTimeTo;
	}

	public void setExpireTimeTo(Date expireTimeTo) {
		this.expireTimeTo = expireTimeTo;
	}

	public Integer getCouponMemberStatus() {
		return couponMemberStatus;
	}

	public void setCouponMemberStatus(Integer couponMemberStatus) {
		this.couponMemberStatus = couponMemberStatus;
	}
}