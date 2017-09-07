package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcRole;
import com.socool.soft.vo.Page;

public interface IRoleService {
	/**
	 * 查询所有角色
	 * 
	 * @return
	 */
	List<RcRole> findAllRoles();
	List<RcRole> findAllPagedRoles(Page page);

	/**
	 * 保存角色
	 * 
	 * @param rcRole
	 * @return
	 */
	int addRole(RcRole role);

	/**
	 * 
	 * @param roleId
	 * @return
	 */
	RcRole findRoleById(int roleId);

	/**
	 * 
	 * @param rcRoleDb
	 * @return
	 */
	int modifyRole(RcRole role);

	/**
	 * 
	 * @param memberId
	 * @return
	 */

	List<RcRole> findRolesByUserId(int userId);
}
