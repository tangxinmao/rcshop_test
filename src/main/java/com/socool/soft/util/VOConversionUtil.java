package com.socool.soft.util;

import java.lang.reflect.Field;

import org.apache.commons.lang.ArrayUtils;

public class VOConversionUtil {

	private VOConversionUtil() {}
	
	public static void Entity2VO(Object obj, String[] reserves, String[] ignores) {
		Field[] fields = obj.getClass().getDeclaredFields();
		boolean isReserve = !ArrayUtils.isEmpty(reserves);
		boolean isIgnore = !isReserve & !ArrayUtils.isEmpty(ignores);
		if(!isReserve && !isIgnore) {
			return;
		}
		
		for(Field field : fields) {
			String fieldName = field.getName();
			if(isReserve) {
				boolean inReserve = false;
				for(String reserve : reserves) {
					if(fieldName.equals(reserve)) {
						inReserve = true;
						break;
					}
				}
				if(!inReserve) {
					field.setAccessible(true);
					try {
						field.set(obj, null);
					} catch (IllegalArgumentException e) {
					} catch (IllegalAccessException e) {
					}
				}
			} else {
				boolean inIgnore = false;
				for(String ignore : ignores) {
					if(fieldName.equals(ignore)) {
						inIgnore = true;
						break;
					}
				}
				if(inIgnore) {
					field.setAccessible(true);
					try {
						field.set(obj, null);
					} catch (IllegalArgumentException e) {
					} catch (IllegalAccessException e) {
					}
				}
			}
		}
	}
}
