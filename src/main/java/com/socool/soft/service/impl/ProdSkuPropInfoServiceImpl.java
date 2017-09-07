package com.socool.soft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcProdSkuPropInfo;
import com.socool.soft.dao.RcProdSkuPropInfoMapper;
import com.socool.soft.service.IProdSkuPropInfoService;

@Service
public class ProdSkuPropInfoServiceImpl implements IProdSkuPropInfoService {
	@Autowired
	private RcProdSkuPropInfoMapper prodSkuPropInfoMapper;

	@Override
	public List<RcProdSkuPropInfo> findProdSkuPropInfosByProdSkuId(String prodSkuId) {
		RcProdSkuPropInfo param = new RcProdSkuPropInfo();
		param.setProdSkuId(prodSkuId);
		return prodSkuPropInfoMapper.select(param);
	}

	@Override
	public List<RcProdSkuPropInfo> findProdSkuPropInfosByProdId(int prodId) {
		RcProdSkuPropInfo param = new RcProdSkuPropInfo();
		param.setProdId(prodId);
		return prodSkuPropInfoMapper.select(param);
	}

	@Override
	public List<RcProdSkuPropInfo> findProdSkuPropInfoByProdSkuPropEnumId(int prodSkuPropEnumId) {
		RcProdSkuPropInfo param = new RcProdSkuPropInfo();
		param.setProdPropEnumId(prodSkuPropEnumId);
		return prodSkuPropInfoMapper.select(param);
	}

	@Override
	public int addProdSkuPropInfo(RcProdSkuPropInfo prodSkuPropInfo) {
		if(prodSkuPropInfoMapper.insert(prodSkuPropInfo) > 0) {
			return prodSkuPropInfo.getProdSkuPropInfoId();
		}
		return 0;
	}

	@Override
	public int removeProdSkuPropInfosByProdId(int prodId) {
		RcProdSkuPropInfo param = new RcProdSkuPropInfo();
		param.setProdId(prodId);
		return prodSkuPropInfoMapper.delete(param);
	}

	@Override
	public RcProdSkuPropInfo findProdSkuPropInfoByProdIdAndProdSkuPropEnumId(
			int prodId, int prodSkuPropEnumId) {
		RcProdSkuPropInfo param = new RcProdSkuPropInfo();
		param.setProdId(prodId);
		param.setProdPropEnumId(prodSkuPropEnumId);
		return prodSkuPropInfoMapper.selectOne(param);
	}
}
