package com.socool.soft.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RcOrderEvaluation extends QueryParam implements Serializable {

	private static final long serialVersionUID = -4525594578865419591L;
	
	// db
	private Long orderId;
	private Integer deliveryScore;
	private Integer serviceScore;
	private Date createTime;
	private Byte delFlag;
	private Integer merchantId;
	private Integer vendorId;

	// rel
	private RcOrder order;
	private RcVendor vendor;
	private RcMerchant merchant;
	private RcBuyer member;
	
	// vo
	private String merchantName;
	
	// se
	private Date createTimeFrom;
	private Date createTimeTo;
	private List<Integer> merchantIds;
	
	public Integer getDeliveryScore() {
		return deliveryScore;
	}

	public void setDeliveryScore(Integer deliveryScore) {
		this.deliveryScore = deliveryScore;
	}

	public Integer getServiceScore() {
		return serviceScore;
	}

	public void setServiceScore(Integer serviceScore) {
		this.serviceScore = serviceScore;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Byte getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Byte delFlag) {
		this.delFlag = delFlag;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public RcOrder getOrder() {
		return order;
	}

	public void setOrder(RcOrder order) {
		this.order = order;
	}

	public RcVendor getVendor() {
		return vendor;
	}

	public void setVendor(RcVendor vendor) {
		this.vendor = vendor;
	}

	public RcMerchant getMerchant() {
		return merchant;
	}

	public void setMerchant(RcMerchant merchant) {
		this.merchant = merchant;
	}

	public RcBuyer getMember() {
		return member;
	}

	public void setMember(RcBuyer member) {
		this.member = member;
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

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
}