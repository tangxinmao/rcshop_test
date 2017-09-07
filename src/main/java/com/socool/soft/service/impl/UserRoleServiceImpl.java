package com.socool.soft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcUserRole;
import com.socool.soft.dao.RcUserRoleMapper;
import com.socool.soft.service.IUserRoleService;

@Service
public class UserRoleServiceImpl implements IUserRoleService {
	@Autowired
	private RcUserRoleMapper userRoleMapper;

	@Override
	public int addUserRole(RcUserRole userRole) {
		if(userRoleMapper.insert(userRole) > 0) {
			return userRole.getMemberRoleId();
		}
		return 0;
	}

	@Override
	public List<RcUserRole> findUserRolesByRoleId(int roleId) {
		RcUserRole param = new RcUserRole();
		param.setRoleId(roleId);
		return userRoleMapper.select(param);
	}

	@Override
	public int removeUserRolesByUserId(int userId) {
		RcUserRole param = new RcUserRole();
		param.setMemberId(userId);
		return userRoleMapper.delete(param);
	}

//	@Override
//	public RcUserRole findMemberRoleByMemberId(int memberId) {
//		RcUserRole param = new RcUserRole();
//		param.setMemberId(memberId);
//		return memberRoleMapper.selectOne(param);
//	}

//	@Override
//	public int removeMemberRolesByRoleId(int roleId) {
//		RcUserRole param = new RcUserRole();
//		param.setRoleId(roleId);
//		return memberRoleMapper.delete(param);
//	}

	@Override
	public List<RcUserRole> findUserRolesByUserId(int userId) {
		RcUserRole param = new RcUserRole();
		param.setMemberId(userId);
		return userRoleMapper.select(param);
	}
}
