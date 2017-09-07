package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcRechargeStation;

public interface RcRechargeStationMapper {

	int deleteByPrimaryKey(int rechargeStationId);

	int insert(RcRechargeStation record);

	RcRechargeStation selectByPrimaryKey(int rechargeStationId);

	int updateByPrimaryKey(RcRechargeStation record);

	List<RcRechargeStation> select(RcRechargeStation record);

	RcRechargeStation selectOne(RcRechargeStation record);

	int delete(RcRechargeStation record);
}