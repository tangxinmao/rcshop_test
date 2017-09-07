package com.socool.soft.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcBuyerConsignee;
import com.socool.soft.bo.RcCity;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.dao.RcBuyerConsigneeMapper;
import com.socool.soft.service.IBuyerConsigneeService;
import com.socool.soft.service.ICityService;
import com.socool.soft.util.VOConversionUtil;

@Service
public class BuyerConsigneeServiceImpl implements IBuyerConsigneeService {
	@Autowired
	private RcBuyerConsigneeMapper consigneeMapper;
	@Autowired
	private ICityService cityService;

	@Override
	public RcBuyerConsignee findBuyerConsigneeById(int buyerConsigneeId) {
		return consigneeMapper.selectByPrimaryKey(buyerConsigneeId);
	}

	@Override
	public List<RcBuyerConsignee> findBuyerConsigneesByBuyerId(int buyerId) {
		RcBuyerConsignee param = new RcBuyerConsignee();
		param.setMemberId(buyerId);
		return consigneeMapper.select(param);
	}

	@Override
	public List<RcBuyerConsignee> findBuyerConsigneesByBuyerIdForAndroid(int buyerId) {
		RcBuyerConsignee param = new RcBuyerConsignee();
		param.setMemberId(buyerId);
		param.setOrderBy("BUYER_CONSIGNEE_ID DESC");
		List<RcBuyerConsignee> buyerConsignees = consigneeMapper.select(param);
		for(RcBuyerConsignee buyerConsignee : buyerConsignees) {
			RcCity city = cityService.findCityById(buyerConsignee.getCityId());
			buyerConsignee.setCityName(city.getName());
			buyerConsignee.setProvinceName(city.getProvinceName());
			VOConversionUtil.Entity2VO(buyerConsignee, null, new String[] {"createTime", "updateTime", "memberId"});
		}
		return buyerConsignees;
	}

	@Override
	public int addBuyerConsignee(RcBuyerConsignee buyerConsignee) {
		buyerConsignee.setCreateTime(new Date());
		if(consigneeMapper.insert(buyerConsignee) > 0) {
			return buyerConsignee.getConsigneeId();
		}
		return 0;
	}

	@Override
	public int modifyBuyerConsignee(RcBuyerConsignee buyerConsignee) {
		if(consigneeMapper.updateByPrimaryKey(buyerConsignee) > 0) {
			return buyerConsignee.getConsigneeId();
		}
		return 0;
	}

	@Override
	public int removeBuyerConsignee(int buyerConsigneeId) {
		return consigneeMapper.deleteByPrimaryKey(buyerConsigneeId);
	}

	@Override
	public int setDefault(int buyerConsigneeId) {
		List<RcBuyerConsignee> buyerConsignees = findBuyerConsigneesByBuyerId(findBuyerConsigneeById(buyerConsigneeId).getMemberId());
		for(RcBuyerConsignee buyerConsignee : buyerConsignees) {
			if(buyerConsignee.getConsigneeId() == buyerConsigneeId) {
				if(buyerConsignee.getIsDefault() == YesOrNoEnum.NO.getValue()) {
					buyerConsignee.setIsDefault(YesOrNoEnum.YES.getValue());
					modifyBuyerConsignee(buyerConsignee);
				}
			} else {
				if(buyerConsignee.getIsDefault() == YesOrNoEnum.YES.getValue()) {
					buyerConsignee.setIsDefault(YesOrNoEnum.NO.getValue());
					modifyBuyerConsignee(buyerConsignee);
				}
			}
		}
		return 1;
	}

	@Override
	public int saveBuyerConsignee(RcBuyerConsignee buyerConsignee) {
		if (buyerConsignee.getConsigneeId() == null) {
			List<RcBuyerConsignee> buyerConsignees = findBuyerConsigneesByBuyerId(buyerConsignee.getMemberId());
			if (CollectionUtils.isEmpty(buyerConsignees)) {
				buyerConsignee.setIsDefault(YesOrNoEnum.YES.getValue());
			} else {
				buyerConsignee.setIsDefault(YesOrNoEnum.NO.getValue());
			}
			return addBuyerConsignee(buyerConsignee);
		} else {
			return modifyBuyerConsignee(buyerConsignee);
		}
	}
}
