package com.socool.soft.vo;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.socool.soft.bo.RcProdPropInfo;
import com.socool.soft.bo.RcProdSku;
import com.socool.soft.bo.RcProdSkuPropInfo;

public class ProductPublishedDTO {
	//判断是新增还是修改
	private boolean ifAdded;
	
	private Integer prodId;
	
	private String prodName;
	
	private String prodAdword;
	
	private Integer prodStorage;
	
	private Integer prodStockWarning;
	
	private String prodMeasureUnit;
	
	private BigDecimal prodOriginPrice;
	
	private BigDecimal prodPrice;
	
    //商品品牌ID
    private Integer prodBrandId;
    private String prodBrandName;
    
    private Integer merchantId;
    
    private Integer prodCatId;
    
    private Integer productType;
    
    //商品图片URL列表
    private List<String> prodImgUrls;
    //商品的单品图片URL列表
    private Map<Integer, List<String>> prodSkuImgUrlsMap;
    
    //修改时用于回显图片
    private Map<Integer, List<String>> skuImgUrlsMap;
    
    //商品描述信息
    private String prodDetail;
    
    private String prodFreightTempId;
    
    private List<RcProdPropInfo> prodPropInfos;
    
    private List<RcProdSku> prodSkus;
    /**
     * SKUID与单品属性集合映射集，用于保存每个单品所对应的商品属性集。
     * 其中，map的key是List<EcProdSku> prodSkus中相应单品信息位于List<EcProdSku>中的下标值
     */
    private Map<Integer, List<RcProdSkuPropInfo>> prodSkuPropInfoMap;
    
    //城市属性
    private List<String> citys;
    
    private Integer baseProdId;
    
	public boolean isIfAdded() {
		return ifAdded;
	}

	public void setIfAdded(boolean ifAdded) {
		this.ifAdded = ifAdded;
	}
	
	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdAdword() {
		return prodAdword;
	}

	public void setProdAdword(String prodAdword) {
		this.prodAdword = prodAdword;
	}

	public Integer getProdStorage() {
		return prodStorage;
	}

	public void setProdStorage(Integer prodStorage) {
		this.prodStorage = prodStorage;
	}

	public Integer getProdStockWarning() {
		return prodStockWarning;
	}

	public void setProdStockWarning(Integer prodStockWarning) {
		this.prodStockWarning = prodStockWarning;
	}

	public String getProdMeasureUnit() {
		return prodMeasureUnit;
	}

	public void setProdMeasureUnit(String prodMeasureUnit) {
		this.prodMeasureUnit = prodMeasureUnit;
	}

	public BigDecimal getProdOriginPrice() {
		return prodOriginPrice;
	}

	public void setProdOriginPrice(BigDecimal prodOriginPrice) {
		this.prodOriginPrice = prodOriginPrice;
	}

	public BigDecimal getProdPrice() {
		return prodPrice;
	}

	public void setProdPrice(BigDecimal prodPrice) {
		this.prodPrice = prodPrice;
	}

	public Integer getProdBrandId() {
		return prodBrandId;
	}

	public void setProdBrandId(Integer prodBrandId) {
		this.prodBrandId = prodBrandId;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public Integer getProdCatId() {
		return prodCatId;
	}

	public void setProdCatId(Integer prodCatId) {
		this.prodCatId = prodCatId;
	}

	public List<String> getProdImgUrls() {
		return prodImgUrls;
	}

	public void setProdImgUrls(List<String> prodImgUrls) {
		this.prodImgUrls = prodImgUrls;
	}

	public Map<Integer, List<String>> getProdSkuImgUrlsMap() {
		return prodSkuImgUrlsMap;
	}

	public void setProdSkuImgUrlsMap(Map<Integer, List<String>> prodSkuImgUrlsMap) {
		this.prodSkuImgUrlsMap = prodSkuImgUrlsMap;
	}

	public Map<Integer, List<String>> getSkuImgUrlsMap() {
		return skuImgUrlsMap;
	}

	public void setSkuImgUrlsMap(Map<Integer, List<String>> skuImgUrlsMap) {
		this.skuImgUrlsMap = skuImgUrlsMap;
	}

	public String getStringDescContent(byte[] prodDetailDescContent) throws UnsupportedEncodingException{
		if(prodDetailDescContent==null)
			return "";
		String str=new String(prodDetailDescContent,"UTF-8");
		return str;
	}
	
	public String getProdDetail() {
		return prodDetail;
	}

	public void setProdDetail(String prodDetail) {
		this.prodDetail = prodDetail;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public String getProdFreightTempId() {
		return prodFreightTempId;
	}

	public void setProdFreightTempId(String prodFreightTempId) {
		this.prodFreightTempId = prodFreightTempId;
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

	public Map<Integer, List<RcProdSkuPropInfo>> getProdSkuPropInfoMap() {
		return prodSkuPropInfoMap;
	}

	public void setProdSkuPropInfoMap(
			Map<Integer, List<RcProdSkuPropInfo>> prodSkuPropInfoMap) {
		this.prodSkuPropInfoMap = prodSkuPropInfoMap;
	}

	public List<String> getCitys() {
		return citys;
	}

	public void setCitys(List<String> citys) {
		this.citys = citys;
	}

	public String getProdBrandName() {
		return prodBrandName;
	}

	public void setProdBrandName(String prodBrandName) {
		this.prodBrandName = prodBrandName;
	}

	public Integer getBaseProdId() {
		return baseProdId;
	}

	public void setBaseProdId(Integer baseProdId) {
		this.baseProdId = baseProdId;
	}
}
