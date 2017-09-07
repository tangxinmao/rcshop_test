package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RcMerchant extends QueryParam implements Serializable {

	private static final long serialVersionUID = 5708032655771251915L;
	
	// db
	private Integer merchantId;
	private Integer vendorId;
	private String name;
	private String description;
	private String logoUrl;
	private Date createTime;
	private String mobile;
	private Byte delFlag;
	private String contactPerson;
	private Float productScore;
	private Float deliveryScore;
	private Float serviceScore;
	private String bankName;
	private String bankAccount;
	private String bankAccountName;
	private Integer cityId;
	private String address;
	private String email;

	// rel
	private RcMerchantUser member;
	private RcVendor vendor;
	private RcCity city;
	private String cityName;
	
	//vo
	private BigDecimal yIncome;//昨日收入
	private BigDecimal tIncome;//今日收入
	private Integer tTrans;//今日交易笔数
	private String account;
	private Integer merchantUserId;
	
	// se
	private List<Integer> cityIds;

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Byte getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Byte delFlag) {
		this.delFlag = delFlag;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public Float getProductScore() {
		return productScore;
	}

	public void setProductScore(Float productScore) {
		this.productScore = productScore;
	}

	public Float getDeliveryScore() {
		return deliveryScore;
	}

	public void setDeliveryScore(Float deliveryScore) {
		this.deliveryScore = deliveryScore;
	}

	public Float getServiceScore() {
		return serviceScore;
	}

	public void setServiceScore(Float serviceScore) {
		this.serviceScore = serviceScore;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public RcMerchantUser getMember() {
		return member;
	}

	public void setMember(RcMerchantUser member) {
		this.member = member;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public RcVendor getVendor() {
		return vendor;
	}

	public void setVendor(RcVendor vendor) {
		this.vendor = vendor;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public RcCity getCity() {
		return city;
	}

	public void setCity(RcCity city) {
		this.city = city;
	}

	public List<Integer> getCityIds() {
		return cityIds;
	}

	public void setCityIds(List<Integer> cityIds) {
		this.cityIds = cityIds;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public BigDecimal getyIncome() {
		return yIncome;
	}

	public void setyIncome(BigDecimal yIncome) {
		this.yIncome = yIncome;
	}

	public Integer gettTrans() {
		return tTrans;
	}

	public void settTrans(Integer tTrans) {
		this.tTrans = tTrans;
	}

	public BigDecimal gettIncome() {
		return tIncome;
	}

	public void settIncome(BigDecimal tIncome) {
		this.tIncome = tIncome;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getMerchantUserId() {
		return merchantUserId;
	}

	public void setMerchantUserId(Integer merchantUserId) {
		this.merchantUserId = merchantUserId;
	}
}