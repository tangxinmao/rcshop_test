package com.socool.soft.task;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.socool.soft.bo.RcMerchant;
import com.socool.soft.bo.RcMerchantWid;
import com.socool.soft.bo.constant.MerchantWidStatusEnum;
import com.socool.soft.service.IMerchantService;
import com.socool.soft.service.IMerchantWidService;
import com.socool.soft.service.IOrderService;

//@EnableScheduling
//@Component
public class WithdrawlTask {
	
	@Autowired
	private IMerchantService merchantService;
	
	@Autowired
	private IMerchantWidService withdrawlService;
	
	@Autowired
	private IOrderService orderService;
	

	/**
	 * 每月1日凌晨1点生成上月报表
	 * @throws ParseException
	 */
	@Scheduled(cron="0 0 1 1 * ? ")
	public void generate() throws ParseException{
		Calendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		if(month == 1){
			year ++;
			month = 12;
		}
		else{
			month =month-1;
		}
		String calTimeStr = year + "-" + month;
//		DateFormat format1 = new SimpleDateFormat("yyyy-MM");
//		Date calTime = format1.parse(calTimeStr);
		List<RcMerchant> merchants = merchantService.findAllEnabledMerchants();
		for(RcMerchant merchant:merchants){
			RcMerchantWid param = new RcMerchantWid();
			param.setMerchantId(merchant.getMerchantId());
			param.setBalanceMonth(calTimeStr);
			RcMerchantWid merchantWid = withdrawlService.findMerchantWid(param);
			if(merchantWid != null && merchantWid.getStatus() != MerchantWidStatusEnum.PENDING.getValue() && merchantWid.getStatus() != MerchantWidStatusEnum.UNCONFIRMED.getValue()){
				continue;
			}
			
			DateFormat format = new SimpleDateFormat("yyyy-MM");
			Date createTimeFrom = format.parse(calTimeStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(createTimeFrom);
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.SECOND, -1);
			Date createTimeTo = cal.getTime();
			BigDecimal applyAmount = orderService.statisticOrderPayPrice(merchant.getMerchantId(), createTimeFrom, createTimeTo);
			
			if(merchantWid != null) {
				merchantWid.setApplyAmount(applyAmount);
				merchantWid.setBankName(merchant.getBankName());
				merchantWid.setBankAccount(merchant.getBankAccount());
				merchantWid.setBankAccountName(merchant.getBankAccountName());
				merchantWid.setStatus(MerchantWidStatusEnum.PENDING.getValue());
				merchantWid.setApplyTime(new Date());
				withdrawlService.modifyMerchantWid(merchantWid);
			} else {
				merchantWid = new RcMerchantWid();
				merchantWid.setMerchantId(merchant.getMerchantId());
				merchantWid.setApplyAmount(applyAmount);
				merchantWid.setBankName(merchant.getBankName());
				merchantWid.setBankAccount(merchant.getBankAccount());
				merchantWid.setBankAccountName(merchant.getBankAccountName());
				merchantWid.setStatus(MerchantWidStatusEnum.PENDING.getValue());
				merchantWid.setApplyTime(new Date());
				merchantWid.setBalanceMonth(calTimeStr);
				withdrawlService.addMerchantWid(merchantWid);
			}
		}
	}
}
