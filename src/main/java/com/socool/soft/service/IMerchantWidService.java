package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcMerchantWid;
import com.socool.soft.vo.Page;

public interface IMerchantWidService {

	List<RcMerchantWid> findPagedMerchantWids(RcMerchantWid param, Page page);
	
	RcMerchantWid findMerchantWidById(long merchantWidId);
	
	long modifyMerchantWid(RcMerchantWid merchantWid);
	
	RcMerchantWid findMerchantWid(RcMerchantWid merchantWid);
	
	long addMerchantWid(RcMerchantWid merchantWid);
}
