package com.socool.soft.service.impl;

import com.socool.soft.bo.RcOrderProdEvaluation;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.dao.RcOrderProdEvaluationMapper;
import com.socool.soft.service.IOrderProdEvaluationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderProdEvaluationServiceImpl implements IOrderProdEvaluationService {
	@Autowired
	private RcOrderProdEvaluationMapper orderProdEvaluationMapper;

//	@Override
//	public void addEvaluations(List<RcOrderProdEvaluation> evaluations) {
//		for(RcOrderProdEvaluation evaluation : evaluations) {
//			addEvaluation(evaluation);
//		}
//	}

	@Override
	public long addOrderProdEvaluation(RcOrderProdEvaluation orderProdEvaluation) {
		if(orderProdEvaluation.getProdSkuId() == null) {
			orderProdEvaluation.setProdSkuId(String.valueOf(orderProdEvaluation.getProdId()));
		}
		orderProdEvaluation.setCreateTime(new Date());
		if(orderProdEvaluationMapper.insert(orderProdEvaluation) > 0) {
			return orderProdEvaluation.getOrderProdId();
		}
		return 0;
	}

	@Override
	public List<RcOrderProdEvaluation> findOrderProdEvaluations(RcOrderProdEvaluation param) {
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		if(StringUtils.isBlank(param.getOrderBy())) {
            param.setOrderBy("CREATE_TIME DESC");
        }
		return orderProdEvaluationMapper.select(param);
	}

//	@Override
//	public List<RcOrderProdEvaluation> findPagedEvaluations(RcOrderProdEvaluation param, Page page) {
//		PageContext.setPage(page);
//		return findEvaluations(param);
//	}

	@Override
	public long modifyOrderProdEvaluation(RcOrderProdEvaluation evaluation) {
		if(orderProdEvaluationMapper.updateByPrimaryKey(evaluation) > 0) {
			return evaluation.getOrderProdId();
		}
		return 0;
	}

	@Override
	public List<RcOrderProdEvaluation> findOrderProdEvaluationsByProdId(int prodId) {
		RcOrderProdEvaluation param = new RcOrderProdEvaluation();
		param.setProdId(prodId);
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		param.setOrderBy("CREATE_TIME DESC");
		return orderProdEvaluationMapper.select(param);
	}

//	@Override
//	public RcOrderProdEvaluation findLatestEvaluationByProdId(int prodId) {
//		RcOrderProdEvaluation param = new RcOrderProdEvaluation();
//		param.setProdId(prodId);
//		param.setDelFlag(YesOrNoEnum.NO.getValue());
//		param.setOrderBy("CREATE_TIME DESC");
//		return evaluationMapper.selectOne(param);
//	}

//	@Override
//	public List<RcOrderProdEvaluation> findEvaluationsByMemberId(int memberId) {
//		RcOrderProdEvaluation param = new RcOrderProdEvaluation();
//		param.setMemberId(memberId);
//		return findEvaluations(param);
//	}

	@Override
	public int replyOrderProdEvaluation(long orderProdId, String reply) {
		RcOrderProdEvaluation param = new RcOrderProdEvaluation();
		param.setOrderProdId(orderProdId);
		param.setMerchantReply(reply);
		if(modifyOrderProdEvaluation(param) > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public RcOrderProdEvaluation findOrderProdEvaluationById(long orderProdId) {
		return orderProdEvaluationMapper.selectByPrimaryKey(orderProdId);
	}

//	@Override
//	public List<RcOrderProdEvaluation> findEvaluations(Integer merchantId,
//			Integer vendorId, Long orderId, Integer prodId,
//			Date startCreateTime, Date endCreateTime, Integer score) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public int removeOrderProdEvaluation(long orderProdId) {
		RcOrderProdEvaluation param = new RcOrderProdEvaluation();
		param.setOrderProdId(orderProdId);
		param.setDelFlag(YesOrNoEnum.YES.getValue());
		if(modifyOrderProdEvaluation(param) > 0) {
			return 1;
		}
		return 0;
	}

//	@Override
//	public void updateMerchantScore() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void updateMerchantProductScore() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void updateProductScore() {
//		// TODO Auto-generated method stub
//		
//	}
}
