package com.socool.soft.service;

import java.util.List;
import java.util.Map;

import com.socool.soft.vo.ProdPropListDTO;
import com.socool.soft.vo.ProductPublishedDTO;

public interface IProductManagerService {

	/**
	 * 查询商品属性/SKU属性
	 * @param prodCatId
	 * @param isSku
	 * @param prodType
	 * @return
	 */
	List<ProdPropListDTO> findProdPropsByProdCatId(int prodCatId);
	
	List<ProdPropListDTO> findProdSkuPropsByProdCatId(int prodCatId, int prodType);
	
	/**
	 * 发布商品
	 * @param productPublishedDTO
	 * @return 商品ID
	 */
	int publishProduct(ProductPublishedDTO productPublishedDTO);
	int publishBaseProduct(ProductPublishedDTO productPublishedDTO);
	
	/**
	 * 商品上架并更新solr索引
	 * @param prodId
	 */
	void doProdPutAway(int prodId);
	
	/**
	 * 商品下架并更新solr索引
	 * @param prodId
	 */
	void doProdOutOfStock(int prodId);
	
	/**
	 * 查询SKU库存、SKU值、sku原价、sku现价
	 * @param prodId
	 * @return
	 */
	List<Map<String, Object>> querySkuStoragesBySkuId(int prodId);
	
	/**
	 * 查询商品名称、库存、商品原价、商品现价
	 * @param prodId
	 * @return
	 */
	List<Map<String, Object>> queryProdStorageByProdId(int prodId);
	
	/**
	 * 根据ID查询SKU信息
	 * @param prodSkuId
	 * @return
	 */
//	RcProdSku findProdSkuById(String prodSkuId);
	
	/**
	 * 修改SKU信息
	 * @param rcProdSku
	 * @return
	 */
//	int modifyProdSku(RcProdSku rcProdSku);
	
	/**
	 * 根据ID获取商品数据模型
	 * @param prodId
	 * @return
	 */
	ProductPublishedDTO getProductDetail(int prodId);
	
	/**
	 * 复制商品
	 * @param prodId
	 * @param merchantId
	 */
	void copyProduct(int prodId, int merchantId);
}
