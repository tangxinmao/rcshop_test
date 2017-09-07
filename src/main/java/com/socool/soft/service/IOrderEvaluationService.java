package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcOrderEvaluation;
import com.socool.soft.vo.Page;

public interface IOrderEvaluationService {
/**
 * 
 * @param rcOrderEvaluation
 */
	long addOrderEvaluation(RcOrderEvaluation orderEvaluation);
	
	long modifyOrderEvaluation(RcOrderEvaluation orderEvaluation);
	
	/**
	 * 查询店铺评价列表
	 * @param Param
	 * @return
	 */
	List<RcOrderEvaluation> findPagedOrderEvaluations(RcOrderEvaluation param, Page page);
	
	List<RcOrderEvaluation> findOrderEvaluations(RcOrderEvaluation param);
	
	/**
	 * 删除店铺评价
	 * @param orderId
	 * @return
	 */
	int removeOrderEvaluation(long orderId);
	
	RcOrderEvaluation findOrderEvaluationById(long orderId);
}
