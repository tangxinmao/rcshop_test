package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcOrderEvaluation;

public interface RcOrderEvaluationMapper {

//	int deleteByPrimaryKey(long orderId);

	int insert(RcOrderEvaluation record);

	RcOrderEvaluation selectByPrimaryKey(long orderId);

	int updateByPrimaryKey(RcOrderEvaluation record);

	List<RcOrderEvaluation> select(RcOrderEvaluation record);

//	RcOrderEvaluation selectOne(RcOrderEvaluation record);

//	int delete(RcOrderEvaluation record);
}