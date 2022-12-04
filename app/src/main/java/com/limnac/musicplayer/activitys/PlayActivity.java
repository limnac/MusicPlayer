package com.limnac.musicplayer.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.limnac.musicplayer.R;
import com.limnac.musicplayer.bean.MusicBean;
import com.limnac.musicplayer.model.Song;
import com.limnac.musicplayer.services.PlayService;
import com.limnac.musicplayer.utils.LogUtil;
import com.limnac.musicplayer.utils.MusicUtil;

import java.util.List;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 10:54
 * @description com.limnac.musicplayer
 */

public class PlayActivity extends AppCompatActivity {

    private static final String TAG = "PlayActivity";

    private int repeat_count = 0;
    private boolean isPlay = true;
    private PlayService mPlayService;
    private Messenger mServiceMessenger;
    private static TextView mSongTextView;
    private static TextView mSingerTextView;
    private static int mPosition;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MusicBean.UPDATE_UI:
                    mPosition = msg.arg1;
                    LogUtil.i(TAG,"现在播放的是: "+MusicUtil.getSongList().get(mPosition).getName());
                    updatePlayUI();
                    break;
            }
        }
    };


    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            PlayService.MyBinder myBinder = (PlayService.MyBinder) iBinder;
            mPlayService = myBinder.getService();
            mServiceMessenger = myBinder.getMessenger();

            Messenger messenger = new Messenger(mHandler);
            Message msg = new Message();
            msg.what = MusicBean.MSG_BIND;
            msg.replyTo = messenger;
            try{
                mServiceMessenger.send(msg);
            }catch (Exception e){
                LogUtil.error(e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);



        initView();
        updatePlayUI();
        LogUtil.i(TAG,"现在播放的是: "+MusicUtil.getSongList().get(mPosition).getName());

        Intent intent = new Intent(PlayActivity.this, PlayService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private void initView(){
        mSongTextView = findViewById(R.id.text_view_name);
        mSingerTextView = findViewById(R.id.text_view_singer);

        ImageButton btnRepeatModel = findViewById(R.id.repeat_model_ibtn);
        ImageButton btnPlayPause = findViewById(R.id.play_pause_ibtn);
        ImageButton btnNextSong = findViewById(R.id.nextsong_ibtn);
        ImageButton btnPreSong = findViewById(R.id.presong_ibtn);

        btnRepeatModel.setOnClickListener(v-> changeRepeatModel(btnRepeatModel));
        btnPlayPause.setOnClickListener(v-> play2pause(btnPlayPause));
        btnNextSong.setOnClickListener(v->nextMusic());
        btnPreSong.setOnClickListener(v->preMusic());
    }

    private void preMusic(){
        mPlayService.preMusic();
    }

    private void nextMusic(){
        mPlayService.nextMusic();
    }

    private void play2pause(ImageButton ibtn){
        if(isPlay){
            isPlay = false;
            ibtn.setImageResource(R.drawable.vector_drawable_play);
        }else{
            isPlay = true;
            ibtn.setImageResource(R.drawable.vector_drawable_pause);
        }
        mPlayService.playMusic();
    }

    private void changeRepeatModel(ImageButton ibtn){
        repeat_count = (repeat_count+1)%3;
        Toast toast = null;
        if(repeat_count==MusicBean.REPEAT_MODEL){
            mPlayService.setPlayModel(MusicBean.REPEAT_MODEL);
            ibtn.setImageResource(R.drawable.vector_drawable_repeat);
            toast = Toast.makeText(this,"已切换到顺序播放",Toast.LENGTH_SHORT);
        }else if(repeat_count==MusicBean.REPEAR_ONCE_MODEL){
            mPlayService.setPlayModel(MusicBean.REPEAR_ONCE_MODEL);
            ibtn.setImageResource(R.drawable.vector_drawable_repeatonce);
            toast = Toast.makeText(this,"已切换到单曲循环",Toast.LENGTH_SHORT);
        }else if(repeat_count==MusicBean.SHUFFLE){
            mPlayService.setPlayModel(MusicBean.SHUFFLE);
            ibtn.setImageResource(R.drawable.vector_drawable_shuffle);
            toast = Toast.makeText(this,"已切换到随机播放",Toast.LENGTH_SHORT);
        }
        if(toast!=null){
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    public static void startPlayActivity(Context context, int pos){
        Intent intent = new Intent(context,PlayActivity.class);
        context.startActivity(intent);
        PlayActivity.mPosition = pos;
    }

    public void updatePlayUI(){
        if(mSongTextView!=null&&mSingerTextView!=null){
            List<Song> songList = MusicUtil.getSongList();
            mSongTextView.setText(songList.get(mPosition).getName());
            mSingerTextView.setText(songList.get(mPosition).getSinger());
        }
    }
}