package com.socool.soft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcProdBrand;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.dao.RcProdBrandMapper;
import com.socool.soft.service.IProdBrandService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class ProdBrandServiceImpl implements IProdBrandService {
	@Autowired
	private RcProdBrandMapper prodBrandMapper;

	@Override
	public List<RcProdBrand> findAllProdBrands(RcProdBrand param) {
		param.setOrderBy("NAME ASC");
		return prodBrandMapper.select(param);
	}
	
	@Override
	public List<RcProdBrand> findAllPagedProdBrands(RcProdBrand param, Page page) {
		PageContext.setPage(page);
		param.setOrderBy("NAME ASC");
		return prodBrandMapper.select(param);
	}

	@Override
	public List<RcProdBrand> findProdBrandsByName(String name) {
		RcProdBrand param = new RcProdBrand();
		param.setName(name);
		return prodBrandMapper.select(param);
	}

	@Override
	public List<RcProdBrand> findPagedProdBrandsByName(String name, Page page) {
		PageContext.setPage(page);
		RcProdBrand param = new RcProdBrand();
		param.setName(name);
		param.setOrderBy("NAME ASC");
		return prodBrandMapper.select(param);
	}

	@Override
	public int addProdBrand(RcProdBrand prodBrand) {
		if(prodBrandMapper.insert(prodBrand) > 0) {
			return prodBrand.getProdBrandId();
		}
		return 0;
	}

	@Override
	public int modifyProdBrand(RcProdBrand prodBrand) {
		if(prodBrandMapper.updateByPrimaryKey(prodBrand) > 0) {
			return prodBrand.getProdBrandId();
		}
		return 0;
	}

	@Override
	public int removeProdBrand(int prodBrandId) {
		RcProdBrand prodBrand = new RcProdBrand();
		prodBrand.setProdBrandId(prodBrandId);
		prodBrand.setDelFlag(YesOrNoEnum.YES.getValue());
		modifyProdBrand(prodBrand);
		return 1;
	}

	@Override
	public RcProdBrand findProdBrandById(int prodBrandId) {
		return prodBrandMapper.selectByPrimaryKey(prodBrandId);
	}

	@Override
	public int saveProdBrand(RcProdBrand prodBrand) {
		if(prodBrand.getProdBrandId() == null) {
			return addProdBrand(prodBrand);
		} else {
			return modifyProdBrand(prodBrand);
		}
	}
}
