package com.socool.soft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcProdPropInfo;
import com.socool.soft.dao.RcProdPropInfoMapper;
import com.socool.soft.service.IProdPropInfoService;

@Service
public class ProdPropInfoServiceImpl implements IProdPropInfoService {
	@Autowired
	private RcProdPropInfoMapper prodPropInfoMapper;

	@Override
	public List<RcProdPropInfo> findProdPropInfosByProdId(int prodId) {
		RcProdPropInfo param = new RcProdPropInfo();
		param.setProdId(prodId);
		return prodPropInfoMapper.select(param);
	}

	@Override
	public int addProdPropInfo(RcProdPropInfo prodPropInfo) {
		if(prodPropInfo.getProdPropId() == null) {
			prodPropInfo.setProdPropId(0);
		}
		if(prodPropInfoMapper.insert(prodPropInfo) > 0) {
			return prodPropInfo.getProdPropInfoId();
		}
		return 0;
	}

	@Override
	public int removeProdPropInfosByProdId(int prodId) {
		RcProdPropInfo param = new RcProdPropInfo();
		param.setProdId(prodId);
		return prodPropInfoMapper.delete(param);
	}
}
