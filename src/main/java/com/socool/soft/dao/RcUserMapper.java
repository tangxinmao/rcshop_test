package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcUser;

public interface RcUserMapper {

//	int deleteByPrimaryKey(int memberId);

	int insert(RcUser record);

	RcUser selectByPrimaryKey(int memberId);

	int updateByPrimaryKey(RcUser record);

	List<RcUser> select(RcUser record);

	RcUser selectOne(RcUser record);

//	int delete(RcUser record);
}