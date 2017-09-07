package com.socool.soft.task;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcOrderEvaluation;
import com.socool.soft.bo.RcOrderProdEvaluation;
import com.socool.soft.bo.RcProd;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IOrderEvaluationService;
import com.socool.soft.service.IOrderProdEvaluationService;
import com.socool.soft.service.IProdService;

@EnableScheduling
@Component
public class ScoreTask {
	/**
	 * 卡券自动过期
	 * 
	 * @param a
	 */
	@Autowired
	private IOrderProdEvaluationService orderProdEvaluationService;
	@Autowired
	private IOrderEvaluationService orderEvaluationService;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private IProdService prodService;
    //每天两点更新当前时间之前一天内有商家订单评论的商家的分
    //每天两点更新当前时间之前一天内有商家订单评论的商家的商品分
    //每天两点更新当前时间之前一天内发生变化的商品的分
	@Scheduled(cron = "0 0 2 * * *")
	public void score() {
		Map<Integer, int[][]> merchantScores = new HashMap<Integer, int[][]>();
		Map<Integer, int[]> productScores = new HashMap<Integer, int[]>();
		RcOrderEvaluation orderEvaluationParam = new RcOrderEvaluation();
		orderEvaluationParam.setDelFlag(YesOrNoEnum.NO.getValue());
		List<RcOrderEvaluation> orderEvaluations = orderEvaluationService.findOrderEvaluations(orderEvaluationParam);
		for(RcOrderEvaluation orderEvaluation : orderEvaluations) {
			if(!merchantScores.containsKey(orderEvaluation.getMerchantId())) {
				merchantScores.put(orderEvaluation.getMerchantId(), new int[][] {new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}});
			}
			int[][] merchatScore = merchantScores.get(orderEvaluation.getMerchantId());
			merchatScore[0][0] += orderEvaluation.getDeliveryScore();
			merchatScore[0][1]++;
			merchatScore[1][0] += orderEvaluation.getServiceScore();
			merchatScore[1][1]++;
		}
		RcOrderProdEvaluation evaluationParam = new RcOrderProdEvaluation();
		evaluationParam.setDelFlag(YesOrNoEnum.NO.getValue());
		List<RcOrderProdEvaluation> evaluations = orderProdEvaluationService.findOrderProdEvaluations(evaluationParam);
		for(RcOrderProdEvaluation evaluation : evaluations) {
			if(!merchantScores.containsKey(evaluation.getMerchantId())) {
				merchantScores.put(evaluation.getMerchantId(), new int[][] {new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}});
			}
			if(!productScores.containsKey(evaluation.getProdId())) {
				productScores.put(evaluation.getProdId(), new int[] {0, 0});
			}
			int[][] merchatScore = merchantScores.get(evaluation.getMerchantId());
			merchatScore[2][0] += evaluation.getScore();
			merchatScore[2][1]++;
			int[] productScore = productScores.get(evaluation.getProdId());
			productScore[0] += evaluation.getScore();
			productScore[1]++;
		}
		
		Iterator<Entry<Integer, int[][]>> merchantScoreIter = merchantScores.entrySet().iterator();
		while(merchantScoreIter.hasNext()) {
			Entry<Integer, int[][]> entry = merchantScoreIter.next();
			int merchantId = entry.getKey();
			int[][] scores = entry.getValue();
			RcMerchant merchant = merchantService.findMerchantById(merchantId);
			merchant.setDeliveryScore(scores[0][1] == 0 ? 5f : new BigDecimal(scores[0][0]).divide(new BigDecimal(scores[0][1]), 2, BigDecimal.ROUND_HALF_UP).floatValue());
			merchant.setServiceScore(scores[1][1] == 0 ? 5f : new BigDecimal(scores[1][0]).divide(new BigDecimal(scores[1][1]), 2, BigDecimal.ROUND_HALF_UP).floatValue());
			merchant.setProductScore(scores[2][1] == 0 ? 5f : new BigDecimal(scores[2][0]).divide(new BigDecimal(scores[2][1]), 2, BigDecimal.ROUND_HALF_UP).floatValue());
			merchantService.modifyMerchant(merchant);
		}
		Iterator<Entry<Integer, int[]>> productScoreIter = productScores.entrySet().iterator();
		while(productScoreIter.hasNext()) {
			Entry<Integer, int[]> entry = productScoreIter.next();
			int prodId = entry.getKey();
			int[] score = entry.getValue();
			RcProd prod = prodService.findProdById(prodId);
			prod.setScore(score[1] == 0 ? 5f : new BigDecimal(score[0]).divide(new BigDecimal(score[1]), 2, BigDecimal.ROUND_HALF_UP).floatValue());
			prodService.modifyProd(prod);
		}
	}
	
}
