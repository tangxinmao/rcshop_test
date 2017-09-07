package com.socool.soft.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcAppVersion;
import com.socool.soft.dao.RcAppVersionMapper;
import com.socool.soft.service.IAppVersionService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class AppVersionServiceImpl implements IAppVersionService {
	@Autowired
	private RcAppVersionMapper appVersionMapper;

	@Override
	public RcAppVersion findAppVersionBySystem(int system) {
		RcAppVersion param = new RcAppVersion();
		param.setSystem(system);
		return appVersionMapper.selectOne(param);
	}

	@Override
	public RcAppVersion findAppVersionBySystemAndVersion(int system, String verNo) {
		RcAppVersion param = new RcAppVersion();
		param.setSystem(system);
		param.setVerNoFrom(verNo);
		return appVersionMapper.selectOne(param);
	}


	@Override
	public List<RcAppVersion> findAllPagedAppVersions(Page page) {
		PageContext.setPage(page);
		return appVersionMapper.select(null);
	}

	@Override
	public int modifyAppVersion(RcAppVersion appVersion) {
		appVersion.setUpdateTime(new Date());
		return appVersionMapper.updateByPrimaryKey(appVersion);
	}
}
