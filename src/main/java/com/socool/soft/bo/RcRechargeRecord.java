package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socool.soft.jsonSerializer.BigDecimalSerializer;

public class RcRechargeRecord extends QueryParam implements Serializable {

	private static final long serialVersionUID = 882230603566188670L;
	
	// db
	private Integer rechargeRecordId;
	private Date createTime;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal amount;
	private Integer memberId;
	private Integer rechargeStationId;
	private Integer rechargeMemberId;

	// rel
	private RcBuyer member;
	private RcRechargeStation rechargeStation;
	private RcUser rechargeMember;
	
	// vo
	private String rechargeStationName;
	
	// se
	private Date createTimeFrom;
	private Date createTimeTo;
	private List<Integer> rechargeStationIds;

	public Integer getRechargeRecordId() {
		return rechargeRecordId;
	}

	public void setRechargeRecordId(Integer rechargeRecordId) {
		this.rechargeRecordId = rechargeRecordId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public Integer getRechargeStationId() {
		return rechargeStationId;
	}

	public void setRechargeStationId(Integer rechargeStationId) {
		this.rechargeStationId = rechargeStationId;
	}

	public RcRechargeStation getRechargeStation() {
		return rechargeStation;
	}

	public void setRechargeStation(RcRechargeStation rechargeStation) {
		this.rechargeStation = rechargeStation;
	}

	public Integer getRechargeMemberId() {
		return rechargeMemberId;
	}

	public void setRechargeMemberId(Integer rechargeMemberId) {
		this.rechargeMemberId = rechargeMemberId;
	}

	public RcUser getRechargeMember() {
		return rechargeMember;
	}

	public void setRechargeMember(RcUser rechargeMember) {
		this.rechargeMember = rechargeMember;
	}

	public String getRechargeStationName() {
		return rechargeStationName;
	}

	public void setRechargeStationName(String rechargeStationName) {
		this.rechargeStationName = rechargeStationName;
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

	public List<Integer> getRechargeStationIds() {
		return rechargeStationIds;
	}

	public void setRechargeStationIds(List<Integer> rechargeStationIds) {
		this.rechargeStationIds = rechargeStationIds;
	}
}