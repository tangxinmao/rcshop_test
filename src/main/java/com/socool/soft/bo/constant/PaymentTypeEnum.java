package com.socool.soft.bo.constant;

public enum PaymentTypeEnum {
	BALANCE(1), //余额
	CREDIT_CARD(2), //信用卡
	PAY_CODE(3), //支付码
	DEBIT_CARD(4); //借记卡
	
	private int value;
	
	private PaymentTypeEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
