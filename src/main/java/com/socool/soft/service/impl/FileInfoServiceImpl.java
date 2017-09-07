package com.socool.soft.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcFileInfo;
import com.socool.soft.dao.RcFileInfoMapper;
import com.socool.soft.service.IFileInfoService;

@Service
public class FileInfoServiceImpl implements IFileInfoService {
	@Autowired
	private RcFileInfoMapper fileInfoMapper;

	@Override
	public String addFileInfo(RcFileInfo fileInfo) {
		fileInfo.setCreateTime(new Date());
		if(fileInfoMapper.insert(fileInfo) > 0) {
			return fileInfo.getFileInfoId();
		}
		return null;
	}

	@Override
	public RcFileInfo findFileInfoById(String fileInfoId) {
		return fileInfoMapper.selectByPrimaryKey(fileInfoId);
	}
}
