package com.limnac.musicplayer;

import android.app.Application;

import com.drake.logcat.LogCat;
import com.limnac.musicplayer.data.congfig.MusicConfig;
import com.tencent.mmkv.MMKV;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/12/3 16:35
 * @description com.limnac.musicplayer
 */
public class MusicApplication extends Application {
    private static final String TAG = "MusicApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        initLogCat();
        MMKV.initialize(this);
        MusicConfig.getInstance().setContext(getApplicationContext());
    }

    private void initLogCat() {
        LogCat.INSTANCE.setDebug(true,"LogCat");
        LogCat.INSTANCE.setTraceEnabled(false);
    }
}
