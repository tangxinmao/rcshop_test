package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcOrderOperation;

public interface RcOrderOperationMapper {

//	int deleteByPrimaryKey(long orderOperationId);

	int insert(RcOrderOperation record);

//	RcOrderOperation selectByPrimaryKey(long orderOperationId);

	int updateByPrimaryKey(RcOrderOperation record);

	List<RcOrderOperation> select(RcOrderOperation record);

	RcOrderOperation selectOne(RcOrderOperation record);

//	int delete(RcOrderOperation record);
}