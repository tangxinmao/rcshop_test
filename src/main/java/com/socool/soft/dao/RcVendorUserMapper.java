package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcVendorUser;

public interface RcVendorUserMapper {

//	int deleteByPrimaryKey(int memberId);

	int insert(RcVendorUser record);

	RcVendorUser selectByPrimaryKey(int memberId);

	int updateByPrimaryKey(RcVendorUser record);

	List<RcVendorUser> select(RcVendorUser record);

	RcVendorUser selectOne(RcVendorUser record);

//	int delete(RcVendorUser record);
}