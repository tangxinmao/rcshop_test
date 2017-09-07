package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socool.soft.jsonSerializer.BigDecimalSerializer;

public class RcOrder extends QueryParam implements Serializable {

	private static final long serialVersionUID = -8665600740072094466L;
	
	// db
	private Long orderId;
	private Integer memberId;
	private Date createTime;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal productPrice;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal payPrice;
	private Integer buyerStatus;
	private Integer sellerStatus;
	private Integer merchantId;
	private Integer deliveryType;
	private Integer paymentInterface;
	private Integer paymentType;
	private String pickupCode;
	private String sellerMemo;
	private String buyerMemo;
	private Integer vendorId;
	private Integer cityId;
	private String payCode;

	// rel
	private RcBuyer member;
	private RcMerchant merchant;
	private List<RcOrderCoupon> couponOrders;
	private RcOrderConsignee orderConsignee;
	private RcOrderDelivery orderDelivery;
	private RcOrderEvaluation orderEvaluation;
	private List<RcOrderOperation> orderOperations;
	private List<RcOrderProd> orderProds;
	private List<RcOrderFee> orderFees;
	
	// vo
	private Integer couponId;
	private Date payTime;
	private Boolean deliveryStatus;
	private Date cancelTime;
	private Date receiveTime;
	private Date deliveryTime;
	private String mobile;
	private String merchantName;
	private String cityName;
	private Date refundTime;//退款时间
	private String orderRejected;//退货理由
	private Integer deliveryScore;
	private Integer serviceScore;
	private Long countDown;//倒计时（分）
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal freight;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal couponAmount;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal creditCardFee;
	private Boolean canceledBySystem;
	
	// se
	private List<Integer> sellerStatuses;
	private List<Long> orderIds;
	private Date createTimeFrom;
	private Date createTimeTo;
	private List<Integer> cityIds;
	private List<Integer> merchantIds;
	private List<Integer> memberIds;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public BigDecimal getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}

	public Integer getBuyerStatus() {
		return buyerStatus;
	}

	public void setBuyerStatus(Integer buyerStatus) {
		this.buyerStatus = buyerStatus;
	}

	public Integer getSellerStatus() {
		return sellerStatus;
	}

	public void setSellerStatus(Integer sellerStatus) {
		this.sellerStatus = sellerStatus;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public Integer getPaymentInterface() {
		return paymentInterface;
	}

	public void setPaymentInterface(Integer paymentInterface) {
		this.paymentInterface = paymentInterface;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public String getPickupCode() {
		return pickupCode;
	}

	public void setPickupCode(String pickupCode) {
		this.pickupCode = pickupCode;
	}

	public String getSellerMemo() {
		return sellerMemo;
	}

	public void setSellerMemo(String sellerMemo) {
		this.sellerMemo = sellerMemo;
	}

	public String getBuyerMemo() {
		return buyerMemo;
	}

	public void setBuyerMemo(String buyerMemo) {
		this.buyerMemo = buyerMemo;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
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

	public RcMerchant getMerchant() {
		return merchant;
	}

	public void setMerchant(RcMerchant merchant) {
		this.merchant = merchant;
	}

	public List<RcOrderCoupon> getCouponOrders() {
		return couponOrders;
	}

	public void setCouponOrders(List<RcOrderCoupon> couponOrders) {
		this.couponOrders = couponOrders;
	}

	public RcOrderConsignee getOrderConsignee() {
		return orderConsignee;
	}

	public void setOrderConsignee(RcOrderConsignee orderConsignee) {
		this.orderConsignee = orderConsignee;
	}

	public RcOrderDelivery getOrderDelivery() {
		return orderDelivery;
	}

	public void setOrderDelivery(RcOrderDelivery orderDelivery) {
		this.orderDelivery = orderDelivery;
	}

	public RcOrderEvaluation getOrderEvaluation() {
		return orderEvaluation;
	}

	public void setOrderEvaluation(RcOrderEvaluation orderEvaluation) {
		this.orderEvaluation = orderEvaluation;
	}

	public List<RcOrderOperation> getOrderOperations() {
		return orderOperations;
	}

	public void setOrderOperations(List<RcOrderOperation> orderOperations) {
		this.orderOperations = orderOperations;
	}

	public List<RcOrderProd> getOrderProds() {
		return orderProds;
	}

	public void setOrderProds(List<RcOrderProd> orderProds) {
		this.orderProds = orderProds;
	}

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public BigDecimal getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(BigDecimal couponAmount) {
		this.couponAmount = couponAmount;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public List<Integer> getSellerStatuses() {
		return sellerStatuses;
	}

	public void setSellerStatuses(List<Integer> sellerStatuses) {
		this.sellerStatuses = sellerStatuses;
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

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

//	public List<Long> getOrderIds() {
//		return orderIds;
//	}
//
//	public void setOrderIds(List<Long> orderIds) {
//		this.orderIds = orderIds;
//	}

	public List<Integer> getCityIds() {
		return cityIds;
	}

	public void setCityIds(List<Integer> cityIds) {
		this.cityIds = cityIds;
	}

	public List<Integer> getMerchantIds() {
		return merchantIds;
	}

	public void setMerchantIds(List<Integer> merchantIds) {
		this.merchantIds = merchantIds;
	}

	public List<Integer> getMemberIds() {
		return memberIds;
	}

	public void setMemberIds(List<Integer> memberIds) {
		this.memberIds = memberIds;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public Boolean getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(Boolean deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public String getOrderRejected() {
		return orderRejected;
	}

	public void setOrderRejected(String orderRejected) {
		this.orderRejected = orderRejected;
	}

	public List<Long> getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(List<Long> orderIds) {
		this.orderIds = orderIds;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

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

	public Long getCountDown() {
		return countDown;
	}

	public void setCountDown(Long countDown) {
		this.countDown = countDown;
	}

	public List<RcOrderFee> getOrderFees() {
		return orderFees;
	}

	public void setOrderFees(List<RcOrderFee> orderFees) {
		this.orderFees = orderFees;
	}

	public BigDecimal getCreditCardFee() {
		return creditCardFee;
	}

	public void setCreditCardFee(BigDecimal creditCardFee) {
		this.creditCardFee = creditCardFee;
	}

	public Boolean getCanceledBySystem() {
		return canceledBySystem;
	}

	public void setCanceledBySystem(Boolean canceledBySystem) {
		this.canceledBySystem = canceledBySystem;
	}
}