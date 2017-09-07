package com.socool.soft.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.exception.ErrorValue;
import com.socool.soft.service.ISMSService;
import com.socool.soft.vo.Packet;
import com.socool.soft.vo.Result;
import com.socool.soft.vo.SentSmsRequst;

@Service
public class SMSServiceImpl implements ISMSService {
	@Autowired
	private PropertyConstants propertyConstants;

	private CloseableHttpClient hc = HttpClients.createDefault();
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Result<String> smsreguler(SentSmsRequst sentSmsRequst) {
		Packet packet = sentSmsRequst.getSending_data().get(0).getDatapacket().get(0).getPacket();
		/* SentSmsResponse smsResponse =new SentSmsResponse(); */
		Result<String> result = new Result<String>();
		CloseableHttpResponse httpResponse = null;
		HttpEntity httpEntity = null;
		try {
			HttpPost httppost = new HttpPost(propertyConstants.smsUrl + "?username=" + propertyConstants.smsUserName
					+ "&key=" + propertyConstants.smsKey + "&number=" + packet.getNumber() + "&message=" + packet.getMessage());
			objectMapper.setSerializationInclusion(Include.NON_NULL);
			String sentSmsRequstJson = objectMapper.writeValueAsString(sentSmsRequst);
			StringEntity entity = new StringEntity(sentSmsRequstJson, "utf-8");
			entity.setContentEncoding("utf-8");
			entity.setContentType("application/json");
			httppost.setEntity(entity);
			httpResponse = hc.execute(httppost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				httpEntity = httpResponse.getEntity();
				String json = EntityUtils.toString(httpEntity, "utf-8");
				if (json.startsWith("0")) {
					return result;
				} else if (json.startsWith("4")) {
					result.setCode(ErrorValue.ACCOUNT_NOT_EXISTS.getStr());
					return result;
				} else {
					result.setCode(ErrorValue.NOT_IDENTIFIED.getStr());
					return result;
	
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				EntityUtils.consume(httpEntity);
				if(httpResponse != null) {
					httpResponse.close();
				}
			} catch (IOException e) {
			}
		}
		result.setCode(ErrorValue.NOT_IDENTIFIED.getStr());
		return result;
	}

	// @Override
	// public SentSmsResponse smssaldo(HttpServletRequest httpServletRequest,
	// SentSmsRequst sentSmsRequst) throws ClientProtocolException, IOException
	// {
	// HttpPost httppost = new
	// HttpPost("http://128.199.232.241/sms/smssaldo.php?username=centrontech&key=4dd671d14ac6581665467af44caf72c7");
	// objectMapper.setSerializationInclusion(Include.NON_NULL);
	// String sentSmsRequstJson=objectMapper.writeValueAsString(sentSmsRequst);
	// StringEntity entity = new StringEntity(sentSmsRequstJson,"utf-8");
	// entity.setContentEncoding("utf-8");
	// entity.setContentType("application/json");
	// httppost.setEntity(entity);
	// HttpResponse httpResponse = hc.execute(httppost);
	// if( httpResponse.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
	// HttpEntity httpEntity = httpResponse.getEntity();
	// String json= EntityUtils.toString(httpEntity, "utf-8");
	// SentSmsResponse sentSmsResponse= objectMapper.readValue(json,
	// SentSmsResponse.class);
	// return sentSmsResponse;
	// }
	// return null;
	// }

	/*
	 * @Override public void saveSmsStatus(RcSmsStatus rcSmsStatus) {
	 * rcSmsStatusMapper.insert(rcSmsStatus); }
	 */
}
