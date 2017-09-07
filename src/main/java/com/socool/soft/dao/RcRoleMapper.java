package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcRole;

public interface RcRoleMapper {

	int deleteByPrimaryKey(int roleId);

	int insert(RcRole record);

	RcRole selectByPrimaryKey(int roleId);

	int updateByPrimaryKey(RcRole record);

	List<RcRole> select(RcRole record);

//	RcRole selectOne(RcRole record);

//	int delete(RcRole record);
}