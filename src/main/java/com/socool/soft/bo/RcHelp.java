package com.socool.soft.bo;

import java.io.Serializable;

public class RcHelp extends QueryParam implements Serializable {

	private static final long serialVersionUID = 3460011447702561272L;
	
	// db
	private Integer helpId;
	private String name;
	private Integer seq;
	private String description;
	private String content;

	public Integer getHelpId() {
		return helpId;
	}

	public void setHelpId(Integer helpId) {
		this.helpId = helpId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}