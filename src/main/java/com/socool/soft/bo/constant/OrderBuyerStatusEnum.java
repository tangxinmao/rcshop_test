package com.socool.soft.bo.constant;

public enum OrderBuyerStatusEnum {
	UNPAID(1), //待付款
	UNRECEIVED(2), //待收货
	UNPICKEDUP(3), //待提货
	REJECTED(4), //已拒绝
	REFUNDED(5), //已退款
	UNEVALUATED(6), //待点评
	FINISHED(7),  //完成
	CANCELED(8); //取消
	
	private int value;
	
	private OrderBuyerStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}

}
