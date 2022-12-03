package com.limnac.musicplayer;

import android.app.Application;

import com.limnac.musicplayer.data.MusicConfig;

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
        MusicConfig.setContext(getApplicationContext());
    }
}
