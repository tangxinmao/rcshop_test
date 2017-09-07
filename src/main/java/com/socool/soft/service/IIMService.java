package com.socool.soft.service;

import com.socool.soft.vo.IMNode;

public interface IIMService {

	/**
	 * 注册环信用户
	 * @param userId  memberId OR merchantId
	 * @param userPrefix  环信ID前缀
	 * @return
	 * @throws Exception
	 */
	IMNode regist(String hxId, String nickName) throws Exception ;
}
