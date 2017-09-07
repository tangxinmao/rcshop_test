package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcMenuRole;

public interface IMenuRoleService {
	
	int addMenuRole(RcMenuRole menuRole);

	List<RcMenuRole> findMenuRolesByRoleId(int roleId);
	
	int removeMenuRolesByRoleId(int roleId);
	
	int removeMenuRolesByMenuId(int menuId);
}
