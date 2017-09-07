package com.socool.soft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcProdImg;
import com.socool.soft.dao.RcProdImgMapper;
import com.socool.soft.service.IProdImgService;

@Service
public class ProdImgServiceImpl implements IProdImgService {
	@Autowired
	private RcProdImgMapper prodImgMapper;

	@Override
	public List<RcProdImg> findProdImgsByProdSkuId(String prodSkuId) {
		RcProdImg param = new RcProdImg();
		param.setProdSkuId(prodSkuId);
		param.setOrderBy("SEQ ASC");
		return prodImgMapper.select(param);
	}

	@Override
	public RcProdImg findFirstProdImgByProdSkuId(String prodSkuId) {
		RcProdImg param = new RcProdImg();
		param.setProdSkuId(prodSkuId);
		param.setOrderBy("SEQ ASC");
		return prodImgMapper.selectOne(param);
	}

	@Override
	public int addProdImg(RcProdImg prodImg) {
		if(prodImgMapper.insert(prodImg) > 0) {
			return prodImg.getProdImgId();
		}
		return 0;
	}

	@Override
	public int removeProdImgsByProdId(int prodId) {
		RcProdImg param = new RcProdImg();
		param.setProdId(prodId);
		return prodImgMapper.delete(param);
	}
}
