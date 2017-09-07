package com.socool.soft.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RcVendorUser extends QueryParam implements Serializable {

	private static final long serialVersionUID = -805012342241812778L;
	
	// db
	private Integer memberId;
	private String account;
	private String password;
	private String name;
	private String email;
	private String mobile;
	private String description;
	private Date lastLoginTime;
	private Date signUpTime;
	private Byte delFlag;
	private Integer vendorId;
	private Byte isSuper;
	
	// rel
	private List<RcRole> roles;
	private List<RcMenu> menus;
	
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getSignUpTime() {
		return signUpTime;
	}

	public void setSignUpTime(Date signUpTime) {
		this.signUpTime = signUpTime;
	}

	public Byte getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Byte delFlag) {
		this.delFlag = delFlag;
	}

	public List<RcRole> getRoles() {
		return roles;
	}

	public void setRoles(List<RcRole> roles) {
		this.roles = roles;
	}

	public List<RcMenu> getMenus() {
		return menus;
	}

	public void setMenus(List<RcMenu> menus) {
		this.menus = menus;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Byte getIsSuper() {
		return isSuper;
	}

	public void setIsSuper(Byte isSuper) {
		this.isSuper = isSuper;
	}
}