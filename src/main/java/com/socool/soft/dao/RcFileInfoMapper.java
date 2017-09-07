package com.socool.soft.dao;

import com.socool.soft.bo.RcFileInfo;

public interface RcFileInfoMapper {
	
//	int deleteByPrimaryKey(String fileInfoId);

	int insert(RcFileInfo record);

	RcFileInfo selectByPrimaryKey(String fileInfoId);

//	int updateByPrimaryKey(RcFileInfo record);

//	List<RcFileInfo> select(RcFileInfo record);

//	RcFileInfo selectOne(RcFileInfo record);

//	int delete(RcFileInfo record);
}