package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcHelp;

public interface RcHelpMapper {

	int deleteByPrimaryKey(int helpId);

	int insert(RcHelp record);

	RcHelp selectByPrimaryKey(int helpId);

	int updateByPrimaryKey(RcHelp record);

	List<RcHelp> select(RcHelp record);

//	RcHelp selectOne(RcHelp record);

	int delete(RcHelp record);
}