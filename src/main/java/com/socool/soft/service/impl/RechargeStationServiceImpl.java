package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcCity;
import com.socool.soft.bo.RcRechargeStation;
import com.socool.soft.dao.RcRechargeStationMapper;
import com.socool.soft.service.ICityService;
import com.socool.soft.service.IRechargeStationService;
import com.socool.soft.service.IUserService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class RechargeStationServiceImpl implements IRechargeStationService {
	
	@Autowired
	private RcRechargeStationMapper rechargeStationMapper;
	@Autowired
	private ICityService cityService;
	@Autowired
	private IUserService userService;

	@Override
	public int removeRechargeStation(int rechargeStationId) {
		return rechargeStationMapper.deleteByPrimaryKey(rechargeStationId);
	}

	@Override
	public List<RcRechargeStation> findRechargeStations(String stationName,
			String cityName) {
		RcRechargeStation param = new RcRechargeStation();
		if(StringUtils.isNotBlank(cityName)) {
			List<RcCity> cities = cityService.findCitiesByName(cityName);
			if(CollectionUtils.isEmpty(cities)) {
				return new ArrayList<RcRechargeStation>();
			}
			List<Integer> cityIds = new ArrayList<Integer>();
			for(RcCity city : cities) {
				cityIds.add(city.getCityId());
			}
			param.setCityIds(cityIds);
		}
		param.setStationName(stationName);
		return rechargeStationMapper.select(param);
	}

	@Override
	public List<RcRechargeStation> findPagedRechargeStations(String stationName,
			String cityName, Page page) {
		RcRechargeStation param = new RcRechargeStation();
		if(StringUtils.isNotBlank(cityName)) {
			List<RcCity> cities = cityService.findCitiesByName(cityName);
			if(CollectionUtils.isEmpty(cities)) {
				return new ArrayList<RcRechargeStation>();
			}
			List<Integer> cityIds = new ArrayList<Integer>();
			for(RcCity city : cities) {
				cityIds.add(city.getCityId());
			}
			param.setCityIds(cityIds);
		}
		param.setStationName(stationName);
		PageContext.setPage(page);
		List<RcRechargeStation> rechargeStations = rechargeStationMapper.select(param);
		for (RcRechargeStation rechargeStation : rechargeStations) {
			rechargeStation.setMember(userService.findUserById(rechargeStation.getMemberId()));
			rechargeStation.setCity(cityService.findCityById(rechargeStation.getCityId()));
		}
		return rechargeStations;
	}

	@Override
	public List<RcRechargeStation> findRechargeStationsByCityId(int cityId) {
		RcRechargeStation param = new RcRechargeStation();
		param.setCityId(cityId);
		return rechargeStationMapper.select(param);
	}

	@Override
	public int addRechargeStation(RcRechargeStation rechargeStation) {
		rechargeStation.setCreateTime(new Date());
		if(rechargeStationMapper.insert(rechargeStation) > 0) {
			return rechargeStation.getRechargeStationId();
		}
		return 0;
	}

	@Override
	public int modifyRechargeStation(RcRechargeStation rechargeStation) {
		if(rechargeStationMapper.updateByPrimaryKey(rechargeStation) > 0) {
			return rechargeStation.getRechargeStationId();
		}
		return 0;
	}

	@Override
	public RcRechargeStation findRechargeStationByMemberId(int memberId) {
		RcRechargeStation param = new RcRechargeStation();
		param.setMemberId(memberId);
		return rechargeStationMapper.selectOne(param);
	}

	@Override
	public List<RcRechargeStation> findAllRechargeStations() {
		return rechargeStationMapper.select(null);
	}
}
