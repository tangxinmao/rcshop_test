package com.socool.soft.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.socool.soft.vo.Result;
import com.socool.soft.vo.SentSmsRequst;

public interface ISMSService {

  	Result<String> smsreguler(SentSmsRequst sentSmsRequst) throws ClientProtocolException, IOException;

//	SentSmsResponse smssaldo(HttpServletRequest httpServletRequest, SentSmsRequst sentSmsRequst) throws ClientProtocolException, IOException;

}
