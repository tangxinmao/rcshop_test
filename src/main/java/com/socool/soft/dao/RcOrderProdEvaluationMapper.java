package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcOrderProdEvaluation;

public interface RcOrderProdEvaluationMapper {

//	int deleteByPrimaryKey(long orderProdId);

	int insert(RcOrderProdEvaluation record);

	RcOrderProdEvaluation selectByPrimaryKey(long orderProdId);

	int updateByPrimaryKey(RcOrderProdEvaluation record);

	List<RcOrderProdEvaluation> select(RcOrderProdEvaluation record);

//	RcOrderProdEvaluation selectOne(RcOrderProdEvaluation record);

//	int delete(RcOrderProdEvaluation record);
}