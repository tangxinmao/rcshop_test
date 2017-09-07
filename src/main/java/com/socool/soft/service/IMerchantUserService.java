package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcMerchantUser;
import com.socool.soft.exception.SystemException;

public interface IMerchantUserService {
	/**
	 * 查询用户信息 排序注册时间和最后登录时间排序
	 * 
	 * @param rcMember
	 * @return
	 */
//	List<RcMerchantUser> findMerchantUsers(RcMerchantUser merchantUser);
//	List<RcMerchantUser> findPagedMerchantUsers(RcMerchantUser merchantUser, Page page);
	List<RcMerchantUser> findMerchantUsersByMerchantId(int merchantId);

	/**
	 * 保存用户信息
	 * 
	 * @param rcMember
	 * @return
	 */
	int addMerchantUser(RcMerchantUser merchantUser);
	
	/**
	 * 通过memberId查询member
	 * 
	 * @param merchantUserId
	 * @return
	 */
	RcMerchantUser findMerchantUserById(int merchantUserId);


	/**
	 * 通过email
	 * 
	 * @param email
	 * @return
	 */
	RcMerchantUser findMerchantUserByEmail(String email);

	/**
	 * 通过email
	 * 
	 * @param email
	 * @return
	 */
	RcMerchantUser findMerchantUserByMobile(String mobile);
	
	/**
	 * 修改用户
	 * 
	 * @param rcMember
	 */

	int modifyMerchantUser(RcMerchantUser merchantUser);

	/**
	 * 查询用户
	 * 
	 * @param rcMember
	 * @return
	 */
//	RcMerchantUser findMerchantUser(RcMerchantUser merchantUser);

	/**
	 * 删除用户
	 * 
	 * @param merchantUserId
	 * @return
	 */
	int removeMerchantUser(int merchantUserId);

	/**
	 * 根据账户查询用户
	 * @param account
	 * @return
	 */
	RcMerchantUser findMerchantUserByAccount(String account);
	
	RcMerchantUser findMerchantSuperAdmin(int merchantId);

}
