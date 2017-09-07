package com.socool.soft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcDokuUserToken;
import com.socool.soft.dao.RcDokuUserTokenMapper;
import com.socool.soft.service.IDokuUserTokenService;

@Service
public class DokuUserTokenServiceImpl implements IDokuUserTokenService {

	@Autowired
	private RcDokuUserTokenMapper dokuUserTokenMapper;
	
	@Override
	public int saveDokuToken(RcDokuUserToken dokuUserToken) {
		// TODO Auto-generated method stub
		return dokuUserTokenMapper.insert(dokuUserToken);
	}

	@Override
	public int modifyDokuToken(RcDokuUserToken dokuUserToken) {
		// TODO Auto-generated method stub
		return dokuUserTokenMapper.updateByPrimaryKey(dokuUserToken);
	}

	@Override
	public RcDokuUserToken findByMemberId(int memberId) {
		// TODO Auto-generated method stub
		return dokuUserTokenMapper.selectByPrimaryKey(memberId);
	}

}
