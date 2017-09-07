package com.socool.soft.bo.constant;

public enum ProdPriceMannerEnum {
	BY_QUANTITY(1), //按份数
	BY_WEIGHT(2); //按重量
	
	private int value;
	
	private ProdPriceMannerEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
