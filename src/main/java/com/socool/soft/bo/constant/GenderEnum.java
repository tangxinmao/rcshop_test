package com.socool.soft.bo.constant;

public enum GenderEnum {
	MALE(1), //男
	FEMALE(2); //女
	
	private int value;
	
	private GenderEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
