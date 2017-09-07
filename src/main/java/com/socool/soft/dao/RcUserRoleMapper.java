package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcUserRole;

public interface RcUserRoleMapper {

//	int deleteByPrimaryKey(int memberRoleId);

	int insert(RcUserRole record);

//	RcUserRole selectByPrimaryKey(int memberRoleId);

//	int updateByPrimaryKey(RcUserRole record);

	List<RcUserRole> select(RcUserRole record);

	RcUserRole selectOne(RcUserRole record);

	int delete(RcUserRole record);
}