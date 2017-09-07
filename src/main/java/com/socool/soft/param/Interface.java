package com.socool.soft.param;

public class Interface {
	private String data;
	private String timestamp;//时间戳
	private String nonceStr;//随机数
	private String product;
	private String signature;//签名
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
