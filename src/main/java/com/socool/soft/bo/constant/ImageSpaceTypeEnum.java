package com.socool.soft.bo.constant;

public enum ImageSpaceTypeEnum {
	FOLDER(1), //文件夹
	IMAGE(2); //图片
	
	private int value;
	
	private ImageSpaceTypeEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
