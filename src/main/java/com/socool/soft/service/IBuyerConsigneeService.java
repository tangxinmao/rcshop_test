package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcBuyerConsignee;

public interface IBuyerConsigneeService {
	/**
	 * 通过id查询收货地址信息
	 * 
	 * @param buyerConsigneeId
	 * @return
	 */
	RcBuyerConsignee findBuyerConsigneeById(int buyerConsigneeId);

	List<RcBuyerConsignee> findBuyerConsigneesByBuyerId(int buyerId);

	List<RcBuyerConsignee> findBuyerConsigneesByBuyerIdForAndroid(int buyerId);

	int saveBuyerConsignee(RcBuyerConsignee buyerConsignee);

	int addBuyerConsignee(RcBuyerConsignee buyerConsignee);

	int modifyBuyerConsignee(RcBuyerConsignee buyerConsignee);

	int removeBuyerConsignee(int buyerConsigneeId);

	int setDefault(int buyerConsigneeId);
}
