package com.socool.soft.service;

import com.socool.soft.bo.RcVendorUser;

import java.util.List;

public interface IVendorUserService {
	/**
	 * 查询用户信息 排序注册时间和最后登录时间排序
	 * 
	 * @param rcMember
	 * @return
	 */
//	List<RcVendorUser> findVendorUsers(RcVendorUser param);
//	List<RcVendorUser> findPagedVendorUsers(RcVendorUser param, Page page);

	/**
	 * 保存用户信息
	 * 
	 * @param rcMember
	 * @return
	 */
	int addVendorUser(RcVendorUser vendorUser);

	/**
	 * 通过memberId查询member
	 * 
	 * @param vendorUserId
	 * @return
	 */
	RcVendorUser findVendorUserById(int vendorUserId);


	/**
	 * 通过email
	 * 
	 * @param email
	 * @return
	 */
	RcVendorUser findVendorUserByEmail(String email);

	/**
	 * 通过email
	 * 
	 * @param email
	 * @return
	 */
	RcVendorUser findVendorUserByMobile(String mobile);

	/**
	 * 修改用户
	 * 
	 * @param rcMember
	 */

	int modifyVendorUser(RcVendorUser vendorUser);

	/**
	 * 查询用户
	 * 
	 * @param rcMember
	 * @return
	 */
//	RcVendorUser findVendorUser(RcVendorUser param);

	/**
	 * 删除用户
	 * 
	 * @param vendorUserId
	 * @return
	 */
	int removeVendorUser(int vendorUserId);

	/**
	 * 根据账户查询用户
	 * @param account
	 * @return
	 */
	RcVendorUser findVendorUserByAccount(String account);
	
	RcVendorUser findVendorSuperAdmin(int vendorId);
	
	List<RcVendorUser> findVendorUsersByVendorId(int vendorId);

    int recoverVendorUser(Integer memberId);
}
