package com.socool.soft.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RcOrderProdEvaluation extends QueryParam implements Serializable {

	private static final long serialVersionUID = -3868601052205065218L;
	
	// db
	private Long orderProdId;
	private Integer prodId;
	private Date createTime;
	private Integer score;
	private Byte isAnoy;
	private Long orderId;
	private Integer memberId;
	private String merchantReply;
	private Byte delFlag;
	private String prodSkuId;
	private Integer merchantId;
	private Integer vendorId;
	private String content;
	private Integer merchantUserId;
	
	// rel
	private RcOrderProd orderProd;
	private RcProd prod;
	private RcOrder order;
	private RcMerchant merchant;
	private RcBuyer member;
	private RcProdSku prodSku;
	
	// vo
	private String memberName;
	
	// se
	private Date createTimeFrom;
	private Date createTimeTo;
	private List<String> prodSkuIds;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Byte getIsAnoy() {
		return isAnoy;
	}

	public void setIsAnoy(Byte isAnoy) {
		this.isAnoy = isAnoy;
	}

	public String getMerchantReply() {
		return merchantReply;
	}

	public void setMerchantReply(String merchantReply) {
		this.merchantReply = merchantReply;
	}

	public Byte getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Byte delFlag) {
		this.delFlag = delFlag;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getOrderProdId() {
		return orderProdId;
	}

	public void setOrderProdId(Long orderProdId) {
		this.orderProdId = orderProdId;
	}

	public RcOrderProd getOrderProd() {
		return orderProd;
	}

	public void setOrderProd(RcOrderProd orderProd) {
		this.orderProd = orderProd;
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public RcProd getProd() {
		return prod;
	}

	public void setProd(RcProd prod) {
		this.prod = prod;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public RcOrder getOrder() {
		return order;
	}

	public void setOrder(RcOrder order) {
		this.order = order;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public RcBuyer getMember() {
		return member;
	}

	public void setMember(RcBuyer member) {
		this.member = member;
	}

	public String getProdSkuId() {
		return prodSkuId;
	}

	public void setProdSkuId(String prodSkuId) {
		this.prodSkuId = prodSkuId;
	}

	public RcProdSku getProdSku() {
		return prodSku;
	}

	public void setProdSku(RcProdSku prodSku) {
		this.prodSku = prodSku;
	}

	public Date getCreateTimeFrom() {
		return createTimeFrom;
	}

	public void setCreateTimeFrom(Date createTimeFrom) {
		this.createTimeFrom = createTimeFrom;
	}

	public Date getCreateTimeTo() {
		return createTimeTo;
	}

	public void setCreateTimeTo(Date createTimeTo) {
		this.createTimeTo = createTimeTo;
	}

	public List<String> getProdSkuIds() {
		return prodSkuIds;
	}

	public void setProdSkuIds(List<String> prodSkuIds) {
		this.prodSkuIds = prodSkuIds;
	}

	public RcMerchant getMerchant() {
		return merchant;
	}

	public void setMerchant(RcMerchant merchant) {
		this.merchant = merchant;
	}

	public Integer getMerchantUserId() {
		return merchantUserId;
	}

	public void setMerchantUserId(Integer merchantUserId) {
		this.merchantUserId = merchantUserId;
	}
}