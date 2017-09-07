package com.socool.soft.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.socool.soft.bo.RcCity;
import com.socool.soft.bo.RcProd;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.service.ICityService;
import com.socool.soft.service.ISearchService;
import com.socool.soft.util.JsonUtil;

import net.sf.json.JSONObject;

@RequestMapping(value="/search")
@Controller
public class SearchController extends BaseController {

	@Autowired
	private ISearchService searchService;
	@Autowired
	private ICityService cityService;
//	@Autowired
//	private IProdBrandService prodBrandService;
	@Autowired
	private PropertyConstants propertyConstants;
	
	@RequestMapping(value="prodlist")
	@ResponseBody
	public String prodList(String data,String timestamp,String nonceStr,String product,String signature){
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		String keyword = json.getString("keyword");
		String allCat = json.getString("allCat");
		int targetPage = 0;
		try {
			targetPage = json.getInt("targetPage");
		} catch(Exception e) {
			targetPage = 1;
		}
		String additional = json.getString("additional");
		String sortWay = json.getString("sortWay");
		String sortType = json.getString("sortType");
		String city = json.getString("city");
		RcCity rcCity = cityService.findCityByName(city);
		
		String query = "";
		keyword = keyword.trim().replace(" ", "\\ ");
		if("nomal".equals(allCat)){
			query = "PROD_NAME:*"+keyword+"* OR CAT_NAME_CP:*"+keyword+"* OR PROD_BRAND_NAME_CP:*"+keyword+"* OR PROD_NAME_CP:*"+keyword+"*";
		}
		else if("prodCategory".equals(allCat)){
			query= "PROD_CAT_ID:"+keyword;
		}
		else if("prodBrand".equals(allCat)){
			query= "PROD_BRAND_ID:"+keyword;
		}
		else if("sameMerchant".equals(allCat)){
			query= "MERCHANT_ID:"+keyword;
		}
		else if("hotProd".equals(allCat)){
			query= "IS_HOT:1";
		}
		String rc_prod=propertyConstants.solrUrl;
		SolrServer server = new HttpSolrServer(rc_prod);
		
		SolrQuery params = new SolrQuery();
		// 查询关键词，*:*代表所有属性、所有值，即所有index
	    params.set("q", query);
	    params.addFilterQuery("PROD_STATUS:2");
	    //RcCity city = cityService.findCityByCityId(city.getCityId());
	    String cityName = rcCity.getName().replace(" ", "\\ ");
	    params.addFilterQuery("CITY:"+cityName);
	    if(StringUtils.isNotBlank(additional)){
			String[] qs = additional.split(";");
			for(int i=0;i<qs.length;i++){
				if(i==0){
					if(StringUtils.isNotBlank(qs[0])){
						String s = qs[0].trim().replace(" ", "\\ ");
						params.addFilterQuery("PROD_BRAND_NAME:"+s);
					}
				}
				else{
					if(StringUtils.isNotBlank(qs[1])){
						String[] price = qs[1].split("-");
						params.addFilterQuery("PROD_PRICE:["+price[0]+" TO "+price[1]+"]");
					}
					//params.set("fq", "PROD_PRICE:["+price[0]+" TO "+price[1]+"]");
				}
				
			}
		}
	    //params.set("fq", "PROD_STATUS:2");
	    // 分页，start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
	    int start = 0;
	    if(!"1".equals(targetPage)){
	    	start = (targetPage-1) * 10;
	    }
	    params.setStart(start);
	    params.setRows(10);
	    params.set("facet", "true");
		params.addFacetField("PROD_BRAND_ID");
		params.addFacetField("PROD_BRAND_NAME");
		params.setFacetMinCount(1);
	    if(StringUtils.isNotBlank(sortWay)){
	    	String sort = "";
	    	if("ALL".equals(sortType)){
	    		sort="PROD_ID "+sortWay;
	    	}
	    	else if("SAILES".equals(sortType)){
	    		sort = "SOLD " + sortWay;
	    	}
	    	else if("PRICE".equals(sortType)){
	    		sort = "PROD_PRICE " + sortWay;
	    	}
	    	else if("PRODUCT".equals(sortType)){
	    		sort="PROD_ID "+sortWay; 
	    		params.addFilterQuery("PRODUCT_TYPE:1");
	    	}
	    	else if("SERVICE".equals(sortType)){
	    		sort="PROD_ID "+sortWay; 
	    		params.addFilterQuery("PRODUCT_TYPE:2");
	    	}
	    	if(!"".equals(sort)){
	    		params.set("sort", sort);
	    	}
	    }
	    try {
	        QueryResponse response = server.query(params);
	        
	        SolrDocumentList list = response.getResults();
	        int count = list.size();
	        List<RcProd> result = new ArrayList<RcProd>();
	        for (SolrDocument solrDocument : list) {
	        	RcProd prod = new RcProd();
	        	String prodId = (String)solrDocument.getFieldValue("PROD_ID");
	        	String prodName = (String)solrDocument.getFieldValue("PROD_NAME");
	        	String prodBrandLogoUrl = (String)solrDocument.getFieldValue("PROD_BRAND_LOGO_URL");
	        	String prodUrl = (String)solrDocument.getFieldValue("IMG_URL");
	        	Double prodOriginPrice = (Double)solrDocument.getFieldValue("PROD_ORIGIN_PRICE");
	        	Double prodPrice = (Double)solrDocument.getFieldValue("PROD_PRICE");
	        	String merchantName = (String)solrDocument.getFieldValue("MERCHANT_NAME");
	        	Integer productType = (Integer)solrDocument.getFieldValue("PRODUCT_TYPE");
	        	String prodBrandName = (String)solrDocument.getFieldValue("PROD_BRAND_NAME");
	        	Float score = (Float)solrDocument.getFieldValue("SCORE");
	        	String catName = (String)solrDocument.getFieldValue("CAT_NAME");
	        	
	        	prod.setProdName(prodName);
	        	prod.setMerchantName(merchantName);
	        	prod.setOriginPrice(new BigDecimal(String.valueOf(prodOriginPrice)));
	        	prod.setProdBrandLogoUrl(prodBrandLogoUrl);
	        	prod.setProdId(Integer.parseInt(prodId));
	        	prod.setProdImgUrl(prodUrl);
//	        	prod.setName(prodName);
	        	prod.setPrice(new BigDecimal(String.valueOf(prodPrice)));
	        	prod.setType(productType);
	        	prod.setProdBrandName(prodBrandName);
	        	prod.setScore(score);
	        	prod.setProdCatName(catName);
	        	
	        	result.add(prod);
	        }
	        List<FacetField> list1 =  response.getFacetFields(); 
	        List<String> brand = new ArrayList<String>();
	        List<Float> price = new ArrayList<Float>();
	        for(FacetField e:list1){
	        	System.out.println(e.getName());
	        	System.out.println("---");
	        	List<Count> counts = e.getValues();
                 for(Count count1 : counts){
                	 if("PROD_BRAND_NAME".equals(e.getName())){
                		 brand.add(count1.getName());
                	 }
                	 else if("PROD_PRICE".equals(e.getName())){
                		 price.add(Float.parseFloat(count1.getName()));
                	 }
                     System.out.println(count1.getName()+":"+count1.getCount());
                  //   result.add(count1.getName());
                 }
	        	
	        }
	        return "{\"code\":\"1\",\"result\":\"success!\",\"data\":{\"count\":"+count+",\"prods\":"+JsonUtil.toJson(result)+","
    		+ "\"brand\":"+JsonUtil.toJson(brand)+"}}";
	    } catch (SolrServerException e) {
	        e.printStackTrace();
	    }
		return "123";
	}
	
	@RequestMapping(value="/getautocomplete")
	@ResponseBody
	public String autoComplete(String data,String timestamp,String nonceStr,String product,String signature){
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		String prefix = json.getString("prefix");
		prefix = prefix.toLowerCase();
		String rc_prod=propertyConstants.solrUrl;
		SolrServer server = new HttpSolrServer(rc_prod);
		SolrQuery params = new SolrQuery();
		
		params.set("q", "*:*");
		params.set("facet", "true");
		params.addFacetField("CAT_NAME_CP");
		params.addFacetField("PROD_BRAND_NAME_CP");
		params.set("facet.prefix", prefix);
		List<String> result = new ArrayList<String>();
		 try {
		        QueryResponse response = server.query(params);
		        List<FacetField> list =  response.getFacetFields(); 
		        for(FacetField e:list){
		        	System.out.println(e.getName());
		        	System.out.println("---");
		        	List<Count> counts = e.getValues();
	                 for(Count count : counts){
	                     System.out.println(count.getName()+":"+count.getCount());
	                     result.add(count.getName());
	                 }
		        	
		        }
		 } catch (SolrServerException e) {
		        e.printStackTrace();
		 }
		        
		return "{\"code\":\"1\",\"result\":\"success!\",\"data\":"+JsonUtil.toJson(result)+"}";
	}
	
	/**
	 * 获取热门关键词
	 * @return
	 */
	@RequestMapping(value="/gethotkey")
	@ResponseBody
	public String gethotkey(String data,String timestamp,String nonceStr,String product,String signature){
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		int size = 6;
		try{
			size = json.getInt("size");
		}catch(Exception e){
		}
		List<String> list = searchService.getHotKeys(size);
		return "{\"code\":\"1\",\"result\":\"success!\",\"data\":"+JsonUtil.toJson(list)+"}";
	}
	
	/**
	 * solr数据初始化
	 * @return
	 */
	@RequestMapping(value="index")
	@ResponseBody
	public String solrIndex(){
		System.out.println("begin");
		searchService.initSolrIndexDocument();
		return "";
	}
}