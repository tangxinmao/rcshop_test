package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcAppBanner;

public interface RcAppBannerMapper {

	int deleteByPrimaryKey(int appBannerId);

	int insert(RcAppBanner record);

	RcAppBanner selectByPrimaryKey(int appBannerId);

	int updateByPrimaryKey(RcAppBanner record);

	List<RcAppBanner> select(RcAppBanner record);

//	RcAppBanner selectOne(RcAppBanner record);

//	int delete(RcAppBanner record);
}