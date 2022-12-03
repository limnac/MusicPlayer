package com.limnac.musicplayer.utils;

import java.util.Arrays;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/12/3 19:15
 * @description com.limnac.musicplayer.utils
 */
public class StringUtil {

    private static final String TAG = "StringUtil";

    public static String removeFileClassName(String name){
        if(name.contains(".")){
            String[]  ss = name.split("\\.");
            return ss[0];
        }

        return name;
    }
}
