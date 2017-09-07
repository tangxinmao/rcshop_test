package com.socool.soft.service.impl;

import com.socool.soft.bo.RcVendor;
import com.socool.soft.bo.RcVendorUser;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.dao.RcVendorMapper;
import com.socool.soft.service.IVendorService;
import com.socool.soft.service.IVendorUserService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VendorServiceImpl implements IVendorService {
	@Autowired
	private RcVendorMapper vendorMapper;
	@Autowired
	private IVendorUserService vendorUserService;

	@Override
	public List<RcVendor> findAllVendors() {
		RcVendor param = new RcVendor();
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		return vendorMapper.select(param);
	}

	@Override
	public RcVendor findVendorByVendorUserId(int vendorUserId) {
		RcVendorUser vendorUser = vendorUserService.findVendorUserById(vendorUserId);
		if(vendorUser != null) {
			return findVendorById(vendorUser.getVendorId());
		}
		return null;
	}

	@Override
	public int removeVendor(int vendorId) {
		RcVendor vendor = new RcVendor();
		vendor.setVendorId(vendorId);
		vendor.setDelFlag(YesOrNoEnum.YES.getValue());
		if(modifyVendor(vendor) > 0) {
			List<RcVendorUser> vendorUsers = vendorUserService.findVendorUsersByVendorId(vendor.getVendorId());
			for(RcVendorUser vendorUser : vendorUsers) {
				vendorUserService.removeVendorUser(vendorUser.getMemberId());
			}
			return 1;
		}
		return 0;
	}

	@Override
	public int addVendor(RcVendor vendor) {
		vendor.setCreateTime(new Date());
		if(vendorMapper.insert(vendor) > 0) {
			return vendor.getVendorId();
		}
		return 0;
	}

	@Override
	public RcVendor findVendorById(int vendorId) {
		return vendorMapper.selectByPrimaryKey(vendorId);
	}

	@Override
	public int modifyVendor(RcVendor vendor) {
		if(vendorMapper.updateByPrimaryKey(vendor) > 0) {
			return vendor.getVendorId();
		}
		return 0;
	}

	@Override
	public List<RcVendor> findPagedVendors(String name, String contactPerson, String email, String mobile, Page page) {
		PageContext.setPage(page);
		RcVendor param = new RcVendor();
		param.setName(name);
		param.setContactPerson(contactPerson);
		param.setEmail(email);
		param.setMobile(mobile);
		List<RcVendor> vendors = vendorMapper.select(param);
		for (RcVendor vendor : vendors) {
			vendor.setMember(vendorUserService.findVendorSuperAdmin(vendor.getVendorId()));
		}
		return vendors;
	}

	@Override
	public int recoverVendor(int vendorId) {
		RcVendor vendor = new RcVendor();
		vendor.setVendorId(vendorId);
		vendor.setDelFlag(YesOrNoEnum.NO.getValue());
		if(modifyVendor(vendor) > 0) {
			/*List<RcVendorUser> vendorUsers = vendorUserService.findVendorUsersByVendorId(vendor.getVendorId());
			for(RcVendorUser vendorUser : vendorUsers) {
				vendorUserService.recoverVendorUser(vendorUser.getMemberId());
			}
			return 1;*/
			RcVendorUser rcVendorUser=vendorUserService.findVendorSuperAdmin(vendorId);
			rcVendorUser.setDelFlag(YesOrNoEnum.NO.getValue());
			vendorUserService.modifyVendorUser(rcVendorUser);
			return 1;
		}
		return 0;
	}
}
