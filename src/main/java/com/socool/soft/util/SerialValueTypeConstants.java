package com.socool.soft.util;
/**
 * 序列值归零类型常量
 * @author Wu Xingyong
 */
public interface SerialValueTypeConstants {
	
	/**
	 * 序列值不归零（默认）
	 */
	public static final int NO_ROLLBACKTOZERO= 0;
	
	/**
	 * 序列值按年归零
	 */
	public static final int ROLLBACKTOZERO_YEAR = 1;
	
	/**
	 * 序列值按月归零
	 */
	public static final int ROLLBACKTOZERO_MONTH = 2;
	
	/**
	 * 序列值按日归零
	 */
	public static final int ROLLBACKTOZERO_DAY = 3;
}
