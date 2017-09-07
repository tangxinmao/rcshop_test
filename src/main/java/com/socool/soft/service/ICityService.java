package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcCity;
import com.socool.soft.vo.Page;

public interface ICityService {

	List<RcCity> findAllCities();

	/**
	 * 获取所有省的数据
	 * 
	 * @return
	 */
	List<String> findProvinces();

	RcCity findCityByName(String name);

	List<RcCity> findCitiesByName(String name);

	RcCity findDefaultCity();

	List<RcCity> findCitiesByProvinceName(String provinceName);

	List<RcCity> findCities(RcCity param);

	List<RcCity> findPagedCities(RcCity param, Page page);

	RcCity findCityById(int cityId);

	int modifyCity(RcCity city);

	int removeCity(int cityId);

	int addCity(RcCity city);

	int saveCity(RcCity city);
}
