package com.socool.soft.service;

import com.socool.soft.bo.RcMerchant;
import com.socool.soft.vo.Page;

import java.math.BigDecimal;
import java.util.List;

public interface IMerchantService {
	/**
	 * 查询所有的商户
	 * 
	 * @return
	 */
	List<RcMerchant> findAllEnabledMerchants();
	
	/**
	 * 通过merchantId
	 * 
	 * @param merchantId
	 * @return
	 */
	RcMerchant findMerchantById(int merchantId);

	/**
	 * 添加商户
	 * 
	 * @param rcMerchant
	 * @return
	 */
	int addMerchant(RcMerchant merchant);

	/**
	 * 查询商户列表所需数据
	 * 
	 * @param storeParam
	 * @return
	 */
	List<RcMerchant> findAllMerchants();
	List<RcMerchant> findPagedMerchants(RcMerchant param, Page page);
	List<RcMerchant> findMerchants(RcMerchant param);

	/**
	 * 修改商户
	 * 
	 * @param rcMerchant
	 * @return
	 */
	int modifyMerchant(RcMerchant merchant);

	/**
	 * 删除商户
	 * 
	 * @param merchantId
	 * @return
	 */
	int removeMerchant(int merchantId);

	/**
	 * 删除所有授权信息
	 * 
	 * @param merchantId
	 * @return
	 */
//	int removeAddedInfo(int merchantId);
	
//	int removeMerchantBrands(int merchantId);

	/**
	 * 添加品牌授权信息
	 * 
	 * @param rcProdBrandRela
	 * @return
	 */
//	int addRcProdBrandRela(RcProdBrandRel rcProdBrandRel);

	/**
	 * 获取品牌商下面的商家
	 * 
	 * @param vendorId
	 * @return
	 */
	List<RcMerchant> findMerchantsByVendorId(Integer vendorId);
	
	RcMerchant findMerchantByMerchantUserId(int memberId);
	
	/**
	 * 模糊搜索商户
	 * @param searchKey
	 * @return
	 */
//	List<RcMerchant> searchMerchant(String searchKey);
	
	/**
	 * 根据商品ID获取商户信息
	 * @param prodId
	 * @return
	 */
	RcMerchant findMerchantByProdId(int prodId);
	
	List<RcMerchant> findMerchantByName(String name);
	
	List<RcMerchant> findSortedMerchantByName(String name);
	
	/**
	 * 根据merchantId获取商家昨日收入
	 * @param storeId
	 * @return
	 */

	BigDecimal findYIncomeByMerchantId(int merchantId);
	/**
	 * 根据merchantId获取商家今日成交笔数
	 * @param storeId
	 * @return
	 */

	Integer findTTransByMerchantId(int merchantId);
	
	BigDecimal findTIncomeByMerchantId(int merchantId);

    void coverMerchant(int merchantId);
}
