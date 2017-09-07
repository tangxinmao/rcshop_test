package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcAppVersion;
import com.socool.soft.vo.Page;

public interface IAppVersionService {

	/**
	 * 根据系统类型获取
	 * @param system
	 * @return
	 */
	RcAppVersion findAppVersionBySystem(int system);
	
	RcAppVersion findAppVersionBySystemAndVersion(int system, String verNo);
	
	/**
	 * 查询所有版本信息
	 * @return
	 */
	List<RcAppVersion> findAllPagedAppVersions(Page page);
	
	/**
	 * 修改版本信息
	 * @param rcVersion
	 * @return
	 */
	int modifyAppVersion(RcAppVersion appVersion);
}
