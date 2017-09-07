package com.socool.soft.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcAppLog;
import com.socool.soft.dao.RcAppLogMapper;
import com.socool.soft.service.IAppLogService;

@Service
public class AppLogServiceImpl implements IAppLogService {
	@Autowired
	private RcAppLogMapper appLogMapper;

	@Override
	public long addAppLog(RcAppLog appLog) {
		appLog.setCreateTime(new Date());
		if(appLogMapper.insert(appLog) > 0) {
			return appLog.getAppLogId();
		}
		return 0;
	}
}
