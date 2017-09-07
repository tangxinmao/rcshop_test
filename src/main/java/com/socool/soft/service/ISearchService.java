package com.socool.soft.service;

import java.util.Collection;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;

import com.socool.soft.bo.RcProd;
import com.socool.soft.vo.Page;

public interface ISearchService {
	
	/**
	 * 初始化solr数据索引
	 */
	Collection<SolrInputDocument> initSolrIndexDocument();
	
	/**
	 * 商品上下架后更新solr索引
	 * @param prodId
	 */
	void updateIndexForProdPutUpDown(int prodId);
	
	/**
	 * 订单交易成功后更新solr索引
	 * @param prodId
	 */
//	void updateIndexForOrderSuccess(int prodId);
	
	/**
	 * 获取热门搜索关键词
	 * @param size 数量
	 * @return
	 */
	List<String> getHotKeys(int size);
	
	List<RcProd> findPagedProdHotsByCityId(int cityId, Page page);

}
