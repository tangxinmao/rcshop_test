package com.socool.soft.service;

import com.socool.soft.bo.RcDokuUserToken;

public interface IDokuUserTokenService {
	
	int saveDokuToken(RcDokuUserToken dokuUserToken);
	
	int modifyDokuToken(RcDokuUserToken dokuUserToken);
	
	RcDokuUserToken findByMemberId(int memberId);

}
