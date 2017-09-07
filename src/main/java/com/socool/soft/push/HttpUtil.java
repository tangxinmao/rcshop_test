package com.socool.soft.push;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送 http / https 请求
 *
 */
public class HttpUtil {
	private static final String DEFALUT_CHARSET = "UTF-8";

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return
	 * @author fb
	 * @date 2017年3月9日上午10:23:25
	 */
	public static String get(String url) {
		return get(url, DEFALUT_CHARSET, null);
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @param headers
	 * @return
	 * @author fb
	 * @date 2017年3月9日上午10:39:27
	 */
	public static String get(String url, Map<String, String> headers) {
		return get(url, DEFALUT_CHARSET, headers);
	}

	/**
	 * 发送post请求
	 * @param url
	 * @param params
	 * @return
	 * @author fb
	 * @date 2017年3月9日上午10:54:54
	 */
	public static String post(String url, Map<String, String> params) {
		return post(url, DEFALUT_CHARSET, params, null);
	}

	/**
	 * 发送post请求
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @author fb
	 * @date 2017年3月9日上午10:55:00
	 */
	public static String post(String url, Map<String, String> params, Map<String, String> headers) {
		return post(url, DEFALUT_CHARSET, params, headers);
	}

	
	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @param charset
	 * @return
	 * @author fb
	 * @date 2017年3月9日上午10:23:35
	 */
	public static String get(String url, String charset, Map<String, String> headers) {
		try {
			HttpClient httpClient = HttpClients.createDefault();
			HttpGet get = new HttpGet(url);
			// 设置请求头
			if (headers != null) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					get.setHeader(entry.getKey(), entry.getValue());
				}
			}
			// 发送请求,获取返回值
			HttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			String res = null;
			if (entity != null) {
				// 将返回值toString
				res = EntityUtils.toString(entity, charset);
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param charset
	 * @param params
	 * @param headers
	 * @return
	 * @author fb
	 * @date 2017年3月9日上午10:53:31
	 */
	public static String post(String url, String charset, Map<String, String> params, Map<String, String> headers) {
		try {
			HttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			if (params != null) {
				// 创建参数队列
				List<BasicNameValuePair> paramList = new ArrayList<BasicNameValuePair>();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				post.setEntity(new UrlEncodedFormEntity(paramList, DEFALUT_CHARSET));
			}

			// 设置请求头
			if (headers != null) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					post.setHeader(entry.getKey(), entry.getValue());
				}
			}
			// 发送请求,获取返回值
			HttpResponse response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			String res = null;
			if (entity != null) {
				// 将返回值toString
				res = EntityUtils.toString(entity, charset);
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

    /**
     * 发送post + json 请求
     * @param url
     * @param headers
     * @param jsonData
     * @return
     */
    public static String postJsonData(String url, Map<String, String> headers, String jsonData) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            // 设置请求头
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    post.setHeader(entry.getKey(), entry.getValue());
                }
            }
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(jsonData, DEFALUT_CHARSET));
            // 发送请求,获取返回值
            HttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            String res = null;
            if (entity != null) {
                // 将返回值toString
                res = EntityUtils.toString(entity, DEFALUT_CHARSET);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	public static void main(String[] args) throws Exception {
        String url = "https://fcm.googleapis.com/fcm/send";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "key=AAAA8w4zUUA:APA91bFI3PzqNKeEs2jMydvxr_t0rtQIGL2NO76H4zuz6Tr0_94Lq2Kq_ycBfBxobXcsEsOxFE3Iu0JYw3p513q2lsDdt8clq-cYEX6Lq9LGoWeCEqHH-cxnV4oVxicgkiNmD6U-C26p");
        JSONObject jsonObject = new JSONObject();
        JSONObject notification = new JSONObject();
        notification.put("title", "hello");
        notification.put("body", "00");
        jsonObject.put("notification", notification);

        String xmId = "eJYDBOzY9C4:APA91bFEP1veR68TXqUeh8bcr4iY8J4VVMkbaOsiQ0CmFIch0zijxVHzaNf7ZFw7buJwNdXEujqXUCB7ZLoOw_2fQvQcRLbkR5-vjExluAZzwxAChbpZ70iWXz58dd6O7Fsa-XBGhVJa";
        jsonObject.put("to", xmId);

        String res = HttpUtil.postJsonData(url, headers, jsonObject.toString());
        System.out.println(res);
	}

}
