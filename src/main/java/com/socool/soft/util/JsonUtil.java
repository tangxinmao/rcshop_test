package com.socool.soft.util;

import com.google.gson.Gson;

public class JsonUtil {

	private static final Gson g = new Gson();
	
	public static String toJson(Object o) {
		return g.toJson(o);
	}
	
	public static Object fromJson(String json, Class<?> clazz) {
		return g.fromJson(json, clazz);
	}
	
	public static void main(String[] args) {
//		RcProd prod = new RcProd();
//		prod.setName("abc");
//		prod.setProdImgs(new ArrayList<RcProdImgRel>());
//		
//		String json = toJson(prod);
//		System.out.println(json);
//		RcProd p = (RcProd)fromJson(json, RcProd.class);
//		System.out.println(p.getAd());
	}
}
