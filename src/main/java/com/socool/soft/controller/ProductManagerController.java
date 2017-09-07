package com.socool.soft.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.socool.soft.bo.RcFileInfo;
import com.socool.soft.bo.RcImageSpace;
import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcMerchantUser;
import com.socool.soft.bo.RcProd;
import com.socool.soft.bo.RcProdBrand;
import com.socool.soft.bo.RcProdCat;
import com.socool.soft.bo.RcProdPropInfo;
import com.socool.soft.bo.RcProdSku;
import com.socool.soft.bo.RcProdSkuPropInfo;
import com.socool.soft.bo.RcVendorUser;
import com.socool.soft.bo.constant.ImageSpaceTypeEnum;
import com.socool.soft.bo.constant.ProdTypeEnum;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.service.IFileInfoService;
import com.socool.soft.service.IImageSpaceService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IMerchantUserService;
import com.socool.soft.service.IProdBrandService;
import com.socool.soft.service.IProdCatService;
import com.socool.soft.service.IProdService;
import com.socool.soft.service.IProdSkuService;
import com.socool.soft.service.IProductManagerService;
import com.socool.soft.service.IVendorUserService;
import com.socool.soft.util.JsonUtil;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.ProdPropListDTO;
import com.socool.soft.vo.ProductParam;
import com.socool.soft.vo.ProductPropListVO;
import com.socool.soft.vo.ProductPropListVO.ComplaintListProductVO;
import com.socool.soft.vo.ProductPublishedDTO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RequestMapping(value="/product")
@Controller
public class ProductManagerController extends BaseController {

	@Autowired
	private IProdCatService prodCatService;
	
	@Autowired
	private IMerchantService merchantService;
	
	@Autowired
	private IMerchantUserService merchantUserService;
	
	@Autowired
	private IVendorUserService vendorUserService;
	
	@Autowired
	private IImageSpaceService imageSpaceService;
	
	@Autowired
	private IProductManagerService productManagerService;
	
	@Autowired
	private IProdService prodService;
	
	@Autowired
	private IProdBrandService prodBrandService;
	@Autowired
	private IFileInfoService fileInfoService;
	@Autowired
	private IProdSkuService prodSkuService;
	@Autowired
	private PropertyConstants propertyConstants;
//	@Autowired
//	private IRedisService redisService;
//	@Autowired
//	private IHomePageService homePageService;
	
	
	private final static Log log = LogFactory.getLog(ProductManagerController.class);
	
	@RequestMapping(value="init")
	public String publish(Model model,HttpServletRequest request) {
		Integer merchantUserId = getMerchantUserId(request);
		if(merchantUserId == null){
			model.addAttribute("rcMerchant", "-1");
		} else{
			RcMerchantUser merchantUser = merchantUserService.findMerchantUserById(merchantUserId);
			model.addAttribute("rcMerchant", merchantUser.getMerchantId());
		}
		List<RcMerchant> list = merchantService.findAllEnabledMerchants();
		model.addAttribute("list", list);
		return "productServiceManage_entryGoods";  
	}
	@RequestMapping(value="initBase")
	public String publishBase(Model model,HttpServletRequest request) {
		return "baseProductServiceManage_entryGoods";  
	}
	
	@RequestMapping(value="/querytopcat")
	@ResponseBody
	public String queryTopCat() throws Exception{ 
		List<RcProdCat> catList = prodCatService.findTopProdCats();
		String json = JsonUtil.toJson(catList);
		return json;
	}
	@RequestMapping(value="/queryNextcat")
	@ResponseBody
	public String queryNextCat(Integer parentCatgId){ 
		List<RcProdCat> list = prodCatService.findChildProdCats(parentCatgId);
		if(CollectionUtils.isEmpty(list)){
			return "{\"code\":\"0\"}";
		} else{
			String json = JsonUtil.toJson(list);
			return json;
		}
	}
	
	@RequestMapping(value="/addGoods")
	public String addGoods(HttpServletRequest request, Model model, Integer prodType, Integer prodCatId, Integer subprodCatId, Integer merchantId, String promptText){
		//品牌
		RcProdBrand param = new RcProdBrand();
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		List<RcProdBrand> prodBrands = prodBrandService.findAllProdBrands(param);
		//获取商品属性
		List<ProdPropListDTO> listProps = productManagerService.findProdPropsByProdCatId(subprodCatId); 
		List<ProductPropListVO> resultProd = new ArrayList<ProductPropListVO>();
		Map<Integer,ProductPropListVO> submap = new HashMap<Integer,ProductPropListVO>();
		if(listProps != null){
			for(ProdPropListDTO listProp: listProps){
				String propSelfdf = "1";
				int id = listProp.getProdPropId();
				ProductPropListVO vo = submap.get(id);
				if(vo==null){
					vo = new ProductPropListVO();
					Map<String,Object> otherMap = new HashMap<String,Object>();
					otherMap.put("PRODPROPID", listProp.getProdPropId());
					otherMap.put("PROD_PROP_NAME", listProp.getProdPropName());
					otherMap.put("PROD_PROP_UNIT", "");
					otherMap.put("IS_COLOR", listProp.getIsColor());
					otherMap.put("IS_EDIT", 1);
					otherMap.put("IS_REQUIRED", 0);
					otherMap.put("PRODPROPNAME", listProp.getProdPropName());
					vo.setOtherData(otherMap);
					submap.put(id,vo);
					resultProd.add(vo);
				}
				ComplaintListProductVO pro = new ComplaintListProductVO(listProp.getProdPropEnum(),listProp.getProdPropEnumId().toString(),propSelfdf);
				vo.getProducts().add(pro);
			}
		}
		//单品属性
		List<ProdPropListDTO> listSales = productManagerService.findProdSkuPropsByProdCatId(subprodCatId, prodType); 
		List<ProductPropListVO> resultSales = new ArrayList<ProductPropListVO>();
		Map<Integer,ProductPropListVO> submap1 = new HashMap<Integer,ProductPropListVO>();
		if(listSales!=null){
			for(ProdPropListDTO e: listSales){
				String propSelfdf = "1";
				int id = e.getProdPropId();
				ProductPropListVO vo = submap1.get(id);
				if(vo==null){
					vo = new ProductPropListVO();
					Map<String,Object> otherMap = new HashMap<String,Object>();
					otherMap.put("PRODPROPID", e.getProdPropId());
					otherMap.put("PROD_PROP_NAME", e.getProdPropName());
					otherMap.put("PROD_PROP_UNIT", "");
					otherMap.put("IS_COLOR", e.getIsColor());
					otherMap.put("IS_EDIT", 1);
					otherMap.put("IS_APPLICATOR", e.getIsApplicator());
					otherMap.put("PRODPROPNAME", e.getProdPropName());
					vo.setOtherData(otherMap);
					submap1.put(id,vo);
					resultSales.add(vo);
				}
				ComplaintListProductVO pro = new ComplaintListProductVO(e.getProdPropEnum(),e.getProdPropEnumId().toString(),propSelfdf);
				vo.getProducts().add(pro);
			}
		}
		setData(model, new String[]{"ImageObj","merchantId","prodCatId","listBrand","listProd","listSales","listFreight","listSelectedCat","prodId","promptText","prodType"},new Object[]{null,merchantId,subprodCatId,prodBrands,resultProd,resultSales,null,null,null,promptText,prodType});
		return "productServiceManage_addGoods";
	} 
	
	@RequestMapping(value="/addBaseGoods")
	public String addBaseGoods(HttpServletRequest request, Model model, Integer prodType, Integer prodCatId, Integer subprodCatId, String promptText){
		//品牌
		RcProdBrand param = new RcProdBrand();
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		List<RcProdBrand> prodBrands = prodBrandService.findAllProdBrands(param);
		//获取商品属性
		List<ProdPropListDTO> listProps = productManagerService.findProdPropsByProdCatId(subprodCatId); 
		List<ProductPropListVO> resultProd = new ArrayList<ProductPropListVO>();
		Map<Integer,ProductPropListVO> submap = new HashMap<Integer,ProductPropListVO>();
		if(listProps != null){
			for(ProdPropListDTO listProp: listProps){
				String propSelfdf = "1";
				int id = listProp.getProdPropId();
				ProductPropListVO vo = submap.get(id);
				if(vo==null){
					vo = new ProductPropListVO();
					Map<String,Object> otherMap = new HashMap<String,Object>();
					otherMap.put("PRODPROPID", listProp.getProdPropId());
					otherMap.put("PROD_PROP_NAME", listProp.getProdPropName());
					otherMap.put("PROD_PROP_UNIT", "");
					otherMap.put("IS_COLOR", listProp.getIsColor());
					otherMap.put("IS_EDIT", 1);
					otherMap.put("IS_REQUIRED", 0);
					otherMap.put("PRODPROPNAME", listProp.getProdPropName());
					vo.setOtherData(otherMap);
					submap.put(id,vo);
					resultProd.add(vo);
				}
				ComplaintListProductVO pro = new ComplaintListProductVO(listProp.getProdPropEnum(),listProp.getProdPropEnumId().toString(),propSelfdf);
				vo.getProducts().add(pro);
			}
		}
		//单品属性
		List<ProdPropListDTO> listSales = productManagerService.findProdSkuPropsByProdCatId(subprodCatId, prodType); 
		List<ProductPropListVO> resultSales = new ArrayList<ProductPropListVO>();
		Map<Integer,ProductPropListVO> submap1 = new HashMap<Integer,ProductPropListVO>();
		if(listSales!=null){
			for(ProdPropListDTO e: listSales){
				String propSelfdf = "1";
				int id = e.getProdPropId();
				ProductPropListVO vo = submap1.get(id);
				if(vo==null){
					vo = new ProductPropListVO();
					Map<String,Object> otherMap = new HashMap<String,Object>();
					otherMap.put("PRODPROPID", e.getProdPropId());
					otherMap.put("PROD_PROP_NAME", e.getProdPropName());
					otherMap.put("PROD_PROP_UNIT", "");
					otherMap.put("IS_COLOR", e.getIsColor());
					otherMap.put("IS_EDIT", 1);
					otherMap.put("IS_APPLICATOR", e.getIsApplicator());
					otherMap.put("PRODPROPNAME", e.getProdPropName());
					vo.setOtherData(otherMap);
					submap1.put(id,vo);
					resultSales.add(vo);
				}
				ComplaintListProductVO pro = new ComplaintListProductVO(e.getProdPropEnum(),e.getProdPropEnumId().toString(),propSelfdf);
				vo.getProducts().add(pro);
			}
		}
		setData(model, new String[]{"ImageObj","merchantId","prodCatId","listBrand","listProd","listSales","listFreight","listSelectedCat","prodId","promptText","prodType"},new Object[]{null,null,subprodCatId,prodBrands,resultProd,resultSales,null,null,null,promptText,prodType});
		return "baseProductServiceManage_addGoods";
	} 
	
	@RequestMapping(value="searchMer")
	@ResponseBody
	public String searchMerchant(String searchKey){
		List<RcMerchant> list = merchantService.findSortedMerchantByName(searchKey);
		String result = JsonUtil.toJson(list);
		return result;
	}
	
	/**
	 * 发布商品
	 */
	@RequestMapping(value="/savegoods")
	@ResponseBody
	public String savegoods(String json,String skuvalue,String propdrr,String skuattr,String colorenumid,String skupropnum,String imgids,Model model,HttpServletRequest request,HttpServletResponse resp) throws Exception{
		JSONObject jo = JSONObject.fromObject(json);
		//SKU属性组装
		List<RcProdSku> prodSkus = new ArrayList<RcProdSku>();
		Map<Integer, List<RcProdSkuPropInfo>> prodSkuPropInfoMap = new HashMap<Integer, List<RcProdSkuPropInfo>>();
		JSONArray jsonSkudrr = JSONArray.fromObject(skuvalue);
		
		JSONArray jsonSkuprop = JSONArray.fromObject(skuattr);
		int i=0;
		//添加SKU图片
		JSONArray skuImageUrls = jo.getJSONArray("propurls");
		int skuColorNum =0;
		Map<Integer, List<String>> prodSkuImgUrlsMap = new HashMap<Integer, List<String>>();
		String[]  enumid = colorenumid.split(",,,");
		for(Object d :skuImageUrls){
			List<String> prodSkuImgUrls = new ArrayList<String>();
			String[] skuUrl = d.toString().split(",,,");
			for(int skui=0;skui<skuUrl.length;skui++){
				prodSkuImgUrls.add(skuUrl[skui]);
			}
			prodSkuImgUrlsMap.put(Integer.parseInt(enumid[skuColorNum]), prodSkuImgUrls);
			skuColorNum++;
		}
			
		for(Object d :jsonSkuprop){
			List<RcProdSkuPropInfo> prodSkuPropInfo = new ArrayList<RcProdSkuPropInfo>();
			String[] propnum = d.toString().split("@@@");
			for(int num=0;num<propnum.length;num++){
				String[] propstring = propnum[num].split(",,,");
				RcProdSkuPropInfo ecProdSkuPropInfo = new RcProdSkuPropInfo();
				ecProdSkuPropInfo.setProdPropId(Integer.parseInt(propstring[0]));
				ecProdSkuPropInfo.setProdPropEnumId(Integer.parseInt(propstring[1]));
				ecProdSkuPropInfo.setProdPropVal(propstring[2]);
				ecProdSkuPropInfo.setProdPropName(propstring[3]);
				prodSkuPropInfo.add(ecProdSkuPropInfo);
			}
			prodSkuPropInfoMap.put(i, prodSkuPropInfo);
			i++;
		}
		for(Object c :jsonSkudrr){
			String[] string = c.toString().split(",,,");
			
			RcProdSku ecProdSku = new RcProdSku();
			ecProdSku.setOriginPrice(new BigDecimal(string[1]));
			ecProdSku.setPrice(new BigDecimal(string[2]));
			ecProdSku.setInventory(Integer.parseInt(string[0]));
			prodSkus.add(ecProdSku);
		}
		
		
		ProductPublishedDTO productPublishedDTO = new ProductPublishedDTO();
		productPublishedDTO.setBaseProdId(0);
		//true：新增操作；false:修改操作
		productPublishedDTO.setIfAdded(true);
		//商品Id
		//productPublishedDTO.setProdId(jo.getInt("prodId"));
		//商品名称
		productPublishedDTO.setProdName(jo.getString("prodName"));
		//库存预警
		productPublishedDTO.setProdStockWarning(jo.getInt("prodStockWarning"));
		//商品库存
		if(jo.containsKey("prodStorage")) {
			productPublishedDTO.setProdStorage(jo.getInt("prodStorage"));
		}
		//计量单位
		productPublishedDTO.setProdMeasureUnit(jo.getString("prodMeasureUnit"));
		//商品描述
		productPublishedDTO.setProdDetail(jo.getString("prodDetailDescContent"));
		//原价
		productPublishedDTO.setProdOriginPrice(new BigDecimal(jo.getString("prodOriginPrice")));
		//当前价
		productPublishedDTO.setProdPrice(new BigDecimal(jo.getString("prodPrice")));
//		//运费模板
//		productPublishedDTO.setProdFreightTempId(null);
		//商品类型
		productPublishedDTO.setProductType(jo.getInt("productType"));
		//组装商品图片列表
		String prodimageurls = jo.getString("prodImgUrls");
		String[] imgurls = prodimageurls.split(",,,");
		List<String> prodimgs = new ArrayList<String>();
		for(int q=0;q<imgurls.length;q++){
			prodimgs.add(imgurls[q]);
		}
		//商品图片
		productPublishedDTO.setProdImgUrls(prodimgs);
		//广告词
		productPublishedDTO.setProdAdword(jo.getString("prodAdword"));    
		//品类ID
		productPublishedDTO.setProdCatId(jo.getInt("prodCatId"));
		//商品品牌ID
		productPublishedDTO.setProdBrandId(jo.getInt("prodBrandId"));
		//其他品牌名称
		if(jo.getInt("prodBrandId") == 0 && jo.containsKey("prodBrandName")){
			productPublishedDTO.setProdBrandName(jo.getString("prodBrandName"));
		}
		//商户ID
		productPublishedDTO.setMerchantId(jo.getInt("merchantId"));
		//添加SKU属性
		productPublishedDTO.setProdSkus(prodSkus);
		productPublishedDTO.setProdSkuPropInfoMap(prodSkuPropInfoMap);
		productPublishedDTO.setProdSkuImgUrlsMap(prodSkuImgUrlsMap);
		
		//设置商品属性
		List<RcProdPropInfo> prodPropInfos = new ArrayList<RcProdPropInfo>();
		JSONArray jsonPropdrr = JSONArray.fromObject(propdrr);
		for(Object c :jsonPropdrr){
			String[] string = c.toString().split(",,,");
			RcProdPropInfo ecProdPropInfo = new RcProdPropInfo();
			ecProdPropInfo.setProdPropId(Integer.parseInt(string[0]));
			ecProdPropInfo.setProdPropVal(string[1]);
			ecProdPropInfo.setProdPropName(string[2]);
			prodPropInfos.add(ecProdPropInfo);
		}
		//用户自定义属性值
		JSONArray customPropdrr = jo.getJSONArray("customPropArr");
		for(Object c :customPropdrr){
			String[] string = c.toString().split(",,,");
			RcProdPropInfo ecProdPropInfo = new RcProdPropInfo();
			ecProdPropInfo.setProdPropVal(string[1]);
			ecProdPropInfo.setProdPropName(string[0]);
			ecProdPropInfo.setProdPropId(0);
			prodPropInfos.add(ecProdPropInfo);
		}
		
		productPublishedDTO.setProdPropInfos(prodPropInfos);
		//设置图片被引用
//		String[] imgidsdrr = imgids.split(",,,");
//		for(int imgnum=0;imgnum<imgidsdrr.length;imgnum++){
//			iImageSpaceService.setImageToUsed(imgidsdrr[imgnum]);
//		}
		
//		String[] cityArry = jo.getString("citys").split(",,,");
//		List<String> citys = new ArrayList<String>();
//		for(int k = 0;k<cityArry.length;k++){
//			citys.add(cityArry[k]);
//		}
//		RcMerchantLoc rcMerchantLoc = iMerchantService.getRcMerchantLoc(jo.getString("merchantId"));
//		citys.add(rcMerchantLoc.getCity());
//		productPublishedDTO.setCitys(citys);
		int prodId = productManagerService.publishProduct(productPublishedDTO);
		return "{\"code\":\"1\",\"prodId\":\""+prodId+"\"}";
	}
	
	/**
	 * 发布商品
	 */
	@RequestMapping(value="/saveBaseGoods")
	@ResponseBody
	public String saveBasegoods(String json,String skuvalue,String propdrr,String skuattr,String colorenumid,String skupropnum,String imgids,Model model,HttpServletRequest request,HttpServletResponse resp) throws Exception{
		JSONObject jo = JSONObject.fromObject(json);
		//SKU属性组装
		List<RcProdSku> prodSkus = new ArrayList<RcProdSku>();
		Map<Integer, List<RcProdSkuPropInfo>> prodSkuPropInfoMap = new HashMap<Integer, List<RcProdSkuPropInfo>>();
		JSONArray jsonSkudrr = JSONArray.fromObject(skuvalue);
		
		JSONArray jsonSkuprop = JSONArray.fromObject(skuattr);
		int i=0;
		//添加SKU图片
		JSONArray skuImageUrls = jo.getJSONArray("propurls");
		int skuColorNum =0;
		Map<Integer, List<String>> prodSkuImgUrlsMap = new HashMap<Integer, List<String>>();
		String[]  enumid = colorenumid.split(",,,");
		for(Object d :skuImageUrls){
			List<String> prodSkuImgUrls = new ArrayList<String>();
			String[] skuUrl = d.toString().split(",,,");
			for(int skui=0;skui<skuUrl.length;skui++){
				prodSkuImgUrls.add(skuUrl[skui]);
			}
			prodSkuImgUrlsMap.put(Integer.parseInt(enumid[skuColorNum]), prodSkuImgUrls);
			skuColorNum++;
		}
			
		for(Object d :jsonSkuprop){
			List<RcProdSkuPropInfo> prodSkuPropInfo = new ArrayList<RcProdSkuPropInfo>();
			String[] propnum = d.toString().split("@@@");
			for(int num=0;num<propnum.length;num++){
				String[] propstring = propnum[num].split(",,,");
				RcProdSkuPropInfo ecProdSkuPropInfo = new RcProdSkuPropInfo();
				ecProdSkuPropInfo.setProdPropId(Integer.parseInt(propstring[0]));
				ecProdSkuPropInfo.setProdPropEnumId(Integer.parseInt(propstring[1]));
				ecProdSkuPropInfo.setProdPropVal(propstring[2]);
				ecProdSkuPropInfo.setProdPropName(propstring[3]);
				prodSkuPropInfo.add(ecProdSkuPropInfo);
			}
			prodSkuPropInfoMap.put(i, prodSkuPropInfo);
			i++;
		}
		for(Object c :jsonSkudrr){
			String[] string = c.toString().split(",,,");
			
			RcProdSku ecProdSku = new RcProdSku();
			ecProdSku.setOriginPrice(new BigDecimal(string[1]));
			ecProdSku.setPrice(new BigDecimal(string[2]));
			ecProdSku.setInventory(Integer.parseInt(string[0]));
			prodSkus.add(ecProdSku);
		}
		
		
		ProductPublishedDTO productPublishedDTO = new ProductPublishedDTO();
		productPublishedDTO.setBaseProdId(0);
		//true：新增操作；false:修改操作
		productPublishedDTO.setIfAdded(true);
		//商品Id
		//productPublishedDTO.setProdId(jo.getInt("prodId"));
		//商品名称
		productPublishedDTO.setProdName(jo.getString("prodName"));
		//库存预警
		productPublishedDTO.setProdStockWarning(jo.getInt("prodStockWarning"));
		//商品库存
		if(jo.containsKey("prodStorage")) {
			productPublishedDTO.setProdStorage(jo.getInt("prodStorage"));
		}
		//计量单位
		productPublishedDTO.setProdMeasureUnit(jo.getString("prodMeasureUnit"));
		//商品描述
		productPublishedDTO.setProdDetail(jo.getString("prodDetailDescContent"));
		//原价
		productPublishedDTO.setProdOriginPrice(new BigDecimal(jo.getString("prodOriginPrice")));
		//当前价
		productPublishedDTO.setProdPrice(new BigDecimal(jo.getString("prodPrice")));
//		//运费模板
//		productPublishedDTO.setProdFreightTempId(null);
		//商品类型
		productPublishedDTO.setProductType(jo.getInt("productType"));
		//组装商品图片列表
		String prodimageurls = jo.getString("prodImgUrls");
		String[] imgurls = prodimageurls.split(",,,");
		List<String> prodimgs = new ArrayList<String>();
		for(int q=0;q<imgurls.length;q++){
			prodimgs.add(imgurls[q]);
		}
		//商品图片
		productPublishedDTO.setProdImgUrls(prodimgs);
		//广告词
		productPublishedDTO.setProdAdword(jo.getString("prodAdword"));    
		//品类ID
		productPublishedDTO.setProdCatId(jo.getInt("prodCatId"));
		//商品品牌ID
		productPublishedDTO.setProdBrandId(jo.getInt("prodBrandId"));
		//其他品牌名称
		if(jo.getInt("prodBrandId") == 0 && jo.containsKey("prodBrandName")){
			productPublishedDTO.setProdBrandName(jo.getString("prodBrandName"));
		}
		//商户ID
		productPublishedDTO.setMerchantId(jo.getInt("merchantId"));
		//添加SKU属性
		productPublishedDTO.setProdSkus(prodSkus);
		productPublishedDTO.setProdSkuPropInfoMap(prodSkuPropInfoMap);
		productPublishedDTO.setProdSkuImgUrlsMap(prodSkuImgUrlsMap);
		
		//设置商品属性
		List<RcProdPropInfo> prodPropInfos = new ArrayList<RcProdPropInfo>();
		JSONArray jsonPropdrr = JSONArray.fromObject(propdrr);
		for(Object c :jsonPropdrr){
			String[] string = c.toString().split(",,,");
			RcProdPropInfo ecProdPropInfo = new RcProdPropInfo();
			ecProdPropInfo.setProdPropId(Integer.parseInt(string[0]));
			ecProdPropInfo.setProdPropVal(string[1]);
			ecProdPropInfo.setProdPropName(string[2]);
			prodPropInfos.add(ecProdPropInfo);
		}
		//用户自定义属性值
		JSONArray customPropdrr = jo.getJSONArray("customPropArr");
		for(Object c :customPropdrr){
			String[] string = c.toString().split(",,,");
			RcProdPropInfo ecProdPropInfo = new RcProdPropInfo();
			ecProdPropInfo.setProdPropVal(string[1]);
			ecProdPropInfo.setProdPropName(string[0]);
			ecProdPropInfo.setProdPropId(0);
			prodPropInfos.add(ecProdPropInfo);
		}
		
		productPublishedDTO.setProdPropInfos(prodPropInfos);
		//设置图片被引用
//		String[] imgidsdrr = imgids.split(",,,");
//		for(int imgnum=0;imgnum<imgidsdrr.length;imgnum++){
//			iImageSpaceService.setImageToUsed(imgidsdrr[imgnum]);
//		}
		
//		String[] cityArry = jo.getString("citys").split(",,,");
//		List<String> citys = new ArrayList<String>();
//		for(int k = 0;k<cityArry.length;k++){
//			citys.add(cityArry[k]);
//		}
//		RcMerchantLoc rcMerchantLoc = iMerchantService.getRcMerchantLoc(jo.getString("merchantId"));
//		citys.add(rcMerchantLoc.getCity());
//		productPublishedDTO.setCitys(citys);
		int prodId = productManagerService.publishBaseProduct(productPublishedDTO);
		return "{\"code\":\"1\",\"prodId\":\""+prodId+"\"}";
	}
	
	/**
	 * 根据父ID显示图片
	 */
	@RequestMapping(value="/queryimage")
	@ResponseBody
	public String queryImage(long parentId,HttpServletRequest request,HttpServletResponse resp,Model model) throws Exception{
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
		for(RcImageSpace imageSpace : imageSpaces) {
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
	 * 商品图片上传
	 * @param request
	 * @param resp
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/upload")
	public String uploadImage(HttpServletRequest request, HttpServletResponse resp,Model model) throws Exception{
		Integer memberId = getUserId(request);
		if(memberId == null) {
			memberId = getMerchantUserId(request);
		}
		if(memberId == null) {
			memberId = getVendorUserId(request);
		}
		String imgsnum = request.getParameter("imgsnum");
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
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
                    		model.addAttribute("errormsg", "图片大小不能大于5M！");
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
                			
                			RcImageSpace dirImageSpaceParam = new RcImageSpace();
                			dirImageSpaceParam.setMemberId(memberId);
                			dirImageSpaceParam.setParentId(0l);
                			dirImageSpaceParam.setType(ImageSpaceTypeEnum.FOLDER.getValue());
                			dirImageSpaceParam.setName(format.format(new Date()));
                			long parentId = 0;
                			RcImageSpace dirImageSpace = imageSpaceService.findImageSpace(dirImageSpaceParam);
                			if(dirImageSpace == null) {
                				parentId = imageSpaceService.addImageSpace(dirImageSpaceParam);
                			} else {
                				parentId = dirImageSpace.getImageSpaceId();
                			}
                			
                			RcImageSpace imageSpace = new RcImageSpace();
                			imageSpace.setFileInfoId(fileInfoId);
                			imageSpace.setMemberId(memberId);
                			imageSpace.setParentId(parentId);
                			imageSpace.setType(ImageSpaceTypeEnum.IMAGE.getValue());
                			imageSpace.setName(originalName + "." + suffixName);
                			imageSpace.setImgSize(fileInfo.getSize());
                			try {
                				BufferedImage image = ImageIO.read(new FileInputStream(file));
                				if (image != null) {
                					imageSpace.setImgWidth(image.getWidth());
                        			imageSpace.setImgHeight(image.getHeight());
                				}
                			} catch (IOException e) {
                			}
                			long imageSpaceId = imageSpaceService.addImageSpace(imageSpace);
                			
                			imgUrl = propertyConstants.systemFileServerUrl + fileInfo.getPath();
                			model.addAttribute("url", imgUrl);
                			model.addAttribute("imgid", imageSpaceId);
                			model.addAttribute("imgsnum", imgsnum);
                		}
                    }
                }
            }
        }
		return "subpage1";
	}
	
	/**
	 * 颜色图片上传
	 * @param request
	 * @param resp
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/uploadprop")
	public String uploadPropImage(HttpServletRequest request, HttpServletResponse resp,Model model) throws Exception{
		Integer memberId = getUserId(request);
		if(memberId == null) {
			memberId = getMerchantUserId(request);
		}
		if(memberId == null) {
			memberId = getVendorUserId(request);
		}
		String imgsnum=request.getParameter("imgsnum");
		String[]  enumdata = imgsnum.split("_");
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
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
                    		model.addAttribute("errormsg", "图片大小不能大于5M！");
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
                			
                			RcImageSpace dirImageSpaceParam = new RcImageSpace();
                			dirImageSpaceParam.setMemberId(memberId);
                			dirImageSpaceParam.setParentId(0l);
                			dirImageSpaceParam.setType(ImageSpaceTypeEnum.FOLDER.getValue());
                			dirImageSpaceParam.setName(format.format(new Date()));
                			long parentId = 0;
                			RcImageSpace dirImageSpace = imageSpaceService.findImageSpace(dirImageSpaceParam);
                			if(dirImageSpace == null) {
                				parentId = imageSpaceService.addImageSpace(dirImageSpaceParam);
                			} else {
                				parentId = dirImageSpace.getImageSpaceId();
                			}
                			
                			RcImageSpace imageSpace = new RcImageSpace();
                			imageSpace.setFileInfoId(fileInfoId);
                			imageSpace.setMemberId(memberId);
                			imageSpace.setParentId(parentId);
                			imageSpace.setType(ImageSpaceTypeEnum.IMAGE.getValue());
                			imageSpace.setName(originalName + "." + suffixName);
                			imageSpace.setImgSize(fileInfo.getSize());
                			try {
                				BufferedImage image = ImageIO.read(new FileInputStream(file));
                				if (image != null) {
                					imageSpace.setImgWidth(image.getWidth());
                        			imageSpace.setImgHeight(image.getHeight());
                				}
                			} catch (IOException e) {
                			}
                			long imageSpaceId = imageSpaceService.addImageSpace(imageSpace);
                			
                			imgUrl = propertyConstants.systemFileServerUrl + fileInfo.getPath();
                			model.addAttribute("url", imgUrl);
                			model.addAttribute("imgid", imageSpaceId);
                			model.addAttribute("enumid",enumdata[0]);
                			model.addAttribute("imgsnum",enumdata[1]);
                		}
                    }
                }
            }
        }
		return "subpage1";
	}
	
	/**
	 * Ckeditor图片上传
	 * @param request
	 * @param resp
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/uploadckeditor")
	public String uploadeditorImage(String prodId,HttpServletRequest request, HttpServletResponse resp,Model model) throws Exception{
		Integer memberId = getUserId(request);
		if(memberId == null) {
			memberId = getMerchantUserId(request);
		}
		if(memberId == null) {
			memberId = getVendorUserId(request);
		}
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
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
                    		model.addAttribute("success",false);
                    		model.addAttribute("errormsg", "图片大小不能大于5M！");
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
                			
                			RcImageSpace dirImageSpaceParam = new RcImageSpace();
                			dirImageSpaceParam.setMemberId(memberId);
                			dirImageSpaceParam.setParentId(0l);
                			dirImageSpaceParam.setType(ImageSpaceTypeEnum.FOLDER.getValue());
                			dirImageSpaceParam.setName(format.format(new Date()));
                			long parentId = 0;
                			RcImageSpace dirImageSpace = imageSpaceService.findImageSpace(dirImageSpaceParam);
                			if(dirImageSpace == null) {
                				parentId = imageSpaceService.addImageSpace(dirImageSpaceParam);
                			} else {
                				parentId = dirImageSpace.getImageSpaceId();
                			}
                			
                			RcImageSpace imageSpace = new RcImageSpace();
                			imageSpace.setFileInfoId(fileInfoId);
                			imageSpace.setMemberId(memberId);
                			imageSpace.setParentId(parentId);
                			imageSpace.setType(ImageSpaceTypeEnum.IMAGE.getValue());
                			imageSpace.setName(originalName + "." + suffixName);
                			imageSpace.setImgSize(fileInfo.getSize());
                			try {
                				BufferedImage image = ImageIO.read(new FileInputStream(file));
                				if (image != null) {
                					imageSpace.setImgWidth(image.getWidth());
                        			imageSpace.setImgHeight(image.getHeight());
                				}
                			} catch (IOException e) {
                			}
                			long imageSpaceId = imageSpaceService.addImageSpace(imageSpace);
                			
                			imgUrl = propertyConstants.systemFileServerUrl + fileInfo.getPath();
                			model.addAttribute("imgUrl", imgUrl);
                			model.addAttribute("fileid", imageSpaceId);
                			model.addAttribute("success", true);
                		}
                    }
                }
            }
        }
		return "ajax_upload_result";
	}
	
	/**
	 * 商品列表
	 * @param request
	 * @param model
	 * @param currentPage
	 * @param productParam
	 * @param isSuccess
	 * @param upDown
	 * @return
	 */
	@RequestMapping("/product_list")
	public String listProducts(
			HttpServletRequest request,
			Model model,
			Integer currentPage,
			ProductParam productParam,
			@RequestParam(value = "is_success", required = false) Boolean isSuccess,
			@RequestParam(value = "up_down", required = false) String upDown) {
		Integer merchantUserId = getMerchantUserId(request);
		if(merchantUserId != null) {
			RcMerchantUser merchantUser = merchantUserService.findMerchantUserById(merchantUserId);
			productParam.setMerchantId(merchantUser.getMerchantId());
		}
		Integer vendorUserId = getVendorUserId(request);
		if(vendorUserId != null) {
			RcVendorUser vendorUser = vendorUserService.findVendorUserById(vendorUserId);
			productParam.setVendorId(vendorUser.getVendorId());
		}
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		RcProd param = new RcProd();
		if("1".equals(productParam.getIsDesc())){
			param.setOrderBy("PUTAWAY_TIME DESC");
		}
		else if("2".equals(productParam.getIsDesc())){
			param.setOrderBy("PUTAWAY_TIME ASC");
		} 
		else{
			param.setOrderBy("PROD_ID DESC");
		}
		param.setProdId(productParam.getProdId());
		param.setName(productParam.getProdName());
		param.setStatus(productParam.getProdStatus());
		param.setType(productParam.getProductType());
		param.setMerchantId(productParam.getMerchantId());
		param.setMerchantName(productParam.getMerchantName());
		param.setCityName(productParam.getCity());
		param.setIsBaseProd(YesOrNoEnum.NO.getValue());
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcProd> prods = prodService.findPagedProds(param, page);
		model.addAttribute("productList", prods);
		model.addAttribute("productParam", productParam);
		model.addAttribute("is_success", isSuccess);
		model.addAttribute("up_down", upDown);
		if(flag) {
			return "productServiceManage_goodsPutAway";
		} else {
			return "productServiceManage_goodsPutAway_inner";
		}
	}
	
	/**
	 * 商品列表
	 * @param request
	 * @param model
	 * @param currentPage
	 * @param productParam
	 * @param isSuccess
	 * @param upDown
	 * @return
	 */
	@RequestMapping("/base_product_list")
	public String listBaseProducts(
			HttpServletRequest request,
			Model model,
			Integer currentPage,
			ProductParam productParam,
			@RequestParam(value = "is_success", required = false) Boolean isSuccess,
			@RequestParam(value = "up_down", required = false) String upDown) {
		RcProd param = new RcProd();
		Integer vendorUserId = getVendorUserId(request);
		if(vendorUserId != null) {
			RcVendorUser vendorUser = vendorUserService.findVendorUserById(vendorUserId);
			param.setVendorId(vendorUser.getVendorId());
		}
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		if("1".equals(productParam.getIsDesc())){
			param.setOrderBy("PUTAWAY_TIME DESC");
		}
		else if("2".equals(productParam.getIsDesc())){
			param.setOrderBy("PUTAWAY_TIME ASC");
		} 
		else{
			param.setOrderBy("PROD_ID DESC");
		}
		param.setProdId(productParam.getProdId());
		param.setName(productParam.getProdName());
		param.setStatus(productParam.getProdStatus());
		param.setType(productParam.getProductType());
		param.setMerchantId(productParam.getMerchantId());
		param.setMerchantName(productParam.getMerchantName());
		param.setCityName(productParam.getCity());
		param.setIsBaseProd(YesOrNoEnum.YES.getValue());
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcProd> prods = prodService.findPagedProds(param, page);
		model.addAttribute("productList", prods);
		model.addAttribute("productParam", productParam);
		model.addAttribute("is_success", isSuccess);
		model.addAttribute("up_down", upDown);
		if(flag) {
			return "baseProductServiceManage_goodsPutAway";
		} else {
			return "baseProductServiceManage_goodsPutAway_inner";
		}
	}
	
	@RequestMapping("/prod_put_up_down")
	@ResponseBody
	public String prodPutOnDown(HttpServletRequest request, String prodId,
			String action, Model model,
			Integer currentPage) {
//		Boolean is_success = Boolean.TRUE;
//		String upDown = null;
//		if ("up".equals(action)) {
//			upDown = "up";
//		} else {
//			upDown = "down";
//		}
		try {
			String[] prodIds = prodId.split(",,,");
			for(String pId : prodIds){
				if ("up".equals(action)) {
					productManagerService.doProdPutAway(Integer.parseInt(pId));
				} else if ("down".equals(action)) {
					productManagerService.doProdOutOfStock(Integer.parseInt(pId));
				}
//				RcProd prod = prodService.findProdById(Integer.parseInt(pId));
//				HomePageVO homePageVO = homePageService.getHomePageData(prod.getCityId());
//				redisService.hsetObject(Constants.REDIS_HOMEPAGE, String.valueOf(prod.getCityId()), homePageVO);
			}
		} catch (Exception e) {
			log.error(e);
//			is_success = Boolean.FALSE;
		}
//		// 商品上/下架后，如果不去除prodId属性，只会查出来上/下架的这一件商品
//		productParam.setProdId(null);
		return "{\"code\":\"1\"}";
//		return listProducts(request, model, currentPage, null,
//				is_success, upDown);
	}
	
	/**
	 * 查询SKU库存
	 * @param prodId
	 * @return
	 */
	@RequestMapping("/search_sku_storage")
	@ResponseBody
	public String searchProdSkuStorage(int prodId) {
		List<Map<String, Object>> skuStorages = productManagerService
				.querySkuStoragesBySkuId(prodId);
		return JSONArray.fromObject(skuStorages).toString();
	}
	
	/**
	 * 查询商品库存
	 * @param prodId
	 * @return
	 */
	@RequestMapping(value="search_prod_storage")
	@ResponseBody
	public String searchProdStorage(int prodId){
		List<Map<String, Object>> skuStorages = productManagerService.queryProdStorageByProdId(prodId);
		return JSONArray.fromObject(skuStorages).toString();
	}
	
	/**
	 * 修改SKU库存
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping("/update_sku_storage")
	@ResponseBody
	public String updateProdSkuStorage(String json) {
		JSONArray jsondata = JSONArray.fromObject(json);
		BigDecimal[] originPrice = new BigDecimal[jsondata.size()];
		BigDecimal[] price = new BigDecimal[jsondata.size()];
		Integer prodId = 0;
		int prodInventory = 0;
		int j = 0;
		for (Object d : jsondata) {
			String[] data = d.toString().split(",,,");
			RcProdSku prodSku = prodSkuService.findProdSkuByProdSkuId(data[0]);
			prodId = prodSku.getProdId();
			
			prodSku.setOriginPrice(new BigDecimal(data[3]));
			prodSku.setPrice(new BigDecimal(data[4]));
			int skuInventory = Integer.parseInt(data[2]);
			prodSku.setInventory(skuInventory);
			prodInventory += skuInventory;
			prodSkuService.modifyProdSku(prodSku);
			originPrice[j] = prodSku.getOriginPrice();
			price[j] = prodSku.getPrice();
			j++;
		}
		//取最大值和最小值
        BigDecimal maxOriginPrice = new BigDecimal(0);
        BigDecimal minPrice = new BigDecimal(Long.MAX_VALUE);
        for (int i = 0; i < originPrice.length; i++) {
            if (originPrice[i].compareTo(maxOriginPrice) > 0) {
            	maxOriginPrice = originPrice[i];
            }
        }
        for (int i = 0; i < price.length; i++) {
            if (price[i].compareTo(minPrice) < 0) {
            	minPrice = price[i];
            }
        }
		RcProd prod = prodService.findProdById(prodId);
		prod.setOriginPrice(maxOriginPrice);
		prod.setPrice(minPrice);
		prod.setInventory(prodInventory);
		prodService.modifyProd(prod);
		return "{\"code\":\"1\"}";
	}
	
	/**
	 * 修改商品库存
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping("/update_prod_storage")
	@ResponseBody
	public String updateProdStorage(String json) {
		JSONArray jsondata = JSONArray.fromObject(json);
		for (Object d : jsondata) {
			String[] data = d.toString().split(",,,");
			RcProd prod = prodService.findProdById(Integer.parseInt(data[0]));
			prod.setOriginPrice(new BigDecimal(data[3]));
			prod.setPrice(new BigDecimal(data[4]));
			prod.setInventory(Integer.parseInt(data[2]));
			prodService.modifyProd(prod);
		}
		return "{\"code\":\"1\"}";
	}
	
	@RequestMapping("/chooselist")
	public String chooselist(
			HttpServletRequest request,
			Model model,
			Integer currentPage,
			ProductParam productParam,
			@RequestParam(value = "is_success", required = false) Boolean isSuccess,
			@RequestParam(value = "up_down", required = false) String upDown) {
		Integer merchantUserId = getMerchantUserId(request);
		if(merchantUserId != null) {
			RcMerchantUser merchantUser = merchantUserService.findMerchantUserById(merchantUserId);
			productParam.setMerchantId(merchantUser.getMerchantId());
		}
		Integer vendorUserId = getVendorUserId(request);
		if(vendorUserId != null) {
			RcVendorUser vendorUser = vendorUserService.findVendorUserById(vendorUserId);
			productParam.setVendorId(vendorUser.getVendorId());
		}
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		RcProd param = new RcProd();
		if(productParam.getIsDesc()!=null){
			if("1".equals(productParam.getIsDesc())){
				param.setOrderBy("PUTAWAY_TIME DESC");
			}
			else{
				param.setOrderBy("PUTAWAY_TIME ASC");
			} 
		}
		param.setProdId(productParam.getProdId());
		param.setName(productParam.getProdName());
		param.setStatus(productParam.getProdStatus());
		param.setType(productParam.getProductType());
		param.setMerchantId(productParam.getMerchantId());
		param.setMerchantName(productParam.getMerchantName());
		param.setCityName(productParam.getCity());
		param.setIsBaseProd(YesOrNoEnum.NO.getValue());
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcProd> prods = prodService.findPagedProds(param, page);
		model.addAttribute("productList", prods);
		model.addAttribute("productParam", productParam);
		model.addAttribute("is_success", isSuccess);
		model.addAttribute("up_down", upDown);
		if(flag) {
			return "homepage/choseProdList";
		} else {
			return "homepage/choseProdList_inner";
		}
	}
	
//	@RequestMapping(value="/checkstorestatus")
//	@ResponseBody
//	public String checkStoreStatus(int prodId){
//		RcMerchant merchant = merchantService.findMerchantByProdId(prodId);
//		if(merchant.getDelFlag() == YesOrNoEnum.YES.getValue()){
//			return "{\"code\":\"1\"}";
//		} else{
//			return "{\"code\":\"0\"}";
//		}
//	}
	
	@RequestMapping(value="initupdate")
	public String initUpdate(Integer prodId,Model model,HttpServletRequest request){
		Integer merchantUserId = getMerchantUserId(request);
		if(merchantUserId == null){
			model.addAttribute("rcMerchant", "-1");
		} else{
			RcMerchantUser merchantUser = merchantUserService.findMerchantUserById(merchantUserId);
			model.addAttribute("rcMerchant", merchantUser.getMerchantId());
		}
//		List<RcMerchant> list = merchantService.findAllMerchants();
		RcProd rcProduct = prodService.findProdById(prodId);
		RcProdCat dadCategory = prodCatService.findProdCatById(rcProduct.getProdCatId());
		Integer dadProdCatId = dadCategory.getParentId();
		setData(model, new String[]{"list","prodId","prodCatId","subProdCatId","prodType"},
				       new Object[]{null,prodId,dadProdCatId,rcProduct.getProdCatId(),rcProduct.getType()});
		return "productServiceManage_entryUpGoods";
	}
	
	@RequestMapping(value="initbaseupdate")
	public String initBaseUpdate(Integer prodId,Model model,HttpServletRequest request){
//		List<RcMerchant> list = merchantService.findAllMerchants();
		RcProd rcProduct = prodService.findProdById(prodId);
		RcProdCat dadCategory = prodCatService.findProdCatById(rcProduct.getProdCatId());
		Integer dadProdCatId = dadCategory.getParentId();
		setData(model, new String[]{"list","prodId","prodCatId","subProdCatId","prodType"},
				       new Object[]{null,prodId,dadProdCatId,rcProduct.getProdCatId(),rcProduct.getType()});
		return "baseProductServiceManage_entryUpGoods";
	}
	
	@RequestMapping(value="updategoods")
	public String updateGoods(Integer prodId,Integer subprodCatId,Integer prodType,Model model,HttpServletRequest request,HttpServletResponse resp) throws Exception{
		ProductPublishedDTO publishedDTO = productManagerService.getProductDetail(prodId);
		List<RcProdPropInfo> prodPropInfos = publishedDTO.getProdPropInfos();
		Integer merchantId = publishedDTO.getMerchantId();
		if(publishedDTO.getProductType()==null){
			publishedDTO.setProductType(1);
		}
		List<ProdPropListDTO> listProps = productManagerService.findProdPropsByProdCatId(subprodCatId);
		List<ProductPropListVO> resultProd = new ArrayList<ProductPropListVO>();
		Map<Integer,ProductPropListVO> submap = new HashMap<Integer,ProductPropListVO>();
		if(listProps != null){
			for(ProdPropListDTO listProp : listProps){
				String propSelfdf = "1";
				int id = listProp.getProdPropId();
				ProductPropListVO vo = submap.get(id);
				if(vo==null){
					vo = new ProductPropListVO();
					Map<String,Object> otherMap = new HashMap<String,Object>();
					otherMap.put("PRODPROPID", listProp.getProdPropId());
					otherMap.put("PROD_PROP_NAME", listProp.getProdPropName());
					otherMap.put("PROD_PROP_UNIT", "");
					otherMap.put("IS_COLOR", listProp.getIsColor());
					otherMap.put("IS_EDIT", 1);
					otherMap.put("IS_REQUIRED", 0);
					otherMap.put("PRODPROPNAME", listProp.getProdPropName());
					vo.setOtherData(otherMap);
					submap.put(id,vo);
					resultProd.add(vo);
				}
				ComplaintListProductVO pro = new ComplaintListProductVO(listProp.getProdPropEnum(),listProp.getProdPropEnumId().toString(),propSelfdf);
				vo.getProducts().add(pro);
			}
		}
		//单品属性
		Map<Integer, List<RcProdSkuPropInfo>> prodSkuPropInfoMap = publishedDTO.getProdSkuPropInfoMap();
		List<ProdPropListDTO> listSales = productManagerService.findProdSkuPropsByProdCatId(subprodCatId, prodType); 
		List<ProductPropListVO> resultSales = new ArrayList<ProductPropListVO>();
		Map<Integer,ProductPropListVO> submap1 = new HashMap<Integer,ProductPropListVO>();
		if(listSales!=null){
			for(ProdPropListDTO e: listSales){
				if (!CollectionUtils.isEmpty(prodSkuPropInfoMap)) {
					for (Entry<Integer,List<RcProdSkuPropInfo>> entry : prodSkuPropInfoMap.entrySet()) {
						List<RcProdSkuPropInfo> listEntry = entry.getValue();
						for(RcProdSkuPropInfo en: listEntry){
							if(en.getProdPropId().equals(e.getProdPropId()) && en.getProdPropEnumId().equals(e.getProdPropEnumId())){
								e.setProdPropEnum( en.getProdPropVal());
							}
						}
					}
				}
				
				String propSelfdf = "1";
				int id = e.getProdPropId();
				ProductPropListVO vo = submap1.get(id);
				if(vo==null){
					vo = new ProductPropListVO();
					Map<String,Object> otherMap = new HashMap<String,Object>();
					otherMap.put("PRODPROPID", e.getProdPropId());
					otherMap.put("PROD_PROP_NAME", e.getProdPropName());
					otherMap.put("PROD_PROP_UNIT", "");
					otherMap.put("IS_COLOR", e.getIsColor());
					otherMap.put("IS_EDIT", 1);
					otherMap.put("PRODPROPNAME", e.getProdPropName());
					vo.setOtherData(otherMap);
					submap1.put(id,vo);
					resultSales.add(vo);
				}
				ComplaintListProductVO pro = new ComplaintListProductVO(e.getProdPropEnum(),e.getProdPropEnumId().toString(),propSelfdf);
				vo.getProducts().add(pro);
			}
		}
		List<String> prodImgUrls = publishedDTO.getProdImgUrls();
		//品牌
		RcProdBrand param = new RcProdBrand();
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		List<RcProdBrand> prodBrands = prodBrandService.findAllProdBrands(param);
		Map<Integer, List<String>> prodSkuImgUrlsMap = publishedDTO.getSkuImgUrlsMap();
		List<RcProdSku> prodSkus = publishedDTO.getProdSkus();
		setData(model, new String[]{"prodPropInfos","prodImgUrls","prodSkuImgUrlsMap","productDTO","ImageObj","listBrand","listFreight","listProd","listSales","prodSkuPropInfoMap","prodSkus","prodSkuStorage","prodType","prodCatId","merchantid"},
				       new Object[]{prodPropInfos,prodImgUrls,prodSkuImgUrlsMap,publishedDTO,null,prodBrands,null,resultProd,resultSales,prodSkuPropInfoMap,prodSkus,null,prodType,subprodCatId,merchantId});
		return "productServiceManage_updateGoods"; 
	}
	
	@RequestMapping(value="updatebasegoods")
	public String updateBaseGoods(Integer prodId,Integer subprodCatId,Integer prodType,Model model,HttpServletRequest request,HttpServletResponse resp) throws Exception{
		ProductPublishedDTO publishedDTO = productManagerService.getProductDetail(prodId);
		List<RcProdPropInfo> prodPropInfos = publishedDTO.getProdPropInfos();
		Integer merchantId = publishedDTO.getMerchantId();
		if(publishedDTO.getProductType()==null){
			publishedDTO.setProductType(1);
		}
		List<ProdPropListDTO> listProps = productManagerService.findProdPropsByProdCatId(subprodCatId);
		List<ProductPropListVO> resultProd = new ArrayList<ProductPropListVO>();
		Map<Integer,ProductPropListVO> submap = new HashMap<Integer,ProductPropListVO>();
		if(listProps != null){
			for(ProdPropListDTO listProp : listProps){
				String propSelfdf = "1";
				int id = listProp.getProdPropId();
				ProductPropListVO vo = submap.get(id);
				if(vo==null){
					vo = new ProductPropListVO();
					Map<String,Object> otherMap = new HashMap<String,Object>();
					otherMap.put("PRODPROPID", listProp.getProdPropId());
					otherMap.put("PROD_PROP_NAME", listProp.getProdPropName());
					otherMap.put("PROD_PROP_UNIT", "");
					otherMap.put("IS_COLOR", listProp.getIsColor());
					otherMap.put("IS_EDIT", 1);
					otherMap.put("IS_REQUIRED", 0);
					otherMap.put("PRODPROPNAME", listProp.getProdPropName());
					vo.setOtherData(otherMap);
					submap.put(id,vo);
					resultProd.add(vo);
				}
				ComplaintListProductVO pro = new ComplaintListProductVO(listProp.getProdPropEnum(),listProp.getProdPropEnumId().toString(),propSelfdf);
				vo.getProducts().add(pro);
			}
		}
		//单品属性
		Map<Integer, List<RcProdSkuPropInfo>> prodSkuPropInfoMap = publishedDTO.getProdSkuPropInfoMap();
		List<ProdPropListDTO> listSales = productManagerService.findProdSkuPropsByProdCatId(subprodCatId, prodType); 
		List<ProductPropListVO> resultSales = new ArrayList<ProductPropListVO>();
		Map<Integer,ProductPropListVO> submap1 = new HashMap<Integer,ProductPropListVO>();
		if(listSales!=null){
			for(ProdPropListDTO e: listSales){
				if (!CollectionUtils.isEmpty(prodSkuPropInfoMap)) {
					for (Entry<Integer,List<RcProdSkuPropInfo>> entry : prodSkuPropInfoMap.entrySet()) {
						List<RcProdSkuPropInfo> listEntry = entry.getValue();
						for(RcProdSkuPropInfo en: listEntry){
							if(en.getProdPropId().equals(e.getProdPropId()) && en.getProdPropEnumId().equals(e.getProdPropEnumId())){
								e.setProdPropEnum( en.getProdPropVal());
							}
						}
					}
				}
				
				String propSelfdf = "1";
				int id = e.getProdPropId();
				ProductPropListVO vo = submap1.get(id);
				if(vo==null){
					vo = new ProductPropListVO();
					Map<String,Object> otherMap = new HashMap<String,Object>();
					otherMap.put("PRODPROPID", e.getProdPropId());
					otherMap.put("PROD_PROP_NAME", e.getProdPropName());
					otherMap.put("PROD_PROP_UNIT", "");
					otherMap.put("IS_COLOR", e.getIsColor());
					otherMap.put("IS_EDIT", 1);
					otherMap.put("PRODPROPNAME", e.getProdPropName());
					vo.setOtherData(otherMap);
					submap1.put(id,vo);
					resultSales.add(vo);
				}
				ComplaintListProductVO pro = new ComplaintListProductVO(e.getProdPropEnum(),e.getProdPropEnumId().toString(),propSelfdf);
				vo.getProducts().add(pro);
			}
		}
		List<String> prodImgUrls = publishedDTO.getProdImgUrls();
		//品牌
		RcProdBrand param = new RcProdBrand();
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		List<RcProdBrand> prodBrands = prodBrandService.findAllProdBrands(param);
		Map<Integer, List<String>> prodSkuImgUrlsMap = publishedDTO.getSkuImgUrlsMap();
		List<RcProdSku> prodSkus = publishedDTO.getProdSkus();
		setData(model, new String[]{"prodPropInfos","prodImgUrls","prodSkuImgUrlsMap","productDTO","ImageObj","listBrand","listFreight","listProd","listSales","prodSkuPropInfoMap","prodSkus","prodSkuStorage","prodType","prodCatId","merchantid"},
				       new Object[]{prodPropInfos,prodImgUrls,prodSkuImgUrlsMap,publishedDTO,null,prodBrands,null,resultProd,resultSales,prodSkuPropInfoMap,prodSkus,null,prodType,subprodCatId,merchantId});
		return "baseProductServiceManage_updateGoods"; 
	}
	
	/**
	 * 修改商品
	 */
	@RequestMapping(value="/saveupdatedgoods")
	@ResponseBody
	public String saveupdatedgoods(Integer prodId, String json,String skuvalue,String propdrr,String skuattr,String colorenumid,String skupropnum,String imgids,Model model,HttpServletRequest request,HttpServletResponse resp) throws Exception{
		JSONObject jo = JSONObject.fromObject(json);
		//SKU属性组装
		List<RcProdSku> prodSkus = new ArrayList<RcProdSku>();
		Map<Integer, List<RcProdSkuPropInfo>> prodSkuPropInfoMap = new HashMap<Integer, List<RcProdSkuPropInfo>>();
		JSONArray jsonSkudrr = JSONArray.fromObject(skuvalue);
		JSONArray jsonSkuprop = JSONArray.fromObject(skuattr);
		int i=0;
		//添加SKU图片
		JSONArray skuImageUrls = jo.getJSONArray("propurls");
		int skuColorNum =0;
		Map<Integer, List<String>> prodSkuImgUrlsMap = new HashMap<Integer, List<String>>();
		String[]  enumid = colorenumid.split(",,,");
		for(Object d :skuImageUrls){
			List<String> prodSkuImgUrls = new ArrayList<String>();
			String[] skuUrl = d.toString().split(",,,");
			for(int skui=0;skui<skuUrl.length;skui++){
				prodSkuImgUrls.add(skuUrl[skui]);
			}
			prodSkuImgUrlsMap.put(Integer.parseInt(enumid[skuColorNum]), prodSkuImgUrls);
			skuColorNum++;
		}
			
		for(Object d :jsonSkuprop) {
			List<RcProdSkuPropInfo> prodSkuPropInfo = new ArrayList<RcProdSkuPropInfo>();
			String[] propnum = d.toString().split("@@@");
			for(int num=0;num<propnum.length;num++){
				String[] propstring = propnum[num].split(",,,");
				RcProdSkuPropInfo ecProdSkuPropInfo = new RcProdSkuPropInfo();
				ecProdSkuPropInfo.setProdPropId(Integer.parseInt(propstring[0]));
				ecProdSkuPropInfo.setProdPropEnumId(Integer.parseInt(propstring[1]));
				ecProdSkuPropInfo.setProdPropVal(propstring[2]);
				ecProdSkuPropInfo.setProdPropName(propstring[3]);
				prodSkuPropInfo.add(ecProdSkuPropInfo);
			}
			prodSkuPropInfoMap.put(i, prodSkuPropInfo);
			i++;
		}
		for(Object c :jsonSkudrr) {
			String[] string = c.toString().split(",,,");
			
			RcProdSku ecProdSku = new RcProdSku();
			ecProdSku.setOriginPrice(new BigDecimal(string[1]));
			ecProdSku.setPrice(new BigDecimal(string[2]));
			ecProdSku.setInventory(Integer.parseInt(string[0]));
			if(string.length==5) {
				ecProdSku.setProdSkuId(string[4]);
			}
			prodSkus.add(ecProdSku);
		}
		
		
		ProductPublishedDTO productPublishedDTO = new ProductPublishedDTO();
		//true：新增操作；false:修改操作
		productPublishedDTO.setIfAdded(false);
		//商品Id
		productPublishedDTO.setProdId(prodId);
		//商品名称
		productPublishedDTO.setProdName(jo.getString("prodName"));
		//库存预警
		productPublishedDTO.setProdStockWarning(jo.getInt("prodStockWarning"));
		//商品库存
		if(jo.containsKey("prodStorage") && !"".equals(jo.getString("prodStorage"))) {
			productPublishedDTO.setProdStorage(jo.getInt("prodStorage"));
		}
		//计量单位
		productPublishedDTO.setProdMeasureUnit(jo.getString("prodMeasureUnit"));
		//商品描述
		productPublishedDTO.setProdDetail(jo.getString("prodDetailDescContent"));
		//原价
		productPublishedDTO.setProdOriginPrice(new BigDecimal(jo.getString("prodOriginPrice")));
		//当前价
		productPublishedDTO.setProdPrice(new BigDecimal(jo.getString("prodPrice")));
		//运费模板
		productPublishedDTO.setProdFreightTempId(null);
		//商品类型
		if(jo.containsKey("productType")) {
			productPublishedDTO.setProductType(jo.getInt("productType"));
		} else {
			productPublishedDTO.setProductType(ProdTypeEnum.ENTITY.getValue());
		}
		//组装商品图片列表
		String prodimageurls = jo.getString("prodImgUrls");
		String[]  imgurls = prodimageurls.split(",,,");
		List<String> prodimgs = new ArrayList<String>();
		for(int q=0;q<imgurls.length;q++){
			prodimgs.add(imgurls[q]);
		}
		//商品图片
		productPublishedDTO.setProdImgUrls(prodimgs);
		//广告词
		productPublishedDTO.setProdAdword(jo.getString("prodAdword"));    
		//品类ID
		productPublishedDTO.setProdCatId(jo.getInt("prodCatId"));
		//商品品牌ID
		productPublishedDTO.setProdBrandId(jo.getInt("prodBrandId"));
		//其他品牌名称
		if(jo.getInt("prodBrandId") == 0 && jo.containsKey("prodBrandName")){
			productPublishedDTO.setProdBrandName(jo.getString("prodBrandName"));
		}
		//商户ID
		productPublishedDTO.setMerchantId(jo.getInt("merchantId"));
		//添加SKU属性
		productPublishedDTO.setProdSkus(prodSkus);
		productPublishedDTO.setProdSkuPropInfoMap(prodSkuPropInfoMap);
		productPublishedDTO.setProdSkuImgUrlsMap(prodSkuImgUrlsMap);
		
		//设置商品属性
		List<RcProdPropInfo> prodPropInfos = new ArrayList<RcProdPropInfo>();
		JSONArray jsonPropdrr = JSONArray.fromObject(propdrr);
		for(Object c :jsonPropdrr){
			String[] string = c.toString().split(",,,");
			RcProdPropInfo ecProdPropInfo = new RcProdPropInfo();
			ecProdPropInfo.setProdPropId(Integer.parseInt(string[0]));
			ecProdPropInfo.setProdPropVal(string[1]);
			ecProdPropInfo.setProdPropName(string[2]);
			//ecProdPropInfo.setProdPropInfoId(UUIDGenerator.getInstance().getReplaceShortUUID());
			prodPropInfos.add(ecProdPropInfo);
		}
		//用户自定义属性值
		JSONArray customPropdrr = jo.getJSONArray("customPropArr");
		for(Object c :customPropdrr){
			String[] string = c.toString().split(",,,");
			RcProdPropInfo ecProdPropInfo = new RcProdPropInfo();
			ecProdPropInfo.setProdPropVal(string[1]);
			ecProdPropInfo.setProdPropName(string[0]);
			ecProdPropInfo.setProdPropId(0);
			//ecProdPropInfo.setProdPropInfoId(UUIDGenerator.getInstance().getReplaceShortUUID());
			prodPropInfos.add(ecProdPropInfo);
		}
		
		productPublishedDTO.setProdPropInfos(prodPropInfos);
		productManagerService.publishProduct(productPublishedDTO);
		return "{\"code\":\"1\",\"prodId\":\""+prodId+"\"}";
	}
	
	@RequestMapping("/deleteProd")
	public String deleteProd(String prodId) {
		String[] prodIds = prodId.split(",,,");
		for(String pId:prodIds){
			prodService.removeProd(Integer.parseInt(pId));
		}
		return "redirect:/product/product_list";
	}
	
	@RequestMapping("/getAllStore")
	@ResponseBody
	public String getAllStore() {
		List<RcMerchant> list = merchantService.findAllEnabledMerchants();
		return JsonUtil.toJson(list);
	}
	
	@RequestMapping("/beginCopyOpt")
	@ResponseBody
	public String beginCopyOpt(String prodIds, String merchantIds) {
		String[] prodId = prodIds.split(",,,");
		String[] merchantId = merchantIds.split(",,,");
		for(String pId:prodId){
			for(String mId:merchantId){
				productManagerService.copyProduct(Integer.parseInt(pId), Integer.parseInt(mId));
			}
		}
		return "{\"code\":\"1\"}";
	}
}
