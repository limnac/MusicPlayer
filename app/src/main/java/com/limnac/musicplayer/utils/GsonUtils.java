package com.limnac.musicplayer.utils;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author chuanghu
 * @email chuanghu@iflytek.com
 * @date 2023/1/12 16:20
 * @description
 */
public class GsonUtils {
    private static final String TAG = "EDUUpdateJsonUtil";

    /**
     * @param json Json字符串
     * @param typeOfT 对象类型
     * @return T Javabean对象
     * @Title: fromJson
     * @Description: 将Json字符串转成javabean对象
     */
    public static <T> T fromJson(String json, Type typeOfT){
        if(json==null){
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json,typeOfT);
    }

    /**
     * @param json Json字符串
     * @param clz Javabean对象的Class
     * @return T Javabean对象
     * @Title: fromJson
     * @Description: 将Json字符串转成javabean对象
     */
    public static <T> T fromJson(String json,Class<T> clz){
        if(json==null){
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json,clz);
    }

    /**
     * @param json JSONObject对象
     * @param clz Javabean对象的Class
     * @return T Javabean对象
     * @Title: fromJson
     * @Description: 将JSONObject对象转成javabean对象
     */
    public static <T> T fromJson(JSONObject json, Class<T> clz){
        if(json == null){
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json.toString(),clz);
    }

    /**
     * @param obj Javabean对象
     * @return String json字符串
     * @Title: toJson
     * @Description: 将javabean对象转成json字符串
     */
    public static String toJson(Object obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}
