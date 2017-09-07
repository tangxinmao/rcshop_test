package com.socool.soft.service;

import java.util.List;

import com.socool.soft.bo.RcRechargeRecord;
import com.socool.soft.vo.Page;

public interface IRechargeRecordService {

	List<RcRechargeRecord> findPagedRechargeRecordsByMemberId(int memberId, Page page);

	List<RcRechargeRecord> findRechargeRecords(RcRechargeRecord rechargeRecord);
	
	List<RcRechargeRecord> findPagedRechargeRecords(RcRechargeRecord rechargeRecord, Page page);
	
	int addRechargeRecord(RcRechargeRecord rechargeRecord);
}
