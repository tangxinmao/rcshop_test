package com.socool.soft.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.socool.soft.bo.RcFileInfo;
import com.socool.soft.bo.RcImageSpace;
import com.socool.soft.bo.constant.ImageSpaceTypeEnum;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.service.IFileInfoService;
import com.socool.soft.service.IImageSpaceService;
import com.socool.soft.util.JsonUtil;
import com.socool.soft.vo.Page;

@Controller
@RequestMapping(value = "/imageopt")
public class ImageSpaceController extends BaseController {

	
	@Autowired
	private IFileInfoService fileInfoService;
	@Autowired
	private IImageSpaceService imageSpaceService;
	@Autowired
	private PropertyConstants propertyConstants;
	
	//private static final String  merchantId = "0";
	
	/**
	 * 显示图片空间（大图模式）
	 * 
	 */
	@RequestMapping(value = "/init")
	public String initImage(Integer currentPage,String searchKey,Long parentId,HttpServletRequest request, Model model){
		Integer memberId = getUserId(request);
		if(memberId == null) {
			memberId = getMerchantUserId(request);
		}
		if(memberId == null) {
			memberId = getVendorUserId(request);
		}
		boolean flag = false;
		if(currentPage == null){
			currentPage = 1;
			flag = true;
		}
		if(parentId == null){
			parentId = 0l;
		}
		long totalSize = imageSpaceService.countImageSpaceSizeByMemberId(memberId);
		BigDecimal size = countSize(totalSize);
		int[] imageSpaceNums = imageSpaceService.countImageSpaceNum(memberId);
//		String encodeMemberId = SimpleAESUtil.encrypt(String.valueOf(memberId));
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		if(parentId.equals("0")) {
			page.setPageSize(30);
		} else {
			page.setPageSize(29);
		}
		page.setCurrentPage(currentPage); 
		model.addAttribute("page", page);
		List<RcImageSpace> imageSpaces = new ArrayList<RcImageSpace>();
		if(StringUtils.isNotBlank(searchKey)){
			RcImageSpace param = new RcImageSpace();
			param.setDelFlag(YesOrNoEnum.NO.getValue());
			param.setName(searchKey);
			param.setMemberId(memberId);
			imageSpaces = imageSpaceService.findPagedImageSpacesWithFileInfo(param, page); 
			parentId = 0l;
		} else{
			RcImageSpace param = new RcImageSpace();
			param.setDelFlag(YesOrNoEnum.NO.getValue());
			param.setParentId(parentId);
			param.setMemberId(memberId);
			imageSpaces = imageSpaceService.findPagedImageSpacesWithFileInfo(param, page);
		}
		for (RcImageSpace imageSpace : imageSpaces) {
			if (imageSpace.getType() == ImageSpaceTypeEnum.IMAGE.getValue()) {   //图片
				imageSpace.setImgUrl(propertyConstants.systemFileServerUrl + imageSpace.getFileInfo().getPath());
			} else {
				imageSpace.setImgUrl("/rcshop/static/images/sellercenter_bg/imgs/imgitem1.png");
			}
		}
		setData(model, new String[] { "ImageObj", "dirnum", "imagenum",
				"merchantid","spacesize","dirurl","parentId","encodeMerchantId" }, new Object[] {imageSpaces,  imageSpaceNums[0], imageSpaceNums[1],
				memberId,size,propertyConstants.systemFileServerUrl,parentId,null});

		if(flag){
			return "img_space";
		}
		return "img_space_inner";
	}
	
	/**
	 * 显示图片空间（列表模式）
	 * 
	 * @param parentid
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/initlist")
	public String initImageList(Integer currentPage,String searchKey,Long parentId,HttpServletRequest request, Model model) throws Exception {
		Integer memberId = getUserId(request);
		if(memberId == null) {
			memberId = getMerchantUserId(request);
		}
		if(memberId == null) {
			memberId = getVendorUserId(request);
		}
		boolean flag = false;
		if(currentPage == null){
			currentPage =1;
			flag = true;
		}
		if(parentId == null){
			parentId = 0l;
		}
//		String encodeMemberId = SimpleAESUtil.encrypt(String.valueOf(memberId));
		// 计算图片空间大小
		long totalSize = imageSpaceService.countImageSpaceSizeByMemberId(memberId);
		BigDecimal size = countSize(totalSize);
		model.addAttribute("spacesize", size);
		int[] imageSpaceNums = imageSpaceService.countImageSpaceNum(memberId);
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcImageSpace> imageSpaces = new ArrayList<RcImageSpace>();
		if(StringUtils.isNotBlank(searchKey)){
			RcImageSpace param = new RcImageSpace();
			param.setDelFlag(YesOrNoEnum.NO.getValue());
			param.setName(searchKey);
			param.setMemberId(memberId);
			param.setType(ImageSpaceTypeEnum.IMAGE.getValue());
			imageSpaces = imageSpaceService.findPagedImageSpacesWithFileInfo(param, page); 
		} else{
			RcImageSpace param = new RcImageSpace();
			param.setMemberId(memberId);
			param.setParentId(parentId);
			param.setType(ImageSpaceTypeEnum.IMAGE.getValue());
			param.setDelFlag(YesOrNoEnum.NO.getValue());
			imageSpaces = imageSpaceService.findPagedImageSpacesWithFileInfo(param, page);
		}
		
		
		for (RcImageSpace imageSpace : imageSpaces) {
			if (imageSpace.getType() == ImageSpaceTypeEnum.IMAGE.getValue()) {   //图片
				imageSpace.setImgUrl(propertyConstants.systemFileServerUrl + imageSpace.getFileInfo().getPath());
			} else {
				imageSpace.setImgUrl("/rcshop/static/images/sellercenter_bg/imgs/imgitem1.png");
			}
		}

		setData(model, new String[] { "ImageObj", "dirnum", "imagenum",
				"merchantid","encodeMerchantId" }, new Object[] { imageSpaces,
				imageSpaceNums[0], imageSpaceNums[1], memberId,null });
		if(flag){
			return "img_space_list";
		}
		
		return "img_space_list_inner";
	}

	/**
	 * 回收站数据;
	 */
	@RequestMapping(value = "/initdeletedlist")
	public String initDeletedList(Integer currentPage,String searchKey,HttpServletRequest request, Model model) throws Exception {
		Integer memberId = getUserId(request);
		if(memberId == null) {
			memberId = getMerchantUserId(request);
		}
		if(memberId == null) {
			memberId = getVendorUserId(request);
		}
		boolean flag = false;
		if(currentPage == null){
			currentPage = 1;
			flag = true;
		}
//		String encodeMerchantId = SimpleAESUtil.encrypt(String.valueOf(memberId));
		// 计算图片空间大小
		long totalSize = imageSpaceService.countImageSpaceSizeByMemberId(memberId);
		BigDecimal size = countSize(totalSize);
		model.addAttribute("spacesize", size);
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(15);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		RcImageSpace param = new RcImageSpace();
		param.setDelFlag(YesOrNoEnum.YES.getValue());
		param.setName(searchKey);
		param.setMemberId(memberId);
		List<RcImageSpace> imageSpaces = imageSpaceService.findPagedImageSpacesWithFileInfo(param, page); 
		
		for (RcImageSpace imageSpace : imageSpaces) {
			if (imageSpace.getType() == ImageSpaceTypeEnum.IMAGE.getValue()) {   //图片
				imageSpace.setImgUrl(propertyConstants.systemFileServerUrl + imageSpace.getFileInfo().getPath());
			} else {
				RcImageSpace imageSpaceParam = new RcImageSpace();
				imageSpaceParam.setMemberId(memberId);
				imageSpaceParam.setParentId(imageSpace.getImageSpaceId());
				imageSpaceParam.setType(ImageSpaceTypeEnum.IMAGE.getValue());
				List<RcImageSpace> dirImageSpaces = imageSpaceService.findImageSpacesWithFileInfo(imageSpaceParam);
				long folderSize = 0l;
				for(RcImageSpace dirImageSpace : dirImageSpaces) {
					if(dirImageSpace.getType() == ImageSpaceTypeEnum.IMAGE.getValue()) {
						folderSize += dirImageSpace.getImgSize();
					}
				}
				imageSpace.setImgSize(folderSize);
				imageSpace.setImgUrl("/rcshop/static/images/sellercenter_bg/imgs/imgitem1.png");
			}
		}
		setData(model, new String[] { "ImageObj", "merchantid","encodeMerchantId" }, new Object[] { imageSpaces, memberId,null });
		if(flag){
			return "img_space_trash";
		}
		return "img_space_trash_inner";
	}

	/**
	 * 上传图片到临时空间
	 * 
	 * @param request
	 * @param resp
	 * @param model
	 * @return
	 * @throws Exception 
	 */
//	@RequestMapping(value = "/upload")
//	public String uploadImage(HttpServletRequest request,
//			HttpServletResponse resp, Model model) throws Exception {
//		RcMember rcMember = getMember(request);
//		Integer memberId = rcMember.getMemberId();
//		String buzid = request.getParameter("path");
//		String path = "";
//		if (!buzid.equals("0")) {
//			//RcImagespace b2cImagespace = iImageSpaceService.getInfoByBuzId(buzid);
//			path = buzid;
//		}
//		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//		MultipartFile mufile = multipartRequest.getFile("Filedata");
//		if(mufile.getSize() > 5 * 1024 * 1024){
//			model.addAttribute("errormsg", "图片大小不能大于5M！");
//			return "subpage";
//		}
//		Fileinfo fileinfo = iImageSpaceService.uploadImageToTemp(memberId,
//				mufile, path);
//		String URL = imgurl
//				+ fileinfo.getRelapath() + fileinfo.getFileaname();
//		String fileid = fileinfo.getFileid();
//		model.addAttribute("url", URL);
//		model.addAttribute("fileid", fileid);
//		// fileInfoService.addFile(UUID, "", uploadPath, abspath, mufile);
//		return "subpage";
//	}
	/**
	 * 上传图片到临时空间
	 * 
	 * @param request
	 * @param resp
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/flexupload")
	@ResponseBody
	public String flexuploadImage(String merchantId,HttpServletRequest request,
			HttpServletResponse resp, Model model) throws Exception {
		Integer memberId = getUserId(request);
		if(memberId == null) {
			memberId = getMerchantUserId(request);
		}
		if(memberId == null) {
			memberId = getVendorUserId(request);
		}
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if(multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
            Iterator<String> iter = multipartRequest.getFileNames();
            while(iter.hasNext()) {
                MultipartFile multipartFile = multipartRequest.getFile(iter.next());
                if(multipartFile != null) {
                    String fileName = multipartFile.getOriginalFilename();
                    if(StringUtils.isNotBlank(fileName)) {
                    	if(multipartFile.getSize() > 5 * 1024 * 1024){
                    		return "{\"code\":0}";
                		} else {
                			String fileInfoId = UUID.randomUUID().toString().replaceAll("-", "");
                			int pos = fileName.lastIndexOf(".");
                			String originalName = fileName.substring(0, pos);
                			String suffixName = fileName.substring(pos + 1, fileName.length());
                			String realFileName = fileInfoId;
                			Calendar cal = Calendar.getInstance();
                			int year = cal.get(Calendar.YEAR);
                			int month = cal.get(Calendar.MONTH) + 1;
                			int day = cal.get(Calendar.DATE);
                			String relativePath = year + File.separator + month + File.separator + day + File.separator;
                			String dirPath = propertyConstants.systemFileServerRoot + relativePath;
                			File dir = new File(dirPath);
                			if(!dir.exists()) {
                				dir.mkdirs();
                			}
                			File file = new File(dirPath + realFileName + "." + suffixName);
                			multipartFile.transferTo(file);
                			RcFileInfo fileInfo = new RcFileInfo();
                			fileInfo.setFileInfoId(fileInfoId);
                			fileInfo.setOriginalName(originalName);
                			fileInfo.setRealName(realFileName);
                			fileInfo.setSuffixName(suffixName);
                			fileInfo.setSize(multipartFile.getSize());
                			fileInfo.setRelativePath(relativePath);
                			fileInfo.setMemberId(memberId);
                			fileInfoService.addFileInfo(fileInfo);
                			return "{\"url\":\""+propertyConstants.systemFileServerUrl + fileInfo.getPath()+"\",\"fileid\":\""+realFileName+"\"}";
                		}
                    }
                }
            }
        }
        return "{\"code\":0}";
	}
	/**
	 * 删除临时空间的图片
	 * 
	 * @param request
	 * @param resp
	 * @throws IOException
	 */
//	@RequestMapping(value = "/deltempimage")
//	public void delTempimage(HttpServletRequest request,
//			HttpServletResponse resp) throws IOException {
//		String fileid = request.getParameter("fileid");
//		if (!fileInfoService.removeFile(fileid)) {
//			this.outputString(resp, "{\"result\":\"删除失败\",\"code\":\"0\"}");
//		}
//		this.outputString(resp, "{\"code\":\"1\"}");
//	}

	/**
	 * 保存临时空间图片信息
	 * 
	 * @param fileids
	 * @param request
	 * @param resp
	 * @throws Exception 
	 */
	@RequestMapping(value = "/saveimageinfo")
	@ResponseBody
	public String saveImageInfo(String fileids, Integer parentid,
			HttpServletRequest request, HttpServletResponse resp)
			throws Exception {
		Integer memberId = getUserId(request);
		if(memberId == null) {
			memberId = getMerchantUserId(request);
		}
		if(memberId == null) {
			memberId = getVendorUserId(request);
		}
		String[] fileid = fileids.split(",,,");
		long result = 0;
		for (int i = 0; i < fileid.length; i++) {
			result = imageSpaceService.addImage(memberId, fileid[i], parentid);
		}
		if (result == 1) {
			return "{\"result\":\"保存成功\"}";
		} else {
			return "{\"result\":\"保存失败，请联系管理员！\"}";
		}
	}

	/**
	 * 根据父ID显示图片
	 * 
	 * @param parentid
	 * @param request
	 * @param resp
	 * @param model
	 * @throws IOException
	 */
	@RequestMapping(value = "/queryimage")
	@ResponseBody
	public String queryImage(Long parentId, HttpServletRequest request,
			HttpServletResponse resp, Model model) throws IOException {
		Integer memberId = getUserId(request);
		if(memberId == null) {
			memberId = getMerchantUserId(request);
		}
		if(memberId == null) {
			memberId = getVendorUserId(request);
		}
		RcImageSpace param = new RcImageSpace();
		param.setMemberId(memberId);
		param.setParentId(parentId);
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		List<RcImageSpace> imageSpaces = imageSpaceService.findImageSpacesWithFileInfo(param);
		for(RcImageSpace imageSpace : imageSpaces){
			if(imageSpace.getType() == ImageSpaceTypeEnum.IMAGE.getValue()){
				imageSpace.setImgUrl(propertyConstants.systemFileServerUrl + imageSpace.getFileInfo().getPath());
			} else {
				imageSpace.setImgUrl("/rcshop/static/images/sellercenter_bg/imgs/imgitem1.png");
			}
		}
		String jsonString = JsonUtil.toJson(imageSpaces);
		return jsonString;
	}

	/**
	 * 新建文件夹
	 * 
	 * @param dirname
	 * @param parentid
	 * @param request
	 * @param resp
	 * @throws IOException
	 */
	@RequestMapping(value = "/newfolder")
	@ResponseBody
	public String newFolder(String dirname, Long parentId,
			HttpServletRequest request, HttpServletResponse resp)
			throws IOException {
		Integer memberId = getUserId(request);
		if(memberId == null) {
			memberId = getMerchantUserId(request);
		}
		if(memberId == null) {
			memberId = getVendorUserId(request);
		}
		RcImageSpace param = new RcImageSpace();
		param.setMemberId(memberId);
		param.setName(dirname);
		param.setParentId(parentId);
		param.setType(ImageSpaceTypeEnum.FOLDER.getValue());
		RcImageSpace folder = imageSpaceService.findImageSpace(param);
		if(folder != null){
			return "{\"result\":\"文件夹名已存在\",\"code\":\"-1\"}";
		}
		imageSpaceService.addImageSpace(param);
		return "{\"result\":\"添加成功\",\"code\":\"1\"}";
	}
	
	@RequestMapping(value = "queryDirInfo")
	@ResponseBody
	public String queryDirInfo(HttpServletRequest request,
			HttpServletResponse resp) throws Exception {
		Integer memberId = getUserId(request);
		if(memberId == null) {
			memberId = getMerchantUserId(request);
		}
		if(memberId == null) {
			memberId = getVendorUserId(request);
		}
		RcImageSpace param = new RcImageSpace();
		param.setMemberId(memberId);
		param.setParentId(0l);
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		param.setType(ImageSpaceTypeEnum.FOLDER.getValue());
		List<RcImageSpace> imageSpaces = imageSpaceService.findImageSpaces(param);
		for (RcImageSpace imageSpace : imageSpaces) {
			imageSpace.setImgUrl("/rcshop/static/images/sellercenter_bg/imgs/imgitem1.png");
		}
		String jsonString = JsonUtil.toJson(imageSpaces);
		return jsonString;
	}
	/**
	 * 文件夹or图片重命名
	 * 
	 * @param buzid
	 * @param newname
	 * @param request
	 * @param resp
	 * @throws IOException
	 */
	@RequestMapping(value = "/rename")
	@ResponseBody
	public String rename(long buzid, String newname,
			HttpServletRequest request, HttpServletResponse resp)
			throws IOException {
		imageSpaceService.modifyImageSpaceName(newname, buzid);
		return "{\"result\":\"修改成功\",\"code\":\"1\"}";
	}
	/**
	 * 软删除图片及文件夹
	 * @param buzid
	 * @param request
	 * @param resp
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delinfo")
	@ResponseBody
	public String delImageOrDir(long buzid, HttpServletRequest request,
			HttpServletResponse resp) throws Exception {
		imageSpaceService.removeImageSpaceToRecycle(buzid);
		return "{\"result\":\"delete success\",\"code\":\"1\"}";
	}
	/**
	 * 批量删除
	 */
	@RequestMapping(value = "/batchdelete")
	public String batchDelete(Integer currentPage,String imgids,Long parentId,HttpServletRequest request,Model model) throws Exception{
		String[] idArr = imgids.split(",,,");
		for(int i=0;i<idArr.length;i++){
			imageSpaceService.removeImageSpaceToRecycle(Long.parseLong(idArr[i]));
		}
		return initImageList(currentPage, null, parentId,request, model);
	}
	/**
	 * 恢复图片
	 * @param buzid
	 * @param request
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/restoreinfo")
	@ResponseBody
	public String restoreImageOrDir(long buzid, HttpServletRequest request,
			HttpServletResponse resp) throws IOException {
		imageSpaceService.restoreImageSpaceFromRecycle(buzid);
		return "{\"result\":\"delete success!\",\"code\":\"1\"}";
	}
	/**
	 * 彻底删除图片（删除数据库中表的信息）
	 * @param buzid
	 * @param request
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/physicaldelete")
	@ResponseBody
	public String physicalDelete(long buzid, HttpServletRequest request,
			HttpServletResponse resp) throws IOException {
		imageSpaceService.removeImageSpace(buzid);
		return "{\"result\":\"delete success!\",\"code\":\"1\"}";
	}
	/**
	 * 查询文件夹里是否有图片
	 */
//	@RequestMapping(value = "/validation")
//	@ResponseBody
//	public String validation(Long buzid, HttpServletRequest request,
//			HttpServletResponse resp) throws Exception {
//		RcMember rcMember = getRcMemberInfo(request);
//		Integer memberId = rcMember.getMemberId();
//		RcImageSpace rcImagespace = iImageSpaceService.findRcImageSpaceById(buzid);
//		if(rcImagespace.getType()==0){
//			RcImageSpace query = new RcImageSpace();
//			query.setMemberId(memberId);
//			query.setParentId(rcImagespace.getImageSpaceId());
//			query.setType(1);
//			query.setDelFlag(YesOrNoEnum.NO.getValue());
//			List<RcImageSpace>  list = iImageSpaceService.findResultBySearch(query);
////		if(list.size()>0)
////			return "{\"result\":\"文件夹：'"+ecImagespace.getDirPath()+"'包含图片，是否删除？\",\"code\":\"11\"}";
////		else{
//			
//			Fileinfo fileinfo = fileInfoService.getFileInfoByFileId(query.getFieldId());
//			if(query.getFileType()==1 && ecImagespace.getIsUserd()==1){
//				return	"{\"result\":\"Some images is be used,it can not be deleted!\",\"code\":\"12\"}";
//			}
//			else
//				return	"{\"result\":\"success\",\"code\":\"10\"}";
//		}
//	}
//	@RequestMapping(value="/usedinfo")
//	@ResponseBody
//	public String getUsedInfoById(Long imgId){
//		return "{\"result\":0}";
//	}
//	/**
//	 * ztree
//	 * @param imgId
//	 * @return
//	 */
//	@RequestMapping(value="/function_list")
//	@ResponseBody
//	public String functionList(HttpServletRequest request,
//			HttpServletResponse resp){
//		RcMember rcMember = getRcMemberInfo(request);
//		Integer memberId = rcMember.getMemberId();
//		List<Map<String,Object>> list = iImageSpaceService.getAllMenu(merchantId);
//		String json = JsonUtil.getInstance().listToString(list);
//		return json;
//	}
	/**
	 * 查询所有文件夹
	 * @param request
	 * @param resp
	 * @return
	 */
	@RequestMapping(value="/listalldir")
	@ResponseBody
	public String listDir(HttpServletRequest request,
			HttpServletResponse resp){
		Integer memberId = getUserId(request);
		if(memberId == null) {
			memberId = getMerchantUserId(request);
		}
		if(memberId == null) {
			memberId = getVendorUserId(request);
		}
		RcImageSpace param = new RcImageSpace();
		param.setMemberId(memberId);
		param.setType(ImageSpaceTypeEnum.FOLDER.getValue());
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		List<RcImageSpace> imageSpaces = imageSpaceService.findImageSpacesWithFileInfo(param);
		String json = JsonUtil.toJson(imageSpaces);
		return json;
	}
	@RequestMapping(value="/changedir")
	@ResponseBody
	public String changeDir(String imgIds,Long parentId,HttpServletRequest request,
			HttpServletResponse resp){
		String[] ids = imgIds.split(",,,");
		for(int i=0;i<ids.length;i++){
			RcImageSpace ecImagespace = imageSpaceService.findImageSpaceById(Long.parseLong(ids[i]));
			if(ecImagespace.getType() == ImageSpaceTypeEnum.IMAGE.getValue()){
				ecImagespace.setParentId(parentId);
				imageSpaceService.modifyImageSpace(ecImagespace);
			}
		}
		return "{\"code\":\"1\"}";
	}
}
