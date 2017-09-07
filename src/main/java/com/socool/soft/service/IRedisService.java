package com.socool.soft.service;

import com.socool.soft.exception.SystemException;

public interface IRedisService {

//	int setObject(String key, Object value) throws SystemException;
	
	int set(String key, String value) throws SystemException;
	
	int hsetObject(String key, String field, Object value) throws SystemException;
	
	int setex(String key, int expireTime, String value) throws SystemException;
	
//	Object getObject(String key) throws SystemException;
	
	String get(String key) throws SystemException;
	
	Object hgetObject(String key, String field) throws SystemException;
	
//	long delObject(String key) throws SystemException;
	
	long del(String key) throws SystemException;
	
	long hdelObject(String key, String field) throws SystemException;
	
	long sadd(String key, String value) throws SystemException;
	
	long srem(String key, String value) throws SystemException;
	
	boolean sismember(String key, String value) throws SystemException;
}
