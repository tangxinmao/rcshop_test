package com.socool.soft.controller;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.socool.soft.bo.RcAppLog;
import com.socool.soft.service.IAppLogService;
import com.socool.soft.vo.Result;

@Controller
@RequestMapping(value="/system")
public class AppLogController extends BaseController {
	
	@Autowired
	private IAppLogService appLogService;
	
	@RequestMapping(value="/uploaderror")
	@ResponseBody
	public Result<RcAppLog> uploadError(String data,String timestamp,String nonceStr,String product,String signature) throws IOException{
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		Result<RcAppLog> res = new Result<RcAppLog>(); 
		if(json==null){
			res.setCode("-1");
			res.setResult("token验证失败！");
			return res;
		}
		String sysVer = json.getString("sysVer");
		String model = json.getString("model");
		String appVer = json.getString("appVer");
		Integer memberId = null;
		try {
			memberId = json.getInt("memberId");
		} catch(Exception e) {
		}
		String errorLog = json.getString("errorLog");
		
		RcAppLog appLog = new RcAppLog();
		appLog.setAppVer(appVer);
		appLog.setErrorLog(errorLog);
		appLog.setMemberId(memberId);
		appLog.setModel(model);
		appLog.setSysVer(sysVer);

		long result = appLogService.addAppLog(appLog);
		if(result > 0){
			res.setResult("success!");
		}
		
		return res;
	}
}
