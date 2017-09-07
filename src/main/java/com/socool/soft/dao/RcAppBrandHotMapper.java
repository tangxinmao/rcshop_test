package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcAppBrandHot;

public interface RcAppBrandHotMapper {

	int deleteByPrimaryKey(int brandHotId);

	int insert(RcAppBrandHot record);

	RcAppBrandHot selectByPrimaryKey(int brandHotId);

	int updateByPrimaryKey(RcAppBrandHot record);

	List<RcAppBrandHot> select(RcAppBrandHot record);

//	RcAppBrandHot selectOne(RcAppBrandHot record);

//	int delete(RcAppBrandHot record);
}