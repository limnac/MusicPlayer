package com.limnac.musicplayer.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.limnac.musicplayer.R;
import com.limnac.musicplayer.model.Song;
import com.limnac.musicplayer.services.PlayService;
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

    private final int REPEAT_MODEL = 0;
    private final int REPEAR_ONCE_MODEL = 1;
    private final int SHUFFLE = 2;
    private int repeat_count = 0;
    private boolean isPlay = true;
    private PlayService mPlayService;
    private static TextView mSongTextView;
    private static TextView mSingerTextView;
    private static int mPosition;


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

        initView();
        updatePlayUI(mPosition );

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
        if(repeat_count==REPEAT_MODEL){
            mPlayService.setPlayModel(REPEAT_MODEL);
            ibtn.setImageResource(R.drawable.vector_drawable_repeat);
            toast = Toast.makeText(this,"已切换到顺序播放",Toast.LENGTH_SHORT);
        }else if(repeat_count==REPEAR_ONCE_MODEL){
            mPlayService.setPlayModel(REPEAR_ONCE_MODEL);
            ibtn.setImageResource(R.drawable.vector_drawable_repeatonce);
            toast = Toast.makeText(this,"已切换到单曲循环",Toast.LENGTH_SHORT);
        }else if(repeat_count==SHUFFLE){
            mPlayService.setPlayModel(SHUFFLE);
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

    public static void updatePlayUI(int pos){
        if(mSongTextView!=null&&mSingerTextView!=null){
            List<Song> songList = MusicUtil.getSongList();
            mSongTextView.setText(songList.get(pos).getName());
            mSingerTextView.setText(songList.get(pos).getSinger());
        }
    }
}