package com.socool.soft.bo;

import java.io.Serializable;

public class RcMember extends QueryParam implements Serializable {

	private static final long serialVersionUID = 778004078024040222L;
	
	// db
	private Integer memberId;
	private String account;
	private String email;
	private String mobile;

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}