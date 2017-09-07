package com.socool.soft.bo.constant;

public enum BuyerTradeTypeEnum {
	RECHARGE(1), //充值
	EXPENSE(2), //支出
	REFUND(3); //退款
	
	private int value;
	
	private BuyerTradeTypeEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
