package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcHotKey;

public interface RcHotKeyMapper {

	int deleteByPrimaryKey(int hotKeyId);

	int insert(RcHotKey record);

	RcHotKey selectByPrimaryKey(int hotKeyId);

	int updateByPrimaryKey(RcHotKey record);

	List<RcHotKey> select(RcHotKey record);

//	RcHotKey selectOne(RcHotKey record);

//	int delete(RcHotKey record);
}