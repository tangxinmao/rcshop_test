package com.socool.soft.bo.constant;

public enum OrderDeliveryDeliveryTypeEnum {
	BY_MERCHANT(1), //商家
	BY_EXPRESS(2); //快递
	
	private int value;
	
	private OrderDeliveryDeliveryTypeEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
