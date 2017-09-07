package com.socool.soft.vo;

import java.io.Serializable;
import java.util.Date;

public class Code implements Serializable {

	private static final long serialVersionUID = 1031803638853910449L;
	
	private Date createDate;
	private String code;
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
