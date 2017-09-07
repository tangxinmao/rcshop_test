package com.socool.soft.vo.newvo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ReportVO {

	private BigDecimal totalOrderAmount;
	private Integer totalOrderCount;
	private BigDecimal orderAmountPerCustomer;
	private BigDecimal orderAmountPerProduct;
	private Integer customerCount;
	private Integer productCount;
	private Integer allProductCount;
	private Integer newProductCount;
	private Integer allCustomerCount;
	private Integer newCustomerCount;
	private Integer allMerchantCount;
	private Integer newMerchantCount;
	private BigDecimal rechargeAmount;
	private Integer rechargeCount;
	private Map<String, BigDecimal> orderAmountDetail;
	private List<ReportProductVO> topProducts;
	private List<ReportMerchantVO> topMerchants;

	public BigDecimal getTotalOrderAmount() {
		return totalOrderAmount;
	}

	public void setTotalOrderAmount(BigDecimal totalOrderAmount) {
		this.totalOrderAmount = totalOrderAmount;
	}

	public Integer getTotalOrderCount() {
		return totalOrderCount;
	}

	public void setTotalOrderCount(Integer totalOrderCount) {
		this.totalOrderCount = totalOrderCount;
	}

	public BigDecimal getOrderAmountPerCustomer() {
		return orderAmountPerCustomer;
	}

	public void setOrderAmountPerCustomer(BigDecimal orderAmountPerCustomer) {
		this.orderAmountPerCustomer = orderAmountPerCustomer;
	}

	public BigDecimal getOrderAmountPerProduct() {
		return orderAmountPerProduct;
	}

	public void setOrderAmountPerProduct(BigDecimal orderAmountPerProduct) {
		this.orderAmountPerProduct = orderAmountPerProduct;
	}

	public Integer getCustomerCount() {
		return customerCount;
	}

	public void setCustomerCount(Integer customerCount) {
		this.customerCount = customerCount;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public Integer getAllProductCount() {
		return allProductCount;
	}

	public void setAllProductCount(Integer allProductCount) {
		this.allProductCount = allProductCount;
	}

	public Integer getNewProductCount() {
		return newProductCount;
	}

	public void setNewProductCount(Integer newProductCount) {
		this.newProductCount = newProductCount;
	}

	public Integer getAllCustomerCount() {
		return allCustomerCount;
	}

	public void setAllCustomerCount(Integer allCustomerCount) {
		this.allCustomerCount = allCustomerCount;
	}

	public Integer getNewCustomerCount() {
		return newCustomerCount;
	}

	public void setNewCustomerCount(Integer newCustomerCount) {
		this.newCustomerCount = newCustomerCount;
	}

	public Integer getAllMerchantCount() {
		return allMerchantCount;
	}

	public void setAllMerchantCount(Integer allMerchantCount) {
		this.allMerchantCount = allMerchantCount;
	}

	public Integer getNewMerchantCount() {
		return newMerchantCount;
	}

	public void setNewMerchantCount(Integer newMerchantCount) {
		this.newMerchantCount = newMerchantCount;
	}

	public BigDecimal getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(BigDecimal rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public Integer getRechargeCount() {
		return rechargeCount;
	}

	public void setRechargeCount(Integer rechargeCount) {
		this.rechargeCount = rechargeCount;
	}

	public Map<String, BigDecimal> getOrderAmountDetail() {
		return orderAmountDetail;
	}

	public void setOrderAmountDetail(Map<String, BigDecimal> orderAmountDetail) {
		this.orderAmountDetail = orderAmountDetail;
	}

	public List<ReportProductVO> getTopProducts() {
		return topProducts;
	}

	public void setTopProducts(List<ReportProductVO> topProducts) {
		this.topProducts = topProducts;
	}

	public List<ReportMerchantVO> getTopMerchants() {
		return topMerchants;
	}

	public void setTopMerchants(List<ReportMerchantVO> topMerchants) {
		this.topMerchants = topMerchants;
	}
}
