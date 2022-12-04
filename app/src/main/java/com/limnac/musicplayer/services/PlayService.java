package com.limnac.musicplayer.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.limnac.musicplayer.bean.MusicBean;
import com.limnac.musicplayer.model.Song;
import com.limnac.musicplayer.utils.LogUtil;
import com.limnac.musicplayer.utils.MusicUtil;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 13:07
 * @description com.limnac.musicplayer.services
 */
public class PlayService extends Service {

    private static final String TAG = "PlayService";

    private static MediaPlayer mMediaPlayer;
    private List<Song> mSongList;
    private int mPlayModel = 0;
    private int mPosition = 0;
    private Messenger mActivityMessenger;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == MusicBean.MSG_BIND) {
                mActivityMessenger = msg.replyTo;
            }

        }
    };

    private final Messenger mServiceMessenger = new Messenger(mHandler);

    public class MyBinder extends Binder{

        public PlayService getService(){
            return PlayService.this;
        }

        public Messenger getMessenger(){
            return mServiceMessenger;
        }

    }

    private final MyBinder binder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(TAG,"PlayService is onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i(TAG,"PlayService is onStartCommmand");
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.i(TAG,"PlayService is onBind");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.e(TAG,"PlayService is onUnbind");
        return false;
    }

    @Override
    public void onDestroy() {
        LogUtil.e(TAG,"PlayService is onDestroy");
        super.onDestroy();
    }

    public void setPlayModel(int repeat_count){
        if(repeat_count>=0&&repeat_count<=3){
            mPlayModel = repeat_count;
        }
    }

    public void playNewMusic(int pos){
        this.mPosition = pos;
        mSongList = MusicUtil.getSongList();
        sendMessageUpdateUI();

        if(mMediaPlayer==null){
            mMediaPlayer = new MediaPlayer();
        }else{
            mMediaPlayer.reset();
        }


        try {
            mMediaPlayer.setDataSource(mSongList.get(mPosition).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.prepareAsync();

        mMediaPlayer.setOnPreparedListener(MediaPlayer::start);

        mMediaPlayer.setOnCompletionListener(mediaPlayer -> {
            if(mPlayModel==MusicBean.REPEAR_ONCE_MODEL){
                playNewMusic(mPosition);
            }else{
                nextMusic();
            }

        });
    }

    public void playMusic(){
        if(mMediaPlayer!=null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
            }else{
                mMediaPlayer.start();
            }
        }else{
            playNewMusic(0);
        }

    }

    public void nextMusic(){
        if(mMediaPlayer!=null){
            if(mPlayModel==MusicBean.SHUFFLE){
                Random random = new Random();
                int i = (1+random.nextInt(mSongList.size()-1)+ mPosition) % mSongList.size();
                playNewMusic(i);
            }else{
                if(mPosition == mSongList.size()-1){
                    mPosition = 0;
                }else{
                    mPosition++;
                }
                playNewMusic(mPosition);
            }
        }
    }

    public void preMusic(){
        if(mMediaPlayer!=null){
            if(mPosition==0){
                mPosition = mSongList.size()-1;
            }else{
                mPosition--;
            }
            playNewMusic(mPosition);
        }

    }

    public void sendMessageUpdateUI(){
        Message msg = new Message();
        msg.what = MusicBean.UPDATE_UI;
        msg.arg1 = mPosition;
        if(mActivityMessenger!=null){
            try {
                mActivityMessenger.send(msg);
            } catch (Exception e) {
                LogUtil.e(TAG,e.getMessage());
                LogUtil.error(e);
            }
        }

    }

}
