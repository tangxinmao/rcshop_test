package com.socool.soft.service;

import com.socool.soft.bo.RcFileInfo;


public interface IFileInfoService {

	String addFileInfo(RcFileInfo fileInfo);
	
	RcFileInfo findFileInfoById(String fileInfoId);
}
