package com.socool.soft.controller;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
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

import com.socool.soft.bo.RcAppBanner;
import com.socool.soft.bo.RcAppBrandHot;
import com.socool.soft.bo.RcAppMenu;
import com.socool.soft.bo.RcAppProdHot;
import com.socool.soft.bo.RcAppProdRec;
import com.socool.soft.bo.RcCity;
import com.socool.soft.bo.RcFileInfo;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.exception.ErrorValue;
import com.socool.soft.exception.SystemException;
import com.socool.soft.service.IAppBannerService;
import com.socool.soft.service.IAppBrandHotService;
import com.socool.soft.service.IAppMenuService;
import com.socool.soft.service.IAppProdHotService;
import com.socool.soft.service.IAppProdRecService;
import com.socool.soft.service.ICityService;
import com.socool.soft.service.IFileInfoService;
import com.socool.soft.service.IHomePageService;
import com.socool.soft.util.JsonUtil;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.newvo.android.HomePageVO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="/general")
public class HomePageController extends BaseController {
	
	@Autowired
	private IHomePageService homePageService;
	
	@Autowired
	private ICityService cityService;
	
//	@Autowired
//	private IRedisService redisService;
	@Autowired
	private IAppBrandHotService appBrandHotService;
	@Autowired
	private IAppProdHotService appProdHotService;
	@Autowired
	private IAppProdRecService appProdRecService;
	@Autowired
	private IAppBannerService appBannerService;
	@Autowired
	private IAppMenuService appMenuService;
	@Autowired
	private IFileInfoService fileInfoService;
	@Autowired
	private PropertyConstants propertyConstants;
	
	
	@RequestMapping(value="/homepage")
	@ResponseBody
	public String homePage(String data,String timestamp,String nonceStr,String product,String signature) throws SystemException{
		int code = 1;
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		String cityName = null;
		try{
			cityName = json.getString("city").trim();
		}catch(Exception e){
			return "{\"code\":\"0\",\"result\":\"未传city\"}";
		}
		
		RcCity city = null;
		if(StringUtils.isNotBlank(cityName)) {
			city = cityService.findCityByName(cityName);
		}
		if(city == null || city.getIsDredged() == YesOrNoEnum.NO.getValue()){
			code = ErrorValue.CITY_ONT_OPEN_ERROR.getValue();
			city = cityService.findDefaultCity();
		}
		
		HomePageVO homePageVO = homePageService.getHomePageData(city.getCityId());
//		homePageVO.setCityId(city.getCityId());
//		homePageVO.setCityName(city.getName());
//		jsonStr = JsonUtil.toJson(homePageVO);
		
		return "{\"code\":\""+code+"\",\"result\":\"success!\",\"data\":"+JsonUtil.toJson(homePageVO)+"}";
	}
	
	@RequestMapping(value="/getstreetlistpage")
	@ResponseBody
	public String getStreetListPage(String data,String timestamp,String nonceStr,String product,String signature){
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		Integer currentPage = null;
		try{
			currentPage = json.getInt("currentPage");
		}catch(Exception e){
			currentPage = 1;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(7);
		page.setCurrentPage(currentPage);
		List<RcAppBrandHot> appBrandHots = homePageService.findAllPagedAppBrandHots(page);
		return "{\"code\":\"1\",\"result\":\"success\",\"data\":"+JsonUtil.toJson(appBrandHots)+"}";
	}
	
	@RequestMapping(value="/getmorerecommend")
	@ResponseBody
	public String getMoreRecommend(String data,String timestamp,String nonceStr,String product,String signature){
		int code = 1;
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		Integer currentPage = null;
		String cityName = json.getString("city");;
		try{
			currentPage = json.getInt("currentPage");
		}catch(Exception e){
			currentPage = 1;
		}
		RcCity city = cityService.findCityByName(cityName);
		if(city == null || city.getIsDredged() == YesOrNoEnum.NO.getValue()){
			code = ErrorValue.CITY_ONT_OPEN_ERROR.getValue();
			city = cityService.findDefaultCity();
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(8);
		page.setCurrentPage(currentPage);
		List<RcAppProdRec> appProdRecs = homePageService.findPagedAppProdRecsByCityId(city.getCityId(), page);
		String result = JsonUtil.toJson(appProdRecs);
		return "{\"code\":\""+code+"\",\"result\":\"success\",\"data\":"+result+"}";
	}
	
	@RequestMapping(value="/generating")
	@ResponseBody
	public String generating() throws SystemException{
//		RcCity param = new RcCity();
//		param.setIsDredged(YesOrNoEnum.YES.getValue());
//		List<RcCity> cities = cityService.findCities(param);
//		for(RcCity city : cities) {
//			HomePageVO homePageVO = homePageService.getHomePageData(city.getCityId());
//			homePageVO.setCityId(city.getCityId());
//			homePageVO.setCityName(city.getName());
////			String jsonStr = JsonUtil.toJson(homePageVO);
//			redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(city.getCityId()), homePageVO);
//		}
		return "{\"code\":\"1\",\"result\":\"success\"}";
	}
	
	@RequestMapping(value="/bannerList")
	public String bannerList(Model model,Integer currentPage,Integer cityId){
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(7);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcAppBanner> appBanners = appBannerService.findPagedAppBanners(cityId, page);
		List<String> provinces = cityService.findProvinces();
		model.addAttribute("list", appBanners);
		model.addAttribute("provinceList", provinces);
		
		if(flag) {
			return "homepage/bannerlist";
		} else {
			return "homepage/bannerlist_inner";
		}
	}
	
	@RequestMapping(value="uploadcover")
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
	
	@RequestMapping(value="uploadmodifycover")
	public String uploadmodifyCover(HttpServletRequest request,Model model) throws IllegalStateException, IOException{
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
	
	@RequestMapping(value="/saveBanner")
	@ResponseBody
	public String saveBanner(int type, int seq, String target, int cityId, String imgUrl) throws SystemException{
		RcAppBanner appBanner = new RcAppBanner();
		appBanner.setType(type);
		appBanner.setSeq(seq);
		appBanner.setTarget(target);
		appBanner.setCityId(cityId);
		appBanner.setImgUrl(imgUrl);
		appBannerService.addAppBanner(appBanner);
		
//		HomePageVO homePageVO = homePageService.getHomePageData(cityId);
//		redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(cityId), homePageVO);
		return "{\"code\":\"1\",\"result\":\"success\"}";
	}
	
	@RequestMapping(value="/modifyBanner")
	@ResponseBody
	public String modifyBanner(int appBannerId, int type, int seq, String target, int cityId, String imgUrl) throws SystemException{
		RcAppBanner appBanner = appBannerService.findAppBannerById(appBannerId);
//		int oldCityId = appBanner.getCityId();
		appBanner.setAppBannerId(appBannerId);
		appBanner.setType(type);
		appBanner.setSeq(seq);
		appBanner.setTarget(target);
		appBanner.setCityId(cityId);
		appBanner.setImgUrl(imgUrl);
		appBannerService.modifyAppBanner(appBanner);
		
//		HomePageVO homePageVO = homePageService.getHomePageData(cityId);
//		redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(cityId), homePageVO);
//		homePageVO = homePageService.getHomePageData(oldCityId);
//		redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(oldCityId), homePageVO);
		return "{\"code\":\"1\",\"result\":\"success\"}";
	}
	
	@RequestMapping(value="/delBanner")
	@ResponseBody
	public String deleteBanner(int appBannerId) throws SystemException{
//		RcAppBanner appBanner = appBannerService.findAppBannerById(appBannerId);
		appBannerService.removeAppBanner(appBannerId);
		
//		HomePageVO homePageVO = homePageService.getHomePageData(appBanner.getCityId());
//		redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(appBanner.getCityId()), homePageVO);
		return "{\"code\":\"1\"}";
	}
	
	@RequestMapping(value="/menuList")
	public String menuList(Model model,Integer currentPage){
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(7);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcAppMenu> list = appMenuService.findAllPagedAppMenus(page);
		model.addAttribute("list", list);
		if(flag) {
			return "homepage/menulist";
		} else {
			return "homepage/menulist_inner";
		}
	}
	
	@RequestMapping(value="/saveMenu")
	@ResponseBody
	public String saveMenu(String name, String icon, String location, int seq){
		RcAppMenu appMenu = new RcAppMenu();
		appMenu.setName(name);
		appMenu.setIcon(icon);
		appMenu.setLocation(location);
		appMenu.setSeq(seq);
		int result = appMenuService.addAppMenu(appMenu);
		return "{\"code\":\""+result+"\"}";
	}
	
	@RequestMapping(value="/delMenu")
	@ResponseBody
	public String deleteMenu(int appMenuId){
		appMenuService.removeAppMenu(appMenuId);
		return "{\"code\":\"1\"}";
	}
	
	@RequestMapping(value="/brandStreet")
	public String brandStreet(Model model,Integer currentPage,String brandName){
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(5);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcAppBrandHot> appBrandHots = appBrandHotService.findAllPagedAppBrandHots(brandName, page);
		
		model.addAttribute("list", appBrandHots);
		
		if(flag) {
			return "homepage/hotBrandlist";
		} else {
			return "homepage/hotBrandlist_inner";
		}
	}
	
	@RequestMapping(value="/saveHotBrand")
	@ResponseBody
	public String saveHotBrand(int prodBrandId, int seq){
		RcAppBrandHot appBrandHot = new RcAppBrandHot();
		appBrandHot.setProdBrandId(prodBrandId);
		appBrandHot.setSeq(seq);
		appBrandHotService.addAppBrandHot(appBrandHot);
		return "{\"code\":\"1\"}";
	}
	
	@RequestMapping(value="/delHotBrand")
	@ResponseBody
	public String delHotBrand(int appBrandHotId){
		appBrandHotService.removeAppBrandHot(appBrandHotId);
		return "{\"code\":\"1\"}";
	}
	
	@RequestMapping(value="/hotProduct")
	public String hotProduct(Model model,Integer currentPage,Integer cityId){
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(7);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcAppProdHot> prodHots = appProdHotService.findPagedAppProdHotsByCityId(cityId, page);
		List<String> provinces = cityService.findProvinces();
		model.addAttribute("list", prodHots);
		model.addAttribute("provinceList", provinces);
		
		if(flag) {
			return "homepage/hotProduct";
		} else {
			return "homepage/hotProduct_inner";
		}
	}
	
	@RequestMapping(value="/saveHotProd")
	@ResponseBody
	public String saveHotProd(int prodId, int cityId, int seq) throws SystemException{
		RcAppProdHot appProdHot = new RcAppProdHot();
		appProdHot.setProdId(prodId);
		appProdHot.setCityId(cityId);
		appProdHot.setSeq(seq);
	    RcAppProdHot hot = appProdHotService.findAppProdHotByProdId(prodId);
	    if(hot == null) {
	    	appProdHotService.addAppProdHot(appProdHot);
	    
//		    HomePageVO homePageVO = homePageService.getHomePageData(cityId);
//			redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(cityId), homePageVO);
	    }
	    else {
			return "{\"code\":\"0\",\"result\":\"already exits.\"}";
		}
		return "{\"code\":\"1\"}";
	}
	@RequestMapping(value="/modifyHotProd")
	@ResponseBody
	public String modifyHotProd(int appProdHotId, int cityId, int seq) throws SystemException{
		RcAppProdHot appProdHot = appProdHotService.findAppProdHotById(appProdHotId);
//		int oldCityId = appProdHot.getCityId();
		appProdHot.setAppProdHotId(appProdHotId);
		appProdHot.setCityId(cityId);
		appProdHot.setSeq(seq);
		appProdHotService.modifyAppProdHot(appProdHot);
		
//		HomePageVO homePageVO = homePageService.getHomePageData(cityId);
//		redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(cityId), homePageVO);
//		homePageVO = homePageService.getHomePageData(oldCityId);
//		redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(oldCityId), homePageVO);
		return "{\"code\":\"1\"}";
	}
	
	@RequestMapping(value="/delHotProd")
	@ResponseBody
	public String delHotProd(int appProdHotId) throws SystemException{
//		RcAppProdHot appProdHot = appProdHotService.findAppProdHotById(appProdHotId);
		appProdHotService.removeAppProdHot(appProdHotId);
		
//		HomePageVO homePageVO = homePageService.getHomePageData(appProdHot.getCityId());
//		redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(appProdHot.getCityId()), homePageVO);
		return "{\"code\":\"1\"}";
	}
	
	@RequestMapping(value="/batsDelHotProd")
	@ResponseBody
	public String batsDelHotProd(String ids) throws SystemException{
		String[] prodHotIds = ids.split(",,,");
		for(String appProdHotId:prodHotIds){
//			RcAppProdHot appProdHot = appProdHotService.findAppProdHotById(Integer.parseInt(appProdHotId));
			appProdHotService.removeAppProdHot(Integer.parseInt(appProdHotId));
			
//			HomePageVO homePageVO = homePageService.getHomePageData(appProdHot.getCityId());
//			redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(appProdHot.getCityId()), homePageVO);
		}
		return "{\"code\":\"1\"}";
	}
	
	@RequestMapping(value="/Recommended")
	public String recommended(Model model,Integer currentPage,Integer cityId){
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(7);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcAppProdRec> prodRecs = appProdRecService.findPagedAppProdRecsByCityId(cityId, page);
		List<String> provinces = cityService.findProvinces();
		model.addAttribute("list", prodRecs);
		model.addAttribute("provinceList", provinces);
		
		if(flag) {
			return "homepage/recommendedProd";
		} else {
			return "homepage/recommendedProd_inner";
		}
	}
	
	@RequestMapping(value="/saveRecommended")
	@ResponseBody
	public String saveRecommended(int prodId, int cityId, int seq) throws SystemException{
		RcAppProdRec appProdRec = new RcAppProdRec();
		appProdRec.setProdId(prodId);
		appProdRec.setCityId(cityId);
		appProdRec.setSeq(seq);
		RcAppProdRec rec = appProdRecService.findAppProdRecByCityIdAndProdId(cityId, prodId);
	    if(rec == null) {
	    	appProdRecService.addAppProdRec(appProdRec);
	    
//		    HomePageVO homePageVO = homePageService.getHomePageData(cityId);
//			redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(cityId), homePageVO);
	    }
	    else {
			return "{\"code\":\"0\",\"result\":\"already exits.\"}";
		}
		return "{\"code\":\"1\"}";
	}
	
	@RequestMapping(value="/modifyRecommended")
	@ResponseBody
	public String modifyRecommended(int appProdRecId, int cityId, int seq) throws SystemException{
		RcAppProdRec appProdRec = appProdRecService.findAppProdRecById(appProdRecId);
//		int oldCityId = appProdRec.getCityId();
		appProdRec.setAppProdRecId(appProdRecId);
		appProdRec.setCityId(cityId);
		appProdRec.setSeq(seq);
		appProdRecService.modifyAppProdRec(appProdRec);
		
//		HomePageVO homePageVO = homePageService.getHomePageData(cityId);
//		redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(cityId), homePageVO);
//		homePageVO = homePageService.getHomePageData(oldCityId);
//		redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(oldCityId), homePageVO);
		return "{\"code\":\"1\"}";
	}
	
	@RequestMapping(value="/delRecommended")
	@ResponseBody
	public String delRecommended(int appProdRecId) throws SystemException{
//		RcAppProdRec appProdRec = appProdRecService.findAppProdRecById(appProdRecId);
		appProdRecService.removeAppProdRec(appProdRecId);
		
//		HomePageVO homePageVO = homePageService.getHomePageData(appProdRec.getCityId());
//		redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(appProdRec.getCityId()), homePageVO);
		return "{\"code\":\"1\"}";
	}
	
	@RequestMapping(value="/batsDelNiceProd")
	@ResponseBody
	public String batsDelNiceProd(String ids) throws SystemException{
		String[] appProdRecIds = ids.split(",,,");
		for(String appProdRecId:appProdRecIds){
//			RcAppProdRec appProdRec = appProdRecService.findAppProdRecById(Integer.parseInt(appProdRecId));
			appProdRecService.removeAppProdRec(Integer.parseInt(appProdRecId));
			
//			HomePageVO homePageVO = homePageService.getHomePageData(appProdRec.getCityId());
//			redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(appProdRec.getCityId()), homePageVO);
		}
		return "{\"code\":\"1\"}";
	}
	
	@RequestMapping(value="uploadBrand")
	public String uploadBrand(HttpServletRequest request,Model model) throws IllegalStateException, IOException{
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
	
	@RequestMapping(value="/modifyBrand")
	@ResponseBody
	public String saveStreetListLogo(int appBrandHotId, Integer seq, String imgUrl){
		RcAppBrandHot appBrandHot = new RcAppBrandHot();
		appBrandHot.setAppBrandHotId(appBrandHotId);
		appBrandHot.setSeq(seq);
		appBrandHot.setImgUrl(imgUrl);
		appBrandHotService.modifyAppBrandHot(appBrandHot);
		return "{\"code\":\"1\"}";
	}
}
