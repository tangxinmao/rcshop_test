package com.socool.soft.vo.constant;

public enum CouponVOStatusEnum {
	UNGOT(1), //未领取
	GOT(2), //已领取
	UNUSED(3), //未使用
	USED(4), //已使用
	RECEIVE_OVERDUE(5), //领取时间已过期
	USE_OVERDUE(6), //使用时间已过期
	EMPTY(7); //已抢光
	
	private int value;
	
	private CouponVOStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
