package com.socool.soft.service;

import java.util.Collection;
import java.util.List;

import com.socool.soft.bo.RcProdCat;
import com.socool.soft.vo.Page;

public interface IProdCatService {
	
	/**
	 * 列出一级分类
	 * @return
	 */
	List<RcProdCat> findTopProdCats();
	
	List<RcProdCat> findPagedTopProdCatsWithChildren(Page page);
	
	/**
	 * 查询二级商品分类
	 * @param parentCatId
	 * @return
	 */
	List<RcProdCat> findChildProdCats(int parentId);
	
	/**
	 * 添加商品分类
	 * @param rcProdCat
	 * @return
	 */
	int addProdCat(RcProdCat prodCat);
	
	/**
	 * 修改商品分类
	 * @param rcProdCat
	 * @return
	 */
	int modifyProdCat(RcProdCat prodCat);
	
	/**
	 * 根据分类ID删除商品分类
	 * @param prodCatId
	 * @return
	 */
	int removeProdCatAndChildrenById(int prodCatId);
	
	RcProdCat findProdCatById(int prodCatId);
	
	List<RcProdCat> findTopProdCatsWithChildrenForAndroid();
	
	List<Integer> findProdCatIdsByProdIds(Collection<Integer> prodIds);
}
