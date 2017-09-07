package com.socool.soft.vo.constant;

public enum ReportVOTypeEnum {
	BY_WEEK(1), //按周
	BY_MONTH(2), //按月
	BY_YEAR(3); //按年
	
	private int value;
	
	private ReportVOTypeEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
