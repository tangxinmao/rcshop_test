package com.socool.soft.bo;

import java.io.Serializable;
import java.util.Date;

public class RcAppVersion extends QueryParam implements Serializable {

	private static final long serialVersionUID = 2420766962282373283L;
	
	// db
	private Integer appVersionId;
	private String verNo;
	private String verInfo;
	private Byte forceFlag;
	private Date updateTime;
	private String downloadUrl;
	private Integer system;
	
	// se
	private String verNoFrom;

	public Integer getAppVersionId() {
		return appVersionId;
	}

	public void setAppVersionId(Integer appVersionId) {
		this.appVersionId = appVersionId;
	}

	public String getVerNo() {
		return verNo;
	}

	public void setVerNo(String verNo) {
		this.verNo = verNo;
	}

	public String getVerInfo() {
		return verInfo;
	}

	public void setVerInfo(String verInfo) {
		this.verInfo = verInfo;
	}

	public Byte getForceFlag() {
		return forceFlag;
	}

	public void setForceFlag(Byte forceFlag) {
		this.forceFlag = forceFlag;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public Integer getSystem() {
		return system;
	}

	public void setSystem(Integer system) {
		this.system = system;
	}

	public String getVerNoFrom() {
		return verNoFrom;
	}

	public void setVerNoFrom(String verNoFrom) {
		this.verNoFrom = verNoFrom;
	}
}