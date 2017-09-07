package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcMenu;

public interface IMenuService {

	/**
	 * 
	 * @param menuId
	 * @return
	 */
	int removeMenu(int menuId);

	/**
	 * 查询所有item
	 * 
	 * @return
	 */
	List<RcMenu> findAllMenus();

	/**
	 * @param id
	 * @return
	 */
	RcMenu findMenuById(int id);

	/**
	 * 查询菜单子节点
	 * 
	 * @param targetId
	 * @return
	 */
	List<RcMenu> findMenusByParentId(int parentId);

	/**
	 * 
	 * @param menu
	 * @return
	 */
	int modifyMenu(RcMenu menu);

	/**
	 * 
	 * @param menu
	 * @return
	 */
	int addMenu(RcMenu menu);

	/**
	 * 查询所有菜单并根据roleid标记checked字段
	 * 
	 * @param roleId
	 * @return
	 */
	List<RcMenu> findSortedMenusByRoleId(int roleId);
	
	List<RcMenu> findMenusByRoleId(int roleId);
	
	RcMenu findMenuByLocation(String location);
}
