package com.socool.soft.bo;

import java.io.Serializable;
import java.util.Date;

public class RcVendor extends QueryParam implements Serializable {

	private static final long serialVersionUID = -5559554352614068323L;
	
	// db
	private Integer vendorId;
	private String name;
	private String contactPerson;
	private Date createTime;
	private Byte delFlag;
	private String email;
	private String mobile;
	private String description;

	// rel
	private RcVendorUser member;

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Byte getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Byte delFlag) {
		this.delFlag = delFlag;
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

	public RcVendorUser getMember() {
		return member;
	}

	public void setMember(RcVendorUser member) {
		this.member = member;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}