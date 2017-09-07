package com.socool.soft.service;

import com.socool.soft.bo.RcVendor;
import com.socool.soft.vo.Page;

import java.util.List;

public interface IVendorService {

	List<RcVendor> findAllVendors();

	RcVendor findVendorByVendorUserId(int memberId);

	//List<RcVendorVo> findVendorVOs(RcVendorVo rcVendorVo);

	int removeVendor(int vendorId);

	int addVendor(RcVendor vendor);

	RcVendor findVendorById(int vendorId);

	int modifyVendor(RcVendor vendor);

	/**
	 * 查询列表
	 * @param contactPerson
	 * @param name
	 * @param email
	 * @param mobile
	 * @return
	 */
	List<RcVendor> findPagedVendors(String name, String contactPerson, String email, String mobile, Page page);

    int recoverVendor(int vendorId);
}
