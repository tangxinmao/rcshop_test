package com.socool.soft.bo.constant;

public enum MerchantWidStatusEnum {
	PENDING(1), //待确认
	CONFIRMED(2), //已确认
	UNCONFIRMED(3), //未确认
	PASSED(4), //通过
	REJECTED(5), //拒绝
	PAID(6); //已支付
	
	private int value;
	
	private MerchantWidStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
