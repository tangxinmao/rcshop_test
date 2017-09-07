package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcImageSpace;
import com.socool.soft.vo.Page;

public interface IImageSpaceService {
	
	/**
	 * 上传图片
	 * @param memberId
	 * @param mufile
	 * @return
	 */
//	Map<String,String> uploadImage(int memberId,MultipartFile mufile);

	/**
	 * 查询用户已使用的空间大小
	 * @param memberId
	 * @return
	 */
	long countImageSpaceSizeByMemberId(int memberId);
	
	List<RcImageSpace> findImageSpacesByMemberId(int memberId);
	
	/**
	 * 查询用户文件夹数量
	 * @param memberId
	 * @return
	 */
	int[] countImageSpaceNum(int memberId);
	
	/**
	 * /**
	 * 关键字模糊查询
	 * @param memberId
	 * @param searchKey 文件夹OR图片名称
	 * @param delFlag 删除状态
	 * @return
	 */
//	List<RcImageSpace> findImageSpacesBykey(int memberId,String searchKey,byte delFalse);
	
	/**
	 * 上传图片到临时空间
	 * @param memberId
	 * @param mufile
	 * @param path
	 * @return
	 */
//	Fileinfo uploadImageToTemp(int memberId,MultipartFile mufile,String path);
	
	/**
	 * 保存临时空间图片
	 * @param memberId
	 * @param fileid
	 * @param parentid
	 * @return
	 */
	long addImage(int memberId, String fileInfoId, long parentId);
	
	/**
	 * 基本查询
	 * @param rcImageSpace
	 * @return
	 */
	List<RcImageSpace> findImageSpacesWithFileInfo(RcImageSpace param);
	List<RcImageSpace> findPagedImageSpacesWithFileInfo(RcImageSpace param, Page page);
	List<RcImageSpace> findImageSpaces(RcImageSpace param);
	RcImageSpace findImageSpace(RcImageSpace imageSpace);
	
	/**
	 * 添加数据
	 * @param rcImageSpace
	 * @return
	 */
	long addImageSpace(RcImageSpace imageSpace);
	
	/**
	 * 根据ID查询
	 * @param imageSpaceId
	 * @return
	 */
	RcImageSpace findImageSpaceById(long imageSpaceId);
	
	/**
	 * 修改数据
	 * @param rcImageSpace
	 * @return
	 */
	long modifyImageSpace(RcImageSpace imageSpace);
	
	/**
	 * 删除数据
	 * @param imageSpaceId
	 * @return
	 */
	int removeImageSpace(long imageSpaceId);
	
	int removeImageSpaceToRecycle(long imageSpaceId);
	
	int restoreImageSpaceFromRecycle(long imageSpaceId);
	
	/**
	 * 文件夹/文件重命名
	 * @param newname
	 * @param imageSpaceId
	 * @param type 1=图片；0=文件夹
	 * @return
	 */
	int modifyImageSpaceName(String name, long imageSpaceId);
}
