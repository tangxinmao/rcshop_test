package com.socool.soft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcProdSku;
import com.socool.soft.dao.RcProdSkuMapper;
import com.socool.soft.service.IProdSkuService;

@Service
public class ProdSkuServiceImpl implements IProdSkuService {
	@Autowired
	private RcProdSkuMapper prodSkuMapper;

	@Override
	public RcProdSku findProdSkuByProdSkuId(String prodSkuId) {
		return prodSkuMapper.selectByPrimaryKey(prodSkuId);
	}

	@Override
	public List<RcProdSku> findProdSkusByProdId(int prodId) {
		RcProdSku param = new RcProdSku();
		param.setProdId(prodId);
		return prodSkuMapper.select(param);
	}

	@Override
	public String addProdSku(RcProdSku prodSku) {
		if(prodSkuMapper.insert(prodSku) > 0) {
			return prodSku.getProdSkuId();
		}
		return null;
	}

	@Override
	public int removeProdSkusByProdId(int prodId) {
		RcProdSku param = new RcProdSku();
		param.setProdId(prodId);
		return prodSkuMapper.delete(param);
	}

	@Override
	public String modifyProdSku(RcProdSku prodSku) {
		if(prodSkuMapper.updateByPrimaryKey(prodSku) > 0) {
			return prodSku.getProdSkuId();
		}
		return null;
	}
}
