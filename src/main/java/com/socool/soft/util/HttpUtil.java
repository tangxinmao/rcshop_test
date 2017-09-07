package com.socool.soft.util;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.socool.soft.vo.IMNode;


public class HttpUtil {

	public static IMNode request(String url, int method,String token, String param) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try{
			HttpResponse response = null;
			switch(method){
				case 1:
					HttpGet get = new HttpGet(url);
					if(StringUtils.isNotBlank(token)){
						get.addHeader("Authorization","Bearer "+token.toString());
					}
					get.addHeader("Content-Type","application/json");
					response = client.execute(get);
					break;
				case 2:
					HttpPost post = new HttpPost(url);
					if(param!=null){
						post.setEntity(new StringEntity(param,"UTF-8"));
					}
					post.addHeader("Content-Type","application/json");
					if(StringUtils.isNotBlank(token)){
						post.addHeader("Authorization","Bearer "+token.toString());
					}
					response = client.execute(post);
					break;
				case 3:
					HttpPut put = new HttpPut(url);
					if(param!=null){
						put.setEntity(new StringEntity(param,"UTF-8"));
					}
					if(StringUtils.isNotBlank(token)){
						put.addHeader("Authorization","Bearer "+token.toString());
					}
					put.addHeader("Content-Type","application/json");
					response = client.execute(put);
					break;
				case 4:
					HttpDelete delete = new HttpDelete(url);
					if(StringUtils.isNotBlank(token)){
						delete.addHeader("Authorization","Bearer "+token.toString());
					}
					delete.addHeader("Content-Type","application/json");
					response = client.execute(delete);
					break;
				default:throw new Exception("非法请求方式");
			}
			int code = response.getStatusLine().getStatusCode();
			System.out.println("request url = "+url+", statusCode = "+code+".");
			if(code==HttpStatus.SC_OK){
				HttpEntity entity = response.getEntity();
				if(entity!=null){
					String json = EntityUtils.toString(entity,"UTF-8");
					IMNode imNode = (IMNode)JsonUtil.fromJson(json, IMNode.class);
					imNode.setCode(HttpStatus.SC_OK);
					return imNode;
				}
			}else{
				IMNode imNode = new IMNode(code);
				return imNode;
			}
		}catch(Exception e){
			throw e;
		}finally{
			client.close();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
//		String url = RestfulConstants.HUANXIN_HOST+"/token";
//		Map<String,String> param = new HashMap<String, String>();
//		param.put("grant_type", "client_credentials");
//		param.put("client_id", RestfulConstants.HUANXIN_CLIENT_ID);
//		param.put("client_secret", RestfulConstants.HUANXIN_CLIENT_SECRET);
//		String paramJson = JsonUtil.toJson(param);
//		IMNode imNode = request(url, 2, null, paramJson);
//		String token = "YWMtAQ5RVFfEEeeBQp8AuVSuHwAAAAAAAAAAAAAAAAAAAAGjKiwgP5AR57Pdh__eezYYAgMAAAFc0v5MhQBPGgA74_3ptjWc_-l9bGoLUmFUO1QCTk_SIzMyHK39WMo9XQ";
//		String url = RestfulConstants.HUANXIN_HOST+"/users";
//		Map<String,String> param = new HashMap<String, String>();
//		param.put("username", "testbyYH5");
//		param.put("password", RestfulConstants.HUANXIN_PASSWORD);
//		param.put("nickname", "testNickname5");
//		String paramJson = JsonUtil.toJson(param);
//		IMNode imNode = request(url, 2, token, paramJson);
//		System.out.println(JsonUtil.toJson(imNode));
	}
}
