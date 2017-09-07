package com.socool.soft.bo.constant;

public enum VersionSystemEnum {
	ANDROID(1), //Android
	IOS(2); //IOS
	
	private int value;
	
	private VersionSystemEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
