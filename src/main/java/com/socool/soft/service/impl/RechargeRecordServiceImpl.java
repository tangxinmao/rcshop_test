package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcRechargeRecord;
import com.socool.soft.bo.RcRechargeStation;
import com.socool.soft.dao.RcRechargeRecordMapper;
import com.socool.soft.service.IBuyerService;
import com.socool.soft.service.IRechargeRecordService;
import com.socool.soft.service.IRechargeStationService;
import com.socool.soft.service.IUserService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class RechargeRecordServiceImpl implements IRechargeRecordService {
	@Autowired
	private RcRechargeRecordMapper rechargeRecordMapper;
	@Autowired
	private IRechargeStationService rechargeStationService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IBuyerService buyerService;

	@Override
	public List<RcRechargeRecord> findPagedRechargeRecordsByMemberId(int memberId, Page page) {
		RcRechargeRecord param = new RcRechargeRecord();
		param.setMemberId(memberId);
		return findRechargeRecords(param);
	}

	@Override
	public List<RcRechargeRecord> findRechargeRecords(RcRechargeRecord rechargeRecord) {
		return rechargeRecordMapper.select(rechargeRecord);
	}

	@Override
	public List<RcRechargeRecord> findPagedRechargeRecords(RcRechargeRecord param, Page page) {
		if(StringUtils.isNotBlank(param.getRechargeStationName())) {
			List<RcRechargeStation> rechargeStations = rechargeStationService.findRechargeStations(param.getRechargeStationName(), null);
			if(CollectionUtils.isEmpty(rechargeStations)) {
				return new ArrayList<RcRechargeRecord>();
			}
			List<Integer> rechargeStationIds = new ArrayList<Integer>();
			for(RcRechargeStation rechargeStation : rechargeStations) {
				rechargeStationIds.add(rechargeStation.getRechargeStationId());
			}
			param.setRechargeStationIds(rechargeStationIds);
		}
		PageContext.setPage(page);
		List<RcRechargeRecord> rechargeRecords = findRechargeRecords(param);
		for (RcRechargeRecord rechargeRecord : rechargeRecords) {
			rechargeRecord.setMember(buyerService.findBuyerById(rechargeRecord.getMemberId()));
			rechargeRecord.setRechargeMember(userService.findUserById(rechargeRecord.getRechargeMemberId()));
			rechargeRecord.setRechargeStation(rechargeStationService.findRechargeStationByMemberId(rechargeRecord.getRechargeMemberId()));
		}
		return rechargeRecords;
	}

	@Override
	public int addRechargeRecord(RcRechargeRecord rechargeRecord) {
		rechargeRecord.setCreateTime(new Date());
		if(rechargeRecordMapper.insert(rechargeRecord) > 0) {
			return rechargeRecord.getRechargeRecordId();
		}
		return 0;
	}
}
