package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcProdCatProp;
import com.socool.soft.bo.RcProdProp;
import com.socool.soft.bo.RcProdPropEnum;
import com.socool.soft.vo.Page;

public interface IProdPropService {

	/**
	 * 查询商品属性/SKU列表
	 * @param isSku
	 * @return
	 */
	List<RcProdProp> findPagedProdProps(RcProdProp param, Page page);
	
	List<RcProdProp> findProdProps(RcProdProp param);
	
	/**
	 * 查找商品属性
	 * @param prodPropId
	 * @return
	 */
	RcProdProp findProdPropById(int prodPropId);
	
	/**
	 * 添加商品属性
	 * @param rcProdProp
	 * @return
	 */
	int addProdProp(RcProdProp prodProp);
	
	/**
	 * 修改商品属性
	 * @param rcProdProp
	 * @return
	 */
	int modifyProdProp(RcProdProp prodProp);
	
	/**
	 * 删除商品属性
	 * @param prodPropId
	 * @return
	 */
	int removeProdPropWithEnums(int prodPropId);
	
	/**
	 * 查询枚举值列表
	 * @param prodPropId
	 * @return
	 */
	List<RcProdPropEnum> findProdPropEnumsByProdPropId(int prodPropId);
	
	/**
	 * 添加枚举值数据
	 * @param rcProdPropEnum
	 * @return
	 */
	int addProdPropEnum(RcProdPropEnum prodPropEnum);
	
	
	/**
	 * 根据主键ID删除枚举值数据
	 * @param prodPropEnumId
	 * @return
	 */
	int removeProdPropEnum(int prodPropEnumId);
	
	/**
	 * 查询枚举值数据
	 * @param prodPropEnumId
	 * @return
	 */
//	RcProdPropEnum findProdPropEnumById(int prodPropEnumId);
	
	
	/**
	 * 修改枚举值数据
	 * @param rcProdPropEnum
	 * @return
	 */
	int modifyProdPropEnum(RcProdPropEnum prodPropEnum);
	
	/**
	 * 根据枚举ID删除枚举值数据
	 * @param prodPropId
	 * @return
	 */
	int removeProdPropEnumsByProdPropId(int prodPropId);
	
	/**
	 * 查询已选择的商品属性
	 * @param prodCatId
	 * @param isSku
	 * @return
	 */
	List<List<RcProdProp>> findProdPropsForBinding(int prodCatId);
	
	/**
	 * 删除商品分类的绑定关系
	 * @param prodCatId
	 * @return
	 */
	int removeProdCatPropsByProdCatId(int prodCatId);
	
	/**
	 * 添加绑定关系
	 * @param rcProdPropRel
	 * @return
	 */
	int addProdCatProp(RcProdCatProp prodCatProp);
	
	List<RcProdCatProp> findProdCatPropsByProdCatId(int prodCatId);
	
	RcProdCatProp findProdCatProp(RcProdCatProp param);
	
	int removeProdCatPropsByProdPropId(int prodPropId);
}
