package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcUserRole;
import com.socool.soft.bo.RcRole;
import com.socool.soft.dao.RcRoleMapper;
import com.socool.soft.service.IUserRoleService;
import com.socool.soft.service.IRoleService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class RoleServiceImpl implements IRoleService {
	@Autowired
	private RcRoleMapper roleMapper;
	@Autowired
	private IUserRoleService userRoleService;

	@Override
	public List<RcRole> findAllRoles() {
		return roleMapper.select(null);
	}

	@Override
	public List<RcRole> findAllPagedRoles(Page page) {
		PageContext.setPage(page);
		return roleMapper.select(null);
	}

	@Override
	public int addRole(RcRole role) {
		if(roleMapper.insert(role) > 0) {
			return role.getRoleId();
		}
		return 0;
	}

	@Override
	public RcRole findRoleById(int roleId) {
		return roleMapper.selectByPrimaryKey(roleId); 
	}

	@Override
	public int modifyRole(RcRole role) {
		if(roleMapper.updateByPrimaryKey(role) > 0) {
			return role.getRoleId();
		}
		return 0;
	}

	@Override
	public List<RcRole> findRolesByUserId(int userId) {
		List<RcUserRole> userRoles = userRoleService.findUserRolesByUserId(userId);
		if(CollectionUtils.isEmpty(userRoles)) {
			return new ArrayList<RcRole>();
		}
		List<Integer> roleIds = new ArrayList<Integer>();
		for(RcUserRole userRole : userRoles) {
			roleIds.add(userRole.getRoleId());
		}
		RcRole param = new RcRole();
		param.setRoleIds(roleIds);
		return roleMapper.select(param);
	}
}
