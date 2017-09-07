package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcProdSkuProp;
import com.socool.soft.bo.RcProdSkuPropEnum;
import com.socool.soft.bo.RcProdCatSkuProp;
import com.socool.soft.vo.Page;

public interface IProdSkuPropService {

	/**
	 * 查询商品属性/SKU列表
	 * @param isSku
	 * @return
	 */
	List<RcProdSkuProp> findPagedProdSkuProps(RcProdSkuProp param, Page page);
	
	List<RcProdSkuProp> findProdSkuProps(RcProdSkuProp param);
	
	/**
	 * 查找商品属性
	 * @param prodPropId
	 * @return
	 */
	RcProdSkuProp findProdSkuPropById(int prodSkuPropId);
	
	/**
	 * 添加商品属性
	 * @param rcProdProp
	 * @return
	 */
	int addProdSkuProp(RcProdSkuProp prodProp);
	
	/**
	 * 修改商品属性
	 * @param rcProdProp
	 * @return
	 */
	int modifyProdSkuProp(RcProdSkuProp prodProp);
	
	/**
	 * 删除商品属性
	 * @param prodPropId
	 * @return
	 */
	int removeProdSkuPropWithEnums(int prodSkuPropId);
	
	/**
	 * 查询枚举值列表
	 * @param prodPropId
	 * @return
	 */
	List<RcProdSkuPropEnum> findProdSkuPropEnumsByProdSkuPropId(int prodSkuPropId);
	
	/**
	 * 添加枚举值数据
	 * @param rcProdPropEnum
	 * @return
	 */
	int addProdSkuPropEnum(RcProdSkuPropEnum prodSkuPropEnum);
	
	
	/**
	 * 根据主键ID删除枚举值数据
	 * @param prodPropEnumId
	 * @return
	 */
	int removeProdSkuPropEnum(int prodSkuPropEnumId);
	
	/**
	 * 查询枚举值数据
	 * @param prodPropEnumId
	 * @return
	 */
	RcProdSkuPropEnum findProdSkuPropEnumById(int prodSkuPropEnumId);
	
	
	/**
	 * 修改枚举值数据
	 * @param rcProdPropEnum
	 * @return
	 */
	int modifyProdSkuPropEnum(RcProdSkuPropEnum prodSkuPropEnum);
	
	/**
	 * 根据枚举ID删除枚举值数据
	 * @param prodPropId
	 * @return
	 */
	int removeProdSkuPropEnumsByProdSkuPropId(int prodSkuPropId);
	
	/**
	 * 查询已选择的商品属性
	 * @param prodCatId
	 * @param isSku
	 * @return
	 */
	List<List<RcProdSkuProp>> findProdSkuPropsForBinding(int prodCatId);
	
	/**
	 * 删除商品分类的绑定关系
	 * @param prodCatId
	 * @return
	 */
	int removeProdCatSkuPropsByProdCatId(int prodCatId);
	
	/**
	 * 添加绑定关系
	 * @param rcProdPropRel
	 * @return
	 */
	int addProdCatSkuProp(RcProdCatSkuProp prodCatSkuProp);
	
	List<RcProdCatSkuProp> findProdCatSkuPropsByProdCatId(int prodCatId);
	
	RcProdCatSkuProp findProdCatSkuProp(RcProdCatSkuProp param);
	
	int removeProdCatSkuPropsByProdSkuPropId(int prodSkuPropId);
}
