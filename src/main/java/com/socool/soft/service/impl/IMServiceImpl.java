package com.socool.soft.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.constant.HttpRequestMethodEnum;
import com.socool.soft.constant.Constants;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.service.IIMService;
import com.socool.soft.service.IRedisService;
import com.socool.soft.util.HttpUtil;
import com.socool.soft.util.JsonUtil;
import com.socool.soft.vo.IMNode;

@Service
public class IMServiceImpl implements IIMService {

	@Autowired
	private IRedisService redisService;
	@Autowired
	private PropertyConstants propertyConstants;
	
	@Override
	public IMNode regist(String hxId, String nickName) throws Exception {
		String token = redisService.get(Constants.REDIS_HUANXIN_TOKEN);
		if(StringUtils.isBlank(token)){
			IMNode imNode = new IMNode(401);
			return imNode;
		}
		String url = propertyConstants.huanxinHost+"/"+propertyConstants.huanxinOrgName+"/"+propertyConstants.huanxinAppName+"/users";
		Map<String,String> param = new HashMap<String, String>();
		param.put("username", hxId);
		param.put("password", propertyConstants.huanxinPassword);
		param.put("nickname", nickName);
		String paramJson = JsonUtil.toJson(param);
		IMNode imNode = HttpUtil.request(url, HttpRequestMethodEnum.POST.getValue(), token, paramJson);
		return imNode;
	}
}
