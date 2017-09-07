package com.socool.soft.bo.constant;

public enum HttpRequestMethodEnum {
	GET(1),
	POST(2),
	PUT(3),
	DELETE(4);
	
	private int value;
	
	private HttpRequestMethodEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
