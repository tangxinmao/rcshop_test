package com.socool.soft.service.impl;

import com.socool.soft.bo.RcCity;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.dao.RcCityMapper;
import com.socool.soft.service.ICityService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CityServiceImpl implements ICityService{
	@Autowired
	private RcCityMapper cityMapper;
	
	@Override
	public List<RcCity> findAllCities() {
		RcCity param = new RcCity();
		param.setOrderBy("NAME ASC");
		param.setIsDredged(YesOrNoEnum.YES.getValue());
		return cityMapper.select(param);
	}

	@Override
	public List<String> findProvinces() {
		RcCity param = new RcCity();
		param.setOrderBy("PROVINCE_NAME ASC");
		List<RcCity> cities = cityMapper.select(param);
		List<String> provinces = new ArrayList<String>();
		for(RcCity city : cities) {
			if(!provinces.contains(city.getProvinceName())) {
				provinces.add(city.getProvinceName());
			}
		}
		return provinces;
	}

	@Override
	public RcCity findCityByName(String name) {
		RcCity param = new RcCity();
		param.setName(name);
		return cityMapper.selectOne(param);
	}

	@Override
	public RcCity findDefaultCity() {
		RcCity param = new RcCity();
		param.setIsDefault(YesOrNoEnum.YES.getValue());;
		return cityMapper.selectOne(param);
	}

	@Override
	public List<RcCity> findCitiesByProvinceName(String provinceName) {
		RcCity param = new RcCity();
		param.setProvinceName(provinceName);
		param.setOrderBy("NAME ASC");
		return cityMapper.select(param);
	}

	@Override
	public List<RcCity> findCities(RcCity param) {
		return cityMapper.select(param);
	}

	@Override
	public List<RcCity> findPagedCities(RcCity param, Page page) {
		PageContext.setPage(page);
		if(StringUtils.isEmpty(param.getOrderBy()))
		param.setOrderBy("PROVINCE_NAME ASC, NAME ASC");
		return cityMapper.select(param);
	}

	@Override
	public RcCity findCityById(int cityId) {
		return cityMapper.selectByPrimaryKey(cityId);
	}

	@Override
	public int modifyCity(RcCity city) {
		if(city.getIsDredged() == YesOrNoEnum.NO.getValue() && city.getIsDefault() == YesOrNoEnum.YES.getValue()) {
			return 0;
		}
		
		RcCity oldCity = findCityById(city.getCityId());
		if(oldCity.getIsDredged() == YesOrNoEnum.NO.getValue() && city.getIsDredged() == YesOrNoEnum.YES.getValue()) {
			city.setDredgeTime(new Date());
		}
		if(oldCity.getIsDefault() == YesOrNoEnum.NO.getValue() && city.getIsDefault() == YesOrNoEnum.YES.getValue()) {
			RcCity defaultCity = findDefaultCity();
			if(defaultCity != null) {
				defaultCity.setIsDefault(YesOrNoEnum.NO.getValue());
				modifyCity(defaultCity);
			}
		}
		if(cityMapper.updateByPrimaryKey(city) > 0) {
			return city.getCityId();
		}
		return 0;
	}

	@Override
	public int removeCity(int cityId) {
		return cityMapper.deleteByPrimaryKey(cityId);
	}

	@Override
	public int addCity(RcCity city) {
		if(city.getIsDredged() == YesOrNoEnum.NO.getValue() && city.getIsDefault() == YesOrNoEnum.YES.getValue()) {
			return 0;
		}
		
		if(city.getIsDredged() == YesOrNoEnum.YES.getValue()) {
			city.setDredgeTime(new Date());
		}
		if(city.getIsDefault() == YesOrNoEnum.YES.getValue()) {
			RcCity defaultCity = findDefaultCity();
			if(defaultCity != null) {
				defaultCity.setIsDefault(YesOrNoEnum.NO.getValue());
				modifyCity(defaultCity);
			}
		}
		if(cityMapper.insert(city) > 0) {
			return city.getCityId();
		}
		return 0;
	}

	@Override
	public int saveCity(RcCity city) {
		if(city.getCityId() == null) {
			return addCity(city);
		} else {
			return modifyCity(city);
		}
	}

	@Override
	public List<RcCity> findCitiesByName(String name) {
		RcCity param = new RcCity();
		param.setName(name);
		return cityMapper.select(param);
	}
}
