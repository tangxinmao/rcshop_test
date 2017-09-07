package com.socool.soft.service;

import com.socool.soft.bo.RcOrder;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.newvo.android.OrderNumVO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IOrderService {
	/**
	 * 查询order列表
	 * 
	 * @param order
	 * @return
	 */
	List<RcOrder> findOrders(RcOrder param);
	
	List<RcOrder> findPagedOrders(RcOrder param, Page page);

	/**
	 * 查询order
	 * 
	 * @param orderId
	 * @return
	 */

	RcOrder findOrderById(long orderId);

	/**
	 * 更新order
	 */

	long modifyOrder(RcOrder rcOrderDb);

	/**
	 * 通过订单id查询下面的商品 商品包含ProdProp属性值
	 * 
	 * @param
	 * @return
	 */

//	List<RcOrderProd> findOrderProdByOrderId(Long orderId);

	/**
	 * 保存订单
	 * 
	 * @param order
	 */
	long addOrder(RcOrder order);

	/**
	 * 批量保存订单
	 * 
	 * @param orders
	 */

//	void addOrders(List<RcOrder> orders);

	/**
	 * 批量修改订单
	 * 
	 * @param orders
	 */
	void modifyOrders(List<RcOrder> orders);

	/**
	 * 获取订单数量
	 * 
	 * @param memberId
	 * @return
	 */

	OrderNumVO findOrderNumByBuyerId(int buyerId);

	/*
	 * List<RcOrderVo> findOrderVOs(RcOrderVo rcOrderVo);
	 */

	/**
	 * 支付金额和订单创建时间排序
	 * sellerStatusList未 pc端传递参数
	 * @param orderId
	 * @param buyerStatus
	 * @param merchantId
	 * @param merchantName
	 * @param vendorId
	 * @param mobile
	 * @param createTimeStart
	 * @param createTimeEnd
	 * @param cityId
	 * @param list 
	 * @return
	 */
//	List<RcOrder> findOrders(Long orderId, Integer buyerStatus, Integer merchantId, String merchantName,
//			Integer vendorId, String mobile, Date createTimeFrom, Date createTimeTo, String cityName, List<Integer> sellerStatuses, String orderBy);
	
//	List<RcOrder> findPagedOrders(Long orderId, Integer buyerStatus, Integer merchantId, String merchantName,
//			Integer vendorId, String mobile, Date createTimeFrom, Date createTimeTo, String cityName, List<Integer> sellerStatuses, String orderBy, Page page);


	/**
	 * app接口获取
	 * 
	 * @param orderStatus
	 * @param memberId
	 * @return
	 */
	List<RcOrder> findPagedBuyerOrders(int buyerId, Integer buyerStatus, Page page);

	/**
	 * 获取用户所有订单
	 * 
	 * @param memberId
	 * @return
	 */
	List<RcOrder> findPagedOrdersByBuyerId(int buyerId, Page page);
	
	
	/**
	 * 计算某月订单收益总金额
	 * @param merchantId  商户ID
	 * @param calMonth 计算的月份 格式：yyyy-MM
	 * @return
	 */
	BigDecimal statisticOrderPayPrice(int merchantId, Date createTimeFrom, Date createTimeTo);
	
	/**
	 * 查询某月历史订单
	 * @param merchantId 商户ID
	 * @param calMonth 计算的月份 格式：yyyy-MM
	 * @return
	 */
	List<RcOrder> findOrderHistories(int merchantId, Date createTimeFrom, Date createTimeTo);
	
	List<RcOrder> findPagedOrderHistories(int merchantId, Date createTimeFrom, Date createTimeTo, Page page);

	void updateOrderInvalidTime(Integer cancelTime);

	void updateOrderCloseTime(Integer receiveServiceTime);
	
	/**
	 * 支付完成
	 * @param orderId
	 */
	void completePayment(long orderId, int paymentInteface, int paymentType);
	
	List<RcOrder> findPagedSellerOrders(int merchantId, Integer status, Page page);
	
	/**
	 * app通过merchatnId和memberId获取订单
	 * @param merchantId
	 * @param memberId
	 * @param pager
	 * @return
	 */
	List<RcOrder> findPagedSellerOrdersByMerchantIdAndBuyerId(int merchantId, int buyerId, Page page);
	
	/**
	 * app通过关键字查询商户订单
	 * @param merchantId
	 * @param keyword
	 * @param pager
	 * @return
	 */
	List<RcOrder> findPagedSellerOrdersByMerchantIdAndKeyword(int merchantId, String keyword, Page page);
	
	OrderNumVO findSellerOrderNumByMerchantId(int merchantId);
	
	RcOrder findSellerOrderById(long orderId);

	RcOrder findBuyerOrderById(long orderId);
}
