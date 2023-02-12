package com.limnac.musicplayer.data.constant.errorcode;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author chuanghu
 * @email chuanghu@iflytek.com
 * @date 2023/1/10 14:03
 * @description
 */
@Retention(SOURCE)
@Target({PARAMETER})
@IntDef(value = {
        UpdateErrorCode.GET_SONG_LIST_EXCEPTION,
        UpdateErrorCode.NO_SONG
})
public @interface UpdateErrorCode {
    /**
     * 获取本地音乐出错
     */
    int GET_SONG_LIST_EXCEPTION = 1000;
    /**
     * 本地音乐不存在
     */
    int NO_SONG = 1001;
}
