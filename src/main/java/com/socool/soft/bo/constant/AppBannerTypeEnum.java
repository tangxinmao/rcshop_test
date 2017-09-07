package com.socool.soft.bo.constant;

public enum AppBannerTypeEnum {
	PRODUCT_DETAIL(1); //产品详情

	private int value;

	private AppBannerTypeEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
