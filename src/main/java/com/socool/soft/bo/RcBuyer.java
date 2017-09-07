package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RcBuyer extends QueryParam implements Serializable {

	private static final long serialVersionUID = 2073234226808833739L;
	
	// db
	private Integer memberId;
	private String account;
	private String password;
	private String name;
	private String email;
	private Integer sex;
	private String mobile;
	private String imgUrl;
	private String description;
	private Date lastLoginTime;
	private Date signUpTime;
	private Byte delFlag;
	private BigDecimal walletAmount;
	private String walletPwd;
	
	//vo
	private String token;
	
	// rel
	private List<RcBuyerTrade> memberTrades;
	
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

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
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

	public BigDecimal getWalletAmount() {
		return walletAmount;
	}

	public void setWalletAmount(BigDecimal walletAmount) {
		this.walletAmount = walletAmount;
	}

	public String getWalletPwd() {
		return walletPwd;
	}

	public void setWalletPwd(String walletPwd) {
		this.walletPwd = walletPwd;
	}

	public List<RcBuyerTrade> getMemberTrades() {
		return memberTrades;
	}

	public void setMemberTrades(List<RcBuyerTrade> memberTrades) {
		this.memberTrades = memberTrades;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}