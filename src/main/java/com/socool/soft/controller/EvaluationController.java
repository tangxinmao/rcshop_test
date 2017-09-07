package com.socool.soft.controller;

import com.socool.soft.bo.*;
import com.socool.soft.bo.constant.OrderSellerStatusEnum;
import com.socool.soft.service.*;
import com.socool.soft.vo.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequestMapping(value = "evaluation")
@Controller
public class EvaluationController extends BaseController{
	@Autowired
	private IOrderProdEvaluationService orderProdEvaluationService;
	@Autowired
	private IOrderEvaluationService orderEvaluationService;
	@Autowired
	private IProdService prodService;
	@Autowired
	private IBuyerService buyerService;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private IMerchantUserService merchantUserService;
	@Autowired
	private IVendorUserService vendorUserService;
	@Autowired
	private IOrderService orderService;
	
	@RequestMapping("evaluation")
	public String evaluation(){
		return "evaluation";
	}

	@RequestMapping("evaluationList") 
	public String evaluationList(HttpServletRequest httpServletRequest,
			Model model,Integer currentPage,Date createTimeFrom, Date createTimeTo, Integer score,
			Integer prodId, Long orderId, Integer merchantId, Integer vendorId,String orderBy) throws UnsupportedEncodingException{
		// 初始化时第一页
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		
		RcOrderProdEvaluation param = new RcOrderProdEvaluation();
		param.setCreateTimeFrom(createTimeFrom);
		param.setCreateTimeTo(createTimeTo);
		param.setScore(score);
		param.setOrderBy(orderBy);
		param.setProdId(prodId);
		param.setOrderId(orderId);
		Integer merchantUserId = getMerchantUserId(httpServletRequest);
		if(merchantUserId != null) {
			RcMerchantUser merchantUser = merchantUserService.findMerchantUserById(merchantUserId);
			merchantId = merchantUser.getMerchantId();
		}
		Integer vendorUserId = getVendorUserId(httpServletRequest);
		if(vendorUserId != null) {
			RcVendorUser vendorUser = vendorUserService.findVendorUserById(vendorUserId);
			vendorId = vendorUser.getVendorId();
		}
		param.setMerchantId(merchantId);
		param.setVendorId(vendorId);
		if(param.getCreateTimeTo() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(param.getCreateTimeTo());
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.SECOND, -1);
			param.setCreateTimeTo(cal.getTime());
		}
		List<RcOrderProdEvaluation> orderProdEvaluations = orderProdEvaluationService.findOrderProdEvaluations(param);
		for (RcOrderProdEvaluation orderProdEvaluation : orderProdEvaluations) {
			orderProdEvaluation.setProd(prodService.findProdForSolr(orderProdEvaluation.getProdId()));
			orderProdEvaluation.setMember(buyerService.findBuyerById(orderProdEvaluation.getMemberId()));
			orderProdEvaluation.setMerchant(merchantService.findMerchantById(orderProdEvaluation.getMerchantId()));
		}
		model.addAttribute("list", orderProdEvaluations );
		if(flag) {
			return "evaluationList";	
		} else {
			return "evaluationList_inner";
		}
	}
	
	@RequestMapping("deleteEvaluation")
	@ResponseBody
	public String deleteEvaluation(long orderProdId){
		int result = orderProdEvaluationService.removeOrderProdEvaluation(orderProdId);
		if(result > 0){
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}
	
	@RequestMapping("/deleteEvaluations")
	public String deleteEvaluations(String orderProdIds) {
		String[] orderProdIdArray = orderProdIds.split(",");
		for(String orderProdId : orderProdIdArray){
			orderProdEvaluationService.removeOrderProdEvaluation(Long.parseLong(orderProdId));
		}
		return "redirect:/evaluation/evaluationList";
	}
	
/*	@RequestMapping("storyReply")
	@ResponseBody
	public String storyReply(long orderProdId, String merchantReply){
		int result = evaluationService.replyEvaluation(orderProdId, merchantReply);
		return "{\"code\":\"" + result + "\"}";
	}*/
	
	@RequestMapping("storyReply")
	@ResponseBody
	public String storyReplys(String orderProdIds, String merchantReply){
		String[] orderProdIdArray = orderProdIds.split(",,,");
		for(String orderProdId : orderProdIdArray){
			orderProdEvaluationService.replyOrderProdEvaluation(Long.parseLong(orderProdId), merchantReply);
			RcOrderProdEvaluation orderProdEvaluation = orderProdEvaluationService.findOrderProdEvaluationById(Long.parseLong(orderProdId));
			boolean allReplied = true;
			RcOrderProdEvaluation param = new RcOrderProdEvaluation();
			param.setOrderId(orderProdEvaluation.getOrderId());
			List<RcOrderProdEvaluation> evaluations = orderProdEvaluationService.findOrderProdEvaluations(param);
			for (RcOrderProdEvaluation evaluation : evaluations) {
				if(StringUtils.isBlank(evaluation.getMerchantReply())){
					allReplied = false;
				}
			}
			if(allReplied){
				RcOrder order = orderService.findOrderById(orderProdEvaluation.getOrderId());
				order.setSellerStatus(OrderSellerStatusEnum.FINISHED.getValue());
				orderService.modifyOrder(order);
			}
		}
		return "{\"code\":\"1\"}";
	}
	
	@RequestMapping(value="/merchantEvaluation")
	public String merchantEvaluation(HttpServletRequest request,
									 Integer currentPage,
									 Model model,
									 RcOrderEvaluation orderEvaluation,
									 String isDesc){
		Integer merchantUserId = getMerchantUserId(request);
		if(merchantUserId != null) {
			RcMerchantUser merchantUser = merchantUserService.findMerchantUserById(merchantUserId);
			orderEvaluation.setMerchantId(merchantUser.getMerchantId());
		}
		Integer vendorUserId = getVendorUserId(request);
		if(vendorUserId != null) {
			RcVendorUser vendorUser = vendorUserService.findVendorUserById(vendorUserId);
			orderEvaluation.setVendorId(vendorUser.getVendorId());
		}
		if("2".equals(isDesc)) {
			orderEvaluation.setOrderBy("CREATE_TIME ASC");
		} else {
			orderEvaluation.setOrderBy("CREATE_TIME DESC");
		}
		if(orderEvaluation.getCreateTimeTo() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(orderEvaluation.getCreateTimeTo());
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.SECOND, -1);
			orderEvaluation.setCreateTimeTo(cal.getTime());
		}
		// 初始化时第一页
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcOrderEvaluation> orderEvaluations = orderEvaluationService.findPagedOrderEvaluations(orderEvaluation, page);
		model.addAttribute("list", orderEvaluations);
		if(flag) {
			return "merchantEvaluationList";
		} else {
			return "merchantEvaluationList_inner";
		}
	}
	
	@RequestMapping(value="/deleteOneME")
	@ResponseBody
	public String deleteOneME(long orderId){
		orderEvaluationService.removeOrderEvaluation(orderId);
		return "{\"code\":\"1\"}";
	}
}
