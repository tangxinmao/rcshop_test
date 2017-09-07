package com.socool.soft.vo;

import java.util.List;

public class SendingData {
 private String apikey;
 private String callbackurl;
 private Integer globalstatus;
 private String Balance;
 private String globalstatustext;
 private String Expired;
 private Integer sendingid;
 private String deliverystatus;
 private String number;
 private String deliverystatustext;
 private List<Datapacket> datapacket;
public String getApikey() {
	return apikey;
}

public void setApikey(String apikey) {
	this.apikey = apikey;
}

public String getCallbackurl() {
	return callbackurl;
}

public void setCallbackurl(String callbackurl) {
	this.callbackurl = callbackurl;
}

public List<Datapacket> getDatapacket() {
	return datapacket;
}

public void setDatapacket(List<Datapacket> datapacket) {
	this.datapacket = datapacket;
}

public Integer getGlobalstatus() {
	return globalstatus;
}

public void setGlobalstatus(Integer globalstatus) {
	this.globalstatus = globalstatus;
}

public String getGlobalstatustext() {
	return globalstatustext;
}

public void setGlobalstatustext(String globalstatustext) {
	this.globalstatustext = globalstatustext;
}

public String getBalance() {
	return Balance;
}

public void setBalance(String balance) {
	Balance = balance;
}

public String getExpired() {
	return Expired;
}

public void setExpired(String expired) {
	Expired = expired;
}


public String getDeliverystatus() {
	return deliverystatus;
}

public void setDeliverystatus(String deliverystatus) {
	this.deliverystatus = deliverystatus;
}

public String getDeliverystatustext() {
	return deliverystatustext;
}

public void setDeliverystatustext(String deliverystatustext) {
	this.deliverystatustext = deliverystatustext;
}

public String getNumber() {
	return number;
}

public void setNumber(String number) {
	this.number = number;
}

public Integer getSendingid() {
	return sendingid;
}

public void setSendingid(Integer sendingid) {
	this.sendingid = sendingid;
}


}
