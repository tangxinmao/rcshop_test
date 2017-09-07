/*package com.socool.soft.util;

import org.springframework.beans.factory.annotation.Autowired;

import com.socool.soft.RestfulConstants;
import com.socool.soft.entity.Fileinfo;
import com.socool.soft.service.api.IFileInfoService;

public class ImageUtil {
	@Autowired
	private IFileInfoService iFileInfoService;
	*//**
	 * 获取图片真实路径
	 * @param imgURL
	 * @return
	 *//*
	public  String imgURL(String imgURL){
		Fileinfo fileinfo = iFileInfoService.getFileInfoByFileId(imgURL);
		if (fileinfo != null) {
		return RestfulConstants.FILE_SERVER + "/static/shop/"+ fileinfo.getFileaname();
		}
		return null;
	
	}
}
*/