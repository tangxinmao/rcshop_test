package com.socool.soft.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyConstants {
	
	@Value("${doku.mallId}")
	public String dokuMallId;

	@Value("${doku.sharedKey}")
	public String dokuSharedKey;
	
	@Value("${doku.paymentUrl}")
	public String dokuPaymentUrl;

	@Value("${doku.nagerageCodeVa}")
	public String dokuNagerageCodeVa;

	@Value("${doku.directPaymentUrl}")
	public String dokuDirectPaymentUrl;

	@Value("${doku.statusCheckUrl}")
	public String dokuStatusCheckUrl;

	@Value("${doku.payCodeDuration}")
	public Integer dokuPayCodeDuration;

	@Value("${huanxin.orgName}")
	public String huanxinOrgName;

	@Value("${huanxin.appName}")
	public String huanxinAppName;

	@Value("${huanxin.host}")
	public String huanxinHost;

	@Value("${huanxin.clientId}")
	public String huanxinClientId;

	@Value("${huanxin.clientSecret}")
	public String huanxinClientSecret;

	@Value("${huanxin.buyerAccountPrefix}")
	public String huanxinBuyerAccountPrefix;

	@Value("${huanxin.sellerAccountPrefix}")
	public String huanxinSellerAccountPrefix;

	@Value("${huanxin.password}")
	public String huanxinPassword;

	@Value("${mail.from}")
	public String mailFrom;

	@Value("${mail.auth}")
	public String mailAuth;

	@Value("${mail.host}")
	public String mailHost;

	@Value("${whitelist.mobile}")
	public String whiteListMobile;

	@Value("${order.receiveTime}")
	public Integer orderReceiveTime;

	@Value("${order.cancelTime}")
	public Integer orderCancelTime;

	@Value("${sms.url}")
	public String smsUrl;

	@Value("${sms.key}")
	public String smsKey;

	@Value("${sms.userName}")
	public String smsUserName;

	@Value("${solr.url}")
	public String solrUrl;

	@Value("${system.machineId}")
	public Integer systemMachineId;

	@Value("${system.serverId}")
	public Integer systemServerId;

	@Value("${system.serverUrl}")
	public String systemServerUrl;

	@Value("${system.fileServerUrl}")
	public String systemFileServerUrl;

	@Value("${system.fileServerRoot}")
	public String systemFileServerRoot;
}
