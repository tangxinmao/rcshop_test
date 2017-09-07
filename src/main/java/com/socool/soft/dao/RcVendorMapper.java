package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcVendor;

public interface RcVendorMapper {

//	int deleteByPrimaryKey(int vendorId);

	int insert(RcVendor record);

	RcVendor selectByPrimaryKey(int vendorId);

	int updateByPrimaryKey(RcVendor record);

	List<RcVendor> select(RcVendor record);

//	RcVendor selectOne(RcVendor record);

//	int delete(RcVendor record);
}