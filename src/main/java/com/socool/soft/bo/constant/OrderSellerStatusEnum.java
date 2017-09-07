package com.socool.soft.bo.constant;

public enum OrderSellerStatusEnum {
	UNPAID(1), //待付款
	UNDELIVERED(2), //待发货
	UNPICKEDUP(3), //待提货
	REJECTED(4), //已拒绝
	REFUNDED(5), //已退款
	DELIVERED(6), //已发货
	RECEIVED(7), //已收货
	PICKEDUP(8), //已提货
	UNREPLIED(9), //待回复
	FINISHED(10), //完成
	CANCELED(11); //取消
	
	private int value;
	
	private OrderSellerStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
