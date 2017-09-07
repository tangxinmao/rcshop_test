package com.socool.soft.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.socool.soft.bo.RcCity;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.exception.SystemException;
import com.socool.soft.service.ICityService;
import com.socool.soft.util.VOConversionUtil;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.Result;

import net.sf.json.JSONObject;

@RequestMapping(value = "freight")
@Controller
public class FreightController extends BaseController {
	@Autowired
	private ICityService cityService;
//	@Autowired
//	private IRedisService redisService;

	@RequestMapping("freight")
	public String freight(HttpServletRequest request, Model model, Integer currentPage, 
			String name, String provinceName, Byte isDredged, Byte isHot,String orderBy) {
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
		
		RcCity city = new RcCity();
		city.setName(name);
		city.setProvinceName(provinceName);
		city.setIsDredged(isDredged);
		city.setIsHot(isHot);
		city.setOrderBy(orderBy);
		List<RcCity> cities = cityService.findPagedCities(city, page);
		model.addAttribute("citys", cities);
		model.addAttribute("cityParam", city);
		if (flag) {
			return "freight";
		} else {
			return "freight_inner";
		}
	}

	@RequestMapping("city")
	public String city() {
		return "city";
	}

	@RequestMapping("cityHot")
	public String cityHot() {
		return "cityHot";
	}

	@RequestMapping("isdredge")
	@ResponseBody
	public String isDredge(String data, String timestamp, String nonceStr, String product, String signature) {
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}

		String city = json.getString("city");
		RcCity rcCity = cityService.findCityByName(city);
		if (rcCity.getIsDredged() == YesOrNoEnum.YES.getValue()) {
			return "{\"code\":\"1\"}";
		} else {
			return "{\"code\":\"0\"}";
		}
	}

	/**
	 * 待定功能
	 * 
	 * @param cityId
	 * @return
	 */
	@RequestMapping("deleteFreight")
	@ResponseBody
	public String deleteFreight(int cityId) {
		int result = cityService.removeCity(cityId);
		if(result > 0){
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}

	@RequestMapping("saveCity")
	@ResponseBody
	public String saveCity(Integer cityId, String name, String provinceName, byte isDredged, byte isHot, byte isDefault,
			BigDecimal freight, BigDecimal freightBaseAmount) throws SystemException {
		if(cityId != null) {
			RcCity check = cityService.findCityById(cityId);
			if(check.getIsDefault() == YesOrNoEnum.YES.getValue() && isDefault == YesOrNoEnum.NO.getValue()) {
				return "{\"code\":\"2\"}";
			}
		}
		RcCity city = new RcCity();
		city.setCityId(cityId);
		city.setName(name);
		city.setProvinceName(provinceName);
		city.setIsDredged(isDredged);
		city.setIsHot(isHot);
		city.setIsDefault(isDefault);
		city.setFreight(freight);
		city.setFreightBaseAmount(freightBaseAmount);
		int result = cityService.saveCity(city);
		if(result > 0){
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}

	@RequestMapping(value = "getFreight")
	@ResponseBody
	public Result<RcCity> getFreight(String data, String timestamp, String nonceStr, String product, String signature) {
		Result<RcCity> result = new Result<RcCity>();
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
		}

		String cityName = json.getString("cityName");
		RcCity city = cityService.findCityByName(cityName);
	//	cityService.findCityByCityId(cityId);
		VOConversionUtil.Entity2VO(city, new String[] {"freight", "freightBaseAmount"}, null);
		result.setData(city);
		return result;
	}
}
