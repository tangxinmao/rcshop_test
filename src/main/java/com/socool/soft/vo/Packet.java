package com.socool.soft.vo;

public class Packet {
private String number;
private String message;
private String sendingdatetime;
private Integer sendingid;
private Integer sendingstatus;
private Integer price;
public String getNumber() {
	return number;
}
public void setNumber(String number) {
	this.number = number;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public String getSendingdatetime() {
	return sendingdatetime;
}
public void setSendingdatetime(String sendingdatetime) {
	this.sendingdatetime = sendingdatetime;
}
public Integer getSendingid() {
	return sendingid;
}
public void setSendingid(Integer sendingid) {
	this.sendingid = sendingid;
}
public Integer getSendingstatus() {
	return sendingstatus;
}
public void setSendingstatus(Integer sendingstatus) {
	this.sendingstatus = sendingstatus;
}
public Integer getPrice() {
	return price;
}
public void setPrice(Integer price) {
	this.price = price;
}

}
