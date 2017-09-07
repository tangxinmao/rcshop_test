package com.socool.soft.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.socool.soft.constant.Constants;
import com.socool.soft.exception.SystemException;
import com.socool.soft.service.IRedisService;
import com.socool.soft.service.ISMSService;
import com.socool.soft.vo.Code;
import com.socool.soft.vo.Datapacket;
import com.socool.soft.vo.Packet;
import com.socool.soft.vo.Result;
import com.socool.soft.vo.SendingData;
import com.socool.soft.vo.SentSmsRequst;

import net.sf.json.JSONObject;

@RequestMapping(value = "SMS")
@Controller
public class SMSControlller extends BaseController {

	@Autowired
	private ISMSService ismsService;
/*	@Autowired
	private IMemberInfoService iMemberInfoService;*/
	@Autowired
	private IRedisService redisService;

	@RequestMapping(value = "smsreguler")
	@ResponseBody
	public Result<String> smsReguler(String data, String timestamp, String nonceStr, String product, String signature,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws UnsupportedEncodingException, JsonProcessingException, ClientProtocolException, IOException, SystemException {
		Result<String> result = new Result<String>();
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if (json == null) {
			result.setCode("-1");
			result.setResult("token验证失败！");
			return result;
		}
		String mobile = json.getString("mobile");
		SentSmsRequst sentSmsRequst = new SentSmsRequst();
		List<SendingData> sendingDatas = new ArrayList<SendingData>();
		SendingData sendingData = new SendingData();
		sendingDatas.add(sendingData);
		sentSmsRequst.setSending_data(sendingDatas);
		sendingData.setApikey("4dd671d14ac6581665467af44caf72c7");
		sendingData.setCallbackurl(httpServletRequest.getServerName()+httpServletRequest.getContextPath() + "/SMS/callbackurl");
		List<Datapacket> datapackets = new ArrayList<Datapacket>();
		sendingData.setDatapacket(datapackets);
		Datapacket datapacket = new Datapacket();
		Packet packet = new Packet();
		// 随机生成4位验证码
		String randomAlphanumeric = RandomStringUtils.randomNumeric(6);
		String str = "Beli Material - Kode verifikasi nomor telepon Anda " + randomAlphanumeric
			+ ". NOTE: Kode hanya berlaku selama 3 menit.";
		String encode = URLEncoder.encode(str, "utf-8");
		packet.setMessage(encode);
		packet.setNumber(mobile);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		packet.setSendingdatetime(simpleDateFormat.format(new Date()));
		datapacket.setPacket(packet);
		datapackets.add(datapacket);
	    result = ismsService.smsreguler(sentSmsRequst);
		/*SendingData sendingData2 = sentSmsResponse.getSending_respon().get(0);
		if (sendingData2.getGlobalstatus() == 10) {
			Code code = new Code();
			code.setCode(packet.getMessage());
			code.setCreateDate(new Date());
			redisTemplate.boundHashOps("code").put(mobile, SerializationUtils.serialize(code));
		} else {
			result.setCode(sendingData2.getGlobalstatus() + "");
			result.setResult(sendingData2.getGlobalstatustext());
			return result;
		}*/
		if(result.getCode().equals("1")){
			Code code = new Code();
			code.setCode(randomAlphanumeric);
			result.setResult(randomAlphanumeric);
			code.setCreateDate(new Date());
			redisService.hsetObject(Constants.REDIS_MOBILE_VERIFICATION_CODE, mobile, code);
		}
//		if(result.getCode().equals("1")){
		//	Code code = new Code();
		//	code.setCode("1111");
		//	result.setResult("1111");
		//	code.setCreateDate(new Date());
			//redisTemplate.boundHashOps("code").put(mobile, SerializationUtils.serialize(code));
//		}
		
		return result;
	}

//	@RequestMapping(value = "smssaldo")
//	@ResponseBody
//	public SentSmsResponse smssaldo(Model model, HttpServletRequest httpServletRequest)
//			throws UnsupportedEncodingException, JsonProcessingException, ClientProtocolException, IOException {
//		SentSmsRequst sentSmsRequst = new SentSmsRequst();
//		List<SendingData> sendingDatas = new ArrayList<SendingData>();
//		SendingData sendingData = new SendingData();
//		sendingDatas.add(sendingData);
//		sentSmsRequst.setSending_data(sendingDatas);
//		sendingData.setApikey("4dd671d14ac6581665467af44caf72c7");
//		return ismsService.smssaldo(httpServletRequest, sentSmsRequst);
//
//	}

//	/**
//	 * 保存发送状态
//	 * @param model
//	 * @param httpServletRequest
//	 * @param statusSms
//	 */
//	@RequestMapping(value = "callbackurl")
//	public void callbackurl(Model model, HttpServletRequest httpServletRequest, @RequestBody StatusSms statusSms) {
//		SendingData sendingData = statusSms.getStatus_respon().get(0);
//		RcSmsStatus rcSmsStatus = new RcSmsStatus();
//		rcSmsStatus.setSendingid(sendingData.getSendingid());
//		rcSmsStatus.setDeliverystatus(sendingData.getDeliverystatus());
//		rcSmsStatus.setDeliverystatustext(sendingData.getDeliverystatustext());
//		rcSmsStatus.setNumber(sendingData.getNumber());
//		ismsService.saveSmsStatus(rcSmsStatus);
//	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String randomAlphanumeric = RandomStringUtils.randomNumeric(4);
		System.out.println();
	}
}
