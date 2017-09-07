package com.socool.soft.vo;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProductPropListVO {
public static class ComplaintListProductVO{
		
		private String prodName;
		private String enumId;
		private String propSelfdf;
		public String getEnumId() {
			return enumId;
		}
		public void setEnumId(String enumId) {
			this.enumId = enumId;
		}
		private Map<String, Object> otherData;
		
		public String getProdName() {
			return prodName;
		}
		public void setProdName(String prodName) {
			this.prodName = prodName;
		}
		public String getPropSelfdf() {
			return propSelfdf;
		}
		public void setPropSelfdf(String propSelfdf) {
			this.propSelfdf = propSelfdf;
		}
		public ComplaintListProductVO(String prodName,String enumId,String propSelfdf) {
			super();
			this.prodName = prodName;
			this.enumId = enumId;
			this.propSelfdf = propSelfdf;
		}
		public Map<String, Object> getOtherData() {
			return otherData;
		}
		public void setOtherData(Map<String, Object> otherData) {
			this.otherData = otherData;
		}
		
	}
	
	
	private Map<String, Object> otherData;
	private List<ComplaintListProductVO> products = new ArrayList<ProductPropListVO.ComplaintListProductVO>();
	
	public Map<String, Object> getOtherData() {
		return otherData;
	}
	public void setOtherData(Map<String, Object> otherData) {
		this.otherData = otherData;
	}
	public List<ComplaintListProductVO> getProducts() {
		return products;
	}
	public void setProducts(List<ComplaintListProductVO> products) {
		this.products = products;
	}
}
