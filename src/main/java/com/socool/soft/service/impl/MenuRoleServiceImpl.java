package com.socool.soft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcMenuRole;
import com.socool.soft.dao.RcMenuRoleMapper;
import com.socool.soft.service.IMenuRoleService;

@Service
public class MenuRoleServiceImpl implements IMenuRoleService {
	@Autowired
	private RcMenuRoleMapper menuRoleMapper;

	@Override
	public int addMenuRole(RcMenuRole menuRole) {
		if(menuRoleMapper.insert(menuRole) > 0) {
			return menuRole.getMenuRoleId();
		}
		return 0;
	}

	@Override
	public List<RcMenuRole> findMenuRolesByRoleId(int roleId) {
		RcMenuRole param = new RcMenuRole();
		param.setRoleId(roleId);
		return menuRoleMapper.select(param);
	}

	@Override
	public int removeMenuRolesByRoleId(int roleId) {
		RcMenuRole param = new RcMenuRole();
		param.setRoleId(roleId);
		return menuRoleMapper.delete(param);
	}

	@Override
	public int removeMenuRolesByMenuId(int menuId) {
		RcMenuRole param = new RcMenuRole();
		param.setMenuId(menuId);
		return menuRoleMapper.delete(param);
	}
}
