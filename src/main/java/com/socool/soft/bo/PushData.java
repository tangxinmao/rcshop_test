package com.socool.soft.bo;

import java.util.Date;
import java.util.List;

public class PushData {
    // 推送类型
    // ====买家端====
    // 1:卖家已发货 | 2:订单付款码支付成功 | 3:订单已被拒绝 | 4:订单超时自动取消
    // 5:付款码即将失效 | 6:订单已自动收货
    // ====卖家端====
    // 7:新订单 | 8:订单已收货
    // 9:订单已评价 | 10:商品库存不足 | 11:订单已支付（所有支付方式）
    private Byte type;
    // 订单id
    private Long orderId;
    // 商店名称
    private String storeName;
    // 时间(和type对应, 发货时间,支付成功时间,拒绝时间等)
    private Date time;
    // 商品图片
    private String pic;

    //=======商品相关==========//
    // 商品id
    private Integer prodId;
    // 库存
    private Integer inventory;
    // sku属性
    private List<RcOrderProdSkuPropInfo> skuPropInfoList;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public List<RcOrderProdSkuPropInfo> getSkuPropInfoList() {
        return skuPropInfoList;
    }

    public void setSkuPropInfoList(List<RcOrderProdSkuPropInfo> skuPropInfoList) {
        this.skuPropInfoList = skuPropInfoList;
    }

    public enum Type{
        DELIVER((byte)1), // 卖家发货
        CODE_PAYMENT_SUCCESS((byte)2), // 付款码支付成功
        REJECTIVE((byte)3), // 订单拒绝
        ORDER_TIMEOUT((byte)4), // 订单超时取消
        PAYMENT_CODE_STALE((byte)5), // 付款码将失效
        ORDER_AUTO_RECEIVING((byte)6), // 订单自动收货

        NEW_ORDER((byte)7), // 新订单
        ORDER_COMPLETED((byte)8), // 订单已收货
        ORDER_REVIEW((byte)9), // 订单已评价
        PROD_INSUFFICIENT((byte)10), // 商品库存不足
        PAY_SUCCESS((byte)11), // 订单已支付(所有支付方式)

        UNKNOWN((byte)99);
        private byte type;
        Type(byte type){
            this.type = type;
        }
        public byte get() {
            return type;
        }
    }
}
