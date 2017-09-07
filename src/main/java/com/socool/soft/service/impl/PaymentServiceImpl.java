package com.socool.soft.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcDokuUserToken;
import com.socool.soft.bo.RcOrder;
import com.socool.soft.bo.RcOrderPayment;
import com.socool.soft.bo.constant.OrderPaymentStatusEnum;
import com.socool.soft.bo.constant.PaymentInterfaceEnum;
import com.socool.soft.bo.constant.PaymentTypeEnum;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.service.IDokuUserTokenService;
import com.socool.soft.service.IOrderPaymentService;
import com.socool.soft.service.IOrderService;
import com.socool.soft.service.IPaymentService;
import com.socool.soft.util.AppsUtil;
import com.socool.soft.util.DateUtil;

import net.sf.json.JSONObject;

@Service
public class PaymentServiceImpl implements IPaymentService {

	private final static Log log = LogFactory.getLog(PaymentServiceImpl.class);
	
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderPaymentService orderPaymentService;
	@Autowired
	private PropertyConstants propertyConstants;
	@Autowired
	private IDokuUserTokenService dokuUserTokenService;
	
	@Override
	public String toPay(String data,String req_payment_channel, int memberId) {
		String path = propertyConstants.dokuPaymentUrl;
		int paymentType = 0;
		if("15".equals(req_payment_channel)){
			paymentType = PaymentTypeEnum.CREDIT_CARD.getValue();
		}
		else if("02".equals(req_payment_channel)){
			paymentType = PaymentTypeEnum.DEBIT_CARD.getValue();
		}
		else{
			paymentType = PaymentTypeEnum.BALANCE.getValue();
		}
		JSONObject reqData = JSONObject.fromObject(data);
		long oId = reqData.getLong("req_trans_id_merchant");
		String sessionId = reqData.getString("req_session_id");
		String amount = reqData.getString("req_amount");
		orderPaymentService.removeOrderPaymentByOrderId(oId);
		RcOrderPayment orderPayment = new RcOrderPayment();
		orderPayment.setOrderId(oId);
		orderPayment.setPaymentInterface(PaymentInterfaceEnum.DOKU.getValue());
		orderPayment.setPaymentType(paymentType);
		orderPayment.setTransactionId(String.valueOf(oId));
		orderPayment.setThirdPartyTransactionId(sessionId);
		orderPayment.setAmount(new BigDecimal(amount));
		orderPayment.setStatus(OrderPaymentStatusEnum.PENDING.getValue());
		orderPaymentService.addOrderPayment(orderPayment);
		String result = sentRequest(path,"data="+data);
		try{
			JSONObject json = JSONObject.fromObject(result);
			if("0000".equals(json.getString("res_response_code"))){
				long orderId = json.getLong("res_trans_id_merchant");
				orderService.completePayment(orderId,PaymentInterfaceEnum.DOKU.getValue(), paymentType);
				orderPayment = orderPaymentService.findOrderPaymentByOrderId(orderId);
				orderPayment.setStatus(OrderPaymentStatusEnum.FINISHED.getValue());
				orderPayment.setFinishTime(new Date());
				orderPaymentService.modifyOrderPayment(orderPayment);
				if(json.containsKey("res_bundle_token")){
					//JSONObject res_bundle_token = json.getString("res_bundle_token");getJSONObject("res_bundle_token");
					String jsonString = json.getString("res_bundle_token");
					JSONObject res_bundle_token = JSONObject.fromObject(jsonString);
					if("0000".equals(res_bundle_token.getString("res_token_code"))){
						String token = res_bundle_token.getString("res_token_payment");
						RcDokuUserToken dokuUserToken = dokuUserTokenService.findByMemberId(memberId);
						if(dokuUserToken==null){
							dokuUserToken = new RcDokuUserToken();
							dokuUserToken.setMemberId(memberId);
							dokuUserToken.setToken(token);
							dokuUserTokenService.saveDokuToken(dokuUserToken);
						}
						else{
							dokuUserToken.setToken(token);
							dokuUserTokenService.modifyDokuToken(dokuUserToken);
							
						}
					}	
					
				}
			}
		}catch(Exception e){
			log.debug(e.getMessage());
		}
		return result;
	}
	
	@Override
	public String generateCode(String orderIds,String data) {
		long orderId = Long.parseLong(orderIds);
		RcOrderPayment param = new RcOrderPayment();
		param.setOrderId(orderId);
		param.setPaymentInterface(PaymentInterfaceEnum.DOKU.getValue());
		param.setPaymentType(PaymentTypeEnum.PAY_CODE.getValue());
		param.setOrderBy("CREATE_TIME DESC");
		RcOrderPayment payCodePayment = orderPaymentService.findOrderPayment(param);
		if(payCodePayment != null && payCodePayment.getStatus() != OrderPaymentStatusEnum.FINISHED.getValue()) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(payCodePayment.getCreateTime());
			cal.add(Calendar.MINUTE, propertyConstants.dokuPayCodeDuration);
			Date expireTime = cal.getTime();
			if(!expireTime.before(new Date())) {
				if(payCodePayment.getStatus() == OrderPaymentStatusEnum.INVALID.getValue()) {
					payCodePayment.setStatus(OrderPaymentStatusEnum.PENDING.getValue());
					orderPaymentService.modifyOrderPayment(payCodePayment);
				}
				return "{\"res_pay_code\":\"" + payCodePayment.getPayCode() + "\",\"expireTime\":\"" + DateUtil.formatDate(expireTime, "dd/MM/yyyy HH:mm 'WIB'") + "\"}";
			}
		}
		
		String path = propertyConstants.dokuNagerageCodeVa;
		String result = sentRequest(path,"data="+data);
		try{
			JSONObject json = JSONObject.fromObject(result);
			if("0000".equals(json.getString("res_response_code"))){
				JSONObject reqData = JSONObject.fromObject(data);
				long oId = reqData.getLong("req_trans_id_merchant");
				String sessionId = reqData.getString("req_session_id");
				String amount = reqData.getString("req_amount");
				orderPaymentService.removeOrderPaymentByOrderId(oId);
				RcOrderPayment orderPayment = new RcOrderPayment();
				orderPayment.setOrderId(oId);
				orderPayment.setPaymentInterface(PaymentInterfaceEnum.DOKU.getValue());
				orderPayment.setPaymentType(PaymentTypeEnum.PAY_CODE.getValue());
				orderPayment.setTransactionId(String.valueOf(oId));
				orderPayment.setThirdPartyTransactionId(sessionId);
				orderPayment.setPayCode(json.getString("res_pay_code"));
				orderPayment.setAmount(new BigDecimal(amount));
				orderPayment.setStatus(OrderPaymentStatusEnum.PENDING.getValue());
				orderPaymentService.addOrderPayment(orderPayment);
				Calendar cal = Calendar.getInstance();
				cal.setTime(orderPayment.getCreateTime());
				cal.add(Calendar.MINUTE, propertyConstants.dokuPayCodeDuration);
				Date expireTime = cal.getTime();
				json.put("expireTime", DateUtil.formatDate(expireTime, "dd/MM/yyyy HH:mm 'WIB'"));
				RcOrder order = orderService.findOrderById(orderId);
//				order.setPayCode(json.getString("res_pay_code"));
				order.setPaymentInterface(PaymentInterfaceEnum.DOKU.getValue());
				order.setPaymentType(PaymentTypeEnum.PAY_CODE.getValue());
				orderService.modifyOrder(order);
				return json.toString();
			}
		}catch(Exception e){
			log.debug(e.getMessage());
		}
		return result;
	}
	
	@Override
	public String mandiry(String data,String req_payment_channel) {
		String path = propertyConstants.dokuDirectPaymentUrl;
		int paymentType = 0;
		if("15".equals(req_payment_channel)){
			paymentType = PaymentTypeEnum.CREDIT_CARD.getValue();
		}
		else if("02".equals(req_payment_channel)){
			paymentType = PaymentTypeEnum.DEBIT_CARD.getValue();
		}
		else{
			paymentType = PaymentTypeEnum.BALANCE.getValue();
		}
		JSONObject reqData = JSONObject.fromObject(data);
		long oId = reqData.getLong("req_trans_id_merchant");
		String sessionId = reqData.getString("req_session_id");
		String amount = reqData.getString("req_amount");
		orderPaymentService.removeOrderPaymentByOrderId(oId);
		RcOrderPayment orderPayment = new RcOrderPayment();
		orderPayment.setOrderId(oId);
		orderPayment.setPaymentInterface(PaymentInterfaceEnum.DOKU.getValue());
		orderPayment.setPaymentType(paymentType);
		orderPayment.setTransactionId(String.valueOf(oId));
		orderPayment.setThirdPartyTransactionId(sessionId);
		orderPayment.setAmount(new BigDecimal(amount));
		orderPayment.setStatus(OrderPaymentStatusEnum.PENDING.getValue());
		orderPaymentService.addOrderPayment(orderPayment);
		String result = sentRequest(path,"data="+data);
		try{
			JSONObject json = JSONObject.fromObject(result);
			if("0000".equals(json.getString("res_response_code"))){
				long orderId = json.getLong("res_trans_id_merchant");
				orderService.completePayment(orderId,PaymentInterfaceEnum.DOKU.getValue(), paymentType);
				orderPayment = orderPaymentService.findOrderPaymentByOrderId(orderId);
				orderPayment.setStatus(OrderPaymentStatusEnum.FINISHED.getValue());
				orderPayment.setFinishTime(new Date());
				orderPaymentService.modifyOrderPayment(orderPayment);
			}
		}catch(Exception e){
			log.debug(e.getMessage());
		}
		return result;
	}

	private String sentRequest(String path,String data1) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(path);
		httpPost.addHeader(HTTP.CONTENT_TYPE,
				"application/x-www-form-urlencoded; charset=UTF-8");
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			StringEntity reqEntity = new StringEntity(data1);
			reqEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");    
			httpPost.setEntity(reqEntity);
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == 200) {// 如果状态码为200,就是正常返回
				byte[] data = readInputStream(entity.getContent());
				String tempStr = new String(data,"UTF-8");
				return tempStr;
			}
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				EntityUtils.consume(entity);
				if(response != null) {
					response.close();
				}
			} catch (IOException e) {
			}
		}
		return "{\"res_response_code\":\"999999\"}";
	}
		
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	@Override
	public boolean getTransactionStatus(long orderId) {
		RcOrderPayment orderPayment = orderPaymentService.findOrderPaymentByOrderId(orderId);
		String mallId = propertyConstants.dokuMallId;
		String sharedKey = propertyConstants.dokuSharedKey;
		String words = AppsUtil.SHA1(mallId + sharedKey + orderId);
		StringBuilder sb = new StringBuilder();
		sb.append("MALLID=").append(mallId)
			.append("&SHAREDKEY=").append(sharedKey)
			.append("&CHAINMERCHANT=NA")
			.append("&TRANSIDMERCHANT=").append(orderPayment.getTransactionId())
			.append("&SESSIONID=").append(orderPayment.getThirdPartyTransactionId())
			.append("&WORDS=").append(words);
		String result = sentRequest(propertyConstants.dokuStatusCheckUrl,sb.toString());
		try {
			Document document = DocumentHelper.parseText(result);
			Element root = document.getRootElement();
			if("0000".equals(root.element("RESPONSECODE").getText())) {
				return true;
			}
		} catch (DocumentException e) {
		}
		return false;
	}
}