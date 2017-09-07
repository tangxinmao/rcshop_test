package com.socool.soft.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.socool.soft.bo.RcProdCat;
import com.socool.soft.bo.RcProdCatProp;
import com.socool.soft.bo.RcProdCatSkuProp;
import com.socool.soft.bo.RcProdProp;
import com.socool.soft.bo.RcProdPropEnum;
import com.socool.soft.bo.RcProdSkuProp;
import com.socool.soft.bo.RcProdSkuPropEnum;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.service.IProdCatService;
import com.socool.soft.service.IProdPropService;
import com.socool.soft.service.IProdSkuPropService;
import com.socool.soft.util.JsonUtil;
import com.socool.soft.vo.Page;

@RequestMapping(value="/prodprop")
@Controller
public class ProdPropController {

	@Autowired
	private IProdPropService prodPropService;
	@Autowired
	private IProdSkuPropService prodSkuPropService;
	
	@Autowired
	private IProdCatService prodCatService;
	
	@RequestMapping(value="/list")
	public String prodPropList(Model model,Integer currentPage){
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		
		List<RcProdProp> prodProps = prodPropService.findPagedProdProps(null, page);
		if(!CollectionUtils.isEmpty(prodProps)){
			int prodPropId = prodProps.get(0).getProdPropId();
			model.addAttribute("prodPropId", prodPropId);
			List<RcProdPropEnum> prodPropEnums = prodPropService.findProdPropEnumsByProdPropId(prodPropId);
			model.addAttribute("enumList", prodPropEnums);
		}
		model.addAttribute("list", prodProps);
		if(flag) {
			return "prodPropList";
		} else {
			return "prodPropList_inner";
		}
	}
	
	@RequestMapping(value="/skulist")
	public String skuprodPropList(Model model,Integer currentPage){
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		
		List<RcProdSkuProp> prodSkuProps = prodSkuPropService.findPagedProdSkuProps(null, page);
		if(!CollectionUtils.isEmpty(prodSkuProps)){
			int prodPropId = prodSkuProps.get(0).getProdPropId();
			model.addAttribute("prodPropId", prodPropId);
			List<RcProdSkuPropEnum> prodSkuPropEnums = prodSkuPropService.findProdSkuPropEnumsByProdSkuPropId(prodPropId);
			model.addAttribute("enumList", prodSkuPropEnums);
		}
		model.addAttribute("list", prodSkuProps);
		if(flag) {
			return "skuPropList";
		} else {
			return "skuPropList_inner";
		}
	}
	
	@RequestMapping(value="/searchEnum")
	@ResponseBody
	public String searchEnum(int pordPropId){
		List<RcProdPropEnum> prodPropEnums = prodPropService.findProdPropEnumsByProdPropId(pordPropId);
		return JsonUtil.toJson(prodPropEnums);
	}
	
	@RequestMapping(value="/searchSkuEnum")
	@ResponseBody
	public String searchSkuEnum(int pordPropId){
		List<RcProdSkuPropEnum> prodSkuPropEnums = prodSkuPropService.findProdSkuPropEnumsByProdSkuPropId(pordPropId);
		return JsonUtil.toJson(prodSkuPropEnums);
	}
	
	@RequestMapping(value="/saveProperty")
	@ResponseBody
	public String saveProperty(String name, byte isSku, byte hasImg, byte isPackService){
		if(isSku == YesOrNoEnum.YES.getValue()) {
			RcProdSkuProp prodSkuProp = new RcProdSkuProp();
			prodSkuProp.setName(name);
			prodSkuProp.setHasImg(hasImg);
			prodSkuProp.setIsPackService(isPackService);
			int result = prodSkuPropService.addProdSkuProp(prodSkuProp);
			if(result > 0){
				return "{\"code\":\"1\"}";
			}
		} else {
			RcProdProp prodProp = new RcProdProp();
			prodProp.setName(name);
			int result = prodPropService.addProdProp(prodProp);
			if(result > 0){
				return "{\"code\":\"1\"}";
			}
		}
		return "{\"code\":\"0\"}";
	}
	
	@RequestMapping(value="/modifyProperty")
	@ResponseBody
	public String modifyProperty(Integer prodPropId, String name, byte isSku, Byte hasImg, Byte isPackService){
		if(isSku == YesOrNoEnum.YES.getValue()) {
			RcProdSkuProp prodSkuProp = new RcProdSkuProp();
			prodSkuProp.setProdPropId(prodPropId);
			prodSkuProp.setName(name);
			prodSkuProp.setHasImg(hasImg);
			prodSkuProp.setIsPackService(isPackService);
			int result = prodSkuPropService.modifyProdSkuProp(prodSkuProp);
			if(result > 0){
				return "{\"code\":\"1\"}";
			}
		} else {
			RcProdProp prodProp = new RcProdProp();
			prodProp.setProdPropId(prodPropId);
			prodProp.setName(name);
			int result = prodPropService.modifyProdProp(prodProp);
			if(result > 0){
				return "{\"code\":\"1\"}";
			}
		}
		return "{\"code\":\"0\"}";
	}
	
	@RequestMapping(value="/savePropEnum")
	@ResponseBody
	public String savePropEnum(String prodPropEnum, Integer seq, int prodPropId){
		RcProdPropEnum propEnum = new RcProdPropEnum();
		propEnum.setProdPropEnum(prodPropEnum);
		propEnum.setSeq(seq);
		propEnum.setProdPropId(prodPropId);
		int result = prodPropService.addProdPropEnum(propEnum);
		if(result > 0){
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}
	
	@RequestMapping(value="/saveSkuPropEnum")
	@ResponseBody
	public String saveSkuPropEnum(String prodPropEnum, Integer seq, int prodPropId){
		RcProdSkuPropEnum prodSkuPropEnum = new RcProdSkuPropEnum();
		prodSkuPropEnum.setProdPropEnum(prodPropEnum);
		prodSkuPropEnum.setSeq(seq);
		prodSkuPropEnum.setProdPropId(prodPropId);
		int result = prodSkuPropService.addProdSkuPropEnum(prodSkuPropEnum);
		if(result > 0){
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}
	
	@RequestMapping(value="/delPropEnum")
	@ResponseBody
	public String delPropEnum(int prodPropEnumId){
		int result = prodPropService.removeProdPropEnum(prodPropEnumId);
		if(result > 0){
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}
	
	@RequestMapping(value="/delSkuPropEnum")
	@ResponseBody
	public String delSkuPropEnum(int prodPropEnumId){
		int result = prodSkuPropService.removeProdSkuPropEnum(prodPropEnumId);
		if(result > 0){
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}
	
	@RequestMapping(value="/modifyPropEnum")
	@ResponseBody
	public String modifyPropEnum(int prodPropEnumId, String prodPropEnum, Integer seq){
		RcProdPropEnum propEnum = new RcProdPropEnum();
		propEnum.setProdPropEnumId(prodPropEnumId);
		propEnum.setProdPropEnum(prodPropEnum);
		propEnum.setSeq(seq);
		int result = prodPropService.modifyProdPropEnum(propEnum);
		if(result > 0){
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}
	
	@RequestMapping(value="/modifySkuPropEnum")
	@ResponseBody
	public String modifySkuPropEnum(int prodPropEnumId, String prodPropEnum, Integer seq){
		RcProdSkuPropEnum prodSkuPropEnum = new RcProdSkuPropEnum();
		prodSkuPropEnum.setProdPropEnumId(prodPropEnumId);
		prodSkuPropEnum.setProdPropEnum(prodPropEnum);
		prodSkuPropEnum.setSeq(seq);
		int result = prodSkuPropService.modifyProdSkuPropEnum(prodSkuPropEnum);
		if(result > 0){
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}
	
	@RequestMapping(value="/delProp")
	@ResponseBody
	public String delProp(int prodPropId, byte isSku){
		int result = isSku == YesOrNoEnum.YES.getValue() ? prodSkuPropService.removeProdSkuPropWithEnums(prodPropId) : prodPropService.removeProdPropWithEnums(prodPropId);
		if(result > 0){
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\""+result+"\"}";
	}
	
	@RequestMapping(value="prodPropChoseBox")
	public String prodPropChoseBox(Model model, int prodCatId, Integer type){
		if(type == 1){
			List<List<RcProdProp>> prodProps = prodPropService.findProdPropsForBinding(prodCatId);
			RcProdCat prodCat = prodCatService.findProdCatById(prodCatId);
			if(prodCat.getParentId() > 0){
				RcProdCat parentProdCat = prodCatService.findProdCatById(prodCat.getParentId());
				model.addAttribute("categoryName", parentProdCat.getName() + " > " + prodCat.getName());
			} else{
				model.addAttribute("categoryName",prodCat.getName());
			}
			model.addAttribute("resultList",  prodProps.get(1));
			model.addAttribute("list", prodProps.get(0));
			model.addAttribute("prodCatId", prodCatId);
		} else{
			List<List<RcProdSkuProp>> prodSkuProps = prodSkuPropService.findProdSkuPropsForBinding(prodCatId);
			RcProdCat prodCat = prodCatService.findProdCatById(prodCatId);
			if(prodCat.getParentId() > 0){
				RcProdCat parentProdCat = prodCatService.findProdCatById(prodCat.getParentId());
				model.addAttribute("categoryName", parentProdCat.getName() + " > " + prodCat.getName());
			} else{
				model.addAttribute("categoryName",prodCat.getName());
			}
			model.addAttribute("resultList",  prodSkuProps.get(1));
			model.addAttribute("list", prodSkuProps.get(0));
			model.addAttribute("prodCatId", prodCatId);
		}
		return "prodPropChoseBox";
	}
	
	@RequestMapping(value="/catBindProp")
	@ResponseBody
	public String catBindProp(String prodPropIds, int prodCatId, Integer type){
		if(type==1){
			prodPropService.removeProdCatPropsByProdCatId(prodCatId);
			if(StringUtils.isBlank(prodPropIds)){
				return "{\"code\":\"1\"}";
			}
			String[] prodPropIdArray = prodPropIds.split(",,,");
			for(String prodPropId : prodPropIdArray){
				int propId = Integer.parseInt(prodPropId);
				RcProdCatProp prodCatProp = new RcProdCatProp();
				prodCatProp.setProdCatId(prodCatId);
				prodCatProp.setProdPropId(propId);
				prodPropService.addProdCatProp(prodCatProp);
			}
		} else{
			prodSkuPropService.removeProdCatSkuPropsByProdCatId(prodCatId);
			if(StringUtils.isBlank(prodPropIds)){
				return "{\"code\":\"1\"}";
			}
			String[] prodSkuPropIdArray = prodPropIds.split(",,,");
			for(String prodSkuPropId : prodSkuPropIdArray){
				int propId = Integer.parseInt(prodSkuPropId);
				RcProdCatSkuProp prodCatSkuProp = new RcProdCatSkuProp();
				prodCatSkuProp.setProdCatId(prodCatId);
				prodCatSkuProp.setProdPropId(propId);
				prodSkuPropService.addProdCatSkuProp(prodCatSkuProp);
			}
		}
		return "{\"code\":\"1\"}";
	}
}
