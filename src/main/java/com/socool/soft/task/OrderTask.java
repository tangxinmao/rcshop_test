package com.socool.soft.task;

import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.exception.SystemException;
import com.socool.soft.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@EnableScheduling
@Component
public class OrderTask {
	/**
	 * 订单自动失效
	 * 
	 * @param a
	 */
	@Autowired
	private IOrderService orderService;
	@Autowired
	private PropertyConstants propertyConstants;
	/**
	 * 自动失效订单
	 * 
	 * @throws IOException
	 * @throws SystemException 
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Scheduled(fixedRate = 60000)
	public void workOrderInvalidTime() throws IOException, SystemException {
		orderService.updateOrderInvalidTime(propertyConstants.orderCancelTime);

	}

	/**
	 * 自动确认收货
	 * 
	 * @throws IOException
	 * @throws SystemException 
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Scheduled(fixedRate =60000)
	public void workOrderCloseTime() throws IOException, SystemException  {
		orderService.updateOrderCloseTime(propertyConstants.orderReceiveTime);
	}

}
