package com.socool.soft.bo.constant;

public enum YesOrNoEnum {
	YES((byte)1), //是
	NO((byte)0); //否
	
	private byte value;
	
	private YesOrNoEnum(byte value) {
		this.value = value;
	}
	
	public byte getValue() {
		return value;
	}
	
	public void setValue(byte value) {
		this.value = value;
	}
}
