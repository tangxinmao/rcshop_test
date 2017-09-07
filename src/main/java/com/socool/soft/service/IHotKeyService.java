package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcHotKey;

public interface IHotKeyService {

//	int addHotKey(RcHotKey hotKey);
	
//	int modifyHotKey(RcHotKey hotKey);
//	
//	int removeHotKey(int hotKeyId);
//	
//	List<RcHotKey> findHotKeys();
	
	List<RcHotKey> findHotKeysForAndroid(int limit);
}
