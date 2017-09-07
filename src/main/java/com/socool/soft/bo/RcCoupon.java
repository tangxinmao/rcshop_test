package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socool.soft.jsonSerializer.BigDecimalSerializer;

public class RcCoupon extends QueryParam implements Serializable {

	private static final long serialVersionUID = -2613246065641963138L;
	
	// db
	private Integer couponId;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal amount;
	private Date createTime;
	private Integer initCount;
	private Integer leftCount;
	@JsonFormat(pattern = "yyyy-MM-dd 'WIB'")
	private Date effectTime;
	@JsonFormat(pattern = "yyyy-MM-dd 'WIB'") 
	private Date expireTime;
	private Integer merchantId;
	private Integer prodCatId;
	private Byte delFlag;
	private String name;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal baseAmount;
	private String description;
	private Integer status;
	private Date receiveStartTime;
	private Date receiveEndTime;

	// rel
	private RcMerchant merchant;
	private RcProdCat prodCat;

	// vo
	private Integer type;
	private String merchantName;
	private String ProdCatName;
	private Integer couponMemberStatus;

	// se
	private Date receiveEndTimeFrom;
	private Date receiveEndTimeTo;
	private BigDecimal amountFrom;
	private BigDecimal amountTo;
	private Date createTimeFrom;
	private Date createTimeTo;
	private List<Integer> merchantIds;
	private List<Integer> statuses;

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Byte getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Byte delFlag) {
		this.delFlag = delFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getReceiveStartTime() {
		return receiveStartTime;
	}

	public void setReceiveStartTime(Date receiveStartTime) {
		this.receiveStartTime = receiveStartTime;
	}

	public Date getReceiveEndTime() {
		return receiveEndTime;
	}

	public void setReceiveEndTime(Date receiveEndTime) {
		this.receiveEndTime = receiveEndTime;
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

	public Integer getProdCatId() {
		return prodCatId;
	}

	public void setProdCatId(Integer prodCatId) {
		this.prodCatId = prodCatId;
	}

	public RcProdCat getProdCat() {
		return prodCat;
	}

	public void setProdCat(RcProdCat prodCat) {
		this.prodCat = prodCat;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getProdCatName() {
		return ProdCatName;
	}

	public void setProdCatName(String prodCatName) {
		ProdCatName = prodCatName;
	}

	public Integer getCouponMemberStatus() {
		return couponMemberStatus;
	}

	public void setCouponMemberStatus(Integer couponMemberStatus) {
		this.couponMemberStatus = couponMemberStatus;
	}

	public Date getReceiveEndTimeFrom() {
		return receiveEndTimeFrom;
	}

	public void setReceiveEndTimeFrom(Date receiveEndTimeFrom) {
		this.receiveEndTimeFrom = receiveEndTimeFrom;
	}

	public BigDecimal getAmountFrom() {
		return amountFrom;
	}

	public void setAmountFrom(BigDecimal amountFrom) {
		this.amountFrom = amountFrom;
	}

	public BigDecimal getAmountTo() {
		return amountTo;
	}

	public void setAmountTo(BigDecimal amountTo) {
		this.amountTo = amountTo;
	}

	public Date getCreateTimeFrom() {
		return createTimeFrom;
	}

	public void setCreateTimeFrom(Date createTimeFrom) {
		this.createTimeFrom = createTimeFrom;
	}

	public Date getCreateTimeTo() {
		return createTimeTo;
	}

	public void setCreateTimeTo(Date createTimeTo) {
		this.createTimeTo = createTimeTo;
	}

	public List<Integer> getMerchantIds() {
		return merchantIds;
	}

	public void setMerchantIds(List<Integer> merchantIds) {
		this.merchantIds = merchantIds;
	}

	public List<Integer> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<Integer> statuses) {
		this.statuses = statuses;
	}

	public Date getReceiveEndTimeTo() {
		return receiveEndTimeTo;
	}

	public void setReceiveEndTimeTo(Date receiveEndTimeTo) {
		this.receiveEndTimeTo = receiveEndTimeTo;
	}
}