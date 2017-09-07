package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcAppMenu;
import com.socool.soft.vo.Page;

public interface IAppMenuService {

	List<RcAppMenu> findAllAppMenus();
	
	List<RcAppMenu> findAllPagedAppMenus(Page page);
	
	int addAppMenu(RcAppMenu appMenu);
	
	int removeAppMenu(int appMenuId);
}
