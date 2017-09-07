/*
 * Copyright (c) 2005, 四川启明星银海科技有限公司
 * All rights reserved.
 *　
 * 文 件 名: DateUtil.java
 * 版    本: V1.0  2006-12-11 10:23:10
 * 描    述: 日期常用方法
 * 备    注: 
 * 修改记录:
 * 修 改 人 - 修改日期 - 修改说明
 * 
 ****************************************************************************/
package com.socool.soft.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * 日期常用方法<br>
 * 提供对日期型数据的常用处理方法
 * 
 * @author yangyn
 */
public final class DateUtil {
	private static final Log log = LogFactory.getLog(DateUtil.class);

	// 日期格式化对象,日期型（yyyy-MM-dd）
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	// 日期格式化对象,日期型（yyyy-MM-dd）
	private static final SimpleDateFormat dateFormats = new SimpleDateFormat(
			"yyyyMMdd");

	// 日期时间格式化对象,日期时间型（yyyy-MM-dd HH:mm:ss）
	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 日期格式:yyyy-mm-dd<br>
	 * 例如:2005-11-02
	 */
	public final static String DATE_PATTERN_LINE = "yyyy-MM-dd";

	/**
	 * 日期格式:yyyy/mm/dd<br>
	 * 例如:2005/11/02
	 */
	public final static String DATE_PATTERN_BIAS = "yyyy/MM/dd";

	/**
	 * 日期时间格式(24小时制):yyyy-mm-dd HH:mm:ss<br>
	 * 例如:2005-11-02 23:01:01
	 */
	public final static String DATETIME24_PATTERN_LINE = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 日期格式:yyyyMMdd<br>
	 * 例如:2005/11/02
	 */
	public final static String DATE_PATTERN_BIASSS = "yyyyMMdd";

	/**
	 * 日期时间格式(12小时制):yyyy-mm-dd hh:mm:ss<br>
	 * 例如:2005-11-02 11:01:01
	 */
	public final static String DATETIME12_PATTERN_LINE = "yyyy-MM-dd hh:mm:ss";

	/**
	 * 日期时间格式(24小时制):yyyy/mm/dd HH:mm:ss<br>
	 * 例如:2005/11/02 23:01:01
	 */
	public final static String DATETIME24_PATTERN_BIAS = "yyyy/MM/dd HH:mm:ss";

	/**
	 * 日期时间格式(12小时制):yyyy/mm/dd hh:mm:ss<br>
	 * 例如:2005/11/02 11:01:01
	 */
	public final static String DATETIME12_PATTERN_BIAS = "yyyy/MM/dd hh:mm:ss";

	// 静态初始化时区
	static {
		// 中国时区
		TimeZone tzChina = TimeZone.getTimeZone("Asia/Chongqing");
		DateUtil.dateTimeFormat.setTimeZone(tzChina);
		DateUtil.dateFormat.setTimeZone(tzChina);
	}

	/**
	 * 根据指定的格式化模式,格式化日历数据<br>
	 * 默认使用yyyy-MM-dd HH:mm:ss
	 * 
	 * @param now
	 *            给定日期
	 * @return 被格式化后的字符串
	 */
	public static String formatDate(java.util.Calendar now) {
		return formatDate(now, DATETIME24_PATTERN_LINE);
	}

	/**
	 * 根据指定的格式化模式,格式化日历数据<br>
	 * 如果格式化模式为null或者为空,则默认使用yyyy-MM-dd HH:mm:ss
	 * 
	 * @param now
	 *            给定日期
	 * @param formatePattern
	 *            格式化模式
	 * @return 被格式化后的字符串<br>
	 */
	public static String formatDate(java.util.Calendar now,
			String formatePattern) {
		if (now == null) {
			return null;
		}
		if (formatePattern == null || formatePattern.trim().length() <= 0) {
			formatePattern = DATETIME24_PATTERN_LINE;
		}
		java.util.Date tempDate = now.getTime();
		SimpleDateFormat dateFormate = new SimpleDateFormat(formatePattern);
		return dateFormate.format(tempDate);
	}

	/**
	 * 将java.util.Date数据转换为指定格式的字符串<br>
	 * 如果格式化模式为null或者为空,则默认使用yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            java.util.Date类型数据
	 * @param formatePattern
	 *            指定的日期格式化模式
	 * @return 格式化后的日期的字符串形式<br>
	 * 
	 */
	public static String formatDate(java.util.Date date, String formatePattern) {
		if (formatePattern == null || formatePattern.trim().length() <= 0) {
			formatePattern = DATETIME24_PATTERN_LINE;
		}
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat dateFormate = new SimpleDateFormat(formatePattern);
			return dateFormate.format(date);
		}
	}

	/**
	 * 将java.sql.Timestamp数据转换为指定格式的字符串<br>
	 * 如果格式化模式为null或者为空,则默认使用yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            Timestamp数据
	 * @param formatePattern
	 *            日期格式化模式
	 * @return 格式化后的日期的字符串形式
	 */
	public static String formatDate(java.sql.Timestamp date,
			String formatePattern) {
		if (formatePattern == null || formatePattern.trim().length() <= 0) {
			formatePattern = DATETIME24_PATTERN_LINE;
		}
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat dateFormate = new SimpleDateFormat(formatePattern);
			return dateFormate.format(date);
		}
	}

	/**
	 * 将java.util.Date数据转换为指定格式的字符串<br>
	 * 如果格式化模式为null或者为空,则默认使用yyyy-MM-dd
	 * 
	 * @param date
	 *            java.util.Date类型数据
	 * @return 格式化后的日期的字符串形式<br>
	 */
	public static String formatDate(java.util.Date date) {
		return formatDate(date, DATE_PATTERN_LINE);
	}

	/**
	 * 将代表日期的长整形数值转换为yyyy-MM-dd HH:mm:ss格式的字符串<br>
	 * 
	 * @param datetime
	 *            需要转换的日期的长整形数值
	 * @return 格式化后的日期字符串
	 */
	public static String formatDate(long datetime) {
		return formatDate(datetime, DATETIME24_PATTERN_LINE);
	}

	/**
	 * 将代表日期的字符串转换yyyy-MM-dd HH:mm:ss格式的字符串
	 * 
	 * @param datetime
	 *            需要转换的日期
	 * @return 格式化后的日期字符串
	 */
	public static String formate(String datetime) {
		return formatDate(datetime, DATETIME24_PATTERN_LINE);
	}

	/**
	 * 将代表日期的字符串转换未指定格式的字符串<br>
	 * 如果格式化模式为null或者为空,则默认使用yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datetime
	 *            需要转换的日期的字符串
	 * @param formatePattern
	 *            指定的日期格式
	 * @return 格式化后的日期字符串
	 */
	public static String formatDate(String datetime, String formatePattern) {
		if (datetime == null || datetime.trim().length() <= 0) {
			return "";
		}
		try {
			Date date = null;
			if (formatePattern != null
					&& (formatePattern.equals(DATE_PATTERN_BIAS) || formatePattern
							.equals(DATE_PATTERN_LINE))) {
				date = parseDate(datetime);
			} else {
				date = parseDateTime(datetime);
			}

			return formatDate(date, formatePattern);
		} catch (Exception ex) {
			log.error("日期转换失败:", ex);
			return null;
		}
	}

	/**
	 * 将代表日期的长整形数值转换为y指定格式的字符串<br>
	 * 如果格式化模式为null或者为空,则默认使用yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datetime
	 *            需要转换的日期的长整形数值
	 * @param formatePattern
	 *            指定的日期格式
	 * @return 格式化后的日期字符串
	 */
	public static String formatDate(long datetime, String formatePattern) {
		if (datetime <= 0) {
			return "";
		}
		try {
			Date date = new Date(datetime);
			return formatDate(date, formatePattern);
		} catch (Exception ex) {
			log.error("日期转换失败:", ex);
			return null;
		}
	}

	/**
	 * 将java.sql.Date数据转换为指定格式的字符串<br>
	 * 默认使用yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            java.sql.Date类型数据
	 * @return 格式化后的日期的字符串形式<br>
	 */
	public static String formatDate(java.sql.Date date) {
		return formatDate(toUtilDate(date));
	}

	/**
	 * 将java.sql.Date转换为java.util.Date数据类型
	 * 
	 * @param date
	 *            需要转换的java.sql.Date数据
	 * @return 转换后的java.util.Date数据
	 */
	public static java.util.Date toUtilDate(java.sql.Date date) {
		if (date == null) {
			return null;
		} else {
			return new java.util.Date(date.getTime());
		}
	}

	/**
	 * 得到当前系统日期
	 * 
	 * @return 得到系统当前日期
	 */
	public static java.util.Date getCurrentDate() {
		return new java.util.Date(System.currentTimeMillis());
	}

	/**
	 * 得到当前系统日期
	 * 
	 * @return 得到系统当前日期
	 */
	public static java.sql.Timestamp getCurrentDateTime() {
		return new java.sql.Timestamp(new java.util.Date().getTime());
	}

	/**
	 * 将字符串转化为日期型数据<br>
	 * 字符串必须是yyyy-MM-dd格式
	 * 
	 * @param src
	 *            日期数据字符串
	 * @return java.util.Date型日期类型数据
	 */
	public static java.util.Date parseDate(String src) {
		if (src == null || src.trim().length() <= 0) {
			return null;
		}
		try {
			return DateUtil.dateFormat.parse(src);
		} catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}

	/**
	 * 根据日期、小时、分钟、秒组合成日期时间
	 * 
	 * @param date
	 *            日期
	 * @param hour
	 *            小时
	 * @param minute
	 *            分钟
	 * @param second
	 *            秒
	 * @return 组合后的日期时间
	 * @throws PjBaseException
	 */
	public static java.util.Date parseDate(String date, int hour, int minute,
			int second) {
		Calendar cal = Calendar.getInstance();

		java.util.Date dateObj = parseDate(date);
		cal.set(getYear(dateObj), getMonth(dateObj), getDay(dateObj), hour,
				minute, second);
		return cal.getTime();
	}

	/**
	 * 将字符串转化为日期型数据<br>
	 * 字符串必须是yyyy-MM-dd HH:mm:ss格式
	 * 
	 * @param src
	 *            日期数据字符串
	 * @return java.util.Date型日期时间型数据
	 */
	public static java.util.Date parseDateTime(String src) {
		if (StringUtils.isBlank(src)) {
			return null;
		}

		try {
			return DateUtil.dateTimeFormat.parse(src);
		} catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}

	/**
	 * 解析Date，字符串必须是yyyy-MM-dd HH:mm:ss格式
	 * 
	 * @param src
	 *            日期数据字符串
	 * @return
	 */
	public static Date parseAllDate(String src) {
		try {
			if (src == null || src.equals(""))
				return null;
			if (src.length() == 10) {
				return dateFormat.parse(src);
			} else if (src.length() == 19) {

				return dateTimeFormat.parse(src);
			} else if (src.length() > 19) {
				src = src.substring(0, 19);
				return dateTimeFormat.parse(src);
			} else {
//				throw new BusinessRunTimeException(
//						"长度不符。日期格式为:yyyy-mm-dd:,时间格式为:yyyy-mm-dd hh:mi:ss");
				return null;
			}

		} catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}

	/**
	 * 将java.util.Date转换为java.sql.Date数据类型
	 * 
	 * @param date
	 *            需要转换的java.util.Date数据
	 * @return 转换后的java.sql.Date数据
	 */
	public static java.sql.Date toSqlDate(java.util.Date date) {
		if (date == null) {
			return null;
		} else {
			return new java.sql.Date(date.getTime());
		}
	}

	/**
	 * 将java.util.Date转换为java.sql.Timestamp
	 * 
	 * @param date
	 *            需要转换的java.util.Date数据
	 * @return 转换后的java.sql.Timestamp
	 */
	public static java.sql.Timestamp toTimestamp(java.util.Date date) {
		if (date == null) {
			return null;
		} else {
			return new java.sql.Timestamp(date.getTime());
		}
	}

	/**
	 * 得到指定年月的最后一天
	 * 
	 * @param year
	 *            指定年
	 * @param month
	 *            指定月
	 * @return 指定年月的最后一天
	 */
	public static java.util.Date getMonthLastDay(int year, int month) {
		if (month >= 1 && month <= 12) {
			Calendar lCal = Calendar.getInstance();
			lCal.set(year, month, 1);
			lCal.add(Calendar.DATE, -1);
			return lCal.getTime();
		} else {
			throw new RuntimeException("月份传入错误必须介于1和12之间");
		}
	}

	/**
	 * 得到指定年月的第一天
	 * 
	 * @param year
	 *            指定年
	 * @param month
	 *            指定月
	 * @return 指定年月的第一天
	 */
	public static java.util.Date getMonthFirstDay(int year, int month) {
		if (month >= 1 && month <= 12) {
			Calendar cal = Calendar.getInstance();
			cal.set(year, month - 1, 1, 0, 0, 0);
			return cal.getTime();
		} else {
//			throw new BusinessRunTimeException("月份传入错误必须介于1和12之间");
			return null;
		}
	}

	/**
	 * 得到指定年月的最后一天的最后小时分秒
	 * 
	 * @param year
	 *            指定年
	 * @param month
	 *            指定月
	 * @return 年月的最后一天的最后小时分秒
	 */
	public static java.util.Date getMonthLastDatetime(int year, int month) {
		if (month >= 1 && month <= 12) {
			Calendar lCal = Calendar.getInstance();
			lCal.set(year, month, 1, 23, 59, 59);
			lCal.add(Calendar.DATE, -1);
			return lCal.getTime();
		} else {
			throw new RuntimeException("月份传入错误必须介于1和12之间");
		}
	}

	/**
	 * 得到指定年月的第一天的开始小时分秒
	 * 
	 * @param year
	 *            指定年
	 * @param month
	 *            指定月
	 * @return 年月的第一天的开始小时分秒
	 */
	public static java.util.Date getMonthFirstDatetime(int year, int month) {
		if (month >= 1 && month <= 12) {
			Calendar cal = Calendar.getInstance();
			cal.set(year, month - 1, 1, 0, 0, 0);
			return cal.getTime();
		} else {
			throw new RuntimeException("月份传入错误必须介于1和12之间");
		}
	}

	/**
	 * 得到指定日期所在周的指定星期几的日期
	 * 
	 * @param date
	 *            指定日期
	 * @param dayOfWeek
	 *            指定星期几
	 * @return 指定星期几的日期
	 */
	public static Date getDateOfWeek(Date date, int dayOfWeek) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		return c.getTime();
	}

	/**
	 * 得到指定日期为当前年的第几周
	 * 
	 * @param date
	 *            指定日期
	 * @return 当前年的第几周
	 */
	public static int getWeekOfYear2(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置周一为一周的第一天
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 设置周一为第一天
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 得到指定日期为当前年的第几周
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.SUNDAY);
		c.setMinimalDaysInFirstWeek(7);
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 得到指定年的第几周的第一天日期
	 * 
	 * @param year
	 *            指定年
	 * @param nWeek
	 *            第几周
	 * @return 第一天日期
	 */
	public static Date getWeekOfFirstDate(int year, int nWeek) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.WEEK_OF_YEAR, nWeek - 1);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 1);
		return c.getTime();
	}

	/**
	 * 周一为一周的第一天
	 * 
	 * @param year
	 * @param nWeek
	 * @return
	 */
	public static Date getWeekOfFirstDate2(int year, int nWeek) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.WEEK_OF_YEAR, nWeek);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return c.getTime();
	}

	/**
	 * 得到指定年的第几周的最后一天日期
	 * 
	 * @param year
	 *            指定年
	 * @param nWeek
	 *            第几周
	 * @return 最后一天日期
	 */
	public static Date getWeekOfLastDate(int year, int nWeek) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.WEEK_OF_YEAR, nWeek);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		return c.getTime();
	}

	/**
	 * 星期天为一周的最后一天
	 * 
	 * @param year
	 * @param nWeek
	 * @return
	 */
	public static Date getWeekOfLastDate2(int year, int nWeek) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.WEEK_OF_YEAR, nWeek);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return c.getTime();
	}

	/**
	 * 得到当前年
	 * 
	 * @return 当前年
	 */
	public static int getCurrentYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}
	
	public static String getFirstDayOfMonth(int year, int month, String PATTERN_BIASSS) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		return formatDate(c.getTime(), PATTERN_BIASSS);
	}
	
	public static String getFirstDayOfMonth() {
		return getFirstDayOfMonth(DATE_PATTERN_BIASSS);
	}
	
	public static String getFirstDayOfMonth(String PATTERN_BIASSS) {
		Calendar c = Calendar.getInstance();
		return getFirstDayOfMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH), PATTERN_BIASSS);
	}
	
	public static String getLastDayOfMonth(int year, int month, String PATTERN_BIASSS) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return formatDate(c.getTime(), PATTERN_BIASSS);
	}
	
	public static String getLastDayOfMonth() {
		return getLastDayOfMonth(DATE_PATTERN_BIASSS);
	}
	
	public static String getLastDayOfMonth(String PATTERN_BIASSS) {
		Calendar c = Calendar.getInstance();
		return getLastDayOfMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH), PATTERN_BIASSS);
	}

	/**
	 * 得到日期中的年份
	 * 
	 * @param date
	 *            日期
	 * @return 年份
	 */
	public static int getYear(java.util.Date date) {
		if (date == null) {
			return 0;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	/**
	 * 得到日期中的月份
	 * 
	 * @param date
	 *            日期
	 * @return 月份
	 */
	public static int getMonth(java.util.Date date) {
		if (date == null) {
			return 0;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH);
	}

	/**
	 * 得到日期中的天
	 * 
	 * @param date
	 *            日期
	 * @return 天
	 */
	public static int getDay(java.util.Date date) {
		if (date == null) {
			return 0;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 得到日期中的小时
	 * 
	 * @param date
	 *            日期
	 * @return 小时
	 */
	public static int getHour(java.util.Date date) {
		if (date == null) {
			return 0;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 得到当前月<br>
	 * 0:一月;1:二月;....;11:十二月
	 * 
	 * @return 当前月
	 */
	public static int getCurrentMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH);
	}

	/**
	 * 根据日期判断是星期几1:表示星期一
	 * 
	 * @param pTime
	 * @return
	 * @throws Throwable
	 */
	@SuppressWarnings("deprecation")
	public static int dayForWeek(String pTime) throws Throwable {

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Date tmpDate = format.parse(pTime);

			Calendar cal = new GregorianCalendar();

			cal.set(tmpDate.getYear(), tmpDate.getMonth(), tmpDate.getDay());

			return cal.get(Calendar.DAY_OF_WEEK);
		} catch (Exception e) {
			throw new Throwable();
		}
	}

	/**
	 * 取某个时间点后几个月的某个时间点
	 * 
	 * @param d
	 *            原日期
	 * @param count
	 *            几个月后
	 * @return 目标日期
	 */
	public static Date afterMonths(Date d, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.MONTH, c.get(Calendar.MONTH) + count);
		return c.getTime();
	}

	/**
	 * 根据传入日期得到，得到年份
	 * 
	 * @param src
	 *            字符串必须是yyyy-MM-dd格式
	 * @return 返回传入日期所在周的年份
	 */
	public static Integer getYearByTime(String src) {
		int retYear = DateUtil.getCurrentYear();
		int month = DateUtil.getCurrentMonth();
		if(src == null || src.trim().length()<=0){
//			throw new BusinessRunTimeException("日期格式不正确！");
			return null;
		}
		try {
			month = NumberUtil.parseInt(src.split("-")[1].trim(), 0);
			Date logTimeDate = DateUtil.parseDate(TextUtil.nvl(src));
			int weeklyNum = DateUtil.getWeekOfYear2(logTimeDate);
			int nowYear = NumberUtil
					.parseInt(src.split("-")[0].trim(), retYear);
			if (weeklyNum == 1) {
				if (month > 1) {
					return nowYear + 1;
				} else {
					return nowYear ;
				}
			} else {
				return nowYear;
			}
		} catch (Exception e) {
//			throw new BusinessRunTimeException("获取年份出错！",e);
			return null;
		}
	}
	/**
	 * 根据传入日期得到，得到周数
	 * 
	 * @param src
	 *            字符串必须是yyyy-MM-dd格式
	 * @return 返回传入日期所在周
	 */
	public static Integer getWeeklyNumByTime(String src) {
		int month = DateUtil.getCurrentMonth();
		if(src == null || src.trim().length()<=0){
//			throw new BusinessRunTimeException("日期格式不正确！");
			return null;
		}
		try {
			month = NumberUtil.parseInt(src.split("-")[1].trim(), 0);
			Date logTimeDate = DateUtil.parseDate(TextUtil.nvl(src));
			int weeklyNum = DateUtil.getWeekOfYear2(logTimeDate);
			if (weeklyNum == 1) {
				if (month > 1) {
					return 1;
				} else {
					return weeklyNum ;
				}
			} else {
				return weeklyNum;
			}
		} catch (Exception e) {
//			throw new BusinessRunTimeException("获取周数出错！",e);
			return null;
		}
	}


		/**
	 * 得到指定年的第一天
	 * 
	 * @param year
	 *            指定年
	 * @return 指定年的第一天
	 */
	public static java.util.Date getYearFirstDay(int year) {
			Calendar cal = Calendar.getInstance();
			cal.set(year, 0, 1, 0, 0, 0);
			return cal.getTime();
	}



	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 *字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}
	
	public static String turnDate(String showDate, String format, int interDay) {
	    
	    // 日期加指定天数
	    Calendar cal = Calendar.getInstance();
	    
	    Date tempDate_001 = DateUtil.parseDates(showDate); 
	    if (null == tempDate_001) {
	    	return null;
	    }
	    cal.setTime(tempDate_001);
	    
	    cal.add(Calendar.DAY_OF_MONTH, interDay);
	    
	    String next = DateUtil.formatDates(cal.getTime(), format);
	    
	    return next;
	    
	}

	/**
	 * 日期增加天数
	 * @param date
	 * @param interDay
	 * @return
	 */
	public static Date addDate(Date date, int interDay) {

		// 日期加指定天数
		Calendar cal = Calendar.getInstance();

		if (null == date) {
			return null;
		}
		cal.setTime(date);

		cal.add(Calendar.DAY_OF_MONTH, interDay);

		return cal.getTime();

	}
	
	
	/**
	 * 将java.util.Date数据转换为指定格式的字符串<br>
	 * 如果格式化模式为null或者为空,则默认使用yyyyMMdd HH:mm:ss
	 * 
	 * @param date
	 *            java.util.Date类型数据
	 * @param formatePattern
	 *            指定的日期格式化模式
	 * @return 格式化后的日期的字符串形式<br>
	 * 
	 */
	public static String formatDates(java.util.Date date, String formatePattern) {
		if (formatePattern == null || formatePattern.trim().length() <= 0) {
			formatePattern = DATE_PATTERN_BIASSS;
		}
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat dateFormate = new SimpleDateFormat(formatePattern);
			return dateFormate.format(date);
		}
	}

	
	/**
	 * 将字符串转化为日期型数据<br>
	 * 字符串必须是yyyyMMdd格式
	 * 
	 * @param src
	 *            日期数据字符串
	 * @return java.util.Date型日期类型数据
	 */
	public static java.util.Date parseDates(String src) {
		if (src == null || src.trim().length() <= 0) {
			return null;
		}
		try {
			return DateUtil.dateFormats.parse(src);
		} catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}

    /**
     * 获取今天0点时间戳
     * @return
     */
    public static long getTodayBeginTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND,0);
        return c.getTimeInMillis();
    }

    /**
     * 获取昨天0点时间戳
     * @return
     */
    public static long getYesterdayBeginTime() {
        return getTodayBeginTime()-(1000*60*60*24);
    }

    public static void main(String[] args) {
        try {
            //System.out.println(getWeeklyNumByTime(""));
//            System.out.println(parseDate("2013-03-29").getTime());
//            System.out.println(parseDate("2013-04-02").getTime());
//            System.out.println(getFirstDayOfMonth(2013, 8, "yyyy-MM-dd"));
            // System.out.println(formatDate(getMonthFirstDay(2009, 6)));
            // System.out.println(formatDate(getMonthFirstDatetime(2009, 6)));
            //	System.out.println(dayForWeek("2011-12-25"));
            // System.out.println("-第一天--"+DateUtil.formatDate(DateUtil.getWeekOfFirstDate2(DateUtil.getCurrentYear(),
            // DateUtil.getWeekOfYear2(DateUtil.getCurrentDate()))));
            // System.out.println("--最后一天--"+DateUtil.formatDate(DateUtil.getWeekOfLastDate2(DateUtil.getCurrentYear(),
            // DateUtil.getWeekOfYear2(DateUtil.getCurrentDate()))));
            // System.out.println(toSqlDate(parseDateTime("2009-05-10 15:00:01")));

            System.out.println(getTodayBeginTime());
            System.out.println(getYesterdayBeginTime());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}