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

import com.drake.logcat.LogCat;
import com.limnac.musicplayer.data.constant.MusicBean;
import com.limnac.musicplayer.data.constant.PlayStatus;
import com.limnac.musicplayer.data.model.Song;
import com.limnac.musicplayer.manager.MusicManager;

import java.util.List;
import java.util.Random;

/**
 * @author chuanghu
 * @email chuanghu@iflytek.com
 * @date 2023/1/9 20:17
 * @description
 */
public class PlayService extends Service {
    private static final String TAG = "PlayService";

    private static MediaPlayer mMediaPlayer;
    private List<Song> mSongList = MusicManager.getInstance().getList();
    private int mPlayModel = 0;
    private int mPosition = 0;
    private int mPlayStatus = PlayStatus.IN_PAUSE;
    private Messenger mPlayActivityMessenger;
    private Messenger mMainActivityMessenger;
    private Messenger mLryActivityMessenger;
    private Handler mLrySynchronizationHandler = new Handler();

    @SuppressLint("HandlerLeak")
    private final Handler mHandlerToPlayActivity = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == MusicBean.MSG_BIND) {
                mPlayActivityMessenger = msg.replyTo;
                sendMessageUpdateUI();
            }

        }
    };

    private final Messenger mServiceMessengerToPlayActivity = new Messenger(mHandlerToPlayActivity);

    @SuppressLint("HandlerLeak")
    private final Handler mHandlerToMainActivity = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == MusicBean.MSG_BIND) {
                mMainActivityMessenger = msg.replyTo;
                sendMessageUpdateUI();
            }

        }
    };

    private final Messenger mServiceMessengerToMainActivity = new Messenger(mHandlerToMainActivity);

    @SuppressLint("HandlerLeak")
    private final Handler mHandlerToLryActivity = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MusicBean.MSG_BIND:
                    mLryActivityMessenger = msg.replyTo;
                    prepare_lyrics();
                    mLrySynchronizationHandler.post(mRunnable);
                    break;
                case MusicBean.STOP_UPDATE_LYRICS:
                    LogCat.i("停止同步歌词",TAG);
                    mLrySynchronizationHandler.removeCallbacks(mRunnable);
                    break;
                case MusicBean.SET_PLAY_POSITION:
                    setPlayPosition(msg.arg1);
                default:
                    break;
            }

        }
    };

    private void setPlayPosition(int time){
        if(mMediaPlayer==null){
            return;
        }
        mMediaPlayer.seekTo(time);
        if(!mMediaPlayer.isPlaying()){
            playMusic();
        }
    }


    private final Messenger mServiceMessengerToLryActivity = new Messenger(mHandlerToLryActivity);

    public class MyBinder extends Binder {

        public PlayService getService(){
            return PlayService.this;
        }

        public Messenger getMessengerToPlayActivity(){
            return mServiceMessengerToPlayActivity;
        }

        public Messenger getMessengerToMainActivity(){
            return mServiceMessengerToMainActivity;
        }

        public Messenger getMessengerToLryActivity(){
            return mServiceMessengerToLryActivity;
        }

    }

    private final MyBinder binder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        LogCat.i("PlayService is onCreate",TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogCat.i("PlayService is onStartCommmand",TAG);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogCat.i("PlayService is onBind",TAG);
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogCat.e("PlayService is onUnbind",TAG);
        return false;
    }

    @Override
    public void onDestroy() {
        LogCat.i("PlayService is onDestroy",TAG);
        super.onDestroy();
    }

    public void setPlayModel(int repeat_count){
        if(repeat_count>=0&&repeat_count<=3){
            mPlayModel = repeat_count;
        }
    }

    public void playNewMusic(int pos,List<Song> mSongList){
        this.mSongList = mSongList;
        playNewMusic(pos);
    }

    private void playNewMusic(int pos){
        try {
            this.mPosition = pos;
            if(mSongList==null||mSongList.size()==0){
                LogCat.e("无法读取音乐",TAG);
                return;
            }
            prepare_lyrics();
            if(mMediaPlayer==null){
                mMediaPlayer = new MediaPlayer();
            }else{
                mMediaPlayer.reset();
            }
            mMediaPlayer.setDataSource(mSongList.get(mPosition).getPath());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                prepare_lyrics();
            });
            mMediaPlayer.setOnCompletionListener(mediaPlayer -> {
                if(mPlayModel==MusicBean.REPEAT_ONCE_MODEL){
                    playNewMusic(mPosition);
                }else{
                    nextMusic();
                }
            });
            mPlayStatus = PlayStatus.IN_PLAY;
            sendMessageUpdateUI();
        } catch (Throwable throwable) {
            LogCat.i(throwable,TAG);
        }


    }

    public void playMusic(){
        if(mMediaPlayer!=null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
                mPlayStatus = PlayStatus.IN_PAUSE;
            }else{
                mMediaPlayer.start();
                mPlayStatus = PlayStatus.IN_PLAY;
            }
            sendMessageUpdateUI();
        }else{
            playNewMusic(0,mSongList);
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
        }else{
            playMusic();
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
        }else{
            playMusic();
        }
    }

    private void prepare_lyrics(){
        if(mLryActivityMessenger!=null){
            try {
                Message msg = new Message();
                msg.what = MusicBean.PREPARE_LYRICS;
                msg.arg1 = mPosition;
                msg.arg2 = mMediaPlayer.getDuration();
                msg.obj = mSongList;
                mLryActivityMessenger.send(msg);
            } catch (Throwable throwable) {
                LogCat.e(throwable,TAG);
            }
        }
    }

    private void update_lyrics(){
        if(mLryActivityMessenger!=null&&mMediaPlayer!=null){
            try {
                Message msg = new Message();
                msg.what = MusicBean.UPDATE_LYRICS;
                msg.arg1 = mMediaPlayer.getCurrentPosition();
                msg.arg2 = mPlayStatus;
                mLryActivityMessenger.send(msg);
            } catch (Throwable throwable) {
                LogCat.e(throwable,TAG);
            }
        }
    }

    private void sendMessageUpdateUI(){
        if(mPlayActivityMessenger!=null){
            try {
                Message msg = new Message();
                msg.what = MusicBean.UPDATE_UI;
                msg.arg1 = mPosition;
                msg.arg2 = mPlayStatus;
                msg.obj = mSongList;
                mPlayActivityMessenger.send(msg);
            } catch (Throwable throwable) {
                LogCat.e(throwable,TAG);
            }
        }

        if(mMainActivityMessenger!=null){
            try {
                Message msg = new Message();
                msg.what = MusicBean.UPDATE_UI;
                msg.arg1 = mPosition;
                msg.arg2 = mPlayStatus;
                msg.obj = mSongList;
                mMainActivityMessenger.send(msg);
            } catch (Throwable throwable) {
                LogCat.e(throwable,TAG);
            }
        }
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            update_lyrics();
            mLrySynchronizationHandler.postDelayed(this, 300);
        }
    };


}
