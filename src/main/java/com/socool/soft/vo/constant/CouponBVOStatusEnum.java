package com.socool.soft.vo.constant;

public enum CouponBVOStatusEnum {
	UNPUBLISHED(1), //未发布
	PUBLISHED(2), //已发布
	UNDID(3), //已撤销
	RECEIVE_OVERDUE(4); //领取时间已过期
	
	private int value;
	
	private CouponBVOStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
