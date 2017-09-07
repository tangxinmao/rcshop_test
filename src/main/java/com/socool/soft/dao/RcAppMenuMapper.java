package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcAppMenu;

public interface RcAppMenuMapper {

	int deleteByPrimaryKey(int appMenuId);

	int insert(RcAppMenu record);

	RcAppMenu selectByPrimaryKey(int appMenuId);

	int updateByPrimaryKey(RcAppMenu record);

	List<RcAppMenu> select(RcAppMenu record);

//	RcAppMenu selectOne(RcAppMenu record);

//	int delete(RcAppMenu record);
}