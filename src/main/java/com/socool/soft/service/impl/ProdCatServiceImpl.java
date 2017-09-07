package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcProd;
import com.socool.soft.bo.RcProdCat;
import com.socool.soft.dao.RcProdCatMapper;
import com.socool.soft.service.IProdCatService;
import com.socool.soft.service.IProdService;
import com.socool.soft.util.VOConversionUtil;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class ProdCatServiceImpl implements IProdCatService {
	@Autowired
	private RcProdCatMapper prodCatMapper;
	@Autowired
	private IProdService prodService;

	@Override
	public List<RcProdCat> findTopProdCats() {
		return findChildProdCats(0);
	}

	@Override
	public List<RcProdCat> findPagedTopProdCatsWithChildren(Page page) {
		PageContext.setPage(page);
		List<RcProdCat> prodCats = findTopProdCats();
		for(RcProdCat prodCat : prodCats){
			List<RcProdCat> childProdCats = findChildProdCats(prodCat.getProdCatId());
			prodCat.setChildProdCats(childProdCats);
		}
		return prodCats;
	}

	@Override
	public List<RcProdCat> findChildProdCats(int parentId) {
		RcProdCat param = new RcProdCat();
		param.setParentId(parentId);
		param.setOrderBy("SEQ ASC");
		return prodCatMapper.select(param);
	}

	@Override
	public int addProdCat(RcProdCat prodCat) {
		if(prodCat.getParentId() == null) {
			prodCat.setParentId(0);
		}
		if(prodCat.getSeq() == null) {
			prodCat.setSeq(99);
		}
		if(prodCatMapper.insert(prodCat) > 0) {
			return prodCat.getProdCatId();
		}
		return 0;
	}

	@Override
	public int modifyProdCat(RcProdCat prodCat) {
		if(prodCatMapper.updateByPrimaryKey(prodCat) > 0) {
			return prodCat.getProdCatId();
		}
		return 0;
	}

	@Override
	public int removeProdCatAndChildrenById(int prodCatId) {
		int result = prodCatMapper.deleteByPrimaryKey(prodCatId);
		if(result > 0) {
			RcProdCat param = new RcProdCat();
			param.setParentId(prodCatId);
			prodCatMapper.delete(param);
		}
		return result;
	}

	@Override
	public RcProdCat findProdCatById(int prodCatId) {
		return prodCatMapper.selectByPrimaryKey(prodCatId);
	}

	@Override
	public List<RcProdCat> findTopProdCatsWithChildrenForAndroid() {
		List<RcProdCat> prodCats = findTopProdCats();
		for(RcProdCat prodCat : prodCats){
			List<RcProdCat> childProdCats = findChildProdCats(prodCat.getProdCatId());
			for(RcProdCat childProdCat : childProdCats){
				VOConversionUtil.Entity2VO(childProdCat, null, new String[] {"seq"});
			}
			prodCat.setChildProdCats(childProdCats);
			VOConversionUtil.Entity2VO(prodCat, null, new String[] {"seq"});
		}
		return prodCats;
	}

	@Override
	public List<Integer> findProdCatIdsByProdIds(Collection<Integer> prodIds) {
		List<Integer> prodCatIds = new ArrayList<Integer>();
		for(Integer prodId : prodIds) {
			RcProd prod = prodService.findProdById(prodId);
			int prodCatId = prod.getProdCatId();
			if(prod != null && !prodCatIds.contains(prodCatId)) {
				prodCatIds.add(prodCatId);
			}
		}
		return prodCatIds;
	}
}
