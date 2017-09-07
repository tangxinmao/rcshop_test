package com.socool.soft.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcAppProdHot;
import com.socool.soft.bo.RcHotKey;
import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcProd;
import com.socool.soft.bo.RcProdBrand;
import com.socool.soft.bo.constant.ProdStatusEnum;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.service.IAppProdHotService;
import com.socool.soft.service.IHotKeyService;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IProdBrandService;
import com.socool.soft.service.IProdImgService;
import com.socool.soft.service.IProdService;
import com.socool.soft.service.ISearchService;
import com.socool.soft.util.VOConversionUtil;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class SearchServiceImpl implements ISearchService {

	@Autowired
	private IProdService prodService;
	
	@Autowired
	private IAppProdHotService prodHotService;
	
	@Autowired
	private IMerchantService merchantService;
	
	@Autowired
	private IProdBrandService prodBrandService;
	@Autowired
	private IHotKeyService hotKeyService;
	@Autowired
	private IProdImgService prodImgRelService;
	@Autowired
	private PropertyConstants propertyConstants;

	@Override
	public List<RcProd> findPagedProdHotsByCityId(int cityId, Page page) {
		PageContext.setPage(page);
		List<RcProd> prods = new ArrayList<RcProd>();
		List<RcAppProdHot> hots = prodHotService.findAppProdHotsByCityId(cityId);
		for(RcAppProdHot hot : hots) {
			RcProd prod = prodService.findProdById(hot.getProdId());
			prod.setProdName(prod.getName());
			RcProdBrand prodBrand = prodBrandService.findProdBrandById(prod.getProdBrandId());
			RcMerchant merchant = merchantService.findMerchantById(prod.getMerchantId());
			
			prod.setProdImgUrl(prodImgRelService.findFirstProdImgByProdSkuId(String.valueOf(prod.getProdId())).getImgUrl());
			prod.setProdBrandLogoUrl(prodBrand.getLogoUrl());
			prod.setProdBrandName(prodBrand.getName());
			prod.setMerchantName(merchant.getName());
			VOConversionUtil.Entity2VO(prod, new String[] {"prodId", "prodName", "prodImgUrl", "originPrice", 
					"price", "type", "score", "prodBrandId", "prodBrandName", "prodBrandLogoUrl", "merchantName"}, null);
			
			prods.add(prod);
		}
		return prods;
	}

	@Override
	public Collection<SolrInputDocument> initSolrIndexDocument() {
		String rc_prod = propertyConstants.solrUrl;
		SolrServer server = new HttpSolrServer(rc_prod);
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		List<RcProd> list = prodService.findProdsForSolr();
		List<String> ids = new ArrayList<String>();
		for(RcProd prod:list){
			SolrInputDocument sd = new SolrInputDocument();
			sd.addField("PROD_ID", prod.getProdId());
			sd.addField("PROD_CAT_ID", prod.getProdCatId());
			if(prod.getProdBrand()==null){
				sd.addField("PROD_BRAND_LOGO_URL", "");
				sd.addField("PROD_BRAND_NAME", "");
			}
			else{
				sd.addField("PROD_BRAND_LOGO_URL", prod.getProdBrand().getLogoUrl());
				sd.addField("PROD_BRAND_NAME", prod.getProdBrand().getName());
			}
			sd.addField("PROD_ORIGIN_PRICE", prod.getOriginPrice());
			sd.addField("PROD_NAME", prod.getName());
			sd.addField("PROD_PRICE", prod.getPrice());
			sd.addField("PROD_BRAND_ID", prod.getProdBrandId());
			sd.addField("MERCHANT_NAME", prod.getMerchant().getName());			
			sd.addField("CAT_NAME", prod.getProdCat().getName());
			sd.addField("IMG_URL", prod.getProdImgUrl());
			sd.addField("PROD_CREATE_TIME", prod.getCreateTime());
			sd.addField("SKU_CNT", prod.getSkuCount());
			sd.addField("PROD_STATUS", prod.getStatus());
			sd.addField("PROD_PUTAWAY_TIME", prod.getPutawayTime());
			sd.addField("MERCHANT_ID", prod.getMerchantId());
			sd.addField("CITY", prod.getCity().getName());
			sd.addField("SCORE", prod.getScore());
			if(prod.getSoldNum()==null){
				sd.addField("SOLD", 0);
			}
			else{
				sd.addField("SOLD", prod.getSoldNum());
			}
			
			sd.addField("PRODUCT_TYPE", prod.getType());
			if(prodHotService.findAppProdHotByProdId(prod.getProdId()) != null) {
				sd.addField("IS_HOT", 1);
			} else {
				sd.addField("IS_HOT", 0);
			}
			ids.add(String.valueOf(prod.getProdId()));
			docs.add(sd);
		}
		try {
	        //add docs
			server.deleteById(ids);
			server.commit();
	        server.add(docs);
	        //commit后才保存到索引库
	        server.commit();
	        System.out.println("123");
	    } catch (SolrServerException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return docs;
	}

	@Override
	public void updateIndexForProdPutUpDown(int prodId) {
		SolrInputDocument sd = new SolrInputDocument();
		RcProd prod = prodService.findProdForSolr(prodId);
		if(prod.getStatus()==ProdStatusEnum.PUTAWAY.getValue()){
			sd.addField("PROD_ID", prodId);
			sd.addField("PROD_CAT_ID", prod.getProdCatId());
			if(prod.getProdBrand()==null){
				sd.addField("PROD_BRAND_LOGO_URL", "");
				sd.addField("PROD_BRAND_NAME", "");
			}
			else{
				sd.addField("PROD_BRAND_LOGO_URL", prod.getProdBrand().getLogoUrl());
				sd.addField("PROD_BRAND_NAME", prod.getProdBrand().getName());
			}
			sd.addField("PROD_ORIGIN_PRICE", prod.getOriginPrice());
			sd.addField("PROD_NAME", prod.getName());
			sd.addField("PROD_PRICE", prod.getPrice());
			sd.addField("PROD_BRAND_ID", prod.getProdBrandId());
			sd.addField("MERCHANT_NAME", prod.getMerchant().getName());			
			sd.addField("CAT_NAME", prod.getProdCat().getName());
			sd.addField("IMG_URL", prod.getProdImgUrl());
			sd.addField("PROD_CREATE_TIME", prod.getCreateTime());
			sd.addField("SKU_CNT", prod.getSkuCount());
			sd.addField("PROD_STATUS", prod.getStatus());
			sd.addField("PROD_PUTAWAY_TIME", prod.getPutawayTime());
			sd.addField("MERCHANT_ID", prod.getMerchantId());
			sd.addField("CITY", prod.getCity().getName());
			sd.addField("SCORE", prod.getScore());
			if(prod.getSoldNum()==null){
				sd.addField("SOLD", 0);
			}
			else{
				sd.addField("SOLD", prod.getSoldNum());
			}
			
			sd.addField("PRODUCT_TYPE", prod.getType());
			if(prodHotService.findAppProdHotByProdId(prodId) != null) {
				sd.addField("IS_HOT", 1);
			} else {
				sd.addField("IS_HOT", 0);
			}
		}
//		else{
//			sd.addField("PROD_ID", prodId);
//			sd.addField("PROD_STATUS", status);
//			sd.addField("PROD_CAT_ID", "0");
//			sd.addField("PROD_BRAND_LOGO_URL", "0");
//			sd.addField("PROD_ORIGIN_PRICE", "0");
//			sd.addField("PROD_NAME", "0");
//			sd.addField("PROD_PRICE", "0");
//			sd.addField("PROD_BRAND_ID", "0");
//			sd.addField("MERCHANT_NAME", "0");			
//			sd.addField("CAT_NAME","0");
//			sd.addField("IMG_URL", "0");
//			sd.addField("PROD_CREATE_TIME", new Date());
//			sd.addField("SKU_CNT", "0");
//			sd.addField("PROD_PUTAWAY_TIME", new Date());
//			sd.addField("PROD_BRAND_NAME", "0");
//			sd.addField("MERCHANT_ID", "0");
//			sd.addField("CITY", "0");
//		}
		String rc_prod=propertyConstants.solrUrl;
		SolrServer server = new HttpSolrServer(rc_prod);
		
		try {
			server.deleteById(String.valueOf(prodId));
			server.commit();
			
			if(prod.getStatus()==ProdStatusEnum.PUTAWAY.getValue()){
				server.add(sd);
				server.commit();
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public void updateIndexForOrderSuccess(int prodId) {
//		SolrInputDocument sd = new SolrInputDocument();
//		RcProd prod = prodService.findProdForSolr(prodId);
//		sd.addField("PROD_ID", prodId);
//		sd.addField("PROD_CAT_ID", prod.getProdCatId());
//		sd.addField("PROD_BRAND_LOGO_URL", prod.getProdBrand().getLogoUrl());
//		sd.addField("PROD_ORIGIN_PRICE", prod.getOriginPrice());
//		sd.addField("PROD_NAME", prod.getName());
//		sd.addField("PROD_PRICE", prod.getPrice());
//		sd.addField("PROD_BRAND_ID", prod.getProdBrandId());
//		sd.addField("MERCHANT_NAME", prod.getMerchant().getName());			
//		sd.addField("CAT_NAME", prod.getProdCat().getName());
//		sd.addField("IMG_URL", prod.getProdImgUrl());
//		sd.addField("PROD_CREATE_TIME", prod.getCreateTime());
//		sd.addField("SKU_CNT", prod.getSkuCount());
//		sd.addField("PROD_STATUS", prod.getStatus());
//		sd.addField("PROD_PUTAWAY_TIME", prod.getPutawayTime());
//		sd.addField("PROD_BRAND_NAME", prod.getProdBrand().getName());
//		sd.addField("MERCHANT_ID", prod.getMerchantId());
//		sd.addField("CITY", prod.getCity().getCityId());
//		sd.addField("SCORE", prod.getScore());
//		sd.addField("SOLD", prod.getSoldNum());
//		sd.addField("PRODUCT_TYPE", prod.getType());
//		String rc_prod=propertyConstants.solrUrl;
//		SolrServer server = new HttpSolrServer(rc_prod);
//		
//		try {
//			server.deleteById(String.valueOf(prodId));
//			server.commit();
//			
//			server.add(sd);
//			server.commit();
//		} catch (SolrServerException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	@Override
	public List<String> getHotKeys(int size) {
		List<String> result = new ArrayList<String>();
		List<RcHotKey> hotKeys = hotKeyService.findHotKeysForAndroid(size);
		for(RcHotKey hotKey : hotKeys) {
			result.add(hotKey.getHotKey());
		}
		return result;
	}
}
