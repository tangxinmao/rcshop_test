package com.socool.soft.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socool.soft.bo.*;
import com.socool.soft.bo.constant.*;
import com.socool.soft.constant.Constants;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.dao.RcOrderMapper;
import com.socool.soft.exception.SystemException;
import com.socool.soft.service.*;
import com.socool.soft.util.VOConversionUtil;
import com.socool.soft.vo.MailVo;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;
import com.socool.soft.vo.newvo.android.OrderNumVO;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class OrderServiceImpl implements IOrderService {
    private Logger log = Logger.getLogger(getClass());
	@Autowired
	private RcOrderMapper orderMapper;
	@Autowired
	private IOrderFeeService orderFeeService;
	@Autowired
	private IOrderPaymentService orderPaymentService;
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private IOrderProdSkuPropInfoService orderProdSkuPropInfoService;
	@Autowired
	private IOrderProdService orderProdService;
	@Autowired
	private IProdService prodService;
	@Autowired
	private IProdSkuService prodSkuService;
	@Autowired
	private IProdImgService prodImgService;
	@Autowired
	private IOrderConsigneeService orderConsigneeService;
	@Autowired
	private IOrderDeliveryService orderDeliveryService;
	@Autowired
	private IOrderOperationService orderOperationService;
	@Autowired
	private IOrderCouponService orderCouponService;
	@Autowired
	private ICityService cityService;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private IBuyerService buyerService;
	@Autowired
	private IBuyerTradeService buyerTradeService;
	@Autowired
	private IMailService mailService;
	@Autowired
	private ISearchService searchService;
	@Autowired
	private IOrderProdEvaluationService orderProdEvaluationService;
	@Autowired
	private IOrderEvaluationService orderEvaluationService;
	@Autowired
	private PropertyConstants propertyConstants;
	@Autowired
	private IPushService pushService;
	
	public static Logger logger = LogManager.getLogger(OrderServiceImpl.class.getName());
	
	@Override
	public List<RcOrder> findOrders(RcOrder param) {
		if(param.getOrderId() != null) {
			RcOrder order = findOrderById(param.getOrderId());
			if(order == null) {
				return new ArrayList<RcOrder>();
			}
			return Arrays.asList(order);
		} else {
			if(StringUtils.isNotBlank(param.getCityName())) {
				List<RcCity> cities = cityService.findCitiesByName(param.getCityName());
				if(CollectionUtils.isEmpty(cities)) {
					return new ArrayList<RcOrder>();
				}
				List<Integer> cityIds = new ArrayList<Integer>();
				for(RcCity city : cities) {
					cityIds.add(city.getCityId());
				}
				param.setCityIds(cityIds);
			}
			if(StringUtils.isNotBlank(param.getMerchantName())) {
				List<RcMerchant> merchants = merchantService.findMerchantByName(param.getMerchantName());
				if(CollectionUtils.isEmpty(merchants)) {
					return new ArrayList<RcOrder>();
				}
				List<Integer> merchantIds = new ArrayList<Integer>();
				for(RcMerchant merchant : merchants) {
					merchantIds.add(merchant.getMerchantId());
				}
				param.setMerchantIds(merchantIds);
			}
			if(StringUtils.isNotBlank(param.getMobile())) {
				RcBuyer buyerParam = new RcBuyer();
				buyerParam.setMobile(param.getMobile());
				List<RcBuyer> buyers = buyerService.findBuyers(buyerParam);
				if(CollectionUtils.isEmpty(buyers)) {
					return new ArrayList<RcOrder>();
				}
				List<Integer> buyerIds = new ArrayList<Integer>();
				for(RcBuyer buyer : buyers) {
					buyerIds.add(buyer.getMemberId());
				}
				param.setMemberIds(buyerIds);
			}
			return orderMapper.select(param);
		}
	}
	
	@Override
	public List<RcOrder> findPagedOrders(RcOrder param, Page page) {
		PageContext.setPage(page);
		if(StringUtils.isEmpty(param.getOrderBy()))
		param.setOrderBy("CREATE_TIME DESC");
		return findOrders(param);
	}

	@Override
	public RcOrder findOrderById(long orderId) {
		return orderMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public long modifyOrder(RcOrder order) {
		if(orderMapper.updateByPrimaryKey(order) > 0) {
			return order.getOrderId();
		}
		return 0;
	}

//	@Override
//	public List<RcOrderProd> findOrderProdByOrderId(Long orderId) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public long addOrder(RcOrder order) {
		order.setCreateTime(new Date());
		if(orderMapper.insert(order) > 0) {
			return order.getOrderId();
		}
		return 0;
	}

//	@Override
//	public void addOrders(List<RcOrder> orders) {
//		// TODO Auto-generated method stub
//
//	}

	@Override
	public void modifyOrders(List<RcOrder> orders) {
		for(RcOrder order : orders) {
			orderMapper.updateByPrimaryKey(order);
		}
	}

	@Override
	public OrderNumVO findOrderNumByBuyerId(int buyerId) {
		OrderNumVO orderNum = new OrderNumVO();
		int unpaid = 0;
		int unreceived = 0;
		int unevaluated = 0;
		int rejected = 0;
		RcOrder param = new RcOrder();
		param.setMemberId(buyerId);
		List<RcOrder> orders = orderMapper.select(param);
		for(RcOrder order : orders) {
			if(order.getBuyerStatus() == OrderBuyerStatusEnum.UNPAID.getValue()) {
				unpaid++;
			} else if(order.getBuyerStatus() == OrderBuyerStatusEnum.UNRECEIVED.getValue()) {
				unreceived++;
			} else if(order.getBuyerStatus() == OrderBuyerStatusEnum.UNEVALUATED.getValue()) {
				unevaluated++;
			} else if(order.getBuyerStatus() == OrderBuyerStatusEnum.REJECTED.getValue()) {
				rejected++;
			}
		}
		orderNum.setUnpaid(unpaid);
		orderNum.setUnreceived(unreceived);
		orderNum.setUnevaluated(unevaluated);
		orderNum.setRejected(rejected);
		return orderNum;
	}

	@Override
	public List<RcOrder> findPagedBuyerOrders(int buyerId, Integer buyerStatus, Page page) {
		PageContext.setPage(page);
		RcOrder param = new RcOrder();
		param.setMemberId(buyerId);
		param.setBuyerStatus(buyerStatus);
		param.setOrderBy("CREATE_TIME DESC");
		List<RcOrder> orders = findOrders(param);
		for(RcOrder order : orders) {
			long orderId = order.getOrderId();
			List<RcOrderProd> orderProds = orderProdService.findOrderProdsByOrderId(orderId);
			for(RcOrderProd orderProd : orderProds) {
				VOConversionUtil.Entity2VO(orderProd, new String[] {"prodId", "quantity", "prodPrice", "prodSkuId","orderProdId",
						"amount"}, null);
			}
			
			RcOrderConsignee orderConsignee = orderConsigneeService.findOrderConsigneeById(orderId);
			RcCity city = cityService.findCityById(orderConsignee.getCityId());
			VOConversionUtil.Entity2VO(orderConsignee, null, new String[] {"orderId", "cityId"});
			orderConsignee.setProvinceName(city.getProvinceName());
			orderConsignee.setCityName(city.getName());
			
			RcOrderCoupon orderCoupon = orderCouponService.findOrderCouponByOrderId(orderId);
			List<RcOrderOperation> orderOperations = orderOperationService.findOrderOperationsByOrderId(orderId);
			
			VOConversionUtil.Entity2VO(order, new String[] {"orderId", "sellerStatus","buyerMemo", "buyerStatus", "createTime","freight","merchantId",
					"productPrice", "payPrice", "memberId", "paymentInterface", "paymentType", 
					"pickupCode", "prodPrice", "amount", "payCode", "couponAmount"}, null);
			if(order.getSellerStatus() == OrderSellerStatusEnum.DELIVERED.getValue()){
				order.setDeliveryStatus(true);
			}
			order.setOrderProds(orderProds);
			order.setOrderConsignee(orderConsignee);
			if(orderCoupon != null){
				order.setCouponId(orderCoupon.getCouponId());
			}
			order.setMerchantName(merchantService.findMerchantById(order.getMerchantId()).getName());
			for(RcOrderProd orderProd : order.getOrderProds()) {
				RcProd prod = prodService.findProdById(orderProd.getProdId());
				orderProd.setProdName(prod.getName());
				RcProdImg prodImg = prodImgService.findFirstProdImgByProdSkuId(orderProd.getProdSkuId());
				if(prodImg == null) {
					prodImg = prodImgService.findFirstProdImgByProdSkuId(String.valueOf(orderProd.getProdId()));
				}
				orderProd.setProdImgUrl(prodImg.getImgUrl());
				if(!orderProd.getProdSkuId().equals(String.valueOf(orderProd.getProdId()))) {
					List<RcOrderProdSkuPropInfo> orderProdSkuPropInfos = orderProdSkuPropInfoService.findOrderProdSkuPropInfosByOrderProdId(orderProd.getOrderProdId());
					List<String> prodPropVals = new ArrayList<String>();
					for(RcOrderProdSkuPropInfo orderProdSkuPropInfo : orderProdSkuPropInfos) {
						prodPropVals.add(orderProdSkuPropInfo.getProdPropVal());
					}
					orderProd.setProdPropVals(prodPropVals);
				}
			}
			order.setCouponAmount(new BigDecimal(0));
			order.setFreight(new BigDecimal(0));
			order.setCreditCardFee(new BigDecimal(0));
			List<RcOrderFee> orderFees = orderFeeService.findOrderFeesByOrderId(orderId);
			for(RcOrderFee orderFee : orderFees) {
				if(orderFee.getType() == OrderFeeTypeEnum.COUPON.getValue()) {
					order.setCouponAmount(orderFee.getAmount());
				}
				if(orderFee.getType() == OrderFeeTypeEnum.FREIGHT.getValue()) {
					order.setFreight(orderFee.getAmount());
				}
				if(orderFee.getType() == OrderFeeTypeEnum.CREDIT_CARD_FEE.getValue()) {
					order.setCreditCardFee(orderFee.getAmount());
				}
			}
			for(RcOrderOperation orderOperation : orderOperations) {
				if(orderOperation.getOperationType() == OrderOperationTypeEnum.PAY.getValue() || 
						orderOperation.getOperationType() == OrderOperationTypeEnum.PAYMENT_CONFIRM.getValue()) {
					order.setPayTime(orderOperation.getOperationTime());
				} else if(orderOperation.getOperationType() == OrderOperationTypeEnum.CANCEL.getValue()) {
					order.setCancelTime(orderOperation.getOperationTime());
					if(orderOperation.getMemberId() == 0) {
						order.setCanceledBySystem(true);
					} else {
						order.setCanceledBySystem(false);
					}
				}else if(orderOperation.getOperationType() == OrderOperationTypeEnum.REFUND.getValue()){
					order.setRefundTime(orderOperation.getOperationTime());
					order.setOrderRejected(orderOperation.getOperationMemo());
				}
			}
		}
		return orders;
	}

	@Override
	public List<RcOrder> findPagedSellerOrders(int merchantId, Integer status, Page page) {
		PageContext.setPage(page);
		RcOrder param = new RcOrder();
		param.setMerchantId(merchantId);
		param.setSellerStatus(status);
		param.setOrderBy("CREATE_TIME DESC");
		List<RcOrder> orders = findOrders(param);
		for (RcOrder order : orders) {
			long orderId = order.getOrderId();
			VOConversionUtil.Entity2VO(order,
					new String[] { "orderId", "sellerStatus", "buyerMemo", "buyerStatus", "createTime", "freight",
							"merchantId", "productPrice", "payPrice", "memberId", "paymentInterface",
							"paymentType", "pickupCode", "prodPrice", "amount", "payCode" },
					null);
			List<RcOrderProd> orderProds = orderProdService.findOrderProdsByOrderId(orderId);
			for (RcOrderProd orderProd : orderProds) {
				VOConversionUtil.Entity2VO(orderProd,
						new String[] { "prodId", "quantity", "prodPrice", "prodSkuId", "orderProdId", "amount" }, null);
			}
			order.setOrderProds(orderProds);
			for (RcOrderProd orderProd : order.getOrderProds()) {
				RcProd prod = prodService.findProdById(orderProd.getProdId());
				orderProd.setProdName(prod.getName());
				RcProdImg prodImg = prodImgService.findFirstProdImgByProdSkuId(orderProd.getProdSkuId());
				if (prodImg == null) {
					prodImg = prodImgService.findFirstProdImgByProdSkuId(String.valueOf(orderProd.getProdId()));
				}
				orderProd.setProdImgUrl(prodImg.getImgUrl());
				if (!orderProd.getProdSkuId().equals(String.valueOf(orderProd.getProdId()))) {
					List<RcOrderProdSkuPropInfo> orderProdSkuPropInfos = orderProdSkuPropInfoService
							.findOrderProdSkuPropInfosByOrderProdId(orderProd.getOrderProdId());
					List<String> prodPropVals = new ArrayList<String>();
					for (RcOrderProdSkuPropInfo orderProdSkuPropInfo : orderProdSkuPropInfos) {
						prodPropVals.add(orderProdSkuPropInfo.getProdPropVal());
					}
					orderProd.setProdPropVals(prodPropVals);
				}
			}
			order.setCouponAmount(new BigDecimal(0));
			order.setFreight(new BigDecimal(0));
			order.setCreditCardFee(new BigDecimal(0));
			List<RcOrderFee> orderFees = orderFeeService.findOrderFeesByOrderId(orderId);
			for(RcOrderFee orderFee : orderFees) {
				if(orderFee.getType() == OrderFeeTypeEnum.COUPON.getValue()) {
					order.setCouponAmount(orderFee.getAmount());
				}
				if(orderFee.getType() == OrderFeeTypeEnum.FREIGHT.getValue()) {
					order.setFreight(orderFee.getAmount());
				}
				if(orderFee.getType() == OrderFeeTypeEnum.CREDIT_CARD_FEE.getValue()) {
					order.setCreditCardFee(orderFee.getAmount());
				}
			}
		}
		return orders;
	}
	
	@Override
	public List<RcOrder> findPagedOrdersByBuyerId(int buyerId, Page page) {
		PageContext.setPage(page);
		RcOrder param = new RcOrder();
		param.setMemberId(buyerId);
		param.setOrderBy("CREATE_TIME DESC");
		return findOrders(param);
	}

	@Override
	public BigDecimal statisticOrderPayPrice(int merchantId, Date createTimeFrom, Date createTimeTo) {
		List<RcOrder> orders = findOrderHistories(merchantId, createTimeFrom, createTimeTo);
		BigDecimal applyAmount = new BigDecimal(0);
		for(RcOrder order : orders) {
			applyAmount = applyAmount.add(order.getPayPrice());
		}
		return applyAmount;
	}

	@Override
	public List<RcOrder> findOrderHistories(int merchantId, Date createTimeFrom, Date createTimeTo) {
		RcOrder param = new RcOrder();
		param.setMerchantId(merchantId);
		param.setCreateTimeFrom(createTimeFrom);
		param.setCreateTimeTo(createTimeTo);
		List<Integer> sellerStatuses = Arrays.asList(OrderSellerStatusEnum.RECEIVED.getValue(), 
				OrderSellerStatusEnum.PICKEDUP.getValue(), OrderSellerStatusEnum.UNREPLIED.getValue(), 
				OrderSellerStatusEnum.FINISHED.getValue());
		param.setSellerStatuses(sellerStatuses);
		return findOrders(param);
	}

	@Override
	public List<RcOrder> findPagedOrderHistories(int merchantId, Date createTimeFrom, Date createTimeTo, Page page) {
		PageContext.setPage(page);
		return findOrderHistories(merchantId, createTimeFrom, createTimeTo);
	}


	@Override
	public void updateOrderInvalidTime(Integer cancelTime) {
		if(cancelTime != null && cancelTime > 0) {
			RcOrder param = new RcOrder();
			param.setBuyerStatus(OrderBuyerStatusEnum.UNPAID.getValue());
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -cancelTime);
			param.setCreateTimeTo(cal.getTime());
			List<RcOrder> orders = findOrders(param);
			for(RcOrder order : orders) {
			    log.info("订单自动超时: " + order);
				RcOrderPayment orderPayment = orderPaymentService.findOrderPaymentByOrderId(order.getOrderId());
				if(orderPayment != null && orderPayment.getPaymentInterface() == PaymentInterfaceEnum.DOKU.getValue()) {
					boolean status = paymentService.getTransactionStatus(order.getOrderId());
					if(status) {
						completePayment(order.getOrderId(), orderPayment.getPaymentInterface(), orderPayment.getPaymentType());
						orderPayment.setStatus(OrderPaymentStatusEnum.FINISHED.getValue());
						orderPayment.setFinishTime(new Date());
						orderPaymentService.modifyOrderPayment(orderPayment);
					}
				}
				order.setBuyerStatus(OrderBuyerStatusEnum.CANCELED.getValue());
				order.setSellerStatus(OrderSellerStatusEnum.CANCELED.getValue());
				modifyOrder(order);
				
				RcOrderOperation orderOperation = new RcOrderOperation();
				orderOperation.setOrderId(order.getOrderId());
				orderOperation.setOperationType(OrderOperationTypeEnum.CANCEL.getValue());
				orderOperation.setMemberId(0);
				orderOperationService.addOrderOperation(orderOperation);

                // 推送通知 "订单超时自动取消"
                try {
                    pushService.orderTimeout(order);
                } catch (SystemException e) {
                    e.printStackTrace();
                }
			}
		}
	}

	@Override
	public void updateOrderCloseTime(Integer receiveServiceTime) {
		if(receiveServiceTime != null && receiveServiceTime > 0) {
			RcOrderOperation param = new RcOrderOperation();
			param.setOperationType(OrderOperationTypeEnum.DELIVER.getValue());
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -receiveServiceTime);
			param.setOperationTimeTo(cal.getTime());
			List<RcOrderOperation> orderOperations = orderOperationService.findOrderOperations(param);
			List<Long> orderIds = new ArrayList<Long>();
			for(RcOrderOperation orderOperation : orderOperations) {
				orderIds.add(orderOperation.getOrderId());
			}
			if(!CollectionUtils.isEmpty(orderIds)) {
				RcOrder orderParam = new RcOrder();
				orderParam.setBuyerStatus(OrderBuyerStatusEnum.UNRECEIVED.getValue());
				orderParam.setOrderIds(orderIds);
				List<RcOrder> orders = findOrders(orderParam);
				for(RcOrder order : orders) {
					order.setBuyerStatus(OrderBuyerStatusEnum.UNEVALUATED.getValue());
					order.setSellerStatus(OrderSellerStatusEnum.RECEIVED.getValue());
					modifyOrder(order);
                    // 推送通知 "买家- 订单已自动收货"
                    try {
                        pushService.orderAutoReceiving(order);
                    } catch (SystemException e) {
                        e.printStackTrace();
                    }

                    // 推送通知 "卖家 - 订单已收货"
                    try {
                        pushService.orderCompleted(order);
                    } catch (SystemException e) {
                        e.printStackTrace();
                    }

                    RcOrderOperation orderOperation = new RcOrderOperation();
					orderOperation.setOrderId(order.getOrderId());
					orderOperation.setOperationType(OrderOperationTypeEnum.RECEIVE.getValue());
					orderOperation.setMemberId(0);
					orderOperationService.addOrderOperation(orderOperation);
					
					List<RcOrderProd> orderProds = orderProdService.findOrderProdsByOrderId(order.getOrderId()); 
					for(RcOrderProd orderProd : orderProds) {
						searchService.updateIndexForProdPutUpDown(orderProd.getProdId()); 
					}
				}
			}
		}
	}

	@Override
	public void completePayment(long orderId, int paymentInteface, int paymentType) {
		RcOrder order = findOrderById(orderId);
		if(order.getDeliveryType() == OrderDeliveryTypeEnum.BY_MERCHANT.getValue()) {
			order.setSellerStatus(OrderSellerStatusEnum.UNDELIVERED.getValue());
			order.setBuyerStatus(OrderBuyerStatusEnum.UNRECEIVED.getValue());
		} else if(order.getDeliveryType() == OrderDeliveryTypeEnum.BY_BUYER.getValue()) {
			order.setSellerStatus(OrderSellerStatusEnum.UNPICKEDUP.getValue());
			order.setBuyerStatus(OrderBuyerStatusEnum.UNPICKEDUP.getValue());
		}
		order.setPaymentInterface(paymentInteface);
		order.setPaymentType(paymentType);
		if(paymentInteface != PaymentInterfaceEnum.DOKU.getValue() || paymentType != PaymentTypeEnum.PAY_CODE.getValue()) {
			order.setPayCode("");
		}
		if(paymentInteface == PaymentInterfaceEnum.DOKU.getValue() && paymentType == PaymentTypeEnum.CREDIT_CARD.getValue()) {
			BigDecimal oldPayPrice = order.getPayPrice();
			BigDecimal creditCardFee = oldPayPrice.multiply(new BigDecimal(String.valueOf(Constants.DOKU_CREDIT_CARD_RATE)));
			creditCardFee = creditCardFee.setScale(0, RoundingMode.HALF_UP);
//			order.setPayPrice(oldPayPrice.add(creditCardFee));
			RcOrderFee orderFee = new RcOrderFee();
			orderFee.setOrderId(orderId);
			orderFee.setType(OrderFeeTypeEnum.CREDIT_CARD_FEE.getValue());
			orderFee.setAmount(creditCardFee);
			orderFee.setAddToTotal(YesOrNoEnum.NO.getValue());
			orderFeeService.addOrderFee(orderFee);
		}
		long result = modifyOrder(order);
		if(result > 0) {
			RcBuyerTrade buyerTrade = new RcBuyerTrade();
			buyerTrade.setAmount(order.getPayPrice());
			buyerTrade.setType(BuyerTradeTypeEnum.EXPENSE.getValue());
			buyerTrade.setOrderId(orderId);
			buyerTrade.setBuyerId(order.getMemberId());
			buyerTrade.setPaymentInterface(paymentInteface);
			buyerTrade.setPaymentType(paymentType);
			buyerTradeService.addBuyerTrade(buyerTrade);
			
			RcOrderOperation orderOperation = new RcOrderOperation();
			orderOperation.setOrderId(orderId);
			orderOperation.setOperationType(OrderOperationTypeEnum.PAY.getValue());
			orderOperation.setMemberId(order.getMemberId());
			orderOperationService.addOrderOperation(orderOperation);
			
			RcMerchant merchant = merchantService.findMerchantById(order.getMerchantId());
			List<RcOrderProd> orderProds = orderProdService.findOrderProdsByOrderId(orderId);
			for(RcOrderProd orderProd : orderProds) {
				RcProd prod = prodService.findProdById(orderProd.getProdId());
				int inventory = 0;
				if(!orderProd.getProdSkuId().equals(String.valueOf(orderProd.getProdId()))) {
					RcProdSku prodSku = prodSkuService.findProdSkuByProdSkuId(orderProd.getProdSkuId());
					prodSku.setInventory(prodSku.getInventory() - orderProd.getQuantity());
					prodSkuService.modifyProdSku(prodSku);
					inventory = prodSku.getInventory();
				}
				prod.setInventory(prod.getInventory() - orderProd.getQuantity());
				prod.setSoldNum(prod.getSoldNum() + orderProd.getQuantity());
				prodService.modifyProd(prod);
				
				inventory = orderProd.getProdSkuId().equals(String.valueOf(orderProd.getProdId())) ? prod.getInventory() : inventory;
				
				if(inventory < prod.getStockWarning()/* && storage.getWarningStatus() == YesOrNoEnum.NO.getValue()*/) {
					final MailVo mailVo = new MailVo();
					mailVo.setVelocity("inventory.vm");
	//				mailVo.setHttpServletRequest(httpServletRequest);
					mailVo.setSubject("product stockout");
					mailVo.setTo(merchant.getEmail());
					mailVo.getContext().put("orderProd", orderProd);
					prod.setMerchant(merchantService.findMerchantById(prod.getMerchantId()));
					orderProd.setProd(prod);
//					prod.setProdStorage(storage);
					RcProdImg prodImg = prodImgService.findFirstProdImgByProdSkuId(orderProd.getProdSkuId());
					if(prodImg == null) {
						prodImg = prodImgService.findFirstProdImgByProdSkuId(String.valueOf(orderProd.getProdId()));
					}
					orderProd.setProdImgUrl(prodImg.getImgUrl());
					if(!orderProd.getProdSkuId().equals(String.valueOf(orderProd.getProdId()))) {
						List<RcOrderProdSkuPropInfo> orderProdSkuPropInfos = orderProdSkuPropInfoService.findOrderProdSkuPropInfosByOrderProdId(orderProd.getOrderProdId());
						orderProd.setOrderProdSkuPropInfos(orderProdSkuPropInfos);
					}
					try {
						System.out.println(new ObjectMapper().writeValueAsString(orderProd));
					} catch (JsonProcessingException e1) {
						e1.printStackTrace();
					}
					final RcOrderProd pushObj = orderProd;
					new Thread(new Runnable() {
					    @Override
					    public void run() {
					    	try {
								mailService.sendMail(mailVo);

//							RcOrderProd orderProd = (RcOrderProd) mailVo.getContext().get("orderProd");
//							orderProd.getProd().getProdStorage().setWarningStatus((byte) 1);
//							prodStorageService.modifyProdStorages(orderProd.getProd().getProdStorage());
							} catch (AddressException e) {
								e.printStackTrace();
							} catch (GeneralSecurityException e) {
								e.printStackTrace();
							} catch (MessagingException e) {
								e.printStackTrace();
							}
					    }
					}).start();
                    // 推送"商品库存不足"
                    try {
                        pushService.prodInsufficient(pushObj);
                    } catch (SystemException e) {
                        e.printStackTrace();
                    }
//					storage.setWarningStatus(YesOrNoEnum.YES.getValue());
				}
				
//				prodStorageService.modifyProdStorages(storage);
			}
			
			final MailVo mailVo = new MailVo();
			mailVo.setVelocity("order.vm");
//				mailVo.setHttpServletRequest(httpServletRequest);
			mailVo.setSubject("one order sucess");
			mailVo.setTo(merchant.getEmail());
			mailVo.getContext().put("order", order);
			
			order.setMerchant(merchantService.findMerchantById(order.getMerchantId()));
			order.setMember(buyerService.findBuyerById(order.getMemberId()));
			order.setOrderProds(orderProdService.findOrderProdsByOrderId(orderId));
			order.setOrderDelivery(orderDeliveryService.findOrderDeliveryById(orderId));
			order.setOrderConsignee(orderConsigneeService.findOrderConsigneeById(order.getOrderId()));
			for(RcOrderProd orderProd : order.getOrderProds()) {
				orderProd.setProd(prodService.findProdById(orderProd.getProdId()));
				RcProdImg prodImg = prodImgService.findFirstProdImgByProdSkuId(orderProd.getProdSkuId());
				if(prodImg == null) {
					prodImg = prodImgService.findFirstProdImgByProdSkuId(String.valueOf(orderProd.getProdId()));
				}
				orderProd.setProdImgUrl(prodImg.getImgUrl());
				if(!orderProd.getProdSkuId().equals(String.valueOf(orderProd.getProdId()))) {
					List<RcOrderProdSkuPropInfo> orderProdSkuPropInfos = orderProdSkuPropInfoService.findOrderProdSkuPropInfosByOrderProdId(orderProd.getOrderProdId());
					orderProd.setOrderProdSkuPropInfos(orderProdSkuPropInfos);
				}
			}
			order.setCouponAmount(new BigDecimal(0));
			order.setFreight(new BigDecimal(0));
			order.setCreditCardFee(new BigDecimal(0));
			List<RcOrderFee> orderFees = orderFeeService.findOrderFeesByOrderId(orderId);
			for(RcOrderFee orderFee : orderFees) {
				if(orderFee.getType() == OrderFeeTypeEnum.COUPON.getValue()) {
					order.setCouponAmount(orderFee.getAmount());
				}
				if(orderFee.getType() == OrderFeeTypeEnum.FREIGHT.getValue()) {
					order.setFreight(orderFee.getAmount());
				}
				if(orderFee.getType() == OrderFeeTypeEnum.CREDIT_CARD_FEE.getValue()) {
					order.setCreditCardFee(orderFee.getAmount());
				}
			}
			try {
				System.out.println(new ObjectMapper().writeValueAsString(order));
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}
			new Thread(new Runnable() {
			    @Override
			    public void run() {
			    	try {
						mailService.sendMail(mailVo);
					} catch (AddressException e) {
						logger.error("AddressException",e);
						e.printStackTrace();
					} catch (GeneralSecurityException e) {
						e.printStackTrace();
					} catch (MessagingException e) {
						e.printStackTrace();
					}
			    }
			}).start();

            try {
                // 通知卖家 "订单已支付"
                pushService.paySuccess(order);
            } catch (SystemException e) {
                e.printStackTrace();
            }

		}
	}

	@Override
	public List<RcOrder> findPagedSellerOrdersByMerchantIdAndBuyerId(
			int merchantId, int buyerId, Page page) {
		PageContext.setPage(page);
		RcOrder param = new RcOrder();
		param.setMerchantId(merchantId);
		param.setMemberId(buyerId);
		param.setOrderBy("CREATE_TIME DESC");
		List<RcOrder> orders = findOrders(param);
		for (RcOrder order : orders) {
			long orderId = order.getOrderId();
			VOConversionUtil.Entity2VO(order,
					new String[] { "orderId", "sellerStatus", "buyerMemo", "buyerStatus", "createTime", "freight",
							"merchantId", "productPrice", "payPrice", "memberId", "paymentInterface",
							"paymentType", "pickupCode", "prodPrice", "amount", "payCode" },
					null);
			List<RcOrderProd> orderProds = orderProdService.findOrderProdsByOrderId(orderId);
			for (RcOrderProd orderProd : orderProds) {
				VOConversionUtil.Entity2VO(orderProd,
						new String[] { "prodId", "quantity", "prodPrice", "prodSkuId", "orderProdId", "amount" }, null);
			}
			order.setOrderProds(orderProds);
			for (RcOrderProd orderProd : order.getOrderProds()) {
				RcProd prod = prodService.findProdById(orderProd.getProdId());
				orderProd.setProdName(prod.getName());
				RcProdImg prodImg = prodImgService.findFirstProdImgByProdSkuId(orderProd.getProdSkuId());
				if (prodImg == null) {
					prodImg = prodImgService.findFirstProdImgByProdSkuId(String.valueOf(orderProd.getProdId()));
				}
				orderProd.setProdImgUrl(prodImg.getImgUrl());
				if (!orderProd.getProdSkuId().equals(String.valueOf(orderProd.getProdId()))) {
					List<RcOrderProdSkuPropInfo> orderProdSkuPropInfos = orderProdSkuPropInfoService
							.findOrderProdSkuPropInfosByOrderProdId(orderProd.getOrderProdId());
					List<String> prodPropVals = new ArrayList<String>();
					for (RcOrderProdSkuPropInfo orderProdSkuPropInfo : orderProdSkuPropInfos) {
						prodPropVals.add(orderProdSkuPropInfo.getProdPropVal());
					}
					orderProd.setProdPropVals(prodPropVals);
				}
			}
			order.setCouponAmount(new BigDecimal(0));
			order.setFreight(new BigDecimal(0));
			order.setCreditCardFee(new BigDecimal(0));
			List<RcOrderFee> orderFees = orderFeeService.findOrderFeesByOrderId(orderId);
			for(RcOrderFee orderFee : orderFees) {
				if(orderFee.getType() == OrderFeeTypeEnum.COUPON.getValue()) {
					order.setCouponAmount(orderFee.getAmount());
				}
				if(orderFee.getType() == OrderFeeTypeEnum.FREIGHT.getValue()) {
					order.setFreight(orderFee.getAmount());
				}
				if(orderFee.getType() == OrderFeeTypeEnum.CREDIT_CARD_FEE.getValue()) {
					order.setCreditCardFee(orderFee.getAmount());
				}
			}
		}
		return orders;
	}

	@Override
	public List<RcOrder> findPagedSellerOrdersByMerchantIdAndKeyword(
			int merchantId, String keyword, Page page) {
		RcProd prodParam = new RcProd();
		prodParam.setName(keyword);
		List<RcProd> prods = prodService.findProds(prodParam);
		List<Integer> prodIds = new ArrayList<Integer>();
		for (RcProd prod : prods) {
			prodIds.add(prod.getProdId());
		}
		List<Long> orderIds = new ArrayList<Long>();
		if (org.apache.commons.collections.CollectionUtils.isNotEmpty(prodIds)) {
			RcOrderProd orderProdParam = new RcOrderProd();
			orderProdParam.setProdIds(prodIds);
			List<RcOrderProd> orderProds = orderProdService.findOrderProds(orderProdParam);
			for (RcOrderProd orderProd : orderProds) {
				orderIds.add(orderProd.getOrderId());
			}
		}
		Long orderId = null;
		try {
			orderId = Long.valueOf(keyword);
		} catch (Exception e) {
		}
		if (orderId != null) {
			orderIds.add(orderId);
		}
		if (CollectionUtils.isEmpty(orderIds)) {
			return new ArrayList<RcOrder>();
		}
		PageContext.setPage(page);
		RcOrder param = new RcOrder();
		param.setMerchantId(merchantId);
		param.setOrderIds(orderIds);
		param.setOrderBy("CREATE_TIME DESC");
		List<RcOrder> orders = findOrders(param);
		for (RcOrder rcOrder : orders) {
			VOConversionUtil.Entity2VO(rcOrder,
					new String[] { "orderId", "sellerStatus", "buyerMemo", "buyerStatus", "createTime", "freight",
							"merchantId", "productPrice", "payPrice", "memberId", "paymentInterface",
							"paymentType", "pickupCode", "prodPrice", "amount", "payCode" },
					null);
			List<RcOrderProd> orderProds = orderProdService.findOrderProdsByOrderId(rcOrder.getOrderId());
			for (RcOrderProd orderProd : orderProds) {
				VOConversionUtil.Entity2VO(orderProd,
						new String[] { "prodId", "quantity", "prodPrice", "prodSkuId", "orderProdId", "amount" }, null);
			}
			rcOrder.setOrderProds(orderProds);
			for (RcOrderProd orderProd : rcOrder.getOrderProds()) {
				RcProd prod = prodService.findProdById(orderProd.getProdId());
				orderProd.setProdName(prod.getName());
				RcProdImg prodImg = prodImgService.findFirstProdImgByProdSkuId(orderProd.getProdSkuId());
				if (prodImg == null) {
					prodImg = prodImgService.findFirstProdImgByProdSkuId(String.valueOf(orderProd.getProdId()));
				}
				orderProd.setProdImgUrl(prodImg.getImgUrl());
				if (!orderProd.getProdSkuId().equals(String.valueOf(orderProd.getProdId()))) {
					List<RcOrderProdSkuPropInfo> orderProdSkuPropInfos = orderProdSkuPropInfoService
							.findOrderProdSkuPropInfosByOrderProdId(orderProd.getOrderProdId());
					List<String> prodPropVals = new ArrayList<String>();
					for (RcOrderProdSkuPropInfo orderProdSkuPropInfo : orderProdSkuPropInfos) {
						prodPropVals.add(orderProdSkuPropInfo.getProdPropVal());
					}
					orderProd.setProdPropVals(prodPropVals);
				}
			}
			rcOrder.setCouponAmount(new BigDecimal(0));
			rcOrder.setFreight(new BigDecimal(0));
			rcOrder.setCreditCardFee(new BigDecimal(0));
			List<RcOrderFee> orderFees = orderFeeService.findOrderFeesByOrderId(orderId);
			for(RcOrderFee orderFee : orderFees) {
				if(orderFee.getType() == OrderFeeTypeEnum.COUPON.getValue()) {
					rcOrder.setCouponAmount(orderFee.getAmount());
				}
				if(orderFee.getType() == OrderFeeTypeEnum.FREIGHT.getValue()) {
					rcOrder.setFreight(orderFee.getAmount());
				}
				if(orderFee.getType() == OrderFeeTypeEnum.CREDIT_CARD_FEE.getValue()) {
					rcOrder.setCreditCardFee(orderFee.getAmount());
				}
			}
		}
		return orders;
	}

	@Override
	public OrderNumVO findSellerOrderNumByMerchantId(int merchantId) {
		OrderNumVO orderNum = new OrderNumVO();
		int unpaid = 0;
		int undelivered = 0;
		int send = 0;
		int unreply = 0;
		RcOrder param = new RcOrder();
		param.setMerchantId(merchantId);
		List<RcOrder> orders = orderMapper.select(param);
		for (RcOrder order : orders) {
			if (order.getSellerStatus() == OrderSellerStatusEnum.UNPAID.getValue()) {
				unpaid++;
			} else if (order.getSellerStatus() == OrderSellerStatusEnum.UNDELIVERED.getValue()) {
				undelivered++;
			} else if (order.getSellerStatus() == OrderSellerStatusEnum.DELIVERED.getValue()) {
				send++;
			} else if (order.getSellerStatus() == OrderSellerStatusEnum.UNREPLIED.getValue()) {
				unreply++;
			}
		}
		orderNum.setUnpaid(unpaid);
		orderNum.setUndelivered(undelivered);
		orderNum.setSend(send);
		orderNum.setUnreply(unreply);
		return orderNum;
	}
	
	@Override
	public RcOrder findSellerOrderById(long orderId) {
		RcOrder order = findOrderById(orderId);
		List<RcOrderProd> orderProds = orderProdService.findOrderProdsByOrderId(orderId);
		for (RcOrderProd orderProd : orderProds) {
			VOConversionUtil.Entity2VO(orderProd,
					new String[] { "prodId", "quantity", "prodPrice", "prodSkuId", "orderProdId", "amount" }, null);
			RcOrderProdEvaluation orderProdEvaluation = orderProdEvaluationService.findOrderProdEvaluationById(orderProd.getOrderProdId());
			if(orderProdEvaluation!=null){
				orderProd.setScore(orderProdEvaluation.getScore());
				orderProd.setMerchantReply(orderProdEvaluation.getMerchantReply());
				orderProd.setContent(orderProdEvaluation.getContent());
			}
		}
		RcOrderConsignee orderConsignee = orderConsigneeService.findOrderConsigneeById(orderId);
		RcCity city = cityService.findCityById(orderConsignee.getCityId());
		VOConversionUtil.Entity2VO(orderConsignee, null, new String[] { "orderId", "cityId" });
		orderConsignee.setProvinceName(city.getProvinceName());
		orderConsignee.setCityName(city.getName());
		RcOrderCoupon orderCoupon = orderCouponService.findOrderCouponByOrderId(orderId);
		List<RcOrderOperation> orderOperations = orderOperationService.findOrderOperationsByOrderId(orderId);
		VOConversionUtil.Entity2VO(order,
				new String[] { "orderId", "sellerStatus", "buyerMemo","sellerMemo", "buyerStatus", "createTime", "freight",
						"merchantId", "productPrice", "payPrice", "memberId", "paymentInterface",
						"paymentType", "pickupCode", "prodPrice", "amount", "payCode" },
				null);
		//发货信息
	    RcOrderDelivery orderDelivery = orderDeliveryService.findOrderDeliveryById(orderId);
	    RcOrderEvaluation orderEvaluation = orderEvaluationService.findOrderEvaluationById(orderId);
	    if(orderEvaluation!=null){
		    order.setDeliveryScore(orderEvaluation.getDeliveryScore());
		    order.setServiceScore(orderEvaluation.getServiceScore());
	    }
	    order.setOrderDelivery(orderDelivery);
	    if(order.getSellerStatus()==OrderSellerStatusEnum.UNPAID.getValue()){
	    	Long countDown = (order.getCreateTime().getTime()+propertyConstants.orderCancelTime*60*60*1000-new Date().getTime())/1000/60;
	    /*	if(countDown<=0){
	    		rcOrder.setSellerStatus(RcOrderSellerStatusEnum.CANCELED.getValue());
	    		rcOrder.setCountDown(null);
	    	}else{*/
	    	order.setCountDown(countDown);
	    	//}
	    	
	    }
		order.setOrderProds(orderProds);
		order.setOrderConsignee(orderConsignee);
		if (orderCoupon != null) {
			order.setCouponId(orderCoupon.getCouponId());
		}
		order.setMerchantName(merchantService.findMerchantById(order.getMerchantId()).getName());
		for (RcOrderProd orderProd : order.getOrderProds()) {
			RcProd prod = prodService.findProdById(orderProd.getProdId());
			orderProd.setProdName(prod.getName());
			RcProdImg prodImg = prodImgService.findFirstProdImgByProdSkuId(orderProd.getProdSkuId());
			if (prodImg == null) {
				prodImg = prodImgService.findFirstProdImgByProdSkuId(String.valueOf(orderProd.getProdId()));
			}
			orderProd.setProdImgUrl(prodImg.getImgUrl());
			if (!orderProd.getProdSkuId().equals(String.valueOf(orderProd.getProdId()))) {
				List<RcOrderProdSkuPropInfo> orderProdSkuPropInfos = orderProdSkuPropInfoService
						.findOrderProdSkuPropInfosByOrderProdId(orderProd.getOrderProdId());
				List<String> prodPropVals = new ArrayList<String>();
				for (RcOrderProdSkuPropInfo orderProdSkuPropInfo : orderProdSkuPropInfos) {
					prodPropVals.add(orderProdSkuPropInfo.getProdPropVal());
				}
				orderProd.setProdPropVals(prodPropVals);
			}
		}
		for (RcOrderOperation orderOperation : orderOperations) {
			if (orderOperation.getOperationType() == OrderOperationTypeEnum.PAY.getValue()
					|| orderOperation.getOperationType() == OrderOperationTypeEnum.PAYMENT_CONFIRM.getValue()) {
				order.setPayTime(orderOperation.getOperationTime());
			} else if (orderOperation.getOperationType() == OrderOperationTypeEnum.CANCEL.getValue()) {
				order.setCancelTime(orderOperation.getOperationTime());
			} else if (orderOperation.getOperationType() == OrderOperationTypeEnum.REFUND.getValue()) {
				order.setRefundTime(orderOperation.getOperationTime());
				order.setOrderRejected(orderOperation.getOperationMemo());
			}
		}
		order.setCouponAmount(new BigDecimal(0));
		order.setFreight(new BigDecimal(0));
		order.setCreditCardFee(new BigDecimal(0));
		List<RcOrderFee> orderFees = orderFeeService.findOrderFeesByOrderId(orderId);
		for(RcOrderFee orderFee : orderFees) {
			if(orderFee.getType() == OrderFeeTypeEnum.COUPON.getValue()) {
				order.setCouponAmount(orderFee.getAmount());
			}
			if(orderFee.getType() == OrderFeeTypeEnum.FREIGHT.getValue()) {
				order.setFreight(orderFee.getAmount());
			}
			if(orderFee.getType() == OrderFeeTypeEnum.CREDIT_CARD_FEE.getValue()) {
				order.setCreditCardFee(orderFee.getAmount());
			}
		}
		return order;
	}
	public RcOrder findBuyerOrderById(long orderId) {
		     RcOrder order = findOrderById(orderId);
			List<RcOrderProd> orderProds = orderProdService.findOrderProdsByOrderId(orderId);
			for(RcOrderProd orderProd : orderProds) {
				VOConversionUtil.Entity2VO(orderProd, new String[] {"prodId", "quantity", "prodPrice", "prodSkuId","orderProdId",
						"amount"}, null);
			}
			RcOrderConsignee orderConsignee = orderConsigneeService.findOrderConsigneeById(orderId);
			RcCity city = cityService.findCityById(orderConsignee.getCityId());
			VOConversionUtil.Entity2VO(orderConsignee, null, new String[] {"orderId", "cityId"});
			orderConsignee.setProvinceName(city.getProvinceName());
			orderConsignee.setCityName(city.getName());
			RcOrderCoupon orderCoupon = orderCouponService.findOrderCouponByOrderId(orderId);
			List<RcOrderOperation> orderOperations = orderOperationService.findOrderOperationsByOrderId(orderId);
			VOConversionUtil.Entity2VO(order, new String[] {"orderId", "sellerStatus","buyerMemo", "buyerStatus", "createTime","freight","merchantId",
					"productPrice", "payPrice", "memberId", "paymentInterface", "paymentType",
					"pickupCode", "prodPrice", "amount", "payCode", "couponAmount"}, null);
			if(order.getSellerStatus() == OrderSellerStatusEnum.DELIVERED.getValue()){
				order.setDeliveryStatus(true);
			}
			order.setOrderProds(orderProds);
			order.setOrderConsignee(orderConsignee);
			if(orderCoupon != null){
				order.setCouponId(orderCoupon.getCouponId());
			}
			order.setMerchantName(merchantService.findMerchantById(order.getMerchantId()).getName());
			for(RcOrderProd orderProd : order.getOrderProds()) {
				RcProd prod = prodService.findProdById(orderProd.getProdId());
				orderProd.setProdName(prod.getName());
				RcProdImg prodImg = prodImgService.findFirstProdImgByProdSkuId(orderProd.getProdSkuId());
				if(prodImg == null) {
					prodImg = prodImgService.findFirstProdImgByProdSkuId(String.valueOf(orderProd.getProdId()));
				}
				orderProd.setProdImgUrl(prodImg.getImgUrl());
				if(!orderProd.getProdSkuId().equals(String.valueOf(orderProd.getProdId()))) {
					List<RcOrderProdSkuPropInfo> orderProdSkuPropInfos = orderProdSkuPropInfoService.findOrderProdSkuPropInfosByOrderProdId(orderProd.getOrderProdId());
					List<String> prodPropVals = new ArrayList<String>();
					for(RcOrderProdSkuPropInfo orderProdSkuPropInfo : orderProdSkuPropInfos) {
						prodPropVals.add(orderProdSkuPropInfo.getProdPropVal());
					}
					orderProd.setProdPropVals(prodPropVals);
				}
			}
			order.setCouponAmount(new BigDecimal(0));
			order.setFreight(new BigDecimal(0));
			order.setCreditCardFee(new BigDecimal(0));
			List<RcOrderFee> orderFees = orderFeeService.findOrderFeesByOrderId(orderId);
			for(RcOrderFee orderFee : orderFees) {
				if(orderFee.getType() == OrderFeeTypeEnum.COUPON.getValue()) {
					order.setCouponAmount(orderFee.getAmount());
				}
				if(orderFee.getType() == OrderFeeTypeEnum.FREIGHT.getValue()) {
					order.setFreight(orderFee.getAmount());
				}
				if(orderFee.getType() == OrderFeeTypeEnum.CREDIT_CARD_FEE.getValue()) {
					order.setCreditCardFee(orderFee.getAmount());
				}
			}
			for(RcOrderOperation orderOperation : orderOperations) {
				if(orderOperation.getOperationType() == OrderOperationTypeEnum.PAY.getValue() ||
						orderOperation.getOperationType() == OrderOperationTypeEnum.PAYMENT_CONFIRM.getValue()) {
					order.setPayTime(orderOperation.getOperationTime());
				} else if(orderOperation.getOperationType() == OrderOperationTypeEnum.CANCEL.getValue()) {
					order.setCancelTime(orderOperation.getOperationTime());
					if(orderOperation.getMemberId() == 0) {
						order.setCanceledBySystem(true);
					} else {
						order.setCanceledBySystem(false);
					}
				}else if(orderOperation.getOperationType() == OrderOperationTypeEnum.REFUND.getValue()){
					order.setRefundTime(orderOperation.getOperationTime());
					order.setOrderRejected(orderOperation.getOperationMemo());
				}
			}

		return order;
	}

//	@Override
//	public List<RcOrder> findOrders(Long orderId, Integer buyerStatus,
//			Integer merchantId, String merchantName, Integer vendorId,
//			String mobile, Date createTimeFrom, Date createTimeTo,
//			String cityName, List<Integer> sellerStatuses, String orderBy) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public List<RcOrder> findPagedOrders(Long orderId, Integer buyerStatus,
//			Integer merchantId, String merchantName, Integer vendorId,
//			String mobile, Date createTimeFrom, Date createTimeTo,
//			String cityName, List<Integer> sellerStatuses, String orderBy,
//			Page page) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
