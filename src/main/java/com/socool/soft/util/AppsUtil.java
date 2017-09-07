package com.socool.soft.util;



import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by zaki on 3/28/16.
 */
public class AppsUtil {

	public static final String DATE_FORMAT_DOKU = "yyyyMMddHHmmss";
	
    public static String SHA1(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] sha1hash = new byte[40];
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String generateMoneyFormat(String number) {
        double amount = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("####.00");
        return formatter.format(amount);
    }


    public static int nDigitRandomNo(int digits) {
        int max = (int) Math.pow(10, (digits)) - 1; //for digits =7, max will be 9999999
        int min = (int) Math.pow(10, digits - 1); //for digits = 7, min will be 1000000
        int range = max - min; //This is 8999999
        Random r = new Random();
        int x = r.nextInt(range);// This will generate random integers in range 0 - 8999999
        int nDigitRandomNo = x + min; //Our random rumber will be any random number x + min
        return nDigitRandomNo;
    }
    
    /**
	 * 格式化时间
	 * @param date
	 * @return
	 */
	public static String dokuDateFormat(Date date) {
		if(date==null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_DOKU);
		return df.format(date);
	}

	public static void main(String[] args) {
		System.out.println(SHA1(dokuDateFormat(new Date())));
	}
}
