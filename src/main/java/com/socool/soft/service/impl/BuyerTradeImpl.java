package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcBuyerTrade;
import com.socool.soft.bo.RcProd;
import com.socool.soft.dao.RcBuyerTradeMapper;
import com.socool.soft.service.IBuyerTradeService;
import com.socool.soft.service.IOrderProdService;
import com.socool.soft.util.VOConversionUtil;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class BuyerTradeImpl implements IBuyerTradeService {
	@Autowired
	private RcBuyerTradeMapper memberTradeMapper;
	@Autowired
	private IOrderProdService orderProdService;

	@Override
	public long addBuyerTrade(RcBuyerTrade buyerTrade) {
		buyerTrade.setCreateTime(new Date());
		if(memberTradeMapper.insert(buyerTrade) > 0) {
			return buyerTrade.getBuyerTradeId();
		}
		return 0;
	}

	@Override
	public List<RcBuyerTrade> findBuyerTradesByBuyerId(int buyerId) {
		RcBuyerTrade param = new RcBuyerTrade();
		param.setOrderBy("BUYER_TRADE_ID DESC");
		param.setBuyerId(buyerId);
		return memberTradeMapper.select(param);
	}

	@Override
	public List<RcBuyerTrade> findPagedBuyerTradesByBuyerIdForAndroid(int buyerId, Page page) {
		PageContext.setPage(page);
		RcBuyerTrade param = new RcBuyerTrade();
		param.setOrderBy("BUYER_TRADE_ID DESC");
		param.setBuyerId(buyerId);
		List<RcBuyerTrade> buyerTrades =  memberTradeMapper.select(param);
		for(RcBuyerTrade buyerTrade : buyerTrades) {
			List<String> prodNames = new ArrayList<String>();
			if(buyerTrade.getOrderId() != null) {
				List<RcProd> prods = orderProdService.findProdsByOrderId(buyerTrade.getOrderId());
				if(!CollectionUtils.isEmpty(prods)) {
					for(RcProd prod : prods) {
						prodNames.add(prod.getName());
					}
				}
			}
			VOConversionUtil.Entity2VO(buyerTrade, new String[] {"amount", "balance", "type", "createTime"}, null);
			buyerTrade.setProdNames(prodNames);
		}
	
		return buyerTrades;
	}

//	@Override
//	public List<RcBuyerTrade> findMemberTradesByType(int type) {
//		RcBuyerTrade param = new RcBuyerTrade();
//		param.setType(type);
//		return memberTradeMapper.select(param);
//	}

//	@Override
//	public List<RcBuyerTrade> findMemberTradesByPayment(int paymentInterface,
//			Integer paymentType) {
//		RcBuyerTrade param = new RcBuyerTrade();
//		param.setPaymentInterface(paymentInterface);
//		param.setPaymentType(paymentType);
//		return memberTradeMapper.select(param);
//	}
}
