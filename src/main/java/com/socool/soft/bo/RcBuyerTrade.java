package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socool.soft.jsonSerializer.BigDecimalSerializer;

public class RcBuyerTrade extends QueryParam implements Serializable {

	private static final long serialVersionUID = 5521956551440733413L;
	
	// db
	private Long buyerTradeId;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal amount;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal balance;
	private Integer type;
	private Long orderId;
	private Date createTime;
	private Integer buyerId;
	private Integer paymentType;
	private Integer paymentInterface;

	// rel
	private RcOrder order;
	private RcBuyer member;
	
	// vo
	private List<RcProd> prods;
	private List<String> prodNames;

	public Long getBuyerTradeId() {
		return buyerTradeId;
	}

	public void setBuyerTradeId(Long buyerTradeId) {
		this.buyerTradeId = buyerTradeId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getPaymentInterface() {
		return paymentInterface;
	}

	public void setPaymentInterface(Integer paymentInterface) {
		this.paymentInterface = paymentInterface;
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

	public Integer getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Integer memberId) {
		this.buyerId = memberId;
	}

	public RcBuyer getMember() {
		return member;
	}

	public void setMember(RcBuyer member) {
		this.member = member;
	}

	public List<RcProd> getProds() {
		return prods;
	}

	public void setProds(List<RcProd> prods) {
		this.prods = prods;
	}

	public List<String> getProdNames() {
		return prodNames;
	}

	public void setProdNames(List<String> prodNames) {
		this.prodNames = prodNames;
	}
}