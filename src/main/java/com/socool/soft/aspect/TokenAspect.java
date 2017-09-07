package com.socool.soft.aspect;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.socool.soft.bo.constant.HttpRequestMethodEnum;
import com.socool.soft.constant.Constants;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.service.IRedisService;
import com.socool.soft.util.HttpUtil;
import com.socool.soft.util.JsonUtil;
import com.socool.soft.vo.IMNode;

@Component
@Aspect
public class TokenAspect {
	
	@Autowired
	private IRedisService redisService;
	@Autowired
	private PropertyConstants propertyConstants;

	@AfterReturning(pointcut = "execution (* com.socool.soft.service.IIMService.regist(..))", returning = "returnValue")
	public void logAfterReturning(JoinPoint  joinPoint, IMNode returnValue) throws Throwable {
		//如果等于401就重新发送
		if(returnValue.getCode()==401){
			String url = propertyConstants.huanxinHost+"/"+propertyConstants.huanxinOrgName+"/"+propertyConstants.huanxinAppName+"/token";
			Map<String,String> param = new HashMap<String, String>();
			param.put("grant_type", "client_credentials");
			param.put("client_id", propertyConstants.huanxinClientId);
			param.put("client_secret", propertyConstants.huanxinClientSecret);
			String paramJson = JsonUtil.toJson(param);
			IMNode imNode = HttpUtil.request(url, HttpRequestMethodEnum.POST.getValue(), null, paramJson);
			redisService.setex(Constants.REDIS_HUANXIN_TOKEN, imNode.getExpires_in() - 100, imNode.getAccess_token());
			((ProceedingJoinPoint) joinPoint).proceed();
		}
	

	}
}
