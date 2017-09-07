package com.socool.soft.controller;

import com.socool.soft.bo.RcDokuUserToken;
import com.socool.soft.bo.RcOrder;
import com.socool.soft.bo.RcOrderPayment;
import com.socool.soft.bo.constant.OrderPaymentStatusEnum;
import com.socool.soft.bo.constant.PaymentInterfaceEnum;
import com.socool.soft.bo.constant.PaymentTypeEnum;
import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.exception.SystemException;
import com.socool.soft.service.IDokuUserTokenService;
import com.socool.soft.service.IOrderPaymentService;
import com.socool.soft.service.IOrderService;
import com.socool.soft.service.IPaymentService;
import com.socool.soft.service.IPushService;
import com.socool.soft.util.AppsUtil;
import com.socool.soft.util.JsonUtil;
import com.socool.soft.vo.*;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(value="/pay")
@Controller
public class PayController extends BaseController {

	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderPaymentService orderPaymentService;
	@Autowired
	private PropertyConstants propertyConstants;
	@Autowired
    private IPushService pushService;
	@Autowired
	private IDokuUserTokenService dokuUserTokenService;
	
	private final static Log log = LogFactory.getLog(PayController.class);
	
	@RequestMapping(value="/topay")
	@ResponseBody
	public String toPay(String data,String timestamp,String nonceStr,String product,String signature) throws IOException{
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		String req_token_id = json.getString("req_token_id");
		String req_amount = json.getString("req_amount");
		String req_mobile_phone = json.getString("req_mobile_phone");
		Integer memberId = json.getInt("memberId");
		String orderId = json.getString("orderId");
		String req_payment_channel = json.getString("req_payment_channel");
		String res_pairing_code = json.getString("res_pairing_code");
		String res_device_id = json.getString("res_device_id");
		String req_name = json.getString("req_name");
		String req_email = json.getString("req_email");
		
//		String req_token_id = "aa988cebcd146956574aa64d0174107b9e04eb16";
//		String req_amount = "15000.00";
//		String req_mobile_phone = "81298231142";
//		String memberId = "123123";
//		String orderId = "1861951865";
//		int req_payment_channel = 15;
//		String res_pairing_code = "09051710472005080015";
//		String res_device_id = "864387023380534";
//		String req_name = "Riza Abdillah";
		
		//RcMember member = memberService.findMemberByMemberId(memberId);
		String mallId = propertyConstants.dokuMallId;
		String sharedKey = propertyConstants.dokuSharedKey;
		
		CreditcardPayment creditcardPayment = new CreditcardPayment();
		creditcardPayment.setReq_name(req_name);
		creditcardPayment.setReq_address("Indonesia");
		creditcardPayment.setReq_amount(String.valueOf(req_amount));
		creditcardPayment.setReq_chain_merchant("NA");
		creditcardPayment.setReq_currency(360);
		creditcardPayment.setReq_email(req_email);
		creditcardPayment.setReq_mall_id(mallId);
		creditcardPayment.setReq_mobile_phone(req_mobile_phone);
		creditcardPayment.setReq_payment_channel(req_payment_channel);
		String amount = AppsUtil.generateMoneyFormat(req_amount);
		creditcardPayment.setReq_purchase_amount(amount);
		creditcardPayment.setReq_purchase_currency(360);
		Date now = new Date();
		String timeFormat = AppsUtil.dokuDateFormat(now);
		creditcardPayment.setReq_request_date_time(timeFormat);
		creditcardPayment.setReq_session_id(AppsUtil.SHA1(timeFormat));
		creditcardPayment.setReq_token_id(req_token_id);
		creditcardPayment.setReq_trans_id_merchant(orderId);
		String req_words = AppsUtil.SHA1(amount + mallId + sharedKey + orderId + "360" + req_token_id 
				           + res_pairing_code + res_device_id);
		creditcardPayment.setReq_words(req_words);
//		PaymentBasket basket = new PaymentBasket();
//		basket.setAmount(amount);
//		basket.setName(orderId);
//		basket.setQuantity("1");
//		basket.setSubtotal(amount);
		String basket = orderId + "," + amount + "," + "1," + amount + ";";
		creditcardPayment.setReq_basket(basket);
		String sendData = JsonUtil.toJson(creditcardPayment);
		String result = paymentService.toPay(sendData,req_payment_channel,memberId);
		return result;
	}
	
	@RequestMapping(value="/virtualAccount")
	@ResponseBody
	public String virtualAccount(String data,String timestamp,String nonceStr,String product,String signature) throws IOException{
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		String req_amount = json.getString("req_amount");
		//int memberId = json.getInt("memberId");
		String orderId = json.getString("orderId");
		String req_name = json.getString("req_name");
		//String res_device_id = json.getString("res_device_id");
		String req_email = json.getString("req_email");

		String mallId = propertyConstants.dokuMallId;
		String sharedKey = propertyConstants.dokuSharedKey;
		
		PaymentVirtualAccount paymentWallet = new PaymentVirtualAccount();
		paymentWallet.setReq_name(req_name);
		paymentWallet.setReq_address("Indonesia");
		paymentWallet.setReq_amount(req_amount);
		paymentWallet.setReq_email(req_email);
		paymentWallet.setReq_mall_id(mallId);
		String amount = AppsUtil.generateMoneyFormat(req_amount);
		paymentWallet.setReq_purchase_amount(amount);
		Date now = new Date();
		String timeFormat = AppsUtil.dokuDateFormat(now);
		paymentWallet.setReq_request_date_time(timeFormat);
		paymentWallet.setReq_session_id(AppsUtil.SHA1(timeFormat));
		paymentWallet.setReq_trans_id_merchant(orderId);
		String req_words = AppsUtil.SHA1(amount + mallId + sharedKey + orderId + "360");
		paymentWallet.setReq_words(req_words);
		paymentWallet.setReq_expiry_time(String.valueOf(propertyConstants.dokuPayCodeDuration));
//		PaymentBasket basket = new PaymentBasket();
//		basket.setAmount(amount);
//		basket.setName(orderId);
//		basket.setQuantity("1");
//		basket.setSubtotal(amount);
		String basket = orderId + "," + amount + "," + "1," + amount + ";";
		paymentWallet.setReq_basket(basket);
		String sendData = JsonUtil.toJson(paymentWallet);
		String result = paymentService.generateCode(orderId,sendData);
		return result;
	}
	
	@RequestMapping(value="/one_click")
	@ResponseBody
	public String oneClick(String data,String timestamp,String nonceStr,String product,String signature) throws IOException{
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		String req_token_id = json.getString("req_token_id");
		String req_amount = json.getString("req_amount");
		String req_mobile_phone = json.getString("req_mobile_phone");
		Integer memberId = json.getInt("memberId");
		String orderId = json.getString("orderId");
		String req_payment_channel = json.getString("req_payment_channel");
		String res_pairing_code = json.getString("res_pairing_code");
		String res_device_id = json.getString("res_device_id");
		String req_name = json.getString("req_name");
		String req_email = json.getString("req_email");
		
		String req_token_payment = "";
		if(json.containsKey("req_token_payment")){
			req_token_payment = json.getString("req_token_payment");
		}
		
//		String req_token_id = "aa988cebcd146956574aa64d0174107b9e04eb16";
//		String req_amount = "15000.00";
//		String req_mobile_phone = "81298231142";
//		String memberId = "123123";
//		String orderId = "1861951865";
//		int req_payment_channel = 15;
//		String res_pairing_code = "09051710472005080015";
//		String res_device_id = "864387023380534";
//		String req_name = "Riza Abdillah";
		
		String mallId = propertyConstants.dokuMallId;
		String sharedKey = propertyConstants.dokuSharedKey;
		
		PaymentFast creditcardPayment = new PaymentFast();
		creditcardPayment.setReq_name(req_name);
		creditcardPayment.setReq_address("Indonesia");
		creditcardPayment.setReq_amount(req_amount);
		creditcardPayment.setReq_chain_merchant("NA");
		creditcardPayment.setReq_currency(360);
		creditcardPayment.setReq_email(req_email);
		creditcardPayment.setReq_mall_id(mallId);
		creditcardPayment.setReq_mobile_phone(req_mobile_phone);
		creditcardPayment.setReq_payment_channel(req_payment_channel);
		String amount = AppsUtil.generateMoneyFormat(req_amount);
		creditcardPayment.setReq_purchase_amount(amount);
		creditcardPayment.setReq_purchase_currency(360);
		Date now = new Date();
		String timeFormat = AppsUtil.dokuDateFormat(now);
		creditcardPayment.setReq_request_date_time(timeFormat);
		creditcardPayment.setReq_session_id(AppsUtil.SHA1(timeFormat));
		creditcardPayment.setReq_token_id(req_token_id);
		creditcardPayment.setReq_trans_id_merchant(orderId);
		String req_words = AppsUtil.SHA1(amount + mallId + sharedKey + orderId + "360" + req_token_id 
		           + res_pairing_code + res_device_id);
		creditcardPayment.setReq_words(req_words);
		creditcardPayment.setReq_customer_id(memberId.toString());
		creditcardPayment.setReq_token_payment(req_token_payment);
//		PaymentBasket basket = new PaymentBasket();
//		basket.setAmount(amount);
//		basket.setName(orderId);
//		basket.setQuantity("1");
//		basket.setSubtotal(amount);
		String basket = orderId + "," + amount + "," + "1," + amount + ";";
		creditcardPayment.setReq_basket(basket);
		String sendData = JsonUtil.toJson(creditcardPayment);
		String result = paymentService.toPay(sendData,req_payment_channel,memberId);
		return result;
	}
	
	@RequestMapping(value="/mandiri")
	@ResponseBody
	public String mandiriPay(String data,String timestamp,String nonceStr,String product,String signature) throws IOException{
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			return "{\"code\":\"-1\",\"result\":\"token验证失败！\"}";
		}
		String req_response_token = json.getString("req_response_token");
		String req_amount = json.getString("req_amount");
		String req_mobile_phone = json.getString("req_mobile_phone");
		//int memberId = json.getInt("memberId");
		String orderId = json.getString("orderId");
		String req_payment_channel = json.getString("req_payment_channel");
//		String res_device_id = json.getString("res_device_id");
		String req_name = json.getString("req_name");
		String req_email = json.getString("req_email");
		String req_card_number = json.getString("req_card_number");
		String req_challenge_code_1 = json.getString("req_challenge_code_1");
		String req_challenge_code_2 = json.getString("req_challenge_code_2");
		String req_challenge_code_3 = json.getString("req_challenge_code_3");
		
		String mallId = propertyConstants.dokuMallId;
		String sharedKey = propertyConstants.dokuSharedKey;
		String amount = AppsUtil.generateMoneyFormat(req_amount);
		
		PaymentMandiri mandiri = new PaymentMandiri();
		mandiri.setReq_address("Indonesia");
		mandiri.setReq_amount(amount);
		String basket = orderId + "," + amount + "," + "1," + amount + ";";
		mandiri.setReq_basket(basket);
		mandiri.setReq_card_number(req_card_number);
		mandiri.setReq_chain_merchant("NA");
		mandiri.setReq_challenge_code_1(req_challenge_code_1);
		mandiri.setReq_challenge_code_2(req_challenge_code_2);
		mandiri.setReq_challenge_code_3(req_challenge_code_3);
		mandiri.setReq_currency("360");
		mandiri.setReq_email(req_email);
		mandiri.setReq_mall_id(mallId);
		mandiri.setReq_name(req_name);
		mandiri.setReq_payment_channel(req_payment_channel);
		mandiri.setReq_purchase_amount(amount);
		mandiri.setReq_purchase_currency("360");
		Date now = new Date();
		String timeFormat = AppsUtil.dokuDateFormat(now);
		mandiri.setReq_request_date_time(timeFormat);
		mandiri.setReq_response_token(req_response_token);
		mandiri.setReq_session_id(AppsUtil.SHA1(timeFormat));
		mandiri.setReq_trans_id_merchant(orderId);
		String req_words = AppsUtil.SHA1(amount + mallId + sharedKey + orderId + "360");
		mandiri.setReq_words(req_words);
		mandiri.setReq_mobile_phone(req_mobile_phone);
		
		String sendData = JsonUtil.toJson(mandiri);
		String result = paymentService.mandiry(sendData,req_payment_channel);
		return result;
	}

	@RequestMapping(value="/notification")
	@ResponseBody
	public String notifucation(PaymentNotifycation paymentNotifycation){
		log.debug(JsonUtil.toJson(paymentNotifycation));
		String orderId = paymentNotifycation.getTRANSIDMERCHANT();
		orderService.completePayment(Long.parseLong(orderId),PaymentInterfaceEnum.DOKU.getValue(),PaymentTypeEnum.PAY_CODE.getValue());
		RcOrderPayment orderPayment = orderPaymentService.findOrderPaymentByOrderId(Long.parseLong(orderId));
		orderPayment.setStatus(OrderPaymentStatusEnum.FINISHED.getValue());
		orderPayment.setFinishTime(new Date());
		orderPaymentService.modifyOrderPayment(orderPayment);

        RcOrder order = orderService.findOrderById(Long.valueOf(orderId));
        try {
            // 通知买家 "订单付款码支付成功"
            pushService.codePaymentSuccess(order);
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return "CONTINUE";
	}
	
	@RequestMapping(value="/gettokenpayment")
	@ResponseBody
	public Result<Map<String,String>> getTokenPayment(String data,String timestamp,String nonceStr,String product,String signature){
		Result<Map<String,String>> result = new Result<Map<String,String>>();
		JSONObject json = parseParam(data, timestamp, nonceStr, product, signature);
		if(json==null){
			result.setCode("-1");
			result.setResult("Token验证失败！");
			return result;
		}
		Integer memberId = json.getInt("memberId");
		RcDokuUserToken dokuUserToken = dokuUserTokenService.findByMemberId(memberId);
		Map<String,String> map = new HashMap<String, String>();
		if(dokuUserToken==null){
			map.put("token", "");
		}
		else{
			map.put("token", dokuUserToken.getToken());
		}
		result.setData(map);
		return result;
	}
}
