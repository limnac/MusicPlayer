package com.limnac.musicplayer.activitys;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.drake.logcat.LogCat;
import com.limnac.musicplayer.R;
import com.limnac.musicplayer.data.constant.MusicBean;
import com.limnac.musicplayer.data.model.Song;
import com.limnac.musicplayer.lry.LrcView;
import com.limnac.musicplayer.services.PlayService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LryActivity extends AppCompatActivity {

    private static final String TAG = "LryActivity";

    private LrcView lrcView;
    private SeekBar seekBar;
    private Button btnPlayPause;
    private PlayService mPlayService;
    private  int mPosition;
    private int mPlayStatus;
    private int mPlayDuration;
    private List<Song> mSongList;
    private int mPlayPosition;
    private Messenger mServiceMessenger;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MusicBean.PREPARE_LYRICS:
                    mPosition = msg.arg1;
                    mPlayDuration = msg.arg2;
                    mSongList = new ArrayList<>();
                    if(msg.obj instanceof List<?>){
                        for(Object o:(List<?>)msg.obj){
                            if(o instanceof Song){
                                mSongList.add((Song)o);
                            }
                        }
                    }
                    loadMusicLry();
                    break;
                case MusicBean.UPDATE_LYRICS:
                    mPlayPosition = msg.arg1;
                    mPlayStatus = msg.arg2;
                    updateMusicLry();
                    break;
                default:
                    break;

            }
        }
    };

    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            PlayService.MyBinder myBinder = (PlayService.MyBinder) iBinder;
            mPlayService = myBinder.getService();
            mServiceMessenger = myBinder.getMessengerToLryActivity();

            Messenger messenger = new Messenger(mHandler);
            Message msg = new Message();
            msg.what = MusicBean.MSG_BIND;
            msg.replyTo = messenger;
            try{
                mServiceMessenger.send(msg);
            }catch (Throwable throwable){
                LogCat.e(throwable,TAG);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lry);

        lrcView = findViewById(R.id.lrc_view);
        seekBar = findViewById(R.id.progress_bar);
        btnPlayPause = findViewById(R.id.btn_play_pause);

        Intent intent = new Intent(LryActivity.this, PlayService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);

        btnPlayPause.setOnClickListener(v->mPlayService.playMusic());

        lrcView.setDraggable(true, (view, time) -> {
            setPlayPosition((int)time);
            return true;
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setPlayPosition(seekBar.getProgress());
            }
        });
    }

    private void setPlayPosition(int time){
        Message msg = new Message();
        msg.what = MusicBean.SET_PLAY_POSITION;
        msg.arg1 = time;
        try{
            mServiceMessenger.send(msg);
        }catch (Throwable throwable){
            LogCat.e(throwable,TAG);
        }
    }

    private void updateMusicLry(){
        lrcView.updateTime(mPlayPosition);
        seekBar.setProgress(mPlayPosition);
    }


    private void loadMusicLry(){
        String path = mSongList.get(mPosition).getPath();
        String lrcPath = path.substring(0,path.lastIndexOf("."))+".lrc";
        LogCat.e(lrcPath,TAG);
        File lrcFile = new File(lrcPath);
        lrcView.loadLrc(lrcFile);
        seekBar.setMax(mPlayDuration);
    }

    private String getLrcText(String fileName) {
        String lrcText = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            lrcText = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcText;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Message msg = new Message();
        msg.what = MusicBean.STOP_UPDATE_LYRICS;
        try{
            mServiceMessenger.send(msg);
        }catch (Throwable throwable){
            LogCat.e(throwable,TAG);
        }
    }



    public static void startActivity(Context context){
        Intent intent = new Intent(context,LryActivity.class);
        context.startActivity(intent);
    }
}