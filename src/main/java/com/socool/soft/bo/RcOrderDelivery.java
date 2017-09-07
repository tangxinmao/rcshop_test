package com.socool.soft.bo;

import java.io.Serializable;
import java.util.Date;

public class RcOrderDelivery extends QueryParam implements Serializable {

	private static final long serialVersionUID = 4514414628397146279L;
	
	// db
	private Long orderId;
	private String trackingNumber;
	private String logisticsCompany;
	private Integer deliveryType;
	private Date deliveryTime;
	private String dispatcherMobile;
	private String dispatcherName;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getLogisticsCompany() {
		return logisticsCompany;
	}

	public void setLogisticsCompany(String logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getDispatcherMobile() {
		return dispatcherMobile;
	}

	public void setDispatcherMobile(String dispatcherMobile) {
		this.dispatcherMobile = dispatcherMobile;
	}

	public String getDispatcherName() {
		return dispatcherName;
	}

	public void setDispatcherName(String dispatcherName) {
		this.dispatcherName = dispatcherName;
	}
}