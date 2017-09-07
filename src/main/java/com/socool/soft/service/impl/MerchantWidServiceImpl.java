package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcMerchantWid;
import com.socool.soft.dao.RcMerchantWidMapper;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IMerchantWidService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class MerchantWidServiceImpl implements IMerchantWidService {
	@Autowired
	private RcMerchantWidMapper merchantWidMapper;
	@Autowired
	private IMerchantService merchantService;

	@Override
	public List<RcMerchantWid> findPagedMerchantWids(RcMerchantWid param, Page page) {
		if(StringUtils.isNotBlank(param.getMerchantName())) {
			List<RcMerchant> merchants = merchantService.findMerchantByName(param.getMerchantName());
			if(CollectionUtils.isEmpty(merchants)) {
				return new ArrayList<RcMerchantWid>();
			}
			List<Integer> merchantIds = new ArrayList<Integer>();
			for(RcMerchant merchant : merchants) {
				merchantIds.add(merchant.getMerchantId());
			}
			param.setMerchantIds(merchantIds);
		}
		PageContext.setPage(page);
		List<RcMerchantWid> merchantWids = merchantWidMapper.select(param);
		for(RcMerchantWid merchantWid : merchantWids) {
			merchantWid.setMerchantName(merchantService.findMerchantById(merchantWid.getMerchantId()).getName());
		}
		return merchantWids;
	}

	@Override
	public RcMerchantWid findMerchantWidById(long merchantWidId) {
		return merchantWidMapper.selectByPrimaryKey(merchantWidId);
	}

	@Override
	public long modifyMerchantWid(RcMerchantWid merchantWid) {
		if(merchantWidMapper.updateByPrimaryKey(merchantWid) > 0) {
			return merchantWid.getMerchantWidId();
		}
		return 0;
	}

	@Override
	public RcMerchantWid findMerchantWid(RcMerchantWid merchantWid) {
		return merchantWidMapper.selectOne(merchantWid);
	}

	@Override
	public long addMerchantWid(RcMerchantWid merchantWid) {
		if(merchantWidMapper.insert(merchantWid) > 0) {
			return merchantWid.getMerchantWidId();
		}
		return 0;
	}
}
