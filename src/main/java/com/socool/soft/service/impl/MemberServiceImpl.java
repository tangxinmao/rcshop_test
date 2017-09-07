package com.socool.soft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcMember;
import com.socool.soft.dao.RcMemberMapper;
import com.socool.soft.service.IMemberService;

@Service
public class MemberServiceImpl implements IMemberService {
	@Autowired
	private RcMemberMapper memberMapper;

	@Override
	public int addMember(RcMember member) {
		if(memberMapper.insert(member) > 0) {
			return member.getMemberId();
		}
		return 0;
	}

	@Override
	public RcMember findMemberById(int memberId) {
		return memberMapper.selectByPrimaryKey(memberId);
	}

	@Override
	public RcMember findMemberByAccount(String account) {
		RcMember param = new RcMember();
		param.setAccount(account);
		return memberMapper.selectOne(param);
	}

	@Override
	public RcMember findMemberByEmail(String email) {
		RcMember param = new RcMember();
		param.setEmail(email);
		return memberMapper.selectOne(param);
	}

	@Override
	public int modifyMember(RcMember member) {
		if(memberMapper.updateByPrimaryKey(member) > 0) {
			return member.getMemberId();
		}
		return 0;
	}

	@Override
	public RcMember findMemberByMobile(String mobile) {
		RcMember param = new RcMember();
		param.setMobile(mobile);
		return memberMapper.selectOne(param);
	}
}
