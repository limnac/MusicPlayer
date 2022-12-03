package com.limnac.musicplayer.data;

import android.content.Context;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/12/3 16:36
 * @description com.limnac.musicplayer.data
 */
public class MusicConfig {

    private static final String TAG = "MusicConfig";

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        MusicConfig.mContext = mContext;
    }
}
