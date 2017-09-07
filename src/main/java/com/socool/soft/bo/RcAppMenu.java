package com.socool.soft.bo;

import java.io.Serializable;

public class RcAppMenu extends QueryParam implements Serializable {

	private static final long serialVersionUID = -3926550856639227930L;
	
	//db
	private Integer appMenuId;
	private String name;
	private String icon;
	private String location;
	private Integer seq;

	public Integer getAppMenuId() {
		return appMenuId;
	}

	public void setAppMenuId(Integer appMenuId) {
		this.appMenuId = appMenuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}