package com.socool.soft.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcBuyerCoupon;
import com.socool.soft.bo.RcCoupon;
import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcProdCat;
import com.socool.soft.bo.constant.BuyerCouponStatusEnum;
import com.socool.soft.dao.RcBuyerCouponMapper;
import com.socool.soft.service.IBuyerCouponService;
import com.socool.soft.service.ICouponService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IProdCatService;
import com.socool.soft.util.VOConversionUtil;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;
import com.socool.soft.vo.constant.CouponVOStatusEnum;
import com.socool.soft.vo.constant.CouponVOTypeEnum;

@Service
public class BuyerCouponServiceImpl implements IBuyerCouponService{
	@Autowired
	private RcBuyerCouponMapper buyerCouponMapper;
	@Autowired
	private IProdCatService prodCatService;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private ICouponService couponService;

	@Override
	public RcBuyerCoupon findBuyerCouponByBuyerIdAndCouponId(int buyerId, int couponId) {
		RcBuyerCoupon param = new RcBuyerCoupon();
		param.setBuyerId(buyerId);
		param.setCouponId(couponId);
  		return buyerCouponMapper.selectOne(param);
	}

	@Override
	public int addBuyerCoupon(RcBuyerCoupon buyerCoupon) {
		buyerCoupon.setCreateTime(new Date());
		if(buyerCouponMapper.insert(buyerCoupon) > 0) {
			return buyerCoupon.getBuyerCouponId();
		}
		return 0;
	}

	@Override
	public int modifyBuyerCoupon(RcBuyerCoupon buyerCoupon) {
		if(buyerCouponMapper.updateByPrimaryKey(buyerCoupon) > 0) {
			return buyerCoupon.getBuyerCouponId();
		}
		return 0;
	}

//	@Override
//	public List<RcBuyerCoupon> findCouponsByMemberId(int memberId) {
//		RcBuyerCoupon param = new RcBuyerCoupon();
//		param.setMemberId(memberId);
//		return couponMemberMapper.select(param);
//	}

//	@Override
//	public List<RcBuyerCoupon> findUsableCouponsByMemberId(int memberId) {
//		RcBuyerCoupon param = new RcBuyerCoupon();
//		param.setMemberId(memberId);
//		param.setExpireTimeFrom(new Date());
//		return couponMemberMapper.select(param);
//	}

	@Override
	public int modifyBuyerCouponStatus(int buyerId, int couponId, int status) {
		RcBuyerCoupon buyerCoupon = findBuyerCouponByBuyerIdAndCouponId(buyerId, couponId);
		buyerCoupon.setStatus(status);
		return modifyBuyerCoupon(buyerCoupon);
	}

	@Override
	public List<RcBuyerCoupon> findBuyerCouponsByBuyerIdAndStatus(int buyerId, 
			Integer status) {
		Date now = new Date();
		RcBuyerCoupon param = new RcBuyerCoupon();
		param.setBuyerId(buyerId);
		if(status != null) {
			if(status == CouponVOStatusEnum.UNUSED.getValue()) {
				param.setStatus(BuyerCouponStatusEnum.UNUSED.getValue());
				param.setExpireTimeFrom(now);
			} else if(status == CouponVOStatusEnum.USED.getValue()) {
				param.setStatus(BuyerCouponStatusEnum.USED.getValue());
			} else if(status == CouponVOStatusEnum.USE_OVERDUE.getValue()) {
				param.setStatus(BuyerCouponStatusEnum.UNUSED.getValue());
				param.setExpireTimeTo(now);
			}
		}
		List<RcBuyerCoupon> buyerCoupons = buyerCouponMapper.select(param);
		for(RcBuyerCoupon buyerCoupon : buyerCoupons) {
			RcCoupon coupon = couponService.findCouponByCouponId(buyerCoupon.getCouponId());
			buyerCoupon.setAmount(coupon.getAmount());
			buyerCoupon.setLeftCount(coupon.getLeftCount());
			buyerCoupon.setBaseAmount(coupon.getBaseAmount());
			buyerCoupon.setDescription(coupon.getDescription());
			buyerCoupon.setMerchantId(coupon.getMerchantId());
			buyerCoupon.setProdCatId(coupon.getProdCatId());
			if(coupon.getMerchantId() > 0) {
				buyerCoupon.setType(CouponVOTypeEnum.MERCHANT.getValue());
				RcMerchant merchant = merchantService.findMerchantById(coupon.getMerchantId());
				if(merchant != null) {
					buyerCoupon.setMerchantName(merchant.getName());
				}
			} else {
				buyerCoupon.setType(CouponVOTypeEnum.PLATFORM.getValue());
				if(coupon.getProdCatId() > 0) {
					RcProdCat prodCat = prodCatService.findProdCatById(coupon.getProdCatId());
					if(prodCat != null) {
						buyerCoupon.setProdCatName(prodCat.getName());
					}
				} else {
					buyerCoupon.setProdCatName("All Categories");
				}
			}
			if(buyerCoupon.getStatus() == BuyerCouponStatusEnum.USED.getValue()) {
				buyerCoupon.setStatus(CouponVOStatusEnum.USED.getValue());
			} else if(buyerCoupon.getExpireTime().after(now)) {
				buyerCoupon.setStatus(CouponVOStatusEnum.UNUSED.getValue());
			} else {
				buyerCoupon.setStatus(CouponVOStatusEnum.USE_OVERDUE.getValue());
			}
			VOConversionUtil.Entity2VO(buyerCoupon, null, new String[] {"couponMemberId", "createTime", "memberId"});
		}
		return buyerCoupons;
	}

	@Override
	public List<RcBuyerCoupon> findBuyerCoupons(int buyerId,
			List<Integer> couponIds) {
		RcBuyerCoupon param = new RcBuyerCoupon();
		param.setBuyerId(buyerId);
		param.setCouponIds(couponIds);
		return buyerCouponMapper.select(param);
	}

//	@Override
//	public RcCoupon findCouponByMerchantIdAndMemberId(int merchantId,
//			int memberId) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public List<RcBuyerCoupon> findPagedBuyerCouponsByBuyerIdAndStatus(
			int buyerId, Integer status, Page page) {
		PageContext.setPage(page);
		return findBuyerCouponsByBuyerIdAndStatus(buyerId, status);
	}
}
