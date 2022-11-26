package com.limnac.musicplayer.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.limnac.musicplayer.fragments.ListFragment;
import com.limnac.musicplayer.model.Song;
import com.limnac.musicplayer.utils.LogUtil;

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

    private String mTAG = "PlayService";
    private static MediaPlayer mMediaPlayer;
    private List<Song> mSongList;
    private int mPlayModel = 0;
    private int mPosition = 0;

    public class MyBinder extends Binder{
        public PlayService getService(){
            return PlayService.this;
        }
    }

    private MyBinder binder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e(mTAG,"PlayService is onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e(mTAG,"PlayService is onStartCommmand");
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.e(mTAG,"PlayService is onBind");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.e(mTAG,"PlayService is onUnbind");
        return false;
    }

    @Override
    public void onDestroy() {
        LogUtil.e(mTAG,"PlayService is onDestroy");
        super.onDestroy();
    }

    public void setPlayModel(int repeat_count){
        if(repeat_count>=0&&repeat_count<=3){
            mPlayModel = repeat_count;
        }
    }

    public void playNewMusic(int pos){
        this.mPosition = pos;
        mSongList = ListFragment.getSongList();

        if(mMediaPlayer==null){
            mMediaPlayer = new MediaPlayer();
        }else{
            mMediaPlayer.reset();
        }

        LogUtil.e(mTAG,"歌曲地址："+mSongList.get(mPosition).getPath());

        try {
            mMediaPlayer.setDataSource(mSongList.get(mPosition).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.prepareAsync();

        mMediaPlayer.setOnPreparedListener(mMediaPlayer -> mMediaPlayer.start());

        mMediaPlayer.setOnCompletionListener(mediaPlayer -> {
            switch (mPlayModel){
                case 1:
                    playNewMusic(mPosition);
                    break;
                case 2:
                    Random random = new Random();
                    int i = random.nextInt(mSongList.size());
                    playNewMusic(i);
                    break;
                default:
                    nextMusic();
                    break;
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
            if(mPosition == mSongList.size()-1){
                mPosition = 0;
            }else{
                mPosition++;
            }
            playNewMusic(mPosition);
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
}
