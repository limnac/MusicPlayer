package com.limnac.musicplayer.callback;

import com.limnac.musicplayer.data.constant.errorcode.UpdateErrorCode;

/**
 * @author chuanghu
 * @email chuanghu@iflytek.com
 * @date 2023/1/10 11:13
 * @description
 */
public interface UpdateCallBack {
    /**
     * 更新歌曲列表成功
     */
    void success();

    /**
     * 更新歌曲列表失败
     */
    void onFailed(@UpdateErrorCode int errorCode, String msg);
}
