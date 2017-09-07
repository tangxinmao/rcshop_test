package com.socool.soft.bo;

import java.io.Serializable;
import java.util.Date;

public class RcFileInfo extends QueryParam implements Serializable {
	
	private static final long serialVersionUID = 5931451092725717962L;
	
	// db
	private String fileInfoId;
	private String originalName;
	private String realName;
	private String suffixName;
	private Long size;
	private String relativePath;
	private Date createTime;
	private Integer memberId;

	public String getFileInfoId() {
		return fileInfoId;
	}

	public void setFileInfoId(String fileInfoId) {
		this.fileInfoId = fileInfoId == null ? null : fileInfoId.trim();
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSuffixName() {
		return suffixName;
	}

	public void setSuffixName(String suffixName) {
		this.suffixName = suffixName == null ? null : suffixName.trim();
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath == null ? null : relativePath.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	
	public String getPath() {
		return (this.relativePath + this.realName + "." + this.suffixName).replaceAll("\\\\", "/");
	}
}