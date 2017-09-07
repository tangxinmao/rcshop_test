package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcBuyerTrade;
import com.socool.soft.vo.Page;

public interface IBuyerTradeService {
	/**
	 * 保存交易信息
	 * 
	 * @param rcMemberTrade
	 * @return
	 */
	long addBuyerTrade(RcBuyerTrade buyerTrade);

	/**
	 * 查询memberTrade
	 * 
	 * @param buyerId
	 * @return
	 */
	List<RcBuyerTrade> findBuyerTradesByBuyerId(int buyerId);
	
	List<RcBuyerTrade> findPagedBuyerTradesByBuyerIdForAndroid(int buyerId, Page page);
	
//	List<RcBuyerTrade> findMemberTradesByType(int type);
	
//	List<RcBuyerTrade> findMemberTradesByPayment(int paymentInterface, Integer paymentType);
}
