package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcMenu;

public interface RcMenuMapper {

	int deleteByPrimaryKey(int menuId);

	int insert(RcMenu record);

	RcMenu selectByPrimaryKey(int menuId);

	int updateByPrimaryKey(RcMenu record);

	List<RcMenu> select(RcMenu record);

	RcMenu selectOne(RcMenu record);

	int delete(RcMenu record);
}