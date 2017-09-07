package com.socool.soft.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcBuyer;
import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcOrder;
import com.socool.soft.bo.RcOrderProd;
import com.socool.soft.bo.RcProd;
import com.socool.soft.bo.RcRechargeRecord;
import com.socool.soft.bo.constant.OrderSellerStatusEnum;
import com.socool.soft.service.IBuyerService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IOrderProdService;
import com.socool.soft.service.IOrderService;
import com.socool.soft.service.IProdService;
import com.socool.soft.service.IRechargeRecordService;
import com.socool.soft.service.IReportService;
import com.socool.soft.vo.constant.ReportVOTypeEnum;
import com.socool.soft.vo.newvo.ReportMerchantVO;
import com.socool.soft.vo.newvo.ReportProductVO;
import com.socool.soft.vo.newvo.ReportVO;

@Service
public class ReportServiceImpl implements IReportService {
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderProdService orderProdService;
	@Autowired
	private IProdService prodService;
	@Autowired
	private IBuyerService buyerService;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private IRechargeRecordService rechargeRecordService;

	@Override
	public ReportVO findOverview(Integer merchantId, Integer vendorId) {
		RcOrder param = new RcOrder();
		param.setMerchantId(merchantId);
		param.setVendorId(vendorId);
		List<Integer> sellerStatuses = Arrays.asList(OrderSellerStatusEnum.RECEIVED.getValue(), 
				OrderSellerStatusEnum.PICKEDUP.getValue(), OrderSellerStatusEnum.UNREPLIED.getValue(), 
				OrderSellerStatusEnum.FINISHED.getValue());
		param.setSellerStatuses(sellerStatuses);
		List<RcOrder> orders = orderService.findOrders(param);
		BigDecimal totalOrderAmount = new BigDecimal(0);
		int totalOrderCount = orders.size();
		BigDecimal orderAmountPerCustomer = new BigDecimal(0);
		BigDecimal orderAmountPerProduct = new BigDecimal(0);
		
		if(!CollectionUtils.isEmpty(orders)) {
			List<Long> orderIds = new ArrayList<Long>();
			List<Integer> memberIds = new ArrayList<Integer>();
			for(RcOrder order : orders) {
				totalOrderAmount = totalOrderAmount.add(order.getPayPrice());
				if(!orderIds.contains(order.getOrderId())) {
					orderIds.add(order.getOrderId());
				}
				if(!memberIds.contains(order.getMemberId())) {
					memberIds.add(order.getMemberId());
				}
			}
			RcOrderProd orderProdParam = new RcOrderProd();
			orderProdParam.setOrderIds(orderIds);
			List<RcOrderProd> orderProds = orderProdService.findOrderProds(orderProdParam);
			List<Integer> prodIds = new ArrayList<Integer>();
			for(RcOrderProd orderProd : orderProds) {
				if(!prodIds.contains(orderProd.getProdId())) {
					prodIds.add(orderProd.getProdId());
				}
			}
			orderAmountPerCustomer = totalOrderAmount.divide(new BigDecimal(memberIds.size()),0,BigDecimal.ROUND_HALF_DOWN);
			orderAmountPerProduct = totalOrderAmount.divide(new BigDecimal(prodIds.size()),0,BigDecimal.ROUND_HALF_DOWN);
		}
		
		ReportVO reportVO = new ReportVO();
		reportVO.setTotalOrderAmount(totalOrderAmount);
		reportVO.setTotalOrderCount(totalOrderCount);
		reportVO.setOrderAmountPerCustomer(orderAmountPerCustomer);
		reportVO.setOrderAmountPerProduct(orderAmountPerProduct);
		return reportVO;
	}

	@Override
	public ReportVO findReport(int reportType, Integer merchantId, Integer vendorId) {
		Date createTimeFrom = null;
		Date createTimeTo = null;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		DateFormat format = null;
		if(reportType == ReportVOTypeEnum.BY_WEEK.getValue()) {
			cal.setFirstDayOfWeek(Calendar.MONDAY);
			int dayInterval = cal.get(Calendar.DAY_OF_WEEK) - cal.getFirstDayOfWeek();
			if(dayInterval < 0) {
				dayInterval += 7;
			}
			cal.add(Calendar.DATE, -dayInterval); 
			createTimeFrom = cal.getTime();
			cal.add(Calendar.DATE, 7);
			cal.add(Calendar.SECOND, -1);
			createTimeTo = cal.getTime();
			
			format = new SimpleDateFormat("yyyy-MM-dd");
		} else if(reportType == ReportVOTypeEnum.BY_MONTH.getValue()) {
			cal.set(Calendar.DATE, 1); 
			createTimeFrom = cal.getTime();
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.SECOND, -1);
			createTimeTo = cal.getTime();
			
			format = new SimpleDateFormat("yyyy-MM-dd");
		} else if(reportType == ReportVOTypeEnum.BY_YEAR.getValue()) {
			cal.set(Calendar.DAY_OF_YEAR, 1); 
			createTimeFrom = cal.getTime();
			cal.add(Calendar.YEAR, 1);
			cal.add(Calendar.SECOND, -1);
			createTimeTo = cal.getTime();
			
			format = new SimpleDateFormat("yyyy-MM");
		}
		
		RcOrder param = new RcOrder();
		param.setMerchantId(merchantId);
		param.setVendorId(vendorId);
		List<Integer> sellerStatuses = Arrays.asList(OrderSellerStatusEnum.RECEIVED.getValue(), 
				OrderSellerStatusEnum.PICKEDUP.getValue(), OrderSellerStatusEnum.UNREPLIED.getValue(), 
				OrderSellerStatusEnum.FINISHED.getValue());
		param.setSellerStatuses(sellerStatuses);
		param.setCreateTimeFrom(createTimeFrom);
		param.setCreateTimeTo(createTimeTo);
		List<RcOrder> orders = orderService.findOrders(param);
		BigDecimal totalOrderAmount = new BigDecimal(0);
		int totalOrderCount = orders.size();
		int customerCount = 0;
		int productCount = 0;
		Map<String, BigDecimal> orderAmountDetail = new TreeMap<String, BigDecimal>();
		List<ReportProductVO> topProducts = new ArrayList<ReportProductVO>();
		if(!CollectionUtils.isEmpty(orders)) {
			List<Long> orderIds = new ArrayList<Long>();
			List<Integer> memberIds = new ArrayList<Integer>();
			for(RcOrder order : orders) {
				totalOrderAmount = totalOrderAmount.add(order.getPayPrice());
				if(!orderIds.contains(order.getOrderId())) {
					orderIds.add(order.getOrderId());
				}
				if(!memberIds.contains(order.getMemberId())) {
					memberIds.add(order.getMemberId());
				}
				String date = format.format(order.getCreateTime());
				if(!orderAmountDetail.containsKey(date)) {
					orderAmountDetail.put(date, new BigDecimal(0));
				}
				orderAmountDetail.put(date, orderAmountDetail.get(date).add(order.getPayPrice()));
			}
			RcOrderProd orderProdParam = new RcOrderProd();
			orderProdParam.setOrderIds(orderIds);
			List<RcOrderProd> orderProds = orderProdService.findOrderProds(orderProdParam);
			Map<Integer, Integer> productCounts = new HashMap<Integer, Integer>();
			for(RcOrderProd orderProd : orderProds) {
				if(!productCounts.containsKey(orderProd.getProdId())) {
					productCounts.put(orderProd.getProdId(), 0);
				}
				productCounts.put(orderProd.getProdId(), productCounts.get(orderProd.getProdId()) + orderProd.getQuantity());
			}
			customerCount = memberIds.size();
			productCount = productCounts.size();
			
			List<Entry<Integer, Integer>> productCountList = new ArrayList<Entry<Integer, Integer>>(productCounts.entrySet());
			Collections.sort(productCountList, new Comparator<Entry<Integer, Integer>>() {
				public int compare(Entry<Integer, Integer> entry1, Entry<Integer, Integer> entry2) {
					return entry2.getValue() - entry1.getValue();
				}
			});
			int size = 0;
			for(Entry<Integer, Integer> reportProduct : productCountList) {
				if(size == 10) {
					break;
				}
				int prodId = reportProduct.getKey();
				ReportProductVO reportProductVO = new ReportProductVO();
				reportProductVO.setProdId(prodId);
				reportProductVO.setProdName(prodService.findProdById(prodId).getName());
				reportProductVO.setProdCount(reportProduct.getValue());
				topProducts.add(reportProductVO);
				size++;
			}
		}
		
		RcProd prodParam = new RcProd();
		prodParam.setMerchantId(merchantId);
		prodParam.setVendorId(vendorId);
		List<RcProd> prods = prodService.findProds(prodParam);
		int allProductCount = prods.size();
		int newProductCount = 0;
		for(RcProd prod : prods) {
			if(!prod.getCreateTime().before(createTimeFrom) && !prod.getCreateTime().after(createTimeTo)) {
				newProductCount++;
			}
		}
		
		ReportVO reportVO = new ReportVO();
		reportVO.setTotalOrderAmount(totalOrderAmount);
		reportVO.setTotalOrderCount(totalOrderCount);
		reportVO.setCustomerCount(customerCount);
		reportVO.setProductCount(productCount);
		reportVO.setAllProductCount(allProductCount);
		reportVO.setNewProductCount(newProductCount);
		reportVO.setOrderAmountDetail(orderAmountDetail);
		reportVO.setTopProducts(topProducts);
		
		if(merchantId == null) {
			List<RcBuyer> buyers = buyerService.findBuyers(null);
			int allCustomerCount = buyers.size();
			int newCustomerCount = 0;
			for(RcBuyer buyer : buyers) {
				if(!buyer.getSignUpTime().before(createTimeFrom) && !buyer.getSignUpTime().after(createTimeTo)) {
					newCustomerCount++;
				}
			}
			reportVO.setAllCustomerCount(allCustomerCount);
			reportVO.setNewCustomerCount(newCustomerCount);
			
			List<RcMerchant> merchants = merchantService.findMerchantsByVendorId(vendorId);
			int allMerchantCount = merchants.size();
			int newMerchantCount = 0;
			for(RcMerchant merchant : merchants) {
				if(!merchant.getCreateTime().before(createTimeFrom) && !merchant.getCreateTime().after(createTimeTo)) {
					newMerchantCount++;
				}
			}
			reportVO.setAllMerchantCount(allMerchantCount);
			reportVO.setNewMerchantCount(newMerchantCount);
			
			List<ReportMerchantVO> topMerchants = new ArrayList<ReportMerchantVO>();
			if(!CollectionUtils.isEmpty(orders)) {
				Map<Integer, BigDecimal> merchantAmounts = new HashMap<Integer, BigDecimal>();
				for(RcOrder order : orders) {
					if(!merchantAmounts.containsKey(order.getMerchantId())) {
						merchantAmounts.put(order.getMerchantId(), new BigDecimal(0));
					}
					merchantAmounts.put(order.getMerchantId(), merchantAmounts.get(order.getMerchantId()).add(order.getPayPrice()));
				}
				
				List<Entry<Integer, BigDecimal>> merchantAmountList = new ArrayList<Entry<Integer, BigDecimal>>(merchantAmounts.entrySet());
				Collections.sort(merchantAmountList, new Comparator<Entry<Integer, BigDecimal>>() {
					public int compare(Entry<Integer, BigDecimal> entry1, Entry<Integer, BigDecimal> entry2) {
						return entry2.getValue().compareTo(entry1.getValue());
					}
				});
				int size = 0;
				for(Entry<Integer, BigDecimal> reportMerchant : merchantAmountList) {
					if(size == 10) {
						break;
					}
					int mId = reportMerchant.getKey();
					ReportMerchantVO reportMerchantVO = new ReportMerchantVO();
					reportMerchantVO.setMerchantId(mId);
					reportMerchantVO.setMerchantName(merchantService.findMerchantById(mId).getName());
					reportMerchantVO.setMerchantAmount(reportMerchant.getValue());
					topMerchants.add(reportMerchantVO);
					size++;
				}
			}
			
			RcRechargeRecord rechargeRecordParam = new RcRechargeRecord();
			rechargeRecordParam.setCreateTimeFrom(createTimeFrom);
			rechargeRecordParam.setCreateTimeTo(createTimeTo);
			List<RcRechargeRecord> rechargeRecords = rechargeRecordService.findRechargeRecords(rechargeRecordParam);
			BigDecimal rechargeAmount = new BigDecimal(0);
			int rechargeCount = rechargeRecords.size();
			for(RcRechargeRecord rechargeRecord : rechargeRecords) {
				rechargeAmount = rechargeAmount.add(rechargeRecord.getAmount());
			}
			reportVO.setRechargeAmount(rechargeAmount);
			reportVO.setRechargeCount(rechargeCount);
			reportVO.setTopMerchants(topMerchants);
		}
		return reportVO;
	}
	
}
