package com.socool.soft.push;

import com.socool.soft.bo.PushData;
import com.socool.soft.util.ThreadPoolUtil;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 谷歌FCM推送
 */
@Component
public class FCMClient {
    private static final String PUSH_URL = "https://fcm.googleapis.com/fcm/send";

    // 买家serverKey
    @Value("${fcm.buyerServerKey}")
    private String buyerServerKey;

    // 卖家serverKey
    @Value("${fcm.merchantServerKey}")
    private String merchantServerKey;

    private void push(final String serverKey, final String title, final String text, final String data, final String cid) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                // 设置请求头
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "key=" + serverKey);
                // 推送请求体json参数
                JSONObject pushRequest = new JSONObject();
                if(StringUtils.isNotBlank(title) || StringUtils.isNotBlank(text)) {
                    // 推送通知
                    JSONObject notification = new JSONObject();
                    notification.put("title", title);
                    notification.put("body", text);
                    pushRequest.put("notification", notification);
                }
                if(data != null) {
                    // 推送透传消息
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("json", data);
                    pushRequest.put("data", jsonObject);
                }

                // 设置接收者id
                pushRequest.put("to", cid);
                HttpUtil.postJsonData(PUSH_URL, headers, pushRequest.toString());
            }
        });
    }

    /**
     * 推送买家通知
     * @param title
     * @param text
     * @param data
     */
    public void pushBuyerNotice(String title, String text, String data, String cid) {
        push(buyerServerKey, title, text, data, cid);
    }

    /**
     * 推送买家透传消息
     * @param data
     */
    public void pushBuyerTransMsg(String data, String cid) {
        push(buyerServerKey, null, null, data, cid);
    }

    /**
     * 推送卖家通知
     * @param title
     * @param text
     * @param data
     */
    public void pushMerchantNotice(String title, String text, String data, String cid) {
        push(merchantServerKey, title, text, data, cid);
    }

    /**
     * 推送卖家透传消息
     * @param data
     */
    public void pushMerchantTransMsg(String data, String cid) {
        push(merchantServerKey, null, null, data, cid);
    }

    public static void main2(String [] args) {
        FCMClient fcm = new FCMClient();
        PushData data = new PushData();
        data.setOrderId(123123123L);
        data.setStoreName("名字");
        data.setTime(new Date());

//        fcm.pushBuyerTransMsg(data,"cid");
        String id = "eJYDBOzY9C4:APA91bFEP1veR68TXqUeh8bcr4iY8J4VVMkbaOsiQ0CmFIch0zijxVHzaNf7ZFw7buJwNdXEujqXUCB7ZLoOw_2fQvQcRLbkR5-vjExluAZzwxAChbpZ70iWXz58dd6O7Fsa-XBGhVJa";
        System.out.println(id.length());
        String json = "";
    }

}
