package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcProdBrand;
import com.socool.soft.vo.Page;

public interface IProdBrandService {
	
	List<RcProdBrand> findAllProdBrands(RcProdBrand param);
	List<RcProdBrand> findAllPagedProdBrands(RcProdBrand param, Page page);
	
	List<RcProdBrand> findProdBrandsByName(String name);
	List<RcProdBrand> findPagedProdBrandsByName(String name, Page page);
	
	int addProdBrand(RcProdBrand prodBrand);
	
	int modifyProdBrand(RcProdBrand prodBrand);
	
	int removeProdBrand(int prodBrandId);
	
	RcProdBrand findProdBrandById(int prodBrandId);
	
	int saveProdBrand(RcProdBrand prodBrand);
}
