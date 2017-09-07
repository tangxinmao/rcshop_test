package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcBuyerCoupon;
import com.socool.soft.bo.RcCoupon;
import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcProd;
import com.socool.soft.bo.RcProdCat;
import com.socool.soft.bo.constant.BuyerCouponStatusEnum;
import com.socool.soft.bo.constant.CouponStatusEnum;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.dao.RcCouponMapper;
import com.socool.soft.service.IBuyerCouponService;
import com.socool.soft.service.ICouponService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IProdCatService;
import com.socool.soft.service.IProdService;
import com.socool.soft.util.VOConversionUtil;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;
import com.socool.soft.vo.constant.CouponBVOStatusEnum;
import com.socool.soft.vo.constant.CouponVOStatusEnum;
import com.socool.soft.vo.constant.CouponVOTypeEnum;

@Service
public class CouponServiceImpl implements ICouponService {
	@Autowired
	private RcCouponMapper couponMapper;
	@Autowired
	private IProdService prodService;
	@Autowired
	private IProdCatService prodCatService;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private IBuyerCouponService buyerCouponService;

	@Override
	public List<RcCoupon> findAllPagedCouponsByBuyerId(int buyerId, Page page) {
		PageContext.setPage(page);
		Date now = new Date();
		RcCoupon param = new RcCoupon();
		param.setStatus(CouponStatusEnum.PUBLISHED.getValue());
		param.setReceiveEndTimeFrom(now);
		param.setOrderBy("RECEIVE_END_TIME ASC, EXPIRE_TIME ASC, LEFT_COUNT ASC");
		List<RcCoupon> coupons = couponMapper.select(param);
		List<Integer> couponIds = new ArrayList<Integer>();
		for (RcCoupon coupon : coupons) {
			couponIds.add(coupon.getCouponId());
		}
		List<RcBuyerCoupon> buyerCoupons = buyerCouponService.findBuyerCoupons(buyerId, couponIds);
		for (RcCoupon coupon : coupons) {
			if (coupon.getLeftCount() == 0) {
				coupon.setCouponMemberStatus(CouponVOStatusEnum.EMPTY.getValue());
			} else {
				coupon.setCouponMemberStatus(CouponVOStatusEnum.UNGOT.getValue());
			}

			for (RcBuyerCoupon buyerCoupon : buyerCoupons) {
				if (coupon.getCouponId().equals(buyerCoupon.getCouponId())) {
					coupon.setCouponMemberStatus(CouponVOStatusEnum.GOT.getValue());
					break;
				}
			}
			if (coupon.getMerchantId() > 0) {
				coupon.setType(CouponVOTypeEnum.MERCHANT.getValue());
				RcMerchant merchant = merchantService.findMerchantById(coupon.getMerchantId());
				if (merchant != null) {
					coupon.setMerchantName(merchant.getName());
				}
			} else {
				coupon.setType(CouponVOTypeEnum.PLATFORM.getValue());
				if (coupon.getProdCatId() > 0) {
					RcProdCat prodCat = prodCatService.findProdCatById(coupon.getProdCatId());
					if (prodCat != null) {
						coupon.setProdCatName(prodCat.getName());
					}
				} else {
					coupon.setProdCatName("All Categories");
				}
			}
			VOConversionUtil.Entity2VO(coupon, null, new String[] { "createTime", "initCount", "delFlag", "name",
					"status", "receiveStartTime", "receiveEndTime" });
		}
		return coupons;
	}

	@Override
	public Map<Integer, List<RcCoupon>> findAllCouponsForShoppingCartByBuyerId(int buyerId) {
		Map<Integer, List<RcCoupon>> result = new HashMap<Integer, List<RcCoupon>>();
		Date now = new Date();
		RcCoupon param = new RcCoupon();
		param.setStatus(CouponStatusEnum.PUBLISHED.getValue());
		param.setReceiveEndTimeFrom(now);
		param.setOrderBy("RECEIVE_END_TIME ASC, EXPIRE_TIME ASC, LEFT_COUNT ASC");
		List<RcCoupon> coupons = couponMapper.select(param);
		if (CollectionUtils.isEmpty(coupons)) {
			return result;
		}
		List<Integer> couponIds = new ArrayList<Integer>();
		for (RcCoupon coupon : coupons) {
			couponIds.add(coupon.getCouponId());
		}
		List<RcBuyerCoupon> buyerCoupons = buyerCouponService.findBuyerCoupons(buyerId, couponIds);
		for (RcCoupon coupon : coupons) {
			if (!coupon.getReceiveEndTime().after(now) || coupon.getLeftCount() == 0) {
				continue;
			}

			if (!result.containsKey(coupon.getMerchantId())) {
				result.put(coupon.getMerchantId(), new ArrayList<RcCoupon>());
			}
			coupon.setCouponMemberStatus(CouponVOStatusEnum.UNGOT.getValue());
			for (RcBuyerCoupon couponMember : buyerCoupons) {
				if (coupon.getCouponId().equals(couponMember.getCouponId())) {
					if (couponMember.getExpireTime().after(now)&& couponMember.getStatus() != BuyerCouponStatusEnum.USED.getValue()) {
						coupon.setCouponMemberStatus(CouponVOStatusEnum.GOT.getValue());
					} else{
						coupon.setCouponMemberStatus(CouponVOStatusEnum.USED.getValue());
					}
					break;
				}
			}
			if(coupon.getCouponMemberStatus() != CouponVOStatusEnum.USED.getValue()) {
				result.get(coupon.getMerchantId()).add(coupon);
				VOConversionUtil.Entity2VO(coupon, new String[] { "couponId", "prodCatId", "name", "baseAmount", "amount",
						"couponMemberStatus", "effectTime", "expireTime" }, null);
			}
		}
		return result;
	}

	@Override
	public RcCoupon findCouponByCouponId(int couponId) {
		return couponMapper.selectByPrimaryKey(couponId);
	}

	@Override
	public int modifyCoupon(RcCoupon coupon) {
		coupon.setReceiveStartTime(coupon.getEffectTime());
		coupon.setReceiveEndTime(coupon.getExpireTime());
		if (couponMapper.updateByPrimaryKey(coupon) > 0) {
			return coupon.getCouponId();
		}
		return 0;
	}

	@Override
	public int addCoupon(RcCoupon coupon) {
		coupon.setReceiveStartTime(coupon.getEffectTime());
		coupon.setReceiveEndTime(coupon.getExpireTime());
		coupon.setLeftCount(coupon.getInitCount());
		coupon.setCreateTime(new Date());
		if(coupon.getMerchantId() == null) {
			coupon.setMerchantId(0);
		}
		if(coupon.getProdCatId() == null) {
			coupon.setProdCatId(0);
		}
		if(couponMapper.insert(coupon) > 0) {
			return coupon.getCouponId();
		}
		return 0;
	}

	// @Override
	// public int removeCoupon(int couponId) {
	// return couponMapper.deleteByPrimaryKey(couponId);
	// }

	// @Override
	// public List<RcCoupon> findCouponsByProdIdsAndMemberId(int memberId,
	// List<Integer> prodIds) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	// @Override
	// public RcCoupon findCouponByMerchantIdAndMemberIdAndAmount(int
	// merchantId, int memberId, BigDecimal amount) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public List<RcCoupon> findPagedCoupons(RcCoupon param, Page page) {
		if(StringUtils.isNotBlank(param.getMerchantName())) {
			List<RcMerchant> merchants = merchantService.findMerchantByName(param.getMerchantName());
			if(CollectionUtils.isEmpty(merchants)) {
				return new ArrayList<RcCoupon>();
			}
			List<Integer> merchantIds = new ArrayList<Integer>();
			for(RcMerchant merchant : merchants) {
				merchantIds.add(merchant.getMerchantId());
			}
			param.setMerchantIds(merchantIds);
		}
		if(param.getCreateTimeTo() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(param.getCreateTimeTo());
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.SECOND, -1);
			param.setCreateTimeTo(cal.getTime());
		}
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		PageContext.setPage(page);
		List<RcCoupon> coupons = couponMapper.select(param);
		for(RcCoupon coupon : coupons) {
			if (coupon.getMerchantId() > 0) {
				coupon.setType(CouponVOTypeEnum.MERCHANT.getValue());
				RcMerchant merchant = merchantService.findMerchantById(coupon.getMerchantId());
				if (merchant != null) {
					coupon.setMerchantName(merchant.getName());
				}
			} else {
				coupon.setType(CouponVOTypeEnum.PLATFORM.getValue());
				if (coupon.getProdCatId() > 0) {
					RcProdCat prodCat = prodCatService.findProdCatById(coupon.getProdCatId());
					if (prodCat != null) {
						coupon.setProdCatName(prodCat.getName());
					}
				} else {
					coupon.setProdCatName("All Categories");
				}
			}
			if(coupon.getReceiveEndTime().before(new Date())) {
				coupon.setStatus(CouponBVOStatusEnum.RECEIVE_OVERDUE.getValue());
			}
		}
		return coupons;
	}

	@Override
	public List<RcCoupon> findCouponsForProd(int buyerId, int prodId) {
		List<RcCoupon> result = new ArrayList<RcCoupon>();
		Date now = new Date();
		RcCoupon param = new RcCoupon();
		param.setStatus(CouponStatusEnum.PUBLISHED.getValue());
		param.setReceiveEndTimeFrom(now);
		param.setOrderBy("RECEIVE_END_TIME ASC, EXPIRE_TIME ASC, LEFT_COUNT ASC");
		List<RcCoupon> coupons = couponMapper.select(param);
		if (CollectionUtils.isEmpty(coupons)) {
			return result;
		}
		List<Integer> couponIds = new ArrayList<Integer>();
		for (RcCoupon coupon : coupons) {
			couponIds.add(coupon.getCouponId());
		}
		List<RcBuyerCoupon> buyerCoupons = buyerCouponService.findBuyerCoupons(buyerId, couponIds);
		for (RcCoupon coupon : coupons) {
			if (!coupon.getReceiveEndTime().after(now) || coupon.getLeftCount() == 0) {
				continue;
			}
			
			boolean rightMerchant = false;
			boolean rightProdCat = false;
			RcProd prod = prodService.findProdById(prodId);
			if(coupon.getMerchantId() == 0 || coupon.getMerchantId().equals(prod.getMerchantId())) {
				rightMerchant = true;
			}
			if(coupon.getProdCatId() == 0) {
				rightProdCat = true;
			} else {
				if(coupon.getProdCatId().equals(prod.getProdCatId())) {
					rightProdCat = true;
				} else {
					RcProdCat prodCat = prodCatService.findProdCatById(prod.getProdCatId());
					while(prodCat.getParentId() > 0) {
						if(coupon.getProdCatId().equals(prodCat.getParentId())) {
							rightProdCat = true;
							break;
						}
						prodCat = prodCatService.findProdCatById(prodCat.getParentId());
					}
				}
			}
			if(!rightMerchant || !rightProdCat) {
				continue;
			}

			coupon.setCouponMemberStatus(CouponVOStatusEnum.UNGOT.getValue());
			for (RcBuyerCoupon buyerCoupon : buyerCoupons) {
				if (coupon.getCouponId().equals(buyerCoupon.getCouponId())) {
					if (buyerCoupon.getExpireTime().after(now)&& buyerCoupon.getStatus() != BuyerCouponStatusEnum.USED.getValue()) {
						coupon.setCouponMemberStatus(CouponVOStatusEnum.GOT.getValue());
					} else{
						coupon.setCouponMemberStatus(CouponVOStatusEnum.USED.getValue());
					}
					break;
				}
			}
			if(coupon.getCouponMemberStatus() != CouponVOStatusEnum.USED.getValue()) {
				VOConversionUtil.Entity2VO(coupon, new String[] { "couponId", "name", "baseAmount", "amount",
						"couponMemberStatus", "effectTime", "expireTime" }, null);
				result.add(coupon);
			}
		}
		return result;
	}

	@Override
	public int saveCoupon(RcCoupon coupon) {
		if (coupon.getCouponId() == null) {
			return addCoupon(coupon);
		} else {
			return modifyCoupon(coupon);
		}
	}
}
