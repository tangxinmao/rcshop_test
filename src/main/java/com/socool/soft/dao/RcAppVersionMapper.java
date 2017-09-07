package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcAppVersion;

public interface RcAppVersionMapper {

//	int deleteByPrimaryKey(int versionId);

	int insert(RcAppVersion record);

	RcAppVersion selectByPrimaryKey(int versionId);

	int updateByPrimaryKey(RcAppVersion record);

	List<RcAppVersion> select(RcAppVersion record);

	RcAppVersion selectOne(RcAppVersion record);

//	int delete(RcAppVersion record);
}