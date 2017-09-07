package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcCity;

public interface RcCityMapper {

	int deleteByPrimaryKey(int cityId);

	int insert(RcCity record);

	RcCity selectByPrimaryKey(int cityId);

	int updateByPrimaryKey(RcCity record);

	List<RcCity> select(RcCity record);

	RcCity selectOne(RcCity record);

//	int delete(RcCity record);
}