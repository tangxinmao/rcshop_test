package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socool.soft.jsonSerializer.BigDecimalSerializer;

public class RcOrderCoupon extends QueryParam implements Serializable {

	private static final long serialVersionUID = -3371455680163131054L;
	
	// db
	private Integer couponOrderId;
	private Long orderId;
	private Integer couponId;
	private Integer memberId;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal couponAmount;
	
	// rel
	private RcOrder order;
	private RcCoupon coupon;
	private RcBuyer member;

	public Integer getCouponOrderId() {
		return couponOrderId;
	}

	public void setCouponOrderId(Integer couponOrderId) {
		this.couponOrderId = couponOrderId;
	}

	public BigDecimal getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(BigDecimal couponAmount) {
		this.couponAmount = couponAmount;
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
}