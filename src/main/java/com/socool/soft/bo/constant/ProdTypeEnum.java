package com.socool.soft.bo.constant;

public enum ProdTypeEnum {
	ENTITY(1), //实物
	SERVICE(2), //服务
	VIRTUAL(3); //虚拟
	
	private int value;
	
	private ProdTypeEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
