package com.socool.soft.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socool.soft.jsonSerializer.BigDecimalSerializer;

public class RcProd extends QueryParam implements Serializable {

	private static final long serialVersionUID = 2419280028429918617L;
	
	// db
	private Integer prodId;
	private String name;
	private String ad;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal originPrice;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal price;
	private Integer merchantId;
	private Integer prodCatId;
	private Integer prodBrandId;
	private String measureUnit;
	private Integer type;
	private Integer stockWarning;
	private Byte delFlag;
	private Float score;
	private Integer status;
	private Date createTime;
	private Date updateTime;
	private Date putawayTime;
	private Date soldoutTime;
	private String detail;
	private Integer vendorId;
	private String prodBrandName;
	private Integer cityId;
	private Integer baseProdId;
	private Integer inventory;
	private Integer soldNum;
	private Integer priceManner;
	private Integer measureUnitCount;

	// rel
	private RcMerchant merchant;
	private RcVendor vendor;
	private RcCity city;
	private RcProdCat prodCat;
	private RcProdBrand prodBrand;
	private List<RcProdImg> prodImgs;
	private List<RcProdPropInfo> prodPropInfos;
	private List<RcProdSku> prodSkus;
	private List<RcProdSkuPropInfo> prodSkuPropInfos;
	private List<RcOrderProdEvaluation> evaluations;
	private List<RcCoupon> coupons;

	// vo
	private String prodName;
	private RcOrderProdEvaluation evaluation; // 显示第一条评论
	private String prodImgUrl; // 商品主图
	private String cityName; // 商品所属城市
	private String prodDetailUrl; // 商品详情URL
	private Integer storage; // 商品显示库存数量（非SKU库存）
	private Integer skuCount; // 商品的sku数量
	private Integer evaluationCount; // 商品的sku数量
	private String prodBrandLogoUrl; // 商品品牌logo图片
	private List<RcProdSkuProp> prodSkuInfos;// 商品SKU信息
	private String merchantName;
	private String prodCatName;
	
	// se
	private List<Integer> merchantIds;
	private List<Integer> cityIds;
	private Byte isBaseProd;

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAd() {
		return ad;
	}

	public void setAd(String ad) {
		this.ad = ad;
	}

	public BigDecimal getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(BigDecimal originPrice) {
		this.originPrice = originPrice;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStockWarning() {
		return stockWarning;
	}

	public void setStockWarning(Integer stockWarning) {
		this.stockWarning = stockWarning;
	}

	public Byte getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Byte delFlag) {
		this.delFlag = delFlag;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getPutawayTime() {
		return putawayTime;
	}

	public void setPutawayTime(Date putawayTime) {
		this.putawayTime = putawayTime;
	}

	public Date getSoldoutTime() {
		return soldoutTime;
	}

	public void setSoldoutTime(Date soldoutTime) {
		this.soldoutTime = soldoutTime;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public RcMerchant getMerchant() {
		return merchant;
	}

	public void setMerchant(RcMerchant merchant) {
		this.merchant = merchant;
	}

	public Integer getProdCatId() {
		return prodCatId;
	}

	public void setProdCatId(Integer prodCatId) {
		this.prodCatId = prodCatId;
	}

	public RcProdCat getProdCat() {
		return prodCat;
	}

	public void setProdCat(RcProdCat prodCat) {
		this.prodCat = prodCat;
	}

	public Integer getProdBrandId() {
		return prodBrandId;
	}

	public void setProdBrandId(Integer prodBrandId) {
		this.prodBrandId = prodBrandId;
	}

	public RcProdBrand getProdBrand() {
		return prodBrand;
	}

	public void setProdBrand(RcProdBrand prodBrand) {
		this.prodBrand = prodBrand;
	}

	public List<RcProdImg> getProdImgs() {
		return prodImgs;
	}

	public void setProdImgs(List<RcProdImg> prodImgs) {
		this.prodImgs = prodImgs;
	}

	public List<RcProdPropInfo> getProdPropInfos() {
		return prodPropInfos;
	}

	public void setProdPropInfos(List<RcProdPropInfo> prodPropInfos) {
		this.prodPropInfos = prodPropInfos;
	}

	public List<RcProdSku> getProdSkus() {
		return prodSkus;
	}

	public void setProdSkus(List<RcProdSku> prodSkus) {
		this.prodSkus = prodSkus;
	}

	public List<RcProdSkuPropInfo> getProdSkuPropInfos() {
		return prodSkuPropInfos;
	}

	public void setProdSkuPropInfos(List<RcProdSkuPropInfo> prodSkuPropInfos) {
		this.prodSkuPropInfos = prodSkuPropInfos;
	}

	public List<RcOrderProdEvaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<RcOrderProdEvaluation> evaluations) {
		this.evaluations = evaluations;
	}

	public RcOrderProdEvaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(RcOrderProdEvaluation evaluation) {
		this.evaluation = evaluation;
	}

	public String getProdImgUrl() {
		return prodImgUrl;
	}

	public void setProdImgUrl(String prodImgUrl) {
		this.prodImgUrl = prodImgUrl;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getProdDetailUrl() {
		return prodDetailUrl;
	}

	public void setProdDetailUrl(String prodDetailUrl) {
		this.prodDetailUrl = prodDetailUrl;
	}

	public Integer getStorage() {
		return storage;
	}

	public void setStorage(Integer storage) {
		this.storage = storage;
	}

	public Integer getSkuCount() {
		return skuCount;
	}

	public void setSkuCount(Integer skuCount) {
		this.skuCount = skuCount;
	}

	public Integer getEvaluationCount() {
		return evaluationCount;
	}

	public void setEvaluationCount(Integer evaluationCount) {
		this.evaluationCount = evaluationCount;
	}

	public String getProdBrandLogoUrl() {
		return prodBrandLogoUrl;
	}

	public void setProdBrandLogoUrl(String prodBrandLogoUrl) {
		this.prodBrandLogoUrl = prodBrandLogoUrl;
	}

	public String getProdBrandName() {
		return prodBrandName;
	}

	public void setProdBrandName(String prodBrandName) {
		this.prodBrandName = prodBrandName;
	}

	public List<RcProdSkuProp> getProdSkuInfos() {
		return prodSkuInfos;
	}

	public void setProdSkuInfos(List<RcProdSkuProp> prodSkuInfos) {
		this.prodSkuInfos = prodSkuInfos;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public List<RcCoupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<RcCoupon> coupons) {
		this.coupons = coupons;
	}

	public RcVendor getVendor() {
		return vendor;
	}

	public void setVendor(RcVendor vendor) {
		this.vendor = vendor;
	}

	public RcCity getCity() {
		return city;
	}

	public void setCity(RcCity city) {
		this.city = city;
	}
	

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public List<Integer> getMerchantIds() {
		return merchantIds;
	}

	public void setMerchantIds(List<Integer> merchantIds) {
		this.merchantIds = merchantIds;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public List<Integer> getCityIds() {
		return cityIds;
	}

	public void setCityIds(List<Integer> cityIds) {
		this.cityIds = cityIds;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public Integer getSoldNum() {
		return soldNum;
	}

	public void setSoldNum(Integer soldNum) {
		this.soldNum = soldNum;
	}

	public Integer getBaseProdId() {
		return baseProdId;
	}

	public void setBaseProdId(Integer baseProdId) {
		this.baseProdId = baseProdId;
	}

	public Byte getIsBaseProd() {
		return isBaseProd;
	}

	public void setIsBaseProd(Byte isBaseProd) {
		this.isBaseProd = isBaseProd;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdCatName() {
		return prodCatName;
	}

	public void setProdCatName(String prodCatName) {
		this.prodCatName = prodCatName;
	}

	public Integer getPriceManner() {
		return priceManner;
	}

	public void setPriceManner(Integer priceManner) {
		this.priceManner = priceManner;
	}

	public Integer getMeasureUnitCount() {
		return measureUnitCount;
	}

	public void setMeasureUnitCount(Integer measureUnitCount) {
		this.measureUnitCount = measureUnitCount;
	}
}