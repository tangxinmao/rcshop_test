package com.socool.soft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcAppMenu;
import com.socool.soft.dao.RcAppMenuMapper;
import com.socool.soft.service.IAppMenuService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class AppMenuServiceImpl implements IAppMenuService {
	@Autowired
	private RcAppMenuMapper appMenuMapper;

	@Override
	public List<RcAppMenu> findAllAppMenus() {
		RcAppMenu param = new RcAppMenu();
		param.setOrderBy("SEQ ASC");
		return appMenuMapper.select(param);
	}
	
	@Override
	public int addAppMenu(RcAppMenu appMenu) {
		if(appMenuMapper.insert(appMenu) > 0) {
			return appMenu.getAppMenuId();
		}
		return 0;
	}

	@Override
	public int removeAppMenu(int appMenuId) {
		return appMenuMapper.deleteByPrimaryKey(appMenuId);
	}

	@Override
	public List<RcAppMenu> findAllPagedAppMenus(Page page) {
		PageContext.setPage(page);
		RcAppMenu param = new RcAppMenu();
		param.setOrderBy("SEQ ASC");
		return appMenuMapper.select(param);
	}
}
