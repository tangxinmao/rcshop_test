package com.socool.soft.dao;

import java.util.List;

import com.socool.soft.bo.RcRechargeRecord;

public interface RcRechargeRecordMapper {

	int deleteByPrimaryKey(int rechargeRecordId);

	int insert(RcRechargeRecord record);

	RcRechargeRecord selectByPrimaryKey(int rechargeRecordId);

	int updateByPrimaryKey(RcRechargeRecord record);

	List<RcRechargeRecord> select(RcRechargeRecord record);

	RcRechargeRecord selectOne(RcRechargeRecord record);

	int delete(RcRechargeRecord record);
}