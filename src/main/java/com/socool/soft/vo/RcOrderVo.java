package com.socool.soft.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.socool.soft.bo.RcOrder;



public class RcOrderVo extends RcOrder {
	private Date orderTimeStart;
	private Date orderTimeEnd;
	private String memberAccount;
	private String merchantName;
	private String memberNickname;
	private Integer couponId;
	private String mobile;
	private Integer vendorId;
	private String condition;//构建查询条件
	private List<RcOrderProdVo> product;
	private RcConsigneeVo consignee;
	private String consigneeName;
	private String consigneeMobile;
	private String consigneeDetail;
	private Date evaluationTime;
	private String orderStatusStr;
	private String city;
	private BigDecimal couponAmount;
	private Date deliveryTime;
	private Integer deliveryType;
	private String deliveryName;
	private String deliveryMobile;
	private String logisticsCompany;
	private String trackingNumber;
	private String orderRejected;
	private String orderMemberMemoStr;
	private String privince;
	private String orderTimeDesc;
	private String orderTimeStr;
	private String orderPayPriceDesc;
	private String vendorName;
	private Date orderTime;
	private Integer orderPayment;
 


	public Date getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public Integer getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}
	public String getDeliveryName() {
		return deliveryName;
	}
	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	public String getDeliveryMobile() {
		return deliveryMobile;
	}
	public void setDeliveryMobile(String deliveryMobile) {
		this.deliveryMobile = deliveryMobile;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public String getConsigneeMobile() {
		return consigneeMobile;
	}
	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}
	public Date getOrderTimeStart() {
		return orderTimeStart;
	}
	public void setOrderTimeStart(Date orderTimeStart) {
		this.orderTimeStart = orderTimeStart;
	}
	public Date getOrderTimeEnd() {
		return orderTimeEnd;
	}
	public void setOrderTimeEnd(Date orderTimeEnd) {
		this.orderTimeEnd = orderTimeEnd;
	}
	public String getMemberAccount() {
		return memberAccount;
	}
	public void setMemberAccount(String memberAccount) {
		this.memberAccount = memberAccount;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public List<RcOrderProdVo> getProduct() {
		return product;
	}
	public void setProduct(List<RcOrderProdVo> product) {
		this.product = product;
	}
	public String getConsigneeDetail() {
		return consigneeDetail;
	}
	public void setConsigneeDetail(String consigneeDetail) {
		this.consigneeDetail = consigneeDetail;
	}
	public String getMemberNickname() {
		return memberNickname;
	}
	public void setMemberNickname(String memberNickname) {
		this.memberNickname = memberNickname;
	}
	
	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}
	public Date getEvaluationTime() {
		return evaluationTime;
	}
	public void setEvaluationTime(Date evaluationTime) {
		this.evaluationTime = evaluationTime;
	}
	public BigDecimal getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(BigDecimal couponAmount) {
		this.couponAmount = couponAmount;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOrderStatusStr() {
		return orderStatusStr;
	}
	public void setOrderStatusStr(String orderStatusStr) {
		this.orderStatusStr = orderStatusStr;
	}
	public String getLogisticsCompany() {
		return logisticsCompany;
	}
	public void setLogisticsCompany(String logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}
	public RcConsigneeVo getConsignee() {
		return consignee;
	}
	public void setConsignee(RcConsigneeVo consignee) {
		this.consignee = consignee;
	}
	public String getOrderRejected() {
		return orderRejected;
	}
	public void setOrderRejected(String orderRejected) {
		this.orderRejected = orderRejected;
	}
	public String getOrderMemberMemoStr() {
		return orderMemberMemoStr;
	}
	public void setOrderMemberMemoStr(String orderMemberMemoStr) {
		this.orderMemberMemoStr = orderMemberMemoStr;
	}

	public String getPrivince() {
		return privince;
	}
	public void setPrivince(String privince) {
		this.privince = privince;
	}
	public String getOrderTimeDesc() {
		return orderTimeDesc;
	}
	public void setOrderTimeDesc(String orderTimeDesc) {
		this.orderTimeDesc = orderTimeDesc;
	}
	public String getOrderPayPriceDesc() {
		return orderPayPriceDesc;
	}
	public void setOrderPayPriceDesc(String orderPayPriceDesc) {
		this.orderPayPriceDesc = orderPayPriceDesc;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public Integer getVendorId() {
		return vendorId;
	}
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	public String getOrderTimeStr() {
		return orderTimeStr;
	}
	public void setOrderTimeStr(String orderTimeStr) {
		this.orderTimeStr = orderTimeStr;
	}


	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Integer getOrderPayment() {
		return orderPayment;
	}
	public void setOrderPayment(Integer orderPayment) {
		this.orderPayment = orderPayment;
	}



	
	
  

}