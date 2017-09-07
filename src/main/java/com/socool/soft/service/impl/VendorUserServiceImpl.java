package com.socool.soft.service.impl;

import com.socool.soft.bo.RcMember;
import com.socool.soft.bo.RcVendorUser;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.dao.RcVendorUserMapper;
import com.socool.soft.service.IMemberService;
import com.socool.soft.service.IVendorUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VendorUserServiceImpl implements IVendorUserService {
	@Autowired
	private RcVendorUserMapper vendorUserMapper;
	@Autowired
	private IMemberService memberService;

//	@Override
//	public List<RcVendorUser> findVendorUsers(RcVendorUser param) {
//		if(param.getMemberId() != null) {
//			RcVendorUser member = findVendorUserById(param.getMemberId());
//			if(member == null) {
//				return new ArrayList<RcVendorUser>();
//			}
//			return Arrays.asList(member);
//		}
//		return vendorUserMapper.select(param);
//	}
	
//	@Override
//	public List<RcVendorUser> findPagedVendorUsers(RcVendorUser param, Page page) {
//		PageContext.setPage(page);
//		return findVendorUsers(param);
//	}

	@Override
	public int addVendorUser(RcVendorUser vendorUser) {
		RcMember member = new RcMember();
		member.setAccount(vendorUser.getAccount());
		member.setEmail(vendorUser.getEmail());
		member.setMobile(vendorUser.getMobile());
		int vendorUserId = memberService.addMember(member);
		vendorUser.setMemberId(vendorUserId);
		vendorUser.setSignUpTime(new Date());
		if(vendorUserMapper.insert(vendorUser) > 0) {
			return vendorUser.getMemberId();
		}
		return 0;
	}

	@Override
	public RcVendorUser findVendorUserById(int vendorUserId) {
		return vendorUserMapper.selectByPrimaryKey(vendorUserId);
	}

	@Override
	public RcVendorUser findVendorUserByAccount(String account) {
		RcVendorUser param = new RcVendorUser();
		param.setAccount(account);
		return vendorUserMapper.selectOne(param);
	}

	@Override
	public RcVendorUser findVendorUserByEmail(String email) {
		RcVendorUser param = new RcVendorUser();
		param.setEmail(email);
		return vendorUserMapper.selectOne(param);
	}

	@Override
	public int modifyVendorUser(RcVendorUser vendorUser) {
		if(vendorUserMapper.updateByPrimaryKey(vendorUser) > 0) {
			if(StringUtils.isNotBlank(vendorUser.getAccount()) || StringUtils.isNotBlank(vendorUser.getEmail()) || StringUtils.isNotBlank(vendorUser.getMobile())) {
				RcMember member = new RcMember();
				member.setMemberId(vendorUser.getMemberId());
				member.setAccount(vendorUser.getAccount());
				member.setEmail(vendorUser.getEmail());
				member.setMobile(vendorUser.getMobile());
				memberService.modifyMember(member);
			}
			return vendorUser.getMemberId();
		}
		return 0;
	}

//	@Override
//	public RcVendorUser findVendorUser(RcVendorUser param) {
//		if(param.getMemberId() != null) {
//			return findVendorUserById(param.getMemberId());
//		} else {
//			return vendorUserMapper.selectOne(param);
//		}
//	}

	@Override
	public int removeVendorUser(int vendorUserId) {
		RcVendorUser param = new RcVendorUser();
		param.setMemberId(vendorUserId);
		param.setDelFlag(YesOrNoEnum.YES.getValue());
		return modifyVendorUser(param);
	}
//
//	@Override
//	public boolean isMerchantOrVendor(int memberId) {
//		RcMerchant merchant = merchantService.findMerchantByMerchantUserId(memberId);
//		if(merchant != null) {
//			return false;
//		}
//		RcVendor vendor = vendorService.findVendorByMemberId(memberId);
//		if(vendor != null) {
//			return false;
//		}
//		return true;
//	}

//	@Override
//	public List<RcVendorUser> findMembersWithRoles(RcVendorUser param) {
//		List<RcVendorUser> members = findMembers(param);
//		for(RcVendorUser member : members) {
//			member.setRoles(roleService.findRolesByMemberId(member.getMemberId()));
//		}
//		return members;
//	}

	@Override
	public RcVendorUser findVendorUserByMobile(String mobile) {
		RcVendorUser param = new RcVendorUser();
		param.setMobile(mobile);
		return vendorUserMapper.selectOne(param);
	}

	@Override
	public RcVendorUser findVendorSuperAdmin(int vendorId) {
		RcVendorUser param = new RcVendorUser();
		param.setVendorId(vendorId);
		param.setIsSuper(YesOrNoEnum.YES.getValue());
		return vendorUserMapper.selectOne(param);
	}

	@Override
	public List<RcVendorUser> findVendorUsersByVendorId(int vendorId) {
		RcVendorUser param = new RcVendorUser();
		param.setVendorId(vendorId);
		return vendorUserMapper.select(param);
	}

	@Override
	public int recoverVendorUser(Integer memberId) {
		RcVendorUser param = new RcVendorUser();
		param.setMemberId(memberId);
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		return modifyVendorUser(param);
	}
}
