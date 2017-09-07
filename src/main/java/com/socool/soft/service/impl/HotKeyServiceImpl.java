package com.socool.soft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcHotKey;
import com.socool.soft.dao.RcHotKeyMapper;
import com.socool.soft.service.IHotKeyService;

@Service
public class HotKeyServiceImpl implements IHotKeyService {
	@Autowired
	private RcHotKeyMapper hotKeyMapper;

//	@Override
//	public int addHotKey(RcHotKey hotKey) {
//		return hotKeyMapper.insert(hotKey);
//	}

//	@Override
//	public int modifyHotKey(RcHotKey hotKey) {
//		return hotKeyMapper.updateByPrimaryKey(hotKey);
//	}
//
//	@Override
//	public int removeHotKey(int hotKeyId) {
//		return hotKeyMapper.deleteByPrimaryKey(hotKeyId);
//	}
//
//	@Override
//	public List<RcHotKey> findHotKeys() {
//		return hotKeyMapper.select(null);
//	}

	@Override
	public List<RcHotKey> findHotKeysForAndroid(int limit) {
		RcHotKey param = new RcHotKey();
		param.setOrderBy("SEQ ASC");
		param.setLimit(limit);
		return hotKeyMapper.select(param);
	}
}
