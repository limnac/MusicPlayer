package com.limnac.musicplayer.data.congfig;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/12/3 16:36
 * @description com.limnac.musicplayer.data
 */
public class MusicConfig {

    @SuppressLint("StaticFieldLeak")
    private static MusicConfig instance;
    private MusicConfig(){

    }
    public static synchronized MusicConfig getInstance(){
        if(instance==null){
            instance = new MusicConfig();
        }
        return instance;
    }

    private Context mContext;

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }
}
