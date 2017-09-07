package com.socool.soft.vo.constant;

public enum OrderVOStatusEnum {
	UNPAID(1), //待付款
	UNDELIVERED(2), //待发货
	UNPICKEDUP(3), //待提货
	REJECTED(4), //已拒绝
	REFUNDED(5), //已退款
	DELIVERED(6), //已发货
	FINISHED(7), //完成
	CANCELED(8); //取消
	
	private int value;
	
	private OrderVOStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
