package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcRechargeStation;
import com.socool.soft.vo.Page;

public interface IRechargeStationService {
	/**
	 * 删除
	 * 
	 * @param rechargeStationId
	 * @return
	 */
	int removeRechargeStation(int rechargeStationId);

	/**
	 * 查询列表
	 * 
	 * @param stationName
	 * @param name
	 * @return
	 */
	List<RcRechargeStation> findRechargeStations(String stationName, String cityName);
	List<RcRechargeStation> findPagedRechargeStations(String stationName, String cityName, Page page);

	/**
	 * 查询当前city站
	 * 
	 * @param cityId
	 * @return
	 */
	List<RcRechargeStation> findRechargeStationsByCityId(int cityId);

	/**
	 * 保存充值站
	 * 
	 * @param rcRechargeStation
	 * @return
	 */
	int addRechargeStation(RcRechargeStation rechargeStation);

	/**
	 * 修改充值站
	 * @param rcRechargeStation
	 * @return
	 */
	int modifyRechargeStation(RcRechargeStation rechargeStation);
	/**
	 * 
	 * @param memberId
	 * @return
	 */

	RcRechargeStation findRechargeStationByMemberId(int memberId);
	
	List<RcRechargeStation> findAllRechargeStations();

}
