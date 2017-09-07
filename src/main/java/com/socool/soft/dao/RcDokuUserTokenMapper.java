package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcDokuUserToken;

public interface RcDokuUserTokenMapper {

	int deleteByPrimaryKey(int helpId);

	int insert(RcDokuUserToken record);

	RcDokuUserToken selectByPrimaryKey(int helpId);

	int updateByPrimaryKey(RcDokuUserToken record);

	List<RcDokuUserToken> select(RcDokuUserToken record);

//	RcHelp selectOne(RcHelp record);

	int delete(RcDokuUserToken record);
}