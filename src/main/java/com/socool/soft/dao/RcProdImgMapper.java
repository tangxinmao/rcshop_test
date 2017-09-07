package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcProdImg;

public interface RcProdImgMapper {

//	int deleteByPrimaryKey(int prodImgRelId);

	int insert(RcProdImg record);

//	RcProdImg selectByPrimaryKey(int prodImgRelId);

//	int updateByPrimaryKey(RcProdImg record);

	List<RcProdImg> select(RcProdImg record);

	RcProdImg selectOne(RcProdImg record);

	int delete(RcProdImg record);
}