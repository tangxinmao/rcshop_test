package com.socool.soft.service;

import com.socool.soft.bo.RcUserGetui;

public interface IUserGetuiService {
    /**
     * 用户 个推cid绑定
     * @param memberId
     * @param cid
     */
	void userBind(Integer memberId, String cid);

    /**
     * 根据memberId获取对象
     * @param memberId
     * @return
     */
	RcUserGetui getByMemberId(Integer memberId);
}
