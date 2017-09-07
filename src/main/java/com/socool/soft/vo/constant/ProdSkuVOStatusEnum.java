package com.socool.soft.vo.constant;

public enum ProdSkuVOStatusEnum {
	VALID(1), //有效
	CAN_NOT_DELIVER(2), //无法配送
	SOLD_OUT(3); //下架
	
	private int value;
	
	private ProdSkuVOStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
