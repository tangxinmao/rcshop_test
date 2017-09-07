package com.socool.soft.service.impl;

import com.socool.soft.bo.*;
import com.socool.soft.bo.constant.OrderSellerStatusEnum;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.dao.RcMerchantMapper;
import com.socool.soft.service.*;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class MerchantServiceImpl implements IMerchantService {
	
	@Autowired
	private IMerchantUserService merchantUserService;
	
	@Autowired
	private IProdService prodService;
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private RcMerchantMapper merchantMapper;
	@Autowired
	private IVendorService vendorService;
	@Autowired
	private ICityService cityService;

	@Override
	public List<RcMerchant> findAllEnabledMerchants() {
		RcMerchant param = new RcMerchant();
		param.setDelFlag(YesOrNoEnum.NO.getValue());
		return merchantMapper.select(param);
	}

	@Override
	public RcMerchant findMerchantById(int merchantId) {
		return merchantMapper.selectByPrimaryKey(merchantId);
	}

	@Override
	public int addMerchant(RcMerchant merchant) {
		merchant.setCreateTime(new Date());
		if(merchantMapper.insert(merchant) > 0) {
			return merchant.getMerchantId();
		}
		return 0;
	}

	@Override
	public List<RcMerchant> findAllMerchants() {
		RcMerchant param = new RcMerchant();
		return merchantMapper.select(param);
	}

	@Override
	public List<RcMerchant> findPagedMerchants(RcMerchant param, Page page) {
		if(StringUtils.isNotBlank(param.getCityName())) {
			List<RcCity> cities = cityService.findCitiesByName(param.getCityName());
			if(CollectionUtils.isEmpty(cities)) {
				return new ArrayList<RcMerchant>();
			}
			List<Integer> cityIds = new ArrayList<Integer>();
			for(RcCity e : cities) {
				cityIds.add(e.getCityId());
			}
			param.setCityIds(cityIds);
		}
		PageContext.setPage(page);
		List<RcMerchant> merchants = null;
		if(param.getMerchantId() != null) {
			RcMerchant merchant = findMerchantById(param.getMerchantId());
			if(merchant == null) {
				merchants = new ArrayList<RcMerchant>();
			} else {
				merchants = Arrays.asList(merchant);
			}
		} else {
			merchants = merchantMapper.select(param);
		}
		for(RcMerchant merchant : merchants) {
			merchant.setMember(merchantUserService.findMerchantSuperAdmin(merchant.getMerchantId()));
			merchant.setVendor(vendorService.findVendorById(merchant.getVendorId()));
			merchant.setCity(cityService.findCityById(merchant.getCityId()));
		}
		return merchants;
	}

	@Override
	public List<RcMerchant> findMerchants(RcMerchant param) {
		if(StringUtils.isNotBlank(param.getCityName())) {
			List<RcCity> cities = cityService.findCitiesByName(param.getCityName());
			if(CollectionUtils.isEmpty(cities)) {
				return new ArrayList<RcMerchant>();
			}
			List<Integer> cityIds = new ArrayList<Integer>();
			for(RcCity e : cities) {
				cityIds.add(e.getCityId());
			}
			param.setCityIds(cityIds);
		}
		List<RcMerchant> merchants = null;
		if(param.getMerchantId() != null) {
			RcMerchant merchant = findMerchantById(param.getMerchantId());
			if(merchant == null) {
				merchants = new ArrayList<RcMerchant>();
			} else {
				merchants = Arrays.asList(merchant);
			}
		} else {
			merchants = merchantMapper.select(param);
		}
		for(RcMerchant merchant : merchants) {
			merchant.setMember(merchantUserService.findMerchantSuperAdmin(merchant.getMerchantId()));
			merchant.setVendor(vendorService.findVendorById(merchant.getVendorId()));
			merchant.setCity(cityService.findCityById(merchant.getCityId()));
		}
		return merchants;
	}

	@Override
	public int modifyMerchant(RcMerchant merchant) {
		if(merchantMapper.updateByPrimaryKey(merchant) > 0) {
			return merchant.getMerchantId();
		}
		return 0;
	}

	@Override
	public int removeMerchant(int merchantId) {
		// todo: 存在上架商品、未处理订单时不能禁用
		RcMerchant merchant = findMerchantById(merchantId);
		merchant.setDelFlag(YesOrNoEnum.YES.getValue());
		if(modifyMerchant(merchant) > 0) {
			List<RcMerchantUser> merchantUsers = merchantUserService.findMerchantUsersByMerchantId(merchant.getMerchantId());
			for(RcMerchantUser merchantUser : merchantUsers) {
				merchantUserService.removeMerchantUser(merchantUser.getMemberId());
			}
			return 1;
		}
		return 0;
	}

//	@Override
//	public List<List<RcProdBrand>> findMerchantBrands(int merchantId) {
//		List<RcProdBrand> allProdBrands = prodBrandService.findAllBrands();
//		List<RcProdBrand> unselectedProdBrands = new ArrayList<RcProdBrand>();
//		List<RcProdBrand> selectedProdBrands = prodBrandService.findMerchantBrands(merchantId);
//		for(RcProdBrand prodBrand : allProdBrands) {
//			boolean isFound = false;
//			for(RcProdBrand selectedProdBrand : selectedProdBrands) {
//				if(prodBrand.getProdBrandId().equals(selectedProdBrand.getProdBrandId())) {
//					isFound = true;
//					break;
//				}
//			}
//			if(!isFound) {
//				unselectedProdBrands.add(prodBrand);
//			}
//		}
//		List<List<RcProdBrand>> result = new ArrayList<List<RcProdBrand>>();
//		result.add(unselectedProdBrands);
//		result.add(selectedProdBrands);
//		return result;
//	}

//	@Override
//	public int removeAddedInfo(int merchantId) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

//	@Override
//	public int addRcProdBrandRela(RcProdBrandRel rcProdBrandRel) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	@Override
	public List<RcMerchant> findMerchantsByVendorId(Integer vendorId) {
		RcMerchant param = new RcMerchant();
		param.setVendorId(vendorId);
		return merchantMapper.select(param);
	}

	@Override
	public RcMerchant findMerchantByMerchantUserId(int merchantUserId) {
		RcMerchantUser merchantUser = merchantUserService.findMerchantUserById(merchantUserId);
		if(merchantUser != null) {
			return findMerchantById(merchantUser.getMerchantId());
		}
		return null;
	}

//	@Override
//	public List<RcMerchant> searchMerchant(String searchKey) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public RcMerchant findMerchantByProdId(int prodId) {
		RcProd prod = prodService.findProdById(prodId);
		if(prod != null) {
			return findMerchantById(prod.getMerchantId());
		}
		return null;
	}

	@Override
	public List<RcMerchant> findMerchantByName(String name) {
		RcMerchant param = new RcMerchant();
		param.setName(name);
		return merchantMapper.select(param);
	}

	@Override
	public List<RcMerchant> findSortedMerchantByName(String name) {
		RcMerchant param = new RcMerchant();
		param.setName(name);
		param.setOrderBy("NAME ASC");
		return merchantMapper.select(param);
	}

//	@Override
//	public int removeMerchantBrands(int merchantId) {
//		return prodBrandService.removeMerchantBrands(merchantId);
//	}

	@Override
	public BigDecimal findYIncomeByMerchantId(int merchantId) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); 
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.SECOND,0);
		Date  createTimeTo= cal.getTime();
		cal.roll(Calendar.DATE, -1);
		Date createTimeFrom = cal.getTime();
		RcOrder param = new RcOrder();
		param.setMerchantId(merchantId);
		List<Integer> sellerStatuses = Arrays.asList(OrderSellerStatusEnum.RECEIVED.getValue(), 
				OrderSellerStatusEnum.PICKEDUP.getValue(), OrderSellerStatusEnum.UNREPLIED.getValue(), 
				OrderSellerStatusEnum.FINISHED.getValue());
		param.setSellerStatuses(sellerStatuses);
		param.setCreateTimeFrom(createTimeFrom);
		param.setCreateTimeTo(createTimeTo);
		List<RcOrder> orders = orderService.findOrders(param);
		BigDecimal totalOrderAmount = new BigDecimal(0);
		for (RcOrder order : orders) {
			 totalOrderAmount = totalOrderAmount.add(order.getPayPrice());
		}
		return totalOrderAmount;
	}

	@Override
	public Integer findTTransByMerchantId(int merchantId) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); 
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.SECOND,0);
		Date createTimeFrom = cal.getTime();
		cal.roll(Calendar.DATE, 1);
		Date createTimeTo = cal.getTime();
		RcOrder param = new RcOrder();
		param.setMerchantId(merchantId);
		List<Integer> sellerStatuses = Arrays.asList(OrderSellerStatusEnum.RECEIVED.getValue(), 
				OrderSellerStatusEnum.PICKEDUP.getValue(), OrderSellerStatusEnum.UNREPLIED.getValue(), 
				OrderSellerStatusEnum.FINISHED.getValue());
		param.setSellerStatuses(sellerStatuses);
		param.setCreateTimeFrom(createTimeFrom);
		param.setCreateTimeTo(createTimeTo);
		List<RcOrder> orders = orderService.findOrders(param);
		if(org.apache.commons.collections.CollectionUtils.isEmpty(orders)){
			return 0;
		}
		return orders.size();
	}

	@Override
	public BigDecimal findTIncomeByMerchantId(int merchantId) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); 
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.SECOND,0);
		Date  createTimeFrom = cal.getTime();
		cal.roll(Calendar.DATE, 1);
		Date createTimeTo = cal.getTime();
		RcOrder param = new RcOrder();
		param.setMerchantId(merchantId);
		List<Integer> sellerStatuses = Arrays.asList(OrderSellerStatusEnum.RECEIVED.getValue(), 
				OrderSellerStatusEnum.PICKEDUP.getValue(), OrderSellerStatusEnum.UNREPLIED.getValue(), 
				OrderSellerStatusEnum.FINISHED.getValue());
		param.setSellerStatuses(sellerStatuses);
		param.setCreateTimeFrom(createTimeFrom);
		param.setCreateTimeTo(createTimeTo);
		List<RcOrder> orders = orderService.findOrders(param);
		BigDecimal totalOrderAmount = new BigDecimal(0);
		for (RcOrder order : orders) {
			 totalOrderAmount = totalOrderAmount.add(order.getPayPrice());
		}
		return totalOrderAmount;
	}

	@Override
	public void coverMerchant(int merchantId) {
		RcMerchant merchant = findMerchantById(merchantId);
		merchant.setDelFlag(YesOrNoEnum.NO.getValue());
		modifyMerchant(merchant);
		RcMerchantUser rcMerchantUser=merchantUserService.findMerchantSuperAdmin(merchantId);
		rcMerchantUser.setDelFlag(YesOrNoEnum.NO.getValue());
		merchantUserService.modifyMerchantUser(rcMerchantUser);
	}
}
