package com.socool.soft.bo.constant;

public enum RoleIdEnum {
	ADMINISTRATOR(1), 
	OPERATOR(2), 
	RECHARGE_OPERATOR(3),
	STORE_SELLER(4),
	PRINCIPAL_MANAGER(7);
	
	private int value;
	
	private RoleIdEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
