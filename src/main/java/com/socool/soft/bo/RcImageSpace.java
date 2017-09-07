package com.socool.soft.bo;

import java.io.Serializable;
import java.util.Date;

public class RcImageSpace extends QueryParam implements Serializable {

	private static final long serialVersionUID = 6801264599418845883L;
	
	// db
	private Long imageSpaceId;
	private String fileInfoId;
	private Integer memberId;
	private Byte delFlag;
	private Long parentId;
	private Integer type;
	private String name;
	private Date delTime;
	private Integer imgWidth;
	private Integer imgHeight;
	private Long imgSize;
	private Date createTime;
	
	// rel
	private RcMember member;
	private RcImageSpace parentImageSpace;
	private RcFileInfo fileInfo;
	
	// vo
	private String imgUrl;

	public Long getImageSpaceId() {
		return imageSpaceId;
	}

	public void setImageSpaceId(Long imageSpaceId) {
		this.imageSpaceId = imageSpaceId;
	}

	public String getFileInfoId() {
		return fileInfoId;
	}

	public void setFileInfoId(String fileInfoId) {
		this.fileInfoId = fileInfoId;
	}

	public Byte getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Byte delFlag) {
		this.delFlag = delFlag;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDelTime() {
		return delTime;
	}

	public void setDelTime(Date delTime) {
		this.delTime = delTime;
	}

	public Integer getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(Integer imgWidth) {
		this.imgWidth = imgWidth;
	}

	public Integer getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(Integer imgHeight) {
		this.imgHeight = imgHeight;
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public RcImageSpace getParentImageSpace() {
		return parentImageSpace;
	}

	public void setParentImageSpace(RcImageSpace parentImageSpace) {
		this.parentImageSpace = parentImageSpace;
	}

	public RcFileInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(RcFileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getImgSize() {
		return imgSize;
	}

	public void setImgSize(Long imgSize) {
		this.imgSize = imgSize;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}