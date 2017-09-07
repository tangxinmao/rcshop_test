package com.socool.soft.bo.constant;

public enum OrderOperationTypeEnum {
	CREATE(1), //创建
	AMOUNT_MODIFY(2), //修改金额
	PAY(3), //支付
	PAYMENT_CONFIRM(4), //确认支付
	REJECT(5), //拒绝
	REFUND(6), //退款
	DELIVER(7), //发货
	RECEIVE(8), //收货
	PICKUP_CONFIRM(9), //确认提货
	EVALUATE(10), //点评
	REPLY(11), //回复
	CANCEL(12); //取消
	
	private int value;
	
	private OrderOperationTypeEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
