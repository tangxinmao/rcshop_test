package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcProdImg;

public interface IProdImgService {
	/**
	 * 通过prodId获取图片数据
	 * 
	 * @param prodId
	 * @return
	 */
	List<RcProdImg> findProdImgsByProdSkuId(String prodSkuId);
	
	RcProdImg findFirstProdImgByProdSkuId(String prodSkuId);
	
	int addProdImg(RcProdImg prodImg);
	
	int removeProdImgsByProdId(int prodId);
}
