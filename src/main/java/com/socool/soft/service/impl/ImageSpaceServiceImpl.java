package com.socool.soft.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcFileInfo;
import com.socool.soft.bo.RcImageSpace;
import com.socool.soft.bo.constant.ImageSpaceTypeEnum;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.dao.RcImageSpaceMapper;
import com.socool.soft.service.IFileInfoService;
import com.socool.soft.service.IImageSpaceService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class ImageSpaceServiceImpl implements IImageSpaceService {
	@Autowired
	private RcImageSpaceMapper imageSpaceMapper;
	@Autowired
	private IFileInfoService fileInfoService;
	@Autowired
	private PropertyConstants propertyConstants;

	@Override
	public long countImageSpaceSizeByMemberId(int memberId) {
		List<RcImageSpace> imageSpaces = findImageSpacesByMemberId(memberId);
		long totalSize = 0l;
		for(RcImageSpace imageSpace : imageSpaces) {
			if(imageSpace.getType() == ImageSpaceTypeEnum.IMAGE.getValue()) {
				totalSize += imageSpace.getImgSize();
			}
		}
		return totalSize;
	}
	
	@Override
	public List<RcImageSpace> findImageSpacesByMemberId(int memberId) {
		RcImageSpace param = new RcImageSpace();
		param.setMemberId(memberId);
		return imageSpaceMapper.select(param);
	}

	@Override
	public int[] countImageSpaceNum(int memberId) {
		RcImageSpace param = new RcImageSpace();
		param.setMemberId(memberId);
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		List<RcImageSpace> imageSpaces = imageSpaceMapper.select(param);
		int folderNum = 0;
		int imgNum = 0;
		for(RcImageSpace imageSpace : imageSpaces) {
			if(imageSpace.getType() == ImageSpaceTypeEnum.FOLDER.getValue()) {
				folderNum++;
			} else if(imageSpace.getType() == ImageSpaceTypeEnum.IMAGE.getValue()) {
				imgNum++;
			}
		}
		return new int[] {folderNum, imgNum};
	}

//	@Override
//	public List<RcImageSpace> findImageSpacesBykey(int memberId, String searchKey,
//			byte delFalse) {
//		RcImageSpace param = new RcImageSpace();
//		param.setMemberId(memberId);
//		param.setName(searchKey);
//		param.setDelFlag(delFalse);
//		return findImageSpaces(param);
//	}

//	@Override
//	public Fileinfo uploadImageToTemp(int memberId, MultipartFile mufile,
//			String path) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public long addImage(int memberId, String fileInfoId, long parentId) {
		RcFileInfo fileInfo = fileInfoService.findFileInfoById(fileInfoId);
		RcImageSpace imageSpace = new RcImageSpace();
		imageSpace.setFileInfoId(fileInfoId);
		imageSpace.setMemberId(memberId);
		imageSpace.setParentId(parentId);
		imageSpace.setType(ImageSpaceTypeEnum.IMAGE.getValue());
		imageSpace.setName(fileInfo.getOriginalName() + "." + fileInfo.getSuffixName());
		imageSpace.setImgSize(fileInfo.getSize());
		File file = new File(propertyConstants.systemFileServerRoot + fileInfo.getRelativePath() + fileInfo.getRealName() + "." + fileInfo.getSuffixName());
		try {
			BufferedImage image = ImageIO.read(new FileInputStream(file));
			if (image != null) {
				imageSpace.setImgWidth(image.getWidth());
    			imageSpace.setImgHeight(image.getHeight());
			}
		} catch (IOException e) {
		}
		return addImageSpace(imageSpace);
	}

	@Override
	public List<RcImageSpace> findImageSpacesWithFileInfo(RcImageSpace param) {
		List<RcImageSpace> imageSpaces = imageSpaceMapper.select(param);
		for(RcImageSpace imageSpace : imageSpaces) {
			if(imageSpace.getType() == ImageSpaceTypeEnum.IMAGE.getValue()) {
				imageSpace.setFileInfo(fileInfoService.findFileInfoById(imageSpace.getFileInfoId()));
			}
		}
		return imageSpaces;
	}

	@Override
	public List<RcImageSpace> findPagedImageSpacesWithFileInfo(RcImageSpace param, Page page) {
		PageContext.setPage(page);
		List<RcImageSpace> imageSpaces = imageSpaceMapper.select(param);
		for(RcImageSpace imageSpace : imageSpaces) {
			if(imageSpace.getType() == ImageSpaceTypeEnum.IMAGE.getValue()) {
				imageSpace.setFileInfo(fileInfoService.findFileInfoById(imageSpace.getFileInfoId()));
			}
		}
		return imageSpaces;
	}

	@Override
	public List<RcImageSpace> findImageSpaces(RcImageSpace param) {
		return imageSpaceMapper.select(param);
	}

	@Override
	public long addImageSpace(RcImageSpace imageSpace) {
		imageSpace.setCreateTime(new Date());
		if(imageSpaceMapper.insert(imageSpace) > 0) {
			return imageSpace.getImageSpaceId();
		}
		return 0;
	}

	@Override
	public RcImageSpace findImageSpaceById(long imageSpaceId) {
		return imageSpaceMapper.selectByPrimaryKey(imageSpaceId);
	}

	@Override
	public long modifyImageSpace(RcImageSpace imageSpace) {
		if(imageSpaceMapper.updateByPrimaryKey(imageSpace) > 0) {
			return imageSpace.getImageSpaceId();
		}
		return 0;
	}

	@Override
	public int removeImageSpace(long imageSpaceId) {
		RcImageSpace imageSpace = findImageSpaceById(imageSpaceId);
		int result = imageSpaceMapper.deleteByPrimaryKey(imageSpaceId);
		if(result > 0 && imageSpace.getType() == ImageSpaceTypeEnum.FOLDER.getValue()) {
			RcImageSpace param = new RcImageSpace();
			param.setParentId(imageSpace.getImageSpaceId());
			List<RcImageSpace> images = imageSpaceMapper.select(param);
			for(RcImageSpace image : images) {
				removeImageSpace(image.getImageSpaceId());
			}
		}
		return result;
	}

	@Override
	public int removeImageSpaceToRecycle(long imageSpaceId) {
		RcImageSpace imageSpace = findImageSpaceById(imageSpaceId);
		imageSpace.setDelFlag(YesOrNoEnum.YES.getValue());
		imageSpace.setDelTime(new Date());
		if(modifyImageSpace(imageSpace) > 0 && imageSpace.getType() == ImageSpaceTypeEnum.FOLDER.getValue()) {
			RcImageSpace param = new RcImageSpace();
			param.setParentId(imageSpace.getImageSpaceId());
			List<RcImageSpace> images = imageSpaceMapper.select(param);
			for(RcImageSpace image : images) {
				removeImageSpaceToRecycle(image.getImageSpaceId());
			}
			return 1;
		}
		return 0;
	}

	@Override
	public int restoreImageSpaceFromRecycle(long imageSpaceId) {
		RcImageSpace imageSpace = findImageSpaceById(imageSpaceId);
		imageSpace.setDelFlag(YesOrNoEnum.NO.getValue());
		if(modifyImageSpace(imageSpace) > 0 && imageSpace.getParentId() > 0) {
			RcImageSpace folder = findImageSpaceById(imageSpace.getParentId());
			if(folder.getDelFlag() == YesOrNoEnum.YES.getValue()) {
				removeImageSpaceToRecycle(folder.getImageSpaceId());
			}
			return 1;
		}
		return 0;
	}

	@Override
	public int modifyImageSpaceName(String name, long imageSpaceId) {
		RcImageSpace param = new RcImageSpace();
		param.setImageSpaceId(imageSpaceId);
		param.setName(name);
		if(modifyImageSpace(param) > 0) {
			return 1;
		}
		return 0;
	}

//	@Override
//	public Map<String, String> uploadImage(int memberId, MultipartFile mufile) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public RcImageSpace findImageSpace(RcImageSpace imageSpace) {
		return imageSpaceMapper.selectOne(imageSpace);
	}
}
