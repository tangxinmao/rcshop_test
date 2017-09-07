package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcImageSpace;

public interface RcImageSpaceMapper {

	int deleteByPrimaryKey(long imageSpaceId);

	int insert(RcImageSpace record);

	RcImageSpace selectByPrimaryKey(long imageSpaceId);

	int updateByPrimaryKey(RcImageSpace record);

	List<RcImageSpace> select(RcImageSpace record);

	RcImageSpace selectOne(RcImageSpace record);

	int delete(RcImageSpace record);
}