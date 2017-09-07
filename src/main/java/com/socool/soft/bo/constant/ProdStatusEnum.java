package com.socool.soft.bo.constant;

public enum ProdStatusEnum {
	SOLD_OUT(1), //下架
	PUTAWAY(2); //上架
	
	private int value;
	
	private ProdStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
