package com.socool.soft.vo.constant;

public enum CouponVOTypeEnum {
	MERCHANT(1), //商家
	PLATFORM(2); //平台
	
	private int value;
	
	private CouponVOTypeEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
