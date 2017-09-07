package com.socool.soft.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socool.soft.bo.RcCity;
import com.socool.soft.bo.RcOrderProdEvaluation;
import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcProd;
import com.socool.soft.bo.RcProdSkuPropEnum;
import com.socool.soft.bo.RcProdPropInfo;
import com.socool.soft.bo.RcProdSku;
import com.socool.soft.service.ICityService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IProdService;
import com.socool.soft.service.IProductAppService;
import com.socool.soft.util.JsonUtil;
import com.socool.soft.util.VOConversionUtil;
import com.socool.soft.util.ViewUtil;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.Result;

/**
 * 商品APP接口服务
 * 
 * @author yh
 *
 */
@Controller
@RequestMapping(value = "/prod")
public class ProductAppController extends BaseController {
	@Autowired
	private IProdService prodService;
	@Autowired
	private IProductAppService productAppService;
	@Autowired
	private IMerchantService merchantService;
	
	@Autowired
	private ICityService cityService;

	private final static Log log = LogFactory.getLog(ProductManagerController.class);

	/**
	 * 商品详情APP接口
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/detail")
	@ResponseBody
	public String prodDetail(String data, String timestamp, String nonceStr, String product, String signature)
			throws UnsupportedEncodingException, JsonProcessingException {
		Date date = new Date();
		log.debug("===开始时间:" + ViewUtil.dateFormat(date));
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		int prodId = json.getInt("prodId");
		int buyerId = 0;
		if(json.containsKey("memberId")){
			buyerId = json.getInt("memberId");
		}
		String cityName = json.getString("city");
//		int prodId = 12;
//		int memberId = 0;
//		String city = "Bandung"; 
		RcCity city = cityService.findCityByName(cityName);
		RcProd prod = productAppService.findProdForAndroid(prodId, city.getCityId(), buyerId);
		ObjectMapper objectMapper=	new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		String result = objectMapper.writeValueAsString(prod);
		//String result = JsonUtil.toJson(prod);
		Date date1 = new Date();
		log.debug("===结束时间:" + ViewUtil.dateFormat(date1));
		long time = date1.getTime() - date.getTime();
		log.debug("=====耗时：" + time);
		return "{\"code\":\"1\",\"result\":\"" + time + "\",\"data\":" + result + "}";
	}

	/**
	 * 商品详情描述html
	 * 
	 * @param model
	 * @param prodId
	 *            商品ID
	 * @return
	 */
	@RequestMapping(value = "/content")
	public String DetailContent(Model model, int prodId) {
		RcProd product = prodService.findProdById(prodId);
		model.addAttribute("html", product.getDetail());
		return "detailContent";
	}

	/**
	 * 获取SKU信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/skuinfo")
	@ResponseBody
	public String getSkuInfo(String data, String timestamp, String nonceStr, String product, String signature) {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
//		 String jsonStr = "{\"prodId\": \"12\",\"city\":\"Jakarta Barat\",\"prodSkuPropInfo\":[{\"prodPropId\":\"3\",\"prodPropEnumId\":\"2\"},{\"prodPropId\":\"4\",\"prodPropEnumId\":\"5\"}]}";
//					JSONObject json = JSONObject.fromObject(jsonStr);
		int prodId = json.getInt("prodId");
		JSONArray prodSkuPropInfos = json.getJSONArray("prodSkuPropInfo");
		List<Integer> prodSkuPropEnumIds = new ArrayList<Integer>();
		for (Object prodSkuPropInfo : prodSkuPropInfos) {
			JSONObject jo = JSONObject.fromObject(prodSkuPropInfo);
			prodSkuPropEnumIds.add(jo.getInt("prodPropEnumId"));
		}
		RcProdSku prodSku = productAppService.findSelectedProdSku(prodId, prodSkuPropEnumIds);
		String result = JsonUtil.toJson(prodSku);
		return "{\"code\":\"1\",\"result\":\"成功！\",\"data\":" + result + "}";
	}

	/**
	 * 商品规格参数接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/propomfpinfo")
	@ResponseBody
	public String propomfpinfo(String data, String timestamp, String nonceStr, String product, String signature) {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		int prodId = json.getInt("prodId");
		List<RcProdPropInfo> infos = productAppService.findProdPropInfosByProdId(prodId);
		String result = JsonUtil.toJson(infos);
		return "{\"code\":\"1\",\"result\":\"成功！\",\"data\":" + result + "}";
	}

	/**
	 * 商品评论列表
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws JsonProcessingException 
	 */
	@RequestMapping(value = "/evaluationlist")
	@ResponseBody
	public String evaluationList(String data, String timestamp, String nonceStr, String product, String signature)
			throws UnsupportedEncodingException, JsonProcessingException {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		int prodId = json.getInt("prodId");
		Integer prodSkuPropEnumId = null;
		try {
			prodSkuPropEnumId = json.getInt("prodPropEnumId");
		} catch(Exception e) {
		}
		Integer currentPage = null;
		try {
			currentPage = json.getInt("currentPage");
		} catch(Exception e) {
			currentPage = 1;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		List<RcOrderProdEvaluation> list = productAppService.findPagedOrderProdEvaluationsByProdIdAndPackageServiceSkuPropEnumId(prodId, prodSkuPropEnumId, page);
		ObjectMapper objectMapper=	new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		String resultStr = objectMapper.writeValueAsString(list);

		return "{\"code\":\"1\",\"result\":\"成功！\",\"data\":" + resultStr + "}";
	}

	/**
	 * 获取供应商详情
	 * 
	 * @param prodId
	 * @return
	 */
	@RequestMapping(value = "/merchantinfo")
	@ResponseBody
	public String merchantinfo(String data, String timestamp, String nonceStr, String product, String signature) {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		int prodId = json.getInt("prodId");
		RcMerchant merchant = merchantService.findMerchantByProdId(prodId);
		VOConversionUtil.Entity2VO(merchant, new String[] {"merchantId", "name", "description", "logoUrl"}, null);
		String result = JsonUtil.toJson(merchant);
		return "{\"code\":\"1\",\"result\":\"成功！\",\"data\":" + result + "}";
	}

	/**
	 * 获取施工队sku
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/applicatorsku")
	@ResponseBody
	public Result<List<RcProdSkuPropEnum>> applicatorSku(String data, String timestamp, String nonceStr, String product, String signature)
			throws UnsupportedEncodingException {
		Result<List<RcProdSkuPropEnum>> result = new Result<List<RcProdSkuPropEnum>>();
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}
		int prodId = json.getInt("prodId");
		List<RcProdSkuPropEnum> list = productAppService.findServiceSkuPropEnums(prodId);
		result.setData(list);
		return result;
	}
}
