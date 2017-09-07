package com.socool.soft.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

public class JSONObjectUtil {
    private static JsonConfig jsonConfig = new JsonConfig();
    static {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jakarta"));
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
    }

    public static String  fromObject(Object o) {
        return JSONObject.fromObject(o, jsonConfig).toString();
    }

    public static String formCollection(Collection collection) {
        return JSONArray.fromObject(collection, jsonConfig).toString();
    }
}

class JsonDateValueProcessor implements JsonValueProcessor {
    private String format = "yyyy-MM-dd HH:mm:ss";
    public JsonDateValueProcessor(){}
    public JsonDateValueProcessor(String format) {
        this.format = format;
    }

    @Override
    public Object processArrayValue(Object value, JsonConfig paramJsonConfig) {
        return process(value);
    }

    @Override
    public Object processObjectValue(String key, Object value, JsonConfig paramJsonConfig) {
        return process(value);
    }

    private Object process(Object value) {
        if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(value);
        }
        return value == null ? "" : value.toString();
    }
}