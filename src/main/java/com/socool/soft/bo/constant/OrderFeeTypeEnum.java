package com.socool.soft.bo.constant;

public enum OrderFeeTypeEnum {
	COUPON(1), //优惠券
	FREIGHT(2), //运费
	TAX(3), //税
	SERVICE_FEE(4), //服务费
	MODIFICATION(5), //修改
	CREDIT_CARD_FEE(6); //信用卡手续费
	
	private int value;
	
	private OrderFeeTypeEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
