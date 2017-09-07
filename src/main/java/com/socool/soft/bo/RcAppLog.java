package com.socool.soft.bo;

import java.io.Serializable;
import java.util.Date;

public class RcAppLog extends QueryParam implements Serializable {

	private static final long serialVersionUID = -5053711519679765883L;
	
	// db
	private Long appLogId;
	private String sysVer;
	private String model;
	private String appVer;
	private Integer memberId;
	private Date createTime;
	private String errorLog;

	// rel
	private RcMember member;

	public Long getAppLogId() {
		return appLogId;
	}

	public void setAppLogId(Long appLogId) {
		this.appLogId = appLogId;
	}

	public String getSysVer() {
		return sysVer;
	}

	public void setSysVer(String sysVer) {
		this.sysVer = sysVer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getAppVer() {
		return appVer;
	}

	public void setAppVer(String appVer) {
		this.appVer = appVer;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
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
}