package com.socool.soft.service;

import com.socool.soft.bo.RcAppLog;

public interface IAppLogService {

	/**
	 * 插入数据
	 * @param rcAppLog
	 * @return 主键ID
	 */
	long addAppLog(RcAppLog appLog);
}
