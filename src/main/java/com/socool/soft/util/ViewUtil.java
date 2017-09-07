package com.socool.soft.util;

import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ViewUtil {
	
	public static final String DATETIME_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
	public static final String MONTH_FORMAT_STRING = "yyyy-MM";
	public static final String DATE_FORMAT_STRING = "yyyy-MM-dd";
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATETIME_FORMAT_STRING);
	
	
	public static class LengthLimitation{
		private String paramName;
		private int    limitedLength;
		
		public LengthLimitation(String paramName, int limitedLength) {
			super();
			this.paramName = paramName;
			this.limitedLength = limitedLength;
		}
	}
	
	public static List<Map<String, Object>> restrictStringLength(List<Map<String, Object>> list, LengthLimitation[] limits){
		
		Map<String, LengthLimitation> map = new HashMap<String, LengthLimitation>();
		for (LengthLimitation s : limits) {
			map.put(s.paramName, s);
		}
		
		for (Map<String, Object> obj : list) {
			for (String key : obj.keySet()) {
				if(map.containsKey(key)){
					Object valueFromList = obj.get(key);
					LengthLimitation limit = map.get(key);
					if(valueFromList instanceof String && ((String) valueFromList).length()> limit.limitedLength){
						String stringVal = (String)valueFromList;
						obj.put(key, stringVal.substring(0, limit.limitedLength)+"...");
					}
				}
			}
		}
		return list;
	}
	
	public static List<Map<String, Object>> formateDate(List<Map<String, Object>> list){
		
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				Object obj = map.get(key);
				if(obj instanceof Date){
					Date dateObj = (Date)obj;
					String dateString = DATE_FORMATTER.format(dateObj);
					map.put(key, dateString);
				}
			}
		}
		return list;
	}
	
	/**
	 * 用于传值
	 * @param model	
	 * @param keys	传往前台时候的取值字符串
	 * @param values	传往前台的值
	 */
	public static void setData(Model model,String[] keys,Object[] values) {
		if (keys != null && values != null && keys.length == values.length) {
			for (int i = 0, len = keys.length; i < len; i++) {
				model.addAttribute(keys[i], values[i]);
			}
		}
	}
	
	/**
	 * 格式化时间
	 * @param date
	 * @return
	 */
	public static String dateFormat(Date date) {
		if(date==null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat(DATETIME_FORMAT_STRING);
		return df.format(date) + " " + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
	}
	public static String datetimeFormat(Date date) {
		if(date==null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_STRING);
		return df.format(date) + " " + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
	}
	

	/**
	 * 格式化时间
	 * @param date
	 * @return
	 */
	public static String calMonth(Date date) {
		if(date==null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat(MONTH_FORMAT_STRING);
		return df.format(date);
	}
	
	/**
	 * 格式化货币
	 * @param number
	 * @return
	 */
	public static String numFormat(Object number) {
		String number2 = String.valueOf(number);
		Double num = Double.valueOf(number2);
		NumberFormat nft = NumberFormat.getCurrencyInstance(Locale.CHINA);
		return nft.format(num);
	}
	/**
	 * 字符串转时间
	 * @param str 
	 * @return
	 */
	public static Date StrToDate(String str) {
	   	  if(str==null || "".equals(str)){
	   		  return null;
	   	  }
	  	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  	   Date date = null;
	  	   try {
	  	    date = format.parse(str);
	  	   } catch (ParseException e) {
	  	    e.printStackTrace();
	  	   }
	  	   return date;
	  	}
	
	public static String moneyStringFormat(int money){
		DecimalFormat df = new DecimalFormat("#,###"); 
		String m = df.format(money);
		return m;
	}
	
	public static String moneyStringFormat(Float money){
		DecimalFormat df = new DecimalFormat("#,###"); 
		String m = df.format(money);
		return m;
	}
	public static String moneyStringFormat(BigDecimal money){
		DecimalFormat df = new DecimalFormat("#,###"); 
		String m = df.format(money);
		return m;
	}
	public static String moneyStringFormat2(BigDecimal money){
		if(money==null)
			return null;
		String moneyStr = money.toString();
		if(moneyStr.indexOf(".") > 0) {
			moneyStr = moneyStr.split("\\.")[0];
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < moneyStr.length(); i++) {
			sb.insert(0, moneyStr.charAt(moneyStr.length() - 1 - i));
			if(i % 3 == 2 && i < moneyStr.length() - 1) {
				sb.insert(0, ".");
			}
		}
		return sb.toString();
	}
	
	public static String moneyStringFormat(String money){
		DecimalFormat df = new DecimalFormat("#,###"); 
		Float mm = Float.parseFloat(money);
		String m = df.format(mm);
		return m;
	}
	
	public static String bytesToK(Long size){
		double filesize = ((double)size / 1024);
		return String.valueOf(Math.round(filesize * 100) / 100.0);
	}
}
