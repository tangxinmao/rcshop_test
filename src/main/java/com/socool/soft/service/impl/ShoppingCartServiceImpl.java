package com.socool.soft.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.socool.soft.bo.RcCoupon;
import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcProd;
import com.socool.soft.bo.RcProdCat;
import com.socool.soft.bo.RcProdImg;
import com.socool.soft.bo.RcProdSku;
import com.socool.soft.bo.RcProdSkuPropInfo;
import com.socool.soft.bo.RcShoppingCartOrder;
import com.socool.soft.bo.RcShoppingCartProd;
import com.socool.soft.constant.Constants;
import com.socool.soft.exception.SystemException;
import com.socool.soft.service.ICouponService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IProdCatService;
import com.socool.soft.service.IProdImgService;
import com.socool.soft.service.IProdService;
import com.socool.soft.service.IProdSkuPropInfoService;
import com.socool.soft.service.IProdSkuService;
import com.socool.soft.service.IRedisService;
import com.socool.soft.service.IShoppingCartService;
import com.socool.soft.vo.newvo.android.ShoppingCartVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ShoppingCartServiceImpl implements IShoppingCartService {
	@Autowired
	private IProdService prodService;
	@Autowired
	private IProdSkuService prodSkuService;
	@Autowired
	private IProdSkuPropInfoService prodSkuPropInfoService;
	@Autowired
	private IProdImgService prodImgService;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private IRedisService redisService;
	@Autowired
	private IProdCatService prodCatService;
	@Autowired
	private ICouponService couponService;

	@Override
	public boolean addShoppingCartProd(int buyerId, int prodId, String prodSkuId, int quantity) throws SystemException {
		RcProd prod = prodService.findProdById(prodId);
		RcMerchant merchant = merchantService.findMerchantById(prod.getMerchantId());
		RcShoppingCartProd scProd = new RcShoppingCartProd();
		scProd.setProdId(prod.getProdId());
		scProd.setName(prod.getName());
		scProd.setQuantity(quantity);;
		if(StringUtils.isNotBlank(prodSkuId)) {
			RcProdSku prodSku = prodSkuService.findProdSkuByProdSkuId(prodSkuId);
			scProd.setPrice(prodSku.getPrice());
		} else{
			scProd.setPrice(prod.getPrice());
		}
		scProd.setProdSkuId(prodSkuId);
		List<RcProdSkuPropInfo> prodSkuPropInfos = prodSkuPropInfoService.findProdSkuPropInfosByProdSkuId(prodSkuId);
		List<String> prodSkuPropVals = new ArrayList<String>();
		for(RcProdSkuPropInfo prodSkuPropInfo : prodSkuPropInfos) {
			prodSkuPropVals.add(prodSkuPropInfo.getProdPropVal());
		}
		scProd.setProdSkuInfos(prodSkuPropVals);
		scProd.setType(prod.getType());
		
		RcProdImg prodImg = prodImgService.findFirstProdImgByProdSkuId(prodSkuId);
		if(prodImg == null) {
			prodImg = prodImgService.findFirstProdImgByProdSkuId(String.valueOf(prodId));
		}
		scProd.setProdImgUrl(prodImg.getImgUrl());
		RcShoppingCartOrder scOrder = new RcShoppingCartOrder();
		scOrder.setMerchantId(prod.getMerchantId());
		scOrder.setMerchantName(merchant.getName());
		List<RcShoppingCartProd> scProds = new ArrayList<RcShoppingCartProd>();
		scProds.add(scProd);
		scOrder.setProds(scProds);
		
		@SuppressWarnings("unchecked")
		List<RcShoppingCartOrder> cachedScOrders = (List<RcShoppingCartOrder>)redisService.hgetObject(Constants.REDIS_SHOPPING_CART, String.valueOf(buyerId));
		if(cachedScOrders != null){
			cachedScOrders = new ArrayList<RcShoppingCartOrder>(cachedScOrders);
			boolean isFound = false;
			for(RcShoppingCartOrder cachedScOrder : cachedScOrders){
				if(cachedScOrder.getMerchantId().equals(scOrder.getMerchantId())) {
					isFound = true;
					List<RcShoppingCartProd> cachedScProds = (List<RcShoppingCartProd>)cachedScOrder.getProds();
					boolean isFoundInMerchant = false;
					for(RcShoppingCartProd cachedScProd : cachedScProds) {
						if(!scProd.getProdSkuId().equals("")) {
							if(scProd.getProdSkuId().equals(cachedScProd.getProdSkuId())) {
								cachedScProd.setQuantity(cachedScProd.getQuantity() + scProd.getQuantity());
								cachedScProds.remove(cachedScProd);
								cachedScProds.add(0, cachedScProd);
								isFoundInMerchant = true;
								break;
							}
						} else {
							if(scProd.getProdId().equals(cachedScProd.getProdId())) {
								cachedScProd.setQuantity(cachedScProd.getQuantity() + scProd.getQuantity());
								cachedScProds.remove(cachedScProd);
								cachedScProds.add(0, cachedScProd);
								isFoundInMerchant = true;
								break;
							}
						}
					} 
					if(!isFoundInMerchant) {
						cachedScProds.add(0, scProd);
					}
					break;
				}
			}
			if(!isFound){
				cachedScOrders.add(0, scOrder);
			}
			redisService.hsetObject(Constants.REDIS_SHOPPING_CART, String.valueOf(buyerId), cachedScOrders);
		} else {
			redisService.hsetObject(Constants.REDIS_SHOPPING_CART, String.valueOf(buyerId), Arrays.asList(scOrder));
		}
		
		return true;
	}

	@Override
	public ShoppingCartVO findShoppingCart(int buyerId) throws SystemException {
		ShoppingCartVO shoppingCartVO = new ShoppingCartVO();
		@SuppressWarnings("unchecked")
		List<RcShoppingCartOrder> cachedScOrders = (List<RcShoppingCartOrder>)redisService.hgetObject(Constants.REDIS_SHOPPING_CART, String.valueOf(buyerId));
		if(cachedScOrders == null) {
			shoppingCartVO.setOrders(new ArrayList<RcShoppingCartOrder>());
			return shoppingCartVO;
		}
		
		int count = 0;
		Map<Integer, List<RcCoupon>> allCoupons = couponService.findAllCouponsForShoppingCartByBuyerId(buyerId);
		List<RcCoupon> platformCoupons = allCoupons.get(0);
//		List<RcShoppingCartOrder> cachedScOrders = iRedisManager.getValueList(String.valueOf(buyerId), RcShoppingCartOrder.class);
//		Map<String, Class> classMap = new HashMap<String, Class>(); 
//		classMap.put("prods", RcShoppingCartProd.class);
//		classMap.put("coupons", RcCoupon.class);
//		List<RcShoppingCartOrder> cachedScOrders = getJavaCollectionByClassMap(classMap, RcShoppingCartOrder.class, jsonStr);
		for(RcShoppingCartOrder cachedScOrder : cachedScOrders){
			List<RcShoppingCartProd> cachedScProds = cachedScOrder.getProds();
			List<Integer> prodCatParentList=new ArrayList<Integer>();
			if(!CollectionUtils.isEmpty(cachedScProds)) {
				Collection<Integer> prodIds=new ArrayList<Integer>();
				for(RcShoppingCartProd cachedScProd : cachedScProds) {
					prodIds.add(cachedScProd.getProdId());
					count++;
					RcProd prod = prodService.findProdById(cachedScProd.getProdId());
					if(StringUtils.isBlank(cachedScProd.getProdSkuId()) || cachedScProd.getProdSkuId().equals(String.valueOf(cachedScProd.getProdId()))) {
						cachedScProd.setStorage(prod.getInventory());
					} else {
						RcProdSku prodSku = prodSkuService.findProdSkuByProdSkuId(cachedScProd.getProdSkuId());
						cachedScProd.setStorage(prodSku.getInventory());
					}
					cachedScProd.setStatus(prod.getStatus());
				}
	
				List<Integer> prodCatIdList = prodCatService.findProdCatIdsByProdIds(prodIds);
				for (Integer integer : prodCatIdList) {
					RcProdCat prodCat = prodCatService.findProdCatById(integer);
					prodCatParentList.add(prodCat.getParentId());
				}
			}
			List<RcCoupon> merchantCoupons = allCoupons.get(cachedScOrder.getMerchantId());
			cachedScOrder.setCoupons(new ArrayList<RcCoupon>());
			if(merchantCoupons != null) {
				cachedScOrder.getCoupons().addAll(merchantCoupons);
			}
			if(platformCoupons != null) {
				for (RcCoupon rcCoupon : platformCoupons) {
					if(prodCatParentList.contains(rcCoupon.getProdCatId())||rcCoupon.getProdCatId().equals(0)){
						cachedScOrder.getCoupons().add(rcCoupon);
					}
				}
			}
		/*	if(merchantCoupons == null) {
				cachedScOrder.setCoupons(platformCoupons);
			} else {
				merchantCoupons.addAll(platformCoupons);
				cachedScOrder.setCoupons(merchantCoupons);
			}*/
		/*	if(CollectionUtils.isEmpty(cachedScOrder.getCoupons())){
				cachedScOrder.setCoupons(new ArrayList<RcCoupon>());
			}*/
		}
		shoppingCartVO.setCount(count);
		shoppingCartVO.setOrders(cachedScOrders);
		return shoppingCartVO;
	}

	@Override
	public int countShoppingCart(int buyerId) throws SystemException {
		@SuppressWarnings("unchecked")
		List<RcShoppingCartOrder> cachedScOrders = (List<RcShoppingCartOrder>)redisService.hgetObject(Constants.REDIS_SHOPPING_CART, String.valueOf(buyerId));
		int count = 0;
		if(cachedScOrders != null){ 
			count = cachedScOrders.size();
//		} else {
//			String jsonStr = iRedisManager.get(String.valueOf(buyerId));
//			if(jsonStr == null){
//				iRedisManager.save("count_" + buyerId, "{\"count\":"+0+"}");
//				return 0;
//			}
//			List<RcShoppingCartOrder> cachedScOrders = iRedisManager.getValueList(String.valueOf(buyerId), RcShoppingCartOrder.class);
//			for(RcShoppingCartOrder cachedScOrder : cachedScOrders){
//				List<RcShoppingCartProd> cachedScProds = cachedScOrder.getProds();
//				count += cachedScProds.size();
//			}
//			iRedisManager.save("count_" + buyerId, "{\"count\":"+count+"}");
		}
		return count;
	}

	@Override
	public boolean modifyShoppingCart(int buyerId, JSONArray cart) throws SystemException {
		if(cart.isEmpty()){
			redisService.hdelObject(Constants.REDIS_SHOPPING_CART, String.valueOf(buyerId));
			return true;
		}
		List<RcShoppingCartOrder> scOrders = new ArrayList<RcShoppingCartOrder>();
		for (Object cartObj : cart) {
			JSONObject cartJsonObj = (JSONObject) cartObj;
			RcShoppingCartOrder shoppingCartOrder = new RcShoppingCartOrder();
			shoppingCartOrder.setMerchantId(cartJsonObj.getInt("merchantId"));
			shoppingCartOrder.setMerchantName(cartJsonObj.getString("merchantName"));
			List<RcShoppingCartProd> prods = new ArrayList<RcShoppingCartProd>();
			JSONArray prodJsonArray = (JSONArray) cartJsonObj.get("prods");
			if(prodJsonArray != null && !prodJsonArray.isEmpty()) {
				for (Object prodObj : prodJsonArray) {
					JSONObject prodJsonObj = (JSONObject) prodObj;
					RcShoppingCartProd shoppingCartprod = new RcShoppingCartProd();
					shoppingCartprod.setName(prodJsonObj.getString("name"));
					shoppingCartprod.setPrice(new BigDecimal(prodJsonObj.getString("price")));
					shoppingCartprod.setProdId(prodJsonObj.getInt("prodId"));
					shoppingCartprod.setProdImgUrl(prodJsonObj.getString("prodImgUrl"));
					shoppingCartprod.setProdSkuId(prodJsonObj.getString("prodSkuId"));
					shoppingCartprod.setQuantity(prodJsonObj.getInt("quantity"));
					shoppingCartprod.setStatus(prodJsonObj.getInt("status"));
					shoppingCartprod.setStorage(prodJsonObj.getInt("storage"));
					shoppingCartprod.setType(prodJsonObj.getInt("type"));
					List<String> prodSkuInfos = new ArrayList<String>();
					JSONArray prodSkuInfoJsonArray = (JSONArray) prodJsonObj.get("prodSkuInfos");
					if(prodSkuInfoJsonArray != null && !prodSkuInfoJsonArray.isEmpty()) {
						for (Object prodSkuInfoObj : prodSkuInfoJsonArray) {
							String prodSkuInfoJsonObj = (String) prodSkuInfoObj;
							prodSkuInfos.add(prodSkuInfoJsonObj);
						}
						shoppingCartprod.setProdSkuInfos(prodSkuInfos);
					}
					prods.add(shoppingCartprod);
				}
				shoppingCartOrder.setProds(prods);
				scOrders.add(shoppingCartOrder);
			}
		}
//		int count = 0;
//		for(RcShoppingCartOrder scOrder : scOrders) {
//			List<RcShoppingCartProd> scProds = scOrder.getProds();
//			count += scProds.size();
//		}
//		iRedisManager.save("count_"+buyerId, "{\"count\":"+count+"}");
//		iRedisManager.saveList(String.valueOf(buyerId), scOrders);
		redisService.hsetObject(Constants.REDIS_SHOPPING_CART, String.valueOf(buyerId), scOrders);
		
		return true;
	}
}
