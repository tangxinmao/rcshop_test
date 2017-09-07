package com.socool.soft.bo.constant;

public enum OrderPaymentStatusEnum {
	FINISHED(1), //完成
	PENDING(2), //待支付
	INVALID(3); //无效
	
	private int value;
	
	private OrderPaymentStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
