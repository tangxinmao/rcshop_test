package com.socool.soft.controller;

import com.socool.soft.bo.RcMember;
import com.socool.soft.bo.RcVendor;
import com.socool.soft.bo.RcVendorUser;
import com.socool.soft.bo.constant.RoleIdEnum;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.service.*;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/*import com.socool.soft.service.api.IMemberInfoService;
import com.socool.soft.service.api.IRcVendorService;
import com.socool.soft.service.api.IRoleService;
import com.socool.soft.service.api.SerialGeneratorService;*/

@Controller
@RequestMapping(value = "vendor")
public class VendorController extends BaseController {
	@Autowired
	private IVendorService vendorService;
	@Autowired
	private IVendorUserService vendorUserService;
	@Autowired
	private IMemberService memberService;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private IRoleService roleService;

	@RequestMapping(value = "vendor")
	public String vendor(Model model, Integer currentPage, String name, String contactPerson, String email, String mobile) {
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
		List<RcVendor> vendors = vendorService.findPagedVendors(name, contactPerson, email, mobile, page);
		
		model.addAttribute("role", roleService.findRoleById(RoleIdEnum.PRINCIPAL_MANAGER.getValue()));
		model.addAttribute("list", vendors);
		if (flag) {
			return "vendor";
		} else {
			return "vendor_inner";
		}
	}

	@RequestMapping(value = "deleteVendor")
	@ResponseBody
	public Result<String> deleteVendor(int vendorId, Model model) {
		Result<String> result = new Result<String>();
	/*	List<RcMerchant> list = merchantService.findMerchantsByVendorId(vendorId);
		if (!CollectionUtils.isEmpty(list)) {
			result.setCode("-1");
			result.setResult("Can't deleted now because this item contains data.");
		} else {*/
		vendorService.removeVendor(vendorId);
		result.setResult("Done.");
	/*	}*/

		return result;
	}
	@RequestMapping(value = "enableVendor")
	@ResponseBody
	public Result<String> enableVendor(int vendorId, Model model) {
		Result<String> result = new Result<String>();
		vendorService.recoverVendor(vendorId);
		return result;
	}
/**
 * 更新品牌商
 * @param vendorUser
 * @param model
 * @return
 * @throws IllegalAccessException
 * @throws InvocationTargetException
 */
	@RequestMapping(value = "updateVendor")
	@ResponseBody
	public Result<Object> updateVendor(RcVendor vendorParam, RcVendorUser vendorUser, Model model)
			throws IllegalAccessException, InvocationTargetException {
		Result<Object> result = new Result<Object>();
		vendorParam.setEmail(null);
		vendorParam.setDescription(null);
		vendorUser.setMobile(null);
		vendorUser.setName(null);
		if (vendorParam.getVendorId() != null) {
			vendorService.modifyVendor(vendorParam);
		}
		
		if (vendorUser.getMemberId() != null) {
			RcMember member = memberService.findMemberByAccount(vendorUser.getAccount());
			if(member!=null&&!member.getMemberId().equals(vendorUser.getMemberId())){
				result.setCode("-1");
				result.setResult("account has been used");
				return result;
			}
			member = memberService.findMemberByEmail(vendorUser.getEmail());
			if(member!=null&&!member.getMemberId().equals(vendorUser.getMemberId())){
				result.setCode("-1");
				result.setResult("email has been used");
				return result;
			}
			if(StringUtils.isNotEmpty(vendorUser.getPassword())){
				vendorUser.setPassword(DigestUtils.md5Hex(vendorUser.getPassword()));
			}
			
			vendorUserService.modifyVendorUser(vendorUser);
		}
		if (vendorParam.getVendorId() == null&&vendorUser.getMemberId() == null) {
			RcMember member = memberService.findMemberByAccount(vendorUser.getAccount());
			if(member!=null){
				result.setCode("-1");
				result.setResult("account has been used");
				return result;
			}
			member = memberService.findMemberByEmail(vendorUser.getEmail());
			if(member!=null){
				result.setCode("-1");
				result.setResult("email has been used");
				return result;
			}
			vendorUser.setPassword(DigestUtils.md5Hex(vendorUser.getPassword()));
			int vendorId = vendorService.addVendor(vendorParam);
			vendorUser.setVendorId(vendorId);
			vendorUser.setIsSuper(YesOrNoEnum.YES.getValue());
			vendorUserService.addVendorUser(vendorUser);
		}
		
		result.setResult("Done.");
		return result;
	}
}
