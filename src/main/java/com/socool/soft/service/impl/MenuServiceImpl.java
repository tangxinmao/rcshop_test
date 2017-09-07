package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcMenu;
import com.socool.soft.bo.RcMenuRole;
import com.socool.soft.dao.RcMenuMapper;
import com.socool.soft.service.IMenuRoleService;
import com.socool.soft.service.IMenuService;

@Service
public class MenuServiceImpl implements IMenuService {
	@Autowired
	private RcMenuMapper menuMapper;
	@Autowired
	private IMenuRoleService menuRoleService;

	@Override
	public int removeMenu(int menuId) {
		int result = menuMapper.deleteByPrimaryKey(menuId);
		if(result > 0) {
			menuRoleService.removeMenuRolesByMenuId(menuId);
			List<RcMenu> childMenus = findMenusByParentId(menuId);
			for(RcMenu childMenu : childMenus) {
				removeMenu(childMenu.getMenuId());
			}
		}
		return result;
	}

	@Override
	public List<RcMenu> findAllMenus() {
		RcMenu param = new RcMenu();
		return menuMapper.select(param);
	}

	@Override
	public RcMenu findMenuById(int menuId) {
		return menuMapper.selectByPrimaryKey(menuId);
	}

	@Override
	public List<RcMenu> findMenusByParentId(int parentId) {
		RcMenu param = new RcMenu();
		param.setParentId(parentId);
		return menuMapper.select(param);
	}

	@Override
	public int modifyMenu(RcMenu menu) {
		return menuMapper.updateByPrimaryKey(menu);
	}

	@Override
	public int addMenu(RcMenu menu) {
		if(menu.getParentId() == null) {
			menu.setParentId(0);
		}
		return menuMapper.insert(menu);
	}

	@Override
	public List<RcMenu> findSortedMenusByRoleId(int roleId) {
		List<RcMenuRole> menuRoles = menuRoleService.findMenuRolesByRoleId(roleId);
		if(CollectionUtils.isEmpty(menuRoles)){
			return new ArrayList<RcMenu>();
		}
		List<Integer> menuIds = new ArrayList<Integer>();
		for(RcMenuRole menuRole : menuRoles) {
			menuIds.add(menuRole.getMenuId());
		}
		RcMenu param = new RcMenu();
		param.setMenuIds(menuIds);
		param.setOrderBy("SEQ ASC");
		return menuMapper.select(param);
	}

	@Override
	public List<RcMenu> findMenusByRoleId(int roleId) {
		List<RcMenuRole> menuRoles = menuRoleService.findMenuRolesByRoleId(roleId);
		if(CollectionUtils.isEmpty(menuRoles)){
			return new ArrayList<RcMenu>();
		}
		List<Integer> menuIds = new ArrayList<Integer>();
		for(RcMenuRole menuRole : menuRoles) {
			menuIds.add(menuRole.getMenuId());
		}
		RcMenu param = new RcMenu();
		param.setMenuIds(menuIds);
		return menuMapper.select(param);
	}

	@Override
	public RcMenu findMenuByLocation(String location) {
		RcMenu param = new RcMenu();
		param.setLocation(location);
		return menuMapper.selectOne(param);
	}
}
