package com.socool.soft.controller;

import com.socool.soft.constant.Constants;
import com.socool.soft.util.AESUtil;
import net.sf.json.JSONObject;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

public class BaseController {
//	  public static Logger logger = LogManager.getLogger(BaseController.class.getName());
//	 /**
//     * 初始化分页
//     * @param currentPage 当前页码
//     * @param pagesize 每页总条数
//     * @return
//     */
//    public Page initPage2(String currentPage,int pagesize){
//    	if(currentPage == null || currentPage.equals("")){
//			currentPage = "1";
//		}
//		// 分页
//		Page page = new Page();
//		//初始化时第一页
//		page.setPagination(true);
//		page.setPageSize(pagesize);
//		page.setCurrentPage(Integer.parseInt(currentPage));
//		PageContext.setPage(page);
//		return page;
//    }
//    
//    /**
//     * 默认分页为10条的初始化分页
//     * @param currentPage 当前页码
//     * @return
//     */
//    public Page initPage2(String currentPage){
//    	return initPage2(currentPage,10);
//    }
	/**
	 * 获取文件后缀名
	 * @param fileName
	 * @return
	 */
	public String getExt(String fileName) {
		int pos = fileName.lastIndexOf(".");
		if (pos == -1)
			return "";
		return fileName.substring(pos + 1, fileName.length());
	}
	
	public Date StringToDate(String dateStr){
		DateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return date;
	}
	public static JSONObject parseParam(String data,String timestamp,String nonceStr,String product,String signature){
		String token = "";
		String string1 = "data="+data+"&timestamp="+timestamp+"&nonceStr="+nonceStr+"&product="+product;
		try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            token = byteToHex(crypt.digest());
            if(token.equals(signature.toLowerCase())){
            	JSONObject json = JSONObject.fromObject(AESUtil.Decrypt(data));
            	return  json;
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("======the data is :"+data+",timestamp is:"+timestamp+",nonceStr is :"+nonceStr+"product is :"+product+"signature"+signature);
			e.printStackTrace();
		}
		return null;
	}
	
	public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }	
	
	public void setData(Model model,String[] keys,Object[] values) {
		if (keys != null && values != null && keys.length == values.length) {
			for (int i = 0, len = keys.length; i < len; i++) {
				model.addAttribute(keys[i], values[i]);
			}
		}
	}

//	public Integer getMemberId(HttpServletRequest request){
//		return (Integer)request.getSession().getAttribute(Constants.MEMBER_ID_IN_SESSION_KEY);
//	}
//	
//	public RcMember getMember(HttpServletRequest request){
//		return (RcMember)request.getSession().getAttribute(Constants.MEMBER_INFO_IN_SESSION_KEY);
//	}
//	
//	public RcMerchant getMerchant(HttpServletRequest request){
//		return (RcMerchant)request.getSession().getAttribute(Constants.MERCHANT_INFO_IN_SESSION_KEY);
//	}
//	
//	public RcVendor getVendor(HttpServletRequest request){
//		return (RcVendor)request.getSession().getAttribute(Constants.VENDOR_INFO_IN_SESSION_KEY);
//	}
	
	public Integer getUserId(HttpServletRequest request){
		return (Integer)request.getSession().getAttribute(Constants.USER_ID_IN_SESSION_KEY);
	}
	
	public Integer getMerchantUserId(HttpServletRequest request){
		return (Integer)request.getSession().getAttribute(Constants.MERCHANT_USER_ID_IN_SESSION_KEY);
	}

	public Integer getVendorUserId(HttpServletRequest request){
		return (Integer)request.getSession().getAttribute(Constants.VENDOR_USER_ID_IN_SESSION_KEY);
	}
	
	public BigDecimal countSize(long size) {
		BigDecimal mb = new BigDecimal(1024 * 1024);
		return new BigDecimal(size).divide(mb, 2, BigDecimal.ROUND_HALF_UP);
	}
	
/*	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class,
				new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}*/
}
