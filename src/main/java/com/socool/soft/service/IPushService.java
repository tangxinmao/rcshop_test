package com.socool.soft.service;

import com.socool.soft.bo.RcOrder;
import com.socool.soft.bo.RcOrderProd;
import com.socool.soft.exception.SystemException;

public interface IPushService {
    /**
     * 通知买家 - 卖家已发货
     */
    void merchantDeliver(RcOrder order) throws SystemException;

    /**
     * 通知买家 - 订单付款码支付成功
     */
    void codePaymentSuccess(RcOrder order) throws SystemException;

    /**
     * 通知买家 - 订单已被拒绝
     * @param order
     */
    void orderRejective(RcOrder order) throws SystemException;

    /**
     * 通知买家 - 订单超时自动取消
     * @param order
     */
    void orderTimeout(RcOrder order) throws SystemException;

    /**
     * 通知买家 - 付款码即将失效
     * @param order
     */
    void paymentCodeStale(RcOrder order) throws SystemException;

    /**
     * 通知买家 - 订单已自动收货
     * @param order
     */
    void orderAutoReceiving(RcOrder order) throws SystemException;


    //==================卖家端=================//
    /**
     * 通知卖家 - 新订单
     * @param order
     * @throws SystemException
     */
    void newOrder(RcOrder order) throws SystemException;

    /**
     * 通知卖家 - 订单已收货
     * @param order
     * @throws SystemException
     */
    void orderCompleted(RcOrder order) throws SystemException;

    /**
     * 通知卖家 - 订单已评价
     * @param order
     * @throws SystemException
     */
    void orderReview(RcOrder order) throws SystemException;

    /**
     * 通知卖家 - 商品库存不足
     * @param orderProd
     * @throws SystemException
     */
    void prodInsufficient(RcOrderProd orderProd) throws SystemException;

    /**
     * 通知卖家 - 订单已支付（所有支付方式）
     * @param order
     * @throws SystemException
     */
    void paySuccess(RcOrder order) throws SystemException;
}
