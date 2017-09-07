package com.socool.soft.service.impl;

import com.socool.soft.bo.RcMember;
import com.socool.soft.bo.RcMerchantUser;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.dao.RcMerchantUserMapper;
import com.socool.soft.service.IMemberService;
import com.socool.soft.service.IMerchantUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MerchantUserServiceImpl implements IMerchantUserService {
	@Autowired
	private RcMerchantUserMapper merchantUserMapper;
	@Autowired
	private IMemberService memberService;

//	@Override
//	public List<RcMerchantUser> findMerchantUsers(RcMerchantUser param) {
//		if(param.getMemberId() != null) {
//			RcMerchantUser member = findMerchantUserById(param.getMemberId());
//			if(member == null) {
//				return new ArrayList<RcMerchantUser>();
//			}
//			return Arrays.asList(member);
//		}
//		return merchantUserMapper.select(param);
//	}
//	
//	@Override
//	public List<RcMerchantUser> findPagedMerchantUsers(RcMerchantUser param, Page page) {
//		PageContext.setPage(page);
//		return findMerchantUsers(param);
//	}

	@Override
	public int addMerchantUser(RcMerchantUser merchantUser) {
		RcMember member = new RcMember();
		member.setAccount(merchantUser.getAccount());
		member.setEmail(merchantUser.getEmail());
		member.setMobile(merchantUser.getMobile());
		int merchantUserId = memberService.addMember(member);
		merchantUser.setMemberId(merchantUserId);
		merchantUser.setSignUpTime(new Date());
		if(merchantUserMapper.insert(merchantUser) > 0) {
			return merchantUser.getMemberId();
		}
		return 0;
	}

	@Override
	public RcMerchantUser findMerchantUserById(int merchantUserId) {
		return merchantUserMapper.selectByPrimaryKey(merchantUserId);
	}

	@Override
	public RcMerchantUser findMerchantUserByAccount(String account) {
		RcMerchantUser param = new RcMerchantUser();
		param.setAccount(account);
		return merchantUserMapper.selectOne(param);
	}

	@Override
	public RcMerchantUser findMerchantUserByEmail(String email) {
		RcMerchantUser param = new RcMerchantUser();
		param.setEmail(email);
		return merchantUserMapper.selectOne(param);
	}

	@Override
	public int modifyMerchantUser(RcMerchantUser merchantUser) {
		if(merchantUserMapper.updateByPrimaryKey(merchantUser) > 0) {
			if(StringUtils.isNotBlank(merchantUser.getAccount()) || StringUtils.isNotBlank(merchantUser.getEmail()) || StringUtils.isNotBlank(merchantUser.getMobile())) {
				RcMember member = new RcMember();
				member.setMemberId(merchantUser.getMemberId());
				member.setAccount(merchantUser.getAccount());
				member.setEmail(merchantUser.getEmail());
				member.setMobile(merchantUser.getMobile());
				RcMember memberDB = memberService.findMemberByMobile(merchantUser.getMobile());
				if(memberDB!=null&&!memberDB.getMemberId().equals(member.getMemberId())){
					return 2;
				}
				memberService.modifyMember(member);
			}
			return merchantUser.getMemberId();
		}
		return 0;
	}

//	@Override
//	public RcMerchantUser findMerchantUser(RcMerchantUser param) {
//		if(param.getMemberId() != null) {
//			return findMerchantUserById(param.getMemberId());
//		} else {
//			return merchantUserMapper.selectOne(param);
//		}
//	}

	@Override
	public int removeMerchantUser(int merchantUserId) {
		RcMerchantUser param = new RcMerchantUser();
		param.setMemberId(merchantUserId);
		param.setDelFlag(YesOrNoEnum.YES.getValue());
		return modifyMerchantUser(param);
	}

	@Override
	public RcMerchantUser findMerchantUserByMobile(String mobile) {
		RcMerchantUser param = new RcMerchantUser();
		param.setMobile(mobile);
		return merchantUserMapper.selectOne(param);
	}

	@Override
	public RcMerchantUser findMerchantSuperAdmin(int merchantId) {
		RcMerchantUser param = new RcMerchantUser();
		param.setMerchantId(merchantId);
		param.setIsSuper(YesOrNoEnum.YES.getValue());
		return merchantUserMapper.selectOne(param);
	}

    @Override
	public List<RcMerchantUser> findMerchantUsersByMerchantId(int merchantId) {
		RcMerchantUser param = new RcMerchantUser();
		param.setMerchantId(merchantId);
		return merchantUserMapper.select(param);
	}
}
