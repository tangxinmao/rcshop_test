package com.socool.soft.exception;

public enum ErrorValue {
	NOT_IDENTIFIED(10000), ERROR_PASSWORD_OR_USER_NAME(10001), ERROR_VERIFICATION_CODE(10002), ACCOUNT_ALREADY_EXISTS(
			10003), // Account already exists
	VERIFICATION_CODE_FAILURE(10004), // Verification code failure
	ACCOUNT_NOT_EXISTS(10027), // 账号不存在
	PICTURE_BEYOND_LIMIT(10005), // Picture beyond limit
	ERROR_PASSWORD(10006), INSUFFICIENT_AMOUNT(10007), // Insufficient amount
	UNDER_STOCK(10008), // Under stock
	GOODS_SHELF(10009), // goods shelf
	ORDER_HAS_BEEN_PAID(10010), // Order has been paid
	SYSTEM_BUSY(10011), // System busy
	GOODS_DELETED(10012), // goods deleted
	PAYMENT_PASSWORD_NOT_SET(10013), // Payment password not set
	DELIVERY_ERROR(10014), // Delivery error
	COUPON_HAS_BEEN_COMPLETED(10015), // Coupon has been completed
	COUPON_DOES_NOT_EXIST(10016), // Coupon does not exist
	COUPON_ALREADY_RECEIVE(10017), // Coupon Already receive
	CITY_ONT_OPEN_ERROR(10018), // city not open
	ORDER_HAS_BEEN_EVALUATED(10019), // Order has been evaluated
	GOODS_NOT_SHIPPED(10020), // goods not shipped
	ORDER_HAS_CANCELLED(10021), // order has cancelled
	FREIGHT_ERROR(10022), // Freight error
	ERROR_PHONENUMBER(10023), // Freight error
	NOT_GET_PICTURE(10024), // 为获取到图片
	PICTURE_FORMAT_ERROR(10025), // 图片格式错误
	NOT_GET_PICTURE_SIZE(10026),//图片尺寸未获取到
	ORDER_PAID(10028),//订单已支付
	ORDER_CANCELED(10029),//订单已取消
	ORDER_NOT_EXISTS(10030);//订单不存在
	private int value;

	ErrorValue(int value) {
		this.value = value;
	}

	public int getValue() {

		return this.value;
	}

	public String getStr() {

		return "" + this.value;
	}

}
