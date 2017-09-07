package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcHelp;
import com.socool.soft.vo.Page;

public interface IHelpService {

	/**
	 * 查询所有帮助信息
	 * @return
	 */
	List<RcHelp> findAllHelps();
	
	List<RcHelp> findAllPagedHelps(Page page);
	
	/**
	 * 列出所有类型
	 * @return
	 */
	List<String> findAllDescriptions();
	
	/**
	 * 插入数据
	 * @param rcHelp
	 * @return 主键ID
	 */
	int addHelp(RcHelp help);
	
	/**
	 * 根据主键ID查询
	 * @param helpId
	 * @return
	 */
	RcHelp findHelpById(int helpId);
	
	/**
	 * 修改数据
	 * @param rcHelp
	 */
	int modifyHelp(RcHelp help);
	
	/**
	 * 删除数据
	 * @param helpId
	 */
	int removeHelp(int helpId);
}
