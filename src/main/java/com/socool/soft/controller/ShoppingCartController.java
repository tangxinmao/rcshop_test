package com.socool.soft.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socool.soft.exception.SystemException;
import com.socool.soft.service.IShoppingCartService;
import com.socool.soft.vo.newvo.android.ShoppingCartVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RequestMapping(value="/cart")
@Controller
public class ShoppingCartController extends BaseController {

	@Autowired
	private IShoppingCartService shoppingCartService;
	
	/**
	 * 添加购物车
	 * @return
	 * @throws SystemException 
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public String addToCart(String data,String timestamp,String nonceStr,String product,String signature) throws SystemException{
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		Integer memberId = 0;
		try{
			memberId = json.getInt("memberId");
		}catch(Exception e){
			return "{\"code\":\"0\",\"result\":\"未传memberId\"}";
		}
		if(memberId==0){
			return "{\"code\":\"0\",\"result\":\"memberId不能为空！\"}";
		}
		Integer prodId = 0;
		try{
			prodId = json.getInt("prodId");
		}catch(Exception e){
			return "{\"code\":\"0\",\"result\":\"未传prodId\"}";
		}
		if(prodId==0){
			return "{\"code\":\"0\",\"result\":\"prodId不能为空！\"}";
		}
		String prodSkuId = "";
		try{
			prodSkuId = json.getString("prodSkuId");
		}catch(Exception e){
		}
		int prodNum = 0;
		try{
			prodNum = json.getInt("prodNum");
		}catch(Exception e){
			return "{\"code\":\"0\",\"result\":\"未传prodNum或格式不正确\"}";
		}
		if(prodNum==0){
			return "{\"code\":\"0\",\"result\":\"prodNum不能为0！\"}";
		}
//		int memberId = 27;
//		int prodId = 12;
//		String prodSkuId = "12_2_5";
//		int prodNum = 1;
		shoppingCartService.addShoppingCartProd(memberId, prodId, prodSkuId, prodNum);
		return "{\"code\":\"1\",\"result\":\"success!\"}";
	}
	
	/**
	 * 查看购物车
	 * @return
	 * @throws JsonProcessingException 
	 * @throws SystemException 
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public String listMyCartInfo(String data,String timestamp,String nonceStr,String product,String signature) throws JsonProcessingException, SystemException{
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		Integer memberId = 0;
		String city = "";
		try{
			memberId = json.getInt("memberId");
		}catch(Exception e){
			return "{\"code\":\"0\",\"result\":\"未传memberId\"}";
		}
		try{
			city = json.getString("city");
		}catch(Exception e){
			return "{\"code\":\"0\",\"result\":\"未传city\"}";
		}
		if(memberId==0){
			return "{\"code\":\"0\",\"result\":\"memberId不能为空！\"}";
		}
		if(StringUtils.isBlank(city)){
			return "{\"code\":\"0\",\"result\":\"city不能为空！\"}";    
		}
		ShoppingCartVO list = shoppingCartService.findShoppingCart(memberId);
		ObjectMapper objectMapper=new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		String result = "{\"code\":\"1\",\"result\":\"success\",\"data\":"+objectMapper.writeValueAsString(list)+"}";
		return result;
	}
	/**
	 * 计算购物车商品总数
	 * @return
	 * @throws SystemException 
	 */
	@RequestMapping(value="/count")
	@ResponseBody
	public String count(String data,String timestamp,String nonceStr,String product,String signature) throws SystemException{
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		Integer memberId = 0;
		try{
			memberId = json.getInt("memberId");
		}catch(Exception e){
			return "{\"code\":\"0\",\"result\":\"未传memberId\"}";
		}
		int count = shoppingCartService.countShoppingCart(memberId);
		return "{\"code\":\"1\",\"result\":\"success!\",\"data\":"+count+"}";
	}
	
	/**
	 * 编辑购物车
	 * @return
	 * @throws SystemException 
	 * @throws JsonProcessingException 
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public String edit(String data,String timestamp,String nonceStr,String product,String signature) throws SystemException, JsonProcessingException{
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		Integer memberId = null;
		try{
			memberId = json.getInt("memberId");
		}catch(Exception e){
			return "{\"code\":\"0\",\"result\":\"请传memberId\"}";
		}
		JSONArray cart = null;
		try{
			cart = json.getJSONArray("cart");
		}catch(Exception e){
			return "{\"code\":\"0\",\"result\":\"请传Cart数组！\"}";
		}
		boolean result = shoppingCartService.modifyShoppingCart(memberId, cart);
		if(result) {
			ShoppingCartVO list = shoppingCartService.findShoppingCart(memberId);
			ObjectMapper objectMapper=new ObjectMapper();
			objectMapper.setSerializationInclusion(Include.NON_NULL);
			return "{\"code\":\"1\",\"result\":\"success\",\"data\":"+objectMapper.writeValueAsString(list)+"}";
		} else {
			return "{\"code\":\"0\",\"result\":\"购物车数据格式不正确\"}";
		}
	}
}
