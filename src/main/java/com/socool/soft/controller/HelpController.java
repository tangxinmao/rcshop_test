package com.socool.soft.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

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
import com.socool.soft.bo.RcHelp;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.service.IFileInfoService;
import com.socool.soft.service.IHelpService;
import com.socool.soft.vo.Page;

@RequestMapping(value="/help")
@Controller
public class HelpController extends BaseController {

	@Autowired
	private IHelpService helpService;
	
	@Autowired
	private IFileInfoService fileInfoService;
	
	@Autowired
	private PropertyConstants propertyConstants;
	
	@RequestMapping(value="/detail")
	public String detail(Model model,Integer currentPage){
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcHelp> helps = helpService.findAllPagedHelps(page);
		model.addAttribute("list", helps);
		
		if(flag) {
			return "help/helpList";
		} else {
			return "help/helpList_inner";    
		}
	}
	
	@RequestMapping(value="/add")
	public String add(Model model){
		List<String> descriptions = helpService.findAllDescriptions();
		model.addAttribute("type", descriptions);
		return "help/addHelp";
	}
	
	@RequestMapping(value="/save")
	@ResponseBody
	public String save(String name, String description, int seq, String content){
		RcHelp help = new RcHelp();
		help.setName(name);
		help.setDescription(description);
		help.setSeq(seq);
		help.setContent(content);
		int result = helpService.addHelp(help);
		if(result > 0) {
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}
	
	@RequestMapping(value="uploadimage")
	public String uploadCover(HttpServletRequest request,Model model) throws IllegalStateException, IOException{
		int userId = getUserId(request);
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if(multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
            Iterator<String> iter = multipartRequest.getFileNames();
            while(iter.hasNext()) {
                MultipartFile multipartFile = multipartRequest.getFile(iter.next());
                if(multipartFile != null) {
                    String fileName = multipartFile.getOriginalFilename();
                    if(StringUtils.isNotBlank(fileName)) {
                    	String imgUrl = "";
                    	if(multipartFile.getSize() > 5 * 1024 * 1024){
                			imgUrl = "001";
                			model.addAttribute("success",false);
                		} else if(multipartFile.getSize() == 0){
                			imgUrl = "002";
                			model.addAttribute("success",false);
                		} else {
                			String fileInfoId = UUID.randomUUID().toString().replaceAll("-", "");
                			int pos = fileName.lastIndexOf(".");
                			String originalName = fileName.substring(0, pos);
                			String suffixName = fileName.substring(pos + 1, fileName.length());
                			Pattern pattern = Pattern.compile("(png|PNG|GIF|gif|jpg|JPG|bmp|BMP)");
                			Matcher matcher = pattern.matcher(suffixName);
                			if(!matcher.matches()){
                				return "{\"code\":\"0\",\"result\":\"图片格式不正确（必须为.jpg/.gif/.bmp/.png图片）！\"}";
                			}
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
                			fileInfo.setMemberId(userId);
                			fileInfoService.addFileInfo(fileInfo);
                			imgUrl = propertyConstants.systemFileServerUrl + fileInfo.getPath();
                			model.addAttribute("imgUrl", imgUrl);
                			model.addAttribute("fileid", realFileName);
                			model.addAttribute("success", true);
                		}
                    }
                }
            }
        }
		return "subpage";
	}
	
	@RequestMapping(value="/help.html")
	public String help(Model model){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		List<String> descriptionList = helpService.findAllDescriptions();
		List<RcHelp> helps = helpService.findAllHelps();
		for(String type: descriptionList){
			Map<String,Object> data = new HashMap<String, Object>();
			List<RcHelp> list = new ArrayList<RcHelp>();
			for(RcHelp help : helps) {
				if(type.equals(help.getDescription())) {
					list.add(help);
				}
			}
			data.put("type", type);
			data.put("helpList", list);
			result.add(data);
		}
		model.addAttribute("mapList", result);
		return "help/help";
	}
	
	@RequestMapping(value="/content")
	public String content(int helpId, Model model){
		RcHelp help = helpService.findHelpById(helpId);
		model.addAttribute("html", help.getContent());
		model.addAttribute("title", help.getName());
		return "help/appContent";
	}
	
	@RequestMapping(value="/pccontent")
	public String pccontent(int helpId, Model model){
		RcHelp help = helpService.findHelpById(helpId);
		model.addAttribute("html", help.getContent());
		model.addAttribute("title", help.getName());
		return "help/content";
	}
	
	@RequestMapping(value="/edit")
	public String edit(int helpId, Model model){
		RcHelp help = helpService.findHelpById(helpId);
		model.addAttribute("html", help.getContent());
		model.addAttribute("help", help);
		return "help/editHelp";
	}
	
	@RequestMapping(value="/modify")
	@ResponseBody
	public String modify(int helpId, String name, String description, int seq, String content){
		RcHelp help = new RcHelp();
		help.setHelpId(helpId);
		help.setName(name);
		help.setDescription(description);
		help.setSeq(seq);
		help.setContent(content);
		int result = helpService.modifyHelp(help);
		if(result > 0) {
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public String delete(int helpId){
		int result = helpService.removeHelp(helpId);
		if(result > 0) {
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}
}
