package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socool.soft.jsonSerializer.BigDecimalSerializer;

public class RcOrderPayment extends QueryParam implements Serializable {

	private static final long serialVersionUID = 1924185258879214953L;

	// db
	private Long orderPaymentId;
	private Long orderId;
	private Integer paymentInterface;
	private Integer paymentType;
	private String transactionId;
	private String thirdPartyTransactionId;
	private String payCode;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal amount;
	private Integer status;
	private Date createTime;
	private Date finishTime;

	public Long getOrderPaymentId() {
		return orderPaymentId;
	}

	public void setOrderPaymentId(Long orderPaymentId) {
		this.orderPaymentId = orderPaymentId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getThirdPartyTransactionId() {
		return thirdPartyTransactionId;
	}

	public void setThirdPartyTransactionId(String thirdPartyTransactionId) {
		this.thirdPartyTransactionId = thirdPartyTransactionId;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date successTime) {
		this.finishTime = successTime;
	}
}