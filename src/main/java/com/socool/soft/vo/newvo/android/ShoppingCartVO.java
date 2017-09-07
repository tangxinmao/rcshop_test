package com.socool.soft.vo.newvo.android;

import java.util.List;

import com.socool.soft.bo.RcShoppingCartOrder;

public class ShoppingCartVO {

	private int count;
	private List<RcShoppingCartOrder> orders;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<RcShoppingCartOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<RcShoppingCartOrder> orders) {
		this.orders = orders;
	}
}
