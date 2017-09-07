package com.socool.soft.bo.constant;

public enum OrderDeliveryTypeEnum {
	BY_MERCHANT(1), //商家配送
	BY_BUYER(2); //自提
	
	private int value;
	
	private OrderDeliveryTypeEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
