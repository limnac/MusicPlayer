package com.limnac.musicplayer.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.drake.logcat.LogCat;
import com.limnac.musicplayer.R;
import com.limnac.musicplayer.bean.MusicBean;
import com.limnac.musicplayer.constant.PlayStatus;
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

    private int repeat_count = 0;
    private PlayService  mPlayService;

    private  TextView mSongTextView;
    private  TextView mSingerTextView;
    private ImageButton mImageButtonPlay;
    private  int mPosition;
    private int mPlayStatus;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == MusicBean.UPDATE_UI) {
                mPosition = msg.arg1;
                mPlayStatus = msg.arg2;
                updatePlayUI();
            }
        }
    };


    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            PlayService.MyBinder myBinder = (PlayService.MyBinder) iBinder;
            mPlayService = myBinder.getService();
            Messenger mServiceMessenger = myBinder.getMessengerToPlayActivity();

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
        setContentView(R.layout.activity_play);



        initView();
        updatePlayUI();

        Intent intent = new Intent(PlayActivity.this, PlayService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private void initView(){
        mSongTextView = findViewById(R.id.text_view_name);
        mSingerTextView = findViewById(R.id.text_view_singer);
        mImageButtonPlay = findViewById(R.id.play_pause_ibtn);

        mImageButtonPlay.setOnClickListener(v->mPlayService.playMusic());

        ImageButton btnRepeatModel = findViewById(R.id.repeat_model_ibtn);
        ImageButton btnNextSong = findViewById(R.id.nextsong_ibtn);
        ImageButton btnPreSong = findViewById(R.id.presong_ibtn);

        btnRepeatModel.setOnClickListener(v-> changeRepeatModel(btnRepeatModel));
        btnNextSong.setOnClickListener(v->mPlayService.nextMusic());
        btnPreSong.setOnClickListener(v->mPlayService.preMusic());
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

    public static void startPlayActivity(Context context){
        Intent intent = new Intent(context,PlayActivity.class);
        context.startActivity(intent);
    }

    private void  updatePlayUI(){
        List<Song> songList = MusicUtil.getSongList();
        if(mSongTextView!=null&&mSingerTextView!=null){
            mSongTextView.setText(songList.get(mPosition).getName());
            mSingerTextView.setText(songList.get(mPosition).getSinger());
        }
        switch (mPlayStatus){
            case PlayStatus.IN_PAUSE:
                mImageButtonPlay.setImageResource(R.drawable.vector_drawable_play);
                break;
            case PlayStatus.IN_PLAY:
                mImageButtonPlay.setImageResource(R.drawable.vector_drawable_pause);
                break;
            default:
                break;
        }
    }
}