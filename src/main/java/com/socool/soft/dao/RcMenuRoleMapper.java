package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcMenuRole;

public interface RcMenuRoleMapper {

//	int deleteByPrimaryKey(int menuRoleId);

	int insert(RcMenuRole record);

//	RcMenuRole selectByPrimaryKey(int menuRoleId);

//	int updateByPrimaryKey(RcMenuRole record);

	List<RcMenuRole> select(RcMenuRole record);

//	RcMenuRole selectOne(RcMenuRole record);

	int delete(RcMenuRole record);
}