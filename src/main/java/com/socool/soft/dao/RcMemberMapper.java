package com.socool.soft.dao;

import com.socool.soft.bo.RcMember;

public interface RcMemberMapper {

//	int deleteByPrimaryKey(int memberId);

	int insert(RcMember record);

	RcMember selectByPrimaryKey(int memberId);

	int updateByPrimaryKey(RcMember record);

//	List<RcMember> select(RcMember record);

	RcMember selectOne(RcMember record);

//	int delete(RcMember record);
}