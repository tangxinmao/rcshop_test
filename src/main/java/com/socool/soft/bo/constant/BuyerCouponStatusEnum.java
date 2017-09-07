package com.socool.soft.bo.constant;

public enum BuyerCouponStatusEnum {
	UNUSED(1), //未使用
	USED(2); //已使用
	
	private int value;
	
	private BuyerCouponStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
