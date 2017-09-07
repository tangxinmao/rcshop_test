package com.socool.soft.bo;

import java.io.Serializable;

public class RcHotKey extends QueryParam implements Serializable {

	private static final long serialVersionUID = -6638791401191852711L;
	
	// db
	private Integer hotKeyId;
	private String hotKey;
	private Integer seq;

	public Integer getHotKeyId() {
		return hotKeyId;
	}

	public void setHotKeyId(Integer hotKeyId) {
		this.hotKeyId = hotKeyId;
	}

	public String getHotKey() {
		return hotKey;
	}

	public void setHotKey(String hotKey) {
		this.hotKey = hotKey;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}