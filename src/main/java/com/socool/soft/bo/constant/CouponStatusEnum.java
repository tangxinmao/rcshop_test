package com.socool.soft.bo.constant;

public enum CouponStatusEnum {
	UNPUBLISHED(1), //未发布
	PUBLISHED(2), //已发布
	UNDID(3); //已撤销
	
	private int value;
	
	private CouponStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
