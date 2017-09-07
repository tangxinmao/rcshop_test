package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcMember;
import com.socool.soft.bo.RcRechargeStation;
import com.socool.soft.bo.RcUser;
import com.socool.soft.bo.RcUserRole;
import com.socool.soft.bo.constant.RoleIdEnum;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.dao.RcUserMapper;
import com.socool.soft.service.IMemberService;
import com.socool.soft.service.IRechargeStationService;
import com.socool.soft.service.IRoleService;
import com.socool.soft.service.IUserRoleService;
import com.socool.soft.service.IUserService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private RcUserMapper userMapper;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IUserRoleService userRoleService;
	@Autowired
	private IRechargeStationService rechargeStationService;
	@Autowired
	private IMemberService memberService;

//	@Override
//	public List<RcUser> findUsers(RcUser param) {
//		if(param.getMemberId() != null) {
//			RcUser member = findUserById(param.getMemberId());
//			if(member == null) {
//				return new ArrayList<RcUser>();
//			}
//			return Arrays.asList(member);
//		}
//		return userMapper.select(param);
//	}
	
//	@Override
//	public List<RcUser> findPagedUsers(RcUser param, Page page) {
//		PageContext.setPage(page);
//		return findUsers(param);
//	}

	@Override
	public int addUser(RcUser user) {
		RcMember member = new RcMember();
		member.setAccount(user.getAccount());
		member.setEmail(user.getEmail());
		member.setMobile(user.getMobile());
		int userId = memberService.addMember(member);
		user.setMemberId(userId);
		user.setSignUpTime(new Date());
		if(userMapper.insert(user) > 0) {
			return user.getMemberId();
		}
		return 0;
	}

	@Override
	public RcUser findUserById(int userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	@Override
	public RcUser findUserByAccount(String account) {
		RcUser param = new RcUser();
		param.setAccount(account);
		return userMapper.selectOne(param);
	}

	@Override
	public RcUser findUserByEmail(String email) {
		RcUser param = new RcUser();
		param.setEmail(email);
		return userMapper.selectOne(param);
	}

	@Override
	public int modifyUser(RcUser user) {
		if(userMapper.updateByPrimaryKey(user) > 0) {
			if(StringUtils.isNotBlank(user.getAccount()) || StringUtils.isNotBlank(user.getEmail()) || StringUtils.isNotBlank(user.getMobile())) {
				RcMember member = new RcMember();
				member.setMemberId(user.getMemberId());
				member.setAccount(user.getAccount());
				member.setEmail(user.getEmail());
				member.setMobile(user.getMobile());
				memberService.modifyMember(member);
			}
			return user.getMemberId();
		}
		return 0;
	}

//	@Override
//	public RcUser findUser(RcUser user) {
//		if(user.getMemberId() != null) {
//			return findUserById(user.getMemberId());
//		} else {
//			return userMapper.selectOne(user);
//		}
//	}

	@Override
	public int removeUser(int userId) {
		RcUser param = new RcUser();
		param.setMemberId(userId);
		param.setDelFlag(YesOrNoEnum.YES.getValue());
		if(modifyUser(param) > 0) {
			userRoleService.removeUserRolesByUserId(userId);
			return 1;
		}
		return 0;
	}

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
//	public List<RcUser> findMembersWithRoles(RcUser param) {
//		List<RcUser> members = findMembers(param);
//		for(RcUser member : members) {
//			member.setRoles(roleService.findRolesByMemberId(member.getMemberId()));
//		}
//		return members;
//	}

	@Override
	public List<RcUser> findPagedUsersWithRoles(Integer userId, String account, Integer roleId, Page page) {
		List<RcUser> users = null;
		if(userId != null) {
			RcUser user = findUserById(userId);
			if(user == null || user.getIsSuper() == YesOrNoEnum.YES.getValue()) {
				return new ArrayList<RcUser>();
			}
			users = Arrays.asList(user);
		} else {
			RcUser param = new RcUser();
			if(StringUtils.isNotBlank(account)) {
				param.setAccount(account);
			}
			if(roleId != null) {
				List<RcUserRole> userRoles = userRoleService.findUserRolesByRoleId(roleId);
				if(CollectionUtils.isEmpty(userRoles)) {
					return new ArrayList<RcUser>();
				}
				List<Integer> userIds = new ArrayList<Integer>();
				for(RcUserRole userRole : userRoles) {
					userIds.add(userRole.getMemberId());
				}
				param.setMemberIds(userIds);
			}
			param.setIsSuper(YesOrNoEnum.NO.getValue());
			PageContext.setPage(page);
			users = userMapper.select(param);
		}
		for(RcUser user : users) {
			user.setRoles(roleService.findRolesByUserId(user.getMemberId()));
		}
		return users;
	}

	@Override
	public RcUser findUserByMobile(String mobile) {
		RcUser param = new RcUser();
		param.setMobile(mobile);
		return userMapper.selectOne(param);
	}

	@Override
	public List<RcUser> findUnboundRechargeOperators(Integer rechargeStationId) {
		List<RcUserRole> userRoles = userRoleService.findUserRolesByRoleId(RoleIdEnum.RECHARGE_OPERATOR.getValue());
		if(CollectionUtils.isEmpty(userRoles)) {
			return new ArrayList<RcUser>();
		}
		
		List<Integer> userIds = new ArrayList<Integer>();
		for(RcUserRole userRole : userRoles) {
			userIds.add(userRole.getMemberId());
		}
		RcUser param = new RcUser();
		param.setMemberIds(userIds);
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		List<RcUser> rechargeOperators = userMapper.select(param);
		List<RcRechargeStation> rechargeStations = rechargeStationService.findAllRechargeStations();
		List<RcUser> result = new ArrayList<RcUser>();
		for(RcUser rechargeOperator : rechargeOperators) {
			boolean isFound = false;
			for(RcRechargeStation rechargeStation : rechargeStations) {
				if(rechargeOperator.getMemberId().equals(rechargeStation.getMemberId()) && !rechargeStation.getRechargeStationId().equals(rechargeStationId)) {
					isFound = true;
					break;
				}
			}
			if(!isFound) {
				result.add(rechargeOperator);
			}
		}
		return result;
	}
}
