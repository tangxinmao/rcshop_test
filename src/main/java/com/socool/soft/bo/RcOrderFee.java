package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socool.soft.jsonSerializer.BigDecimalSerializer;

public class RcOrderFee extends QueryParam implements Serializable {

	private static final long serialVersionUID = -5663731327542221783L;

	// db
	private Long orderFeeId;
	private Long orderId;
	private Integer type;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal amount;
	private Byte addToTotal;

	public Long getOrderFeeId() {
		return orderFeeId;
	}

	public void setOrderFeeId(Long orderFeeId) {
		this.orderFeeId = orderFeeId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Byte getAddToTotal() {
		return addToTotal;
	}

	public void setAddToTotal(Byte addToTotal) {
		this.addToTotal = addToTotal;
	}
}