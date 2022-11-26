package com.limnac.musicplayer.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageButton;

import com.limnac.musicplayer.R;
import com.limnac.musicplayer.services.PlayService;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 10:54
 * @description com.limnac.musicplayer
 */

public class PlayActivity extends AppCompatActivity {

    private final int REPEAT_MODEL = 0;
    private final int REPEAR_ONCE_MODEL = 1;
    private final int SHUFFLE = 2;
    private int repeat_count = 0;
    private boolean isPlay = true;
    private PlayService mPlayService;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            PlayService.MyBinder myBinder = (PlayService.MyBinder) iBinder;
            mPlayService = myBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        ImageButton btnRepeatModel = findViewById(R.id.repeat_model_ibtn);
        ImageButton btnPlayPause = findViewById(R.id.play_pause_ibtn);
        ImageButton btnNextSong = findViewById(R.id.nextsong_ibtn);
        ImageButton btnPreSong = findViewById(R.id.presong_ibtn);

        btnRepeatModel.setOnClickListener(v-> changeRepeatModel(btnRepeatModel));
        btnPlayPause.setOnClickListener(v-> play2pause(btnPlayPause));
        btnNextSong.setOnClickListener(v->nextMusic());
        btnPreSong.setOnClickListener(v->preMusic());

        Intent intent = new Intent(PlayActivity.this, PlayService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
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
        if(repeat_count==REPEAT_MODEL){
            mPlayService.setPlayModel(REPEAT_MODEL);
            ibtn.setImageResource(R.drawable.vector_drawable_repeat);
        }else if(repeat_count==REPEAR_ONCE_MODEL){
            mPlayService.setPlayModel(REPEAR_ONCE_MODEL);
            ibtn.setImageResource(R.drawable.vector_drawable_repeatonce);
        }else if(repeat_count==SHUFFLE){
            mPlayService.setPlayModel(SHUFFLE);
            ibtn.setImageResource(R.drawable.vector_drawable_shuffle);
        }
    }

    public static void startPlayActivity(Context context){
        Intent intent = new Intent(context,PlayActivity.class);
        context.startActivity(intent);
    }
}