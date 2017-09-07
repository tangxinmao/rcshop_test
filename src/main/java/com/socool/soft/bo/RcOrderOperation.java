package com.socool.soft.bo;

import java.io.Serializable;
import java.util.Date;

public class RcOrderOperation extends QueryParam implements Serializable {

	private static final long serialVersionUID = 3626137755479786255L;
	
	// db
	private Long orderOperationId;
	private Long orderId;
	private Integer operationType;
	private Integer memberId;
	private String operationMemo;
	private Date operationTime;
	
	// rel
	private RcMember member;
	
	// se
	private Date operationTimeTo;

	public Long getOrderOperationId() {
		return orderOperationId;
	}

	public void setOrderOperationId(Long orderOperationId) {
		this.orderOperationId = orderOperationId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public String getOperationMemo() {
		return operationMemo;
	}

	public void setOperationMemo(String operationMemo) {
		this.operationMemo = operationMemo;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public RcMember getMember() {
		return member;
	}

	public void setMember(RcMember member) {
		this.member = member;
	}

	public Date getOperationTimeTo() {
		return operationTimeTo;
	}

	public void setOperationTimeTo(Date operationTimeTo) {
		this.operationTimeTo = operationTimeTo;
	}
}