package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcBuyer;
import com.socool.soft.exception.SystemException;
import com.socool.soft.vo.Page;

public interface IBuyerService {
	/**
	 * 查询用户信息 排序注册时间和最后登录时间排序
	 * 
	 * @param rcMember
	 * @return
	 */
	List<RcBuyer> findBuyers(RcBuyer param);
	List<RcBuyer> findPagedBuyers(RcBuyer param, Page page);

	int addBuyer(RcBuyer buyer);

	/**
	 * 通过memberId查询member
	 * 
	 * @param buyerId
	 * @return
	 */
	RcBuyer findBuyerById(int buyerId);


	/**
	 * 通过email
	 * 
	 * @param email
	 * @return
	 */
	RcBuyer findBuyerByEmail(String email);

	RcBuyer findBuyerByMobile(String mobile);

	/**
	 * 修改用户
	 * 
	 * @param rcMember
	 */

	int modifyBuyer(RcBuyer buyer);

	/**
	 * 删除用户
	 * 
	 * @param buyerId
	 * @return
	 */
	int removeBuyer(int buyerId);

	/**
	 * 根据账户查询用户
	 * @param account
	 * @return
	 */
	RcBuyer findBuyerByAccount(String account);

    /**
     * 个推cid绑定
     * @param mobile
     * @param password
     * @param cid
     */
    void geTuiBind(String mobile, String password, String cid) throws SystemException;
}
