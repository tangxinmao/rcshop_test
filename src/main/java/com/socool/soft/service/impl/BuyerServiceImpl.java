package com.socool.soft.service.impl;

import com.socool.soft.bo.RcBuyer;
import com.socool.soft.bo.RcMember;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.dao.RcBuyerMapper;
import com.socool.soft.exception.ErrorCode;
import com.socool.soft.exception.SystemException;
import com.socool.soft.service.IBuyerService;
import com.socool.soft.service.IMemberService;
import com.socool.soft.service.IUserGetuiService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class BuyerServiceImpl implements IBuyerService {
	@Autowired
	private RcBuyerMapper buyerMapper;
	@Autowired
	private IMemberService memberService;
	@Autowired
	private IUserGetuiService userGetuiService;

	@Override
	public List<RcBuyer> findBuyers(RcBuyer param) {
		return buyerMapper.select(param);
	}
	
	@Override
	public List<RcBuyer> findPagedBuyers(RcBuyer param, Page page) {
		PageContext.setPage(page);
		if(param.getMemberId() != null) {
			RcBuyer buyer = findBuyerById(param.getMemberId());
			if(buyer == null) {
				return new ArrayList<RcBuyer>();
			}
			return Arrays.asList(buyer);
		}
		if(StringUtils.isEmpty(param.getOrderBy()))
		param.setOrderBy("BUYER_ID DESC");
		return buyerMapper.select(param);
	}

	@Override
	public int addBuyer(RcBuyer buyer) {
		RcMember member = new RcMember();
		member.setAccount(buyer.getAccount());
		member.setEmail(buyer.getEmail());
		member.setMobile(buyer.getMobile());
		int buyerId = memberService.addMember(member);
		Date now = new Date();
		buyer.setMemberId(buyerId);
		buyer.setSignUpTime(now);
		buyer.setLastLoginTime(now);
		if(buyerMapper.insert(buyer) > 0) {
			return buyer.getMemberId();
		}
		return 0;
	}

	@Override
	public RcBuyer findBuyerById(int buyerId) {
		return buyerMapper.selectByPrimaryKey(buyerId);
	}

	@Override
	public RcBuyer findBuyerByAccount(String account) {
		RcBuyer param = new RcBuyer();
		param.setAccount(account);
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		return buyerMapper.selectOne(param);
	}



    @Override
	public RcBuyer findBuyerByEmail(String email) {
		RcBuyer param = new RcBuyer();
		param.setEmail(email);
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		return buyerMapper.selectOne(param);
	}

	@Override
	public RcBuyer findBuyerByMobile(String mobile) {
		RcBuyer param = new RcBuyer();
		param.setMobile(mobile);
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		return buyerMapper.selectOne(param);
	}

	@Override
	public int modifyBuyer(RcBuyer buyer) {
		if(buyerMapper.updateByPrimaryKey(buyer) > 0) {
			if(buyer.getAccount() != null || buyer.getEmail() != null || buyer.getMobile() != null) {
				RcMember member = new RcMember();
				member.setMemberId(buyer.getMemberId());
				member.setAccount(buyer.getAccount());
				member.setEmail(buyer.getEmail());
				member.setMobile(buyer.getMobile());
				memberService.modifyMember(member);
			}
			return buyer.getMemberId();
		}
		return 0;
	}

	@Override
	public int removeBuyer(int buyerId) {
		RcBuyer buyer = findBuyerById(buyerId);
		buyer.setDelFlag(YesOrNoEnum.YES.getValue());
		return modifyBuyer(buyer);
	}

    @Override
    public void geTuiBind(String mobile, String password, String cid) throws SystemException {
	    RcBuyer buyer = new RcBuyer();
	    buyer.setMobile(mobile);
	    buyer.setPassword(DigestUtils.md5Hex(password));
        RcBuyer dbBuyer = buyerMapper.selectOne(buyer);
        if(dbBuyer == null) {
            throw new SystemException(ErrorCode.SYSTEM_ERROR, "账号或密码错误");
        }

        // 绑定
        userGetuiService.userBind(dbBuyer.getMemberId(), cid);
    }

}