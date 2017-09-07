package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcOrderProdEvaluation;

public interface IOrderProdEvaluationService {
	/**
	 * 
	 * @param evaluations
	 */
//	void addEvaluations(List<RcOrderProdEvaluation> evaluations);

	long addOrderProdEvaluation(RcOrderProdEvaluation orderProdEvaluation);

	/**
	 * 通过wendorId，merchantId等条件查询
	 * 包含排序
	 * @param rcEvaluationVo
	 * @return
	 */
	List<RcOrderProdEvaluation> findOrderProdEvaluations(RcOrderProdEvaluation param);
	
//	List<RcOrderProdEvaluation> findPagedEvaluations(RcOrderProdEvaluation evaluation, Page page);

	List<RcOrderProdEvaluation> findOrderProdEvaluationsByProdId(int prodId);

//	RcOrderProdEvaluation findLatestEvaluationByProdId(int prodId);

//	List<RcOrderProdEvaluation> findEvaluationsByMemberId(int memberId);

	/**
	 * 修改评论
	 * 
	 * @param rcEvaluation
	 * @return
	 */
	long modifyOrderProdEvaluation(RcOrderProdEvaluation orderProdEvaluation);

	int replyOrderProdEvaluation(long orderProdId, String reply);

	RcOrderProdEvaluation findOrderProdEvaluationById(long orderProdId);

	/**
	 * 根据merchanid和vendorId 查询评论列表
	 * @param merchantId
	 * @param vendorId
	 * @return
	 */
//	List<RcOrderProdEvaluation> findEvaluations(Integer merchantId, Integer vendorId, Long orderId, 
//			Integer prodId, Date startCreateTime, Date endCreateTime, Integer score);
	/**
	 * 
	 * @param evaluationId
	 * @return
	 */

	int removeOrderProdEvaluation(long orderProdId);

//	void updateMerchantScore();
//
//	void updateMerchantProductScore();
//
//	void updateProductScore();

}
