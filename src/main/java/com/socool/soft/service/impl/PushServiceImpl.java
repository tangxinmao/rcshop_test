package com.socool.soft.service.impl;

import com.socool.soft.bo.*;
import com.socool.soft.dao.*;
import com.socool.soft.exception.ErrorCode;
import com.socool.soft.exception.SystemException;
import com.socool.soft.push.FCMClient;
import com.socool.soft.service.IProdSkuService;
import com.socool.soft.service.IPushService;
import com.socool.soft.service.IUserGetuiService;
import com.socool.soft.util.JSONObjectUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PushServiceImpl implements IPushService {
    private Logger log = Logger.getLogger(getClass());

    // 推送给买家端
    private static byte PUSH_BUYER = 1;
    // 推送给卖家端
    private static byte PUSH_MERCHANT = 2;

//    @Autowired
//    private GetuiMerchantClient getuiMerchantClient;
//    @Autowired
//    private GetuiBuyerClient getuiBuyerClient;

    @Autowired
    private FCMClient fcmClient;
    @Autowired
    private IUserGetuiService userGetuiService;
    @Autowired
    private RcMerchantMapper merchantMapper;
    @Autowired
    private RcProdImgMapper prodImgMapper;
    @Autowired
    private RcOrderProdMapper orderProdMapper;
    @Autowired
    private RcOrderProdSkuPropInfoMapper orderProdSkuPropInfoMapper;
    @Autowired
    private IProdSkuService prodSkuService;
    @Autowired
    private RcProdMapper prodMapper;
    @Autowired
    private RcMerchantUserMapper merchantUserMapper;

    /**
     * 推送透传消息
     * @param data 透传内容
     * @param memberId 用户id
     * @throws SystemException
     */
    private void pushTrans(PushData data, Integer memberId, byte pushTarget) throws SystemException {
        String dataJson = JSONObjectUtil.fromObject(data).toString();
        log.info("[1]data: " + dataJson + "  memberId: " + memberId);
        RcUserGetui userCid = userGetuiService.getByMemberId(memberId);
        if(userCid == null) {
            log.error("找不到用户推送关联信息, 用户没有绑定推送(无CID)");
            throw new SystemException(ErrorCode.SYSTEM_ERROR, "无CID");
        }

        try {
            if(pushTarget == PUSH_BUYER) {
                fcmClient.pushBuyerTransMsg(dataJson, userCid.getCid());
            } else if(pushTarget == PUSH_MERCHANT) {
                fcmClient.pushMerchantTransMsg(dataJson, userCid.getCid());
            } else {
                return;
            }
            log.info("push success data: " + dataJson + "  memberId: " + memberId);
        } catch (Exception e) {
            log.error("push error [" + e.getMessage() + "]");
            throw new SystemException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }

    /**
     * 根据订单号获取第一个商品的第一张图片
     * @param orderId
     * @return
     */
    private String getProdImg(Long orderId) {
        // 根据订单编号获取对应的商品
        RcOrderProd orderProdQuery = new RcOrderProd();
        orderProdQuery.setOrderId(orderId);
        List<RcOrderProd> orderProdList = orderProdMapper.select(orderProdQuery);
        if(orderProdList.size() == 0) {
            return null;
        }
        RcProdImg prodImgQuery = new RcProdImg();
        prodImgQuery.setProdId(orderProdList.get(0).getProdId());
        List<RcProdImg> prodImgList = prodImgMapper.select(prodImgQuery);
        if(prodImgList.size() == 0) {
            return null;
        }
        return prodImgList.get(0).getImgUrl();
    }

    /**
     * 通知买家 - 卖家已发货
     */
    @Override
    public void merchantDeliver(RcOrder order) throws SystemException {
        // 设置商户信息
        order.setMerchant(merchantMapper.selectByPrimaryKey(order.getMerchantId()));
        if(order.getMerchant() == null) return;
        PushData data = new PushData();
        data.setType(PushData.Type.DELIVER.get());
        data.setOrderId(order.getOrderId());
        data.setStoreName(order.getMerchant().getName());
        data.setTime(new Date());
        data.setPic(getProdImg(order.getOrderId()));

        pushTrans(data, order.getMemberId(), PUSH_BUYER);
    }

    /**
     * 通知买家 - 订单付款码支付成功
     */
    @Override
    public void codePaymentSuccess(RcOrder order) throws SystemException {
        order.setMerchant(merchantMapper.selectByPrimaryKey(order.getMerchantId()));
        if(order.getMerchant() == null) return;
        PushData data = new PushData();
        data.setType(PushData.Type.CODE_PAYMENT_SUCCESS.get());
        data.setOrderId(order.getOrderId());
        data.setStoreName(order.getMerchant().getName());
        data.setTime(new Date());
        data.setPic(getProdImg(order.getOrderId()));
        pushTrans(data, order.getMemberId(), PUSH_BUYER);
    }

    /**
     * 通知买家 - 订单已被拒绝
     * @param order
     */
    @Override
    public void orderRejective(RcOrder order) throws SystemException {
        order.setMerchant(merchantMapper.selectByPrimaryKey(order.getMerchantId()));
        if(order.getMerchant() == null) return;
        PushData data = new PushData();
        data.setType(PushData.Type.REJECTIVE.get());
        data.setOrderId(order.getOrderId());
        data.setStoreName(order.getMerchant().getName());
        data.setTime(new Date());
        data.setPic(getProdImg(order.getOrderId()));
        pushTrans(data, order.getMemberId(), PUSH_BUYER);
    }

    /**
     * 通知买家 - 订单超时自动取消
     * @param order
     */
    @Override
    public void orderTimeout(RcOrder order) throws SystemException {
        order.setMerchant(merchantMapper.selectByPrimaryKey(order.getMerchantId()));
        if(order.getMerchant() == null) return;
        PushData data = new PushData();
        data.setType(PushData.Type.ORDER_TIMEOUT.get());
        data.setOrderId(order.getOrderId());
        data.setStoreName(order.getMerchant().getName());
        data.setTime(new Date());
        data.setPic(getProdImg(order.getOrderId()));
        pushTrans(data, order.getMemberId(), PUSH_BUYER);
    }

    /**
     * 通知买家 - 付款码即将失效
     * @param order
     */
    @Override
    public void paymentCodeStale(RcOrder order) throws SystemException {
        order.setMerchant(merchantMapper.selectByPrimaryKey(order.getMerchantId()));
        if(order.getMerchant() == null) return;
        PushData data = new PushData();
        data.setType(PushData.Type.PAYMENT_CODE_STALE.get());
        data.setOrderId(order.getOrderId());
        data.setStoreName(order.getMerchant().getName());
        data.setTime(new Date());
        data.setPic(getProdImg(order.getOrderId()));
        pushTrans(data, order.getMemberId(), PUSH_BUYER);
    }

    /**
     * 通知买家 - 订单已自动收货
     * @param order
     */
    @Override
    public void orderAutoReceiving(RcOrder order) throws SystemException {
        order.setMerchant(merchantMapper.selectByPrimaryKey(order.getMerchantId()));
        if(order.getMerchant() == null) return;
        PushData data = new PushData();
        data.setType(PushData.Type.ORDER_AUTO_RECEIVING.get());
        data.setOrderId(order.getOrderId());
        data.setStoreName(order.getMerchant().getName());
        data.setTime(new Date());
        data.setPic(getProdImg(order.getOrderId()));
        pushTrans(data, order.getMemberId(), PUSH_BUYER);
    }

    /**
     * 根据商户id获取商户管理员列表
     * @param merchantId
     * @return
     */
    private List<RcMerchantUser> getMerchantUserList(Integer merchantId) {
        RcMerchantUser merchantUserQuery = new RcMerchantUser();
        merchantUserQuery.setMerchantId(merchantId);
        List<RcMerchantUser> merchantUserList = merchantUserMapper.select(merchantUserQuery);
        return merchantUserList;
    }

    /**
     * 通知卖家 - 新订单
     *
     * @param order
     * @throws SystemException
     */
    @Override
    public void newOrder(RcOrder order) throws SystemException {
        PushData data = new PushData();
        data.setType(PushData.Type.NEW_ORDER.get());
        data.setOrderId(order.getOrderId());
        data.setTime(new Date());
        data.setPic(getProdImg(order.getOrderId()));

        // 根据merchantId获取memberId列表
        List<RcMerchantUser> merchantUserList = getMerchantUserList(order.getMerchantId());
        for(RcMerchantUser mu : merchantUserList) {
            pushTrans(data, mu.getMemberId(), PUSH_MERCHANT);
        }
    }

    /**
     * 通知卖家 - 订单已收货
     *
     * @param order
     * @throws SystemException
     */
    @Override
    public void orderCompleted(RcOrder order) throws SystemException {
        PushData data = new PushData();
        data.setType(PushData.Type.ORDER_COMPLETED.get());
        data.setOrderId(order.getOrderId());
        data.setTime(new Date());
        data.setPic(getProdImg(order.getOrderId()));

        // 根据merchantId获取memberId列表
        List<RcMerchantUser> merchantUserList = getMerchantUserList(order.getMerchantId());
        for(RcMerchantUser mu : merchantUserList) {
            pushTrans(data, mu.getMemberId(), PUSH_MERCHANT);
        }
    }

    /**
     * 通知卖家 - 订单已评价
     *
     * @param order
     * @throws SystemException
     */
    @Override
    public void orderReview(RcOrder order) throws SystemException {
        PushData data = new PushData();
        data.setType(PushData.Type.ORDER_REVIEW.get());
        data.setOrderId(order.getOrderId());
        data.setTime(new Date());
        data.setPic(getProdImg(order.getOrderId()));

        // 根据merchantId获取memberId列表
        List<RcMerchantUser> merchantUserList = getMerchantUserList(order.getMerchantId());
        for(RcMerchantUser mu : merchantUserList) {
            pushTrans(data, mu.getMemberId(), PUSH_MERCHANT);
        }
    }

    /**
     * 通知卖家 - 商品库存不足
     *
     * @param orderProd
     * @throws SystemException
     */
    @Override
    public void prodInsufficient(RcOrderProd orderProd) throws SystemException {
        PushData data = new PushData();
        data.setType(PushData.Type.PROD_INSUFFICIENT.get());
        data.setTime(new Date());
        data.setProdId(orderProd.getProdId());

        // 有sku
        if(!orderProd.getProdSkuId().equals(String.valueOf(orderProd.getProdId()))) {
            RcProdSku prodSku = prodSkuService.findProdSkuByProdSkuId(orderProd.getProdSkuId());
            if(prodSku != null) data.setInventory(prodSku.getInventory());

            // 获取sku属性
            RcOrderProdSkuPropInfo orderProdSkuPropInfoQuery = new RcOrderProdSkuPropInfo();
            orderProdSkuPropInfoQuery.setOrderProdId(orderProd.getOrderProdId());
            List<RcOrderProdSkuPropInfo> skuPropInfoList = orderProdSkuPropInfoMapper.select(orderProdSkuPropInfoQuery);
            data.setSkuPropInfoList(skuPropInfoList);
        }

        // 根据prodId, 获取prod对象
        RcProd prod = prodMapper.selectByPrimaryKey(orderProd.getProdId());
        if(prod == null) {
            return;
        }
        // 根据merchantId获取memberId列表
        List<RcMerchantUser> merchantUserList = getMerchantUserList(prod.getMerchantId());
        for(RcMerchantUser mu : merchantUserList) {
            pushTrans(data, mu.getMemberId(), PUSH_MERCHANT);
        }
    }

    /**
     * 通知卖家 - 订单已支付（所有支付方式）
     *
     * @param order
     * @throws SystemException
     */
    @Override
    public void paySuccess(RcOrder order) throws SystemException {
        PushData data = new PushData();
        data.setType(PushData.Type.PAY_SUCCESS.get());
        data.setOrderId(order.getOrderId());
        data.setTime(new Date());
        data.setPic(getProdImg(order.getOrderId()));

        // 根据merchantId获取memberId列表
        List<RcMerchantUser> merchantUserList = getMerchantUserList(order.getMerchantId());
        for(RcMerchantUser mu : merchantUserList) {
            pushTrans(data, mu.getMemberId(), PUSH_MERCHANT);
        }
    }

}
