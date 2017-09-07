package com.socool.soft.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.socool.soft.bo.RcMerchantUser;
import com.socool.soft.bo.RcVendorUser;
import com.socool.soft.service.IMerchantUserService;
import com.socool.soft.service.IReportService;
import com.socool.soft.service.IVendorUserService;
import com.socool.soft.vo.newvo.ReportVO;

@Controller
@RequestMapping(value = "report")
public class ReportController extends BaseController {
	@Autowired
	private IReportService  reportService;
	
	@Autowired
	private IMerchantUserService merchantUserService;
	
	@Autowired
	private IVendorUserService vendorUserService;
	/**
	 * @param httpServletRequest
	 * @param vendorId
	 * @param merchantId
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "reportManagement")
	public String reportManagement(HttpServletRequest httpServletRequest,Integer vendorId,Integer merchantId,Model model){
		//	用户权限控制
		Integer merchantUserId = getMerchantUserId(httpServletRequest);
		if(merchantUserId != null) {
			RcMerchantUser merchantUser = merchantUserService.findMerchantUserById(merchantUserId);
			merchantId = merchantUser.getMerchantId();
		}
		Integer vendorUserId = getVendorUserId(httpServletRequest);
		if(vendorUserId != null) {
			RcVendorUser vendorUser = vendorUserService.findVendorUserById(vendorUserId);
			vendorId = vendorUser.getVendorId();
		}
		ReportVO report = reportService.findOverview(merchantId, vendorId);
		model.addAttribute("overview", report);
		return "reportManagement";
	}
	
	@RequestMapping(value = "transaction")
	@ResponseBody
	public Map<String, Object> transaction(int type,HttpServletRequest httpServletRequest,Integer vendorId,Integer merchantId,Model model){
		Map<String, Object> m=new HashMap<String, Object>();
		Integer merchantUserId = getMerchantUserId(httpServletRequest);
		if(merchantUserId != null) {
			RcMerchantUser merchantUser = merchantUserService.findMerchantUserById(merchantUserId);
			merchantId = merchantUser.getMerchantId();
			m.put("isHide", true);
		} else {
			m.put("isHide", false);
		}
		
		Integer vendorUserId = getVendorUserId(httpServletRequest);
		if(vendorUserId != null) {
			RcVendorUser vendorUser = vendorUserService.findVendorUserById(vendorUserId);
			vendorId = vendorUser.getVendorId();
		}
		//用户权限控制
		/*if(httpServletRequest.getAttribute("merchantId")!=null){
			merchantId=Integer.valueOf(httpServletRequest.getAttribute("merchantId").toString());
			m.put("isHide", true);
		}else{
			m.put("isHide", false);
		}
		if(httpServletRequest.getAttribute("vendorId")!=null) {
			vendorId=Integer.valueOf(httpServletRequest.getAttribute("vendorId").toString());
		}*/
		ReportVO report = reportService.findReport(type, merchantId, vendorId);
		m.put("report",report);
		return m;
	}
	

}
