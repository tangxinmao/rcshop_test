package com.socool.soft.service;

import net.sf.json.JSONArray;

import com.socool.soft.exception.SystemException;
import com.socool.soft.vo.newvo.android.ShoppingCartVO;

public interface IShoppingCartService {

	/**
	 * 添加购物车
	 * @param memberId
	 * @param prodId
	 * @param prodSkuId
	 * @param prodNum 添加数量
	 * @return
	 */
	boolean addShoppingCartProd(int memberId, int prodId, String prodSkuId, int quantity) throws SystemException;
	
	/**
	 * 查看购物车
	 * @param memberId
	 * @param cityName
	 * @return
	 */
	ShoppingCartVO findShoppingCart(int memberId) throws SystemException;
	
	/**
	 * 计算购物车总数
	 * @param memberId
	 * @return
	 */
	int countShoppingCart(int memberId) throws SystemException;
	
	/**
	 * 编辑购物车
	 * @param json
	 * @return
	 */
	boolean modifyShoppingCart(int memberId, JSONArray cart) throws SystemException;
}
