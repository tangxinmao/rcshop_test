package com.socool.soft.service;

import com.socool.soft.bo.RcMember;

public interface IMemberService {
	
	int addMember(RcMember member);
	
	/**
	 * 通过memberId查询member
	 * 
	 * @param memberId
	 * @return
	 */
	RcMember findMemberById(int memberId);


	/**
	 * 通过email
	 * 
	 * @param email
	 * @return
	 */
	RcMember findMemberByEmail(String email);

	/**
	 * 通过email
	 * 
	 * @param email
	 * @return
	 */
	RcMember findMemberByMobile(String mobile);
	

	/**
	 * 修改用户
	 * 
	 * @param rcMember
	 */

	int modifyMember(RcMember member);

	/**
	 * 根据账户查询用户
	 * @param account
	 * @return
	 */
	RcMember findMemberByAccount(String account);
}
