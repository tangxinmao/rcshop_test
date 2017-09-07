package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcBuyer;
import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcOrder;
import com.socool.soft.bo.RcOrderEvaluation;
import com.socool.soft.bo.RcVendor;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.dao.RcOrderEvaluationMapper;
import com.socool.soft.service.IBuyerService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IOrderEvaluationService;
import com.socool.soft.service.IOrderService;
import com.socool.soft.service.IVendorService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class OrderEvaluationServiceImpl implements IOrderEvaluationService {
	@Autowired
	private RcOrderEvaluationMapper orderEvaluationMapper;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private IVendorService vendorService;
	@Autowired
	private IBuyerService buyerService;
	@Autowired
	private IOrderService orderService;

	@Override
	public long addOrderEvaluation(RcOrderEvaluation orderEvaluation) {
		orderEvaluation.setCreateTime(new Date());
		if(orderEvaluationMapper.insert(orderEvaluation) > 0) {
			return orderEvaluation.getOrderId();
		}
		return 0;
	}
	
	@Override
	public long modifyOrderEvaluation(RcOrderEvaluation orderEvaluation) {
		if(orderEvaluationMapper.updateByPrimaryKey(orderEvaluation) > 0) {
			return orderEvaluation.getOrderId();
		}
		return 0;
	}

	@Override
	public List<RcOrderEvaluation> findPagedOrderEvaluations(
			RcOrderEvaluation param, Page page) {
		if(StringUtils.isNotBlank(param.getMerchantName())) {
			List<RcMerchant> merchants = merchantService.findMerchantByName(param.getMerchantName());
			if(CollectionUtils.isEmpty(merchants)) {
				return new ArrayList<RcOrderEvaluation>();
			}
			List<Integer> merchantIds = new ArrayList<Integer>();
			for(RcMerchant merchant : merchants) {
				merchantIds.add(merchant.getMerchantId());
			}
			param.setMerchantIds(merchantIds);
		}
		PageContext.setPage(page);
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		List<RcOrderEvaluation> orderEvaluations = orderEvaluationMapper.select(param);
		for(RcOrderEvaluation orderEvaluation : orderEvaluations) {
			RcOrder order = orderService.findOrderById(orderEvaluation.getOrderId());
			RcMerchant merchant = merchantService.findMerchantById(order.getMerchantId());
			orderEvaluation.setMerchant(merchant);
			RcVendor vendor = vendorService.findVendorById(order.getVendorId());
			orderEvaluation.setVendor(vendor);
			RcBuyer buyer = buyerService.findBuyerById(order.getMemberId());
			orderEvaluation.setMember(buyer);
		}
		return orderEvaluations;
	}

	@Override
	public List<RcOrderEvaluation> findOrderEvaluations(RcOrderEvaluation param) {
		return orderEvaluationMapper.select(param);
	}

	@Override
	public int removeOrderEvaluation(long orderId) {
		RcOrderEvaluation param = new RcOrderEvaluation();
		param.setOrderId(orderId);
		param.setDelFlag(YesOrNoEnum.YES.getValue());
		if(modifyOrderEvaluation(param) > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public RcOrderEvaluation findOrderEvaluationById(long orderId) {
		return orderEvaluationMapper.selectByPrimaryKey(orderId);
	}
}
