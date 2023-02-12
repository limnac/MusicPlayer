package com.limnac.musicplayer.activitys;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.drake.logcat.LogCat;
import com.limnac.musicplayer.R;
import com.limnac.musicplayer.data.constant.MusicBean;
import com.limnac.musicplayer.data.constant.PlayStatus;
import com.limnac.musicplayer.data.model.Song;
import com.limnac.musicplayer.services.PlayService;
import com.limnac.musicplayer.utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 10:54
 * @description com.limnac.musicplayer
 */

public class PlayActivity extends AppCompatActivity {

    private static final String TAG = "PlayActivity";

    private int mRepeatCount = 0;
    private PlayService  mPlayService;

    private  TextView mSongTextView;
    private  TextView mSingerTextView;
    private ImageButton mImageSong;
    private ImageButton mImageButtonPlay;
    private  int mPosition;
    private int mPlayStatus;
    private List<Song> mSongList;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == MusicBean.UPDATE_UI) {
                mPosition = msg.arg1;
                mPlayStatus = msg.arg2;
                mSongList = new ArrayList<>();
                if(msg.obj instanceof List<?>){
                    for(Object o:(List<?>)msg.obj){
                        if(o instanceof Song){
                            mSongList.add((Song)o);
                        }
                    }
                }
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
        mSongTextView = findViewById(R.id.song_name_activity_play);
        mSingerTextView = findViewById(R.id.singer_name_activity_play);
        mImageSong = findViewById(R.id.image_song_activity_play);
        mImageButtonPlay = findViewById(R.id.play_pause_button_activity_play);

        mImageButtonPlay.setOnClickListener(v->mPlayService.playMusic());

        mImageSong.setOnClickListener(v->startLryActivity());

        ImageButton btnRepeatModel = findViewById(R.id.repeat_model_button_activity_play);
        btnRepeatModel.setOnClickListener(v-> changeRepeatModel(btnRepeatModel));

        ImageButton btnNextSong = findViewById(R.id.next_song_button_activity_play);
        btnNextSong.setOnClickListener(v->mPlayService.nextMusic());

        ImageButton btnPreSong = findViewById(R.id.pre_song_button_activity_play);
        btnPreSong.setOnClickListener(v->mPlayService.preMusic());

        ImageButton btnPlayMenu = findViewById(R.id.play_menu_button_activity_play);
        btnPlayMenu.setOnClickListener(v->startListActivity());
    }

    private long mFlag1;
    private void startListActivity(){
        if(System.currentTimeMillis()-mFlag1<300){
            mFlag1 = System.currentTimeMillis();
            return;
        }
        mFlag1 = System.currentTimeMillis();
        ListActivity.startActivity(PlayActivity.this,mSongList);
    }

    private long mFlag2;
    private void startLryActivity(){
        if(System.currentTimeMillis()-mFlag2<300){
            mFlag2 = System.currentTimeMillis();
            return;
        }
        mFlag2 = System.currentTimeMillis();
        LryActivity.startActivity(PlayActivity.this);
    }




    private void changeRepeatModel(ImageButton iButton){
        mRepeatCount = (mRepeatCount+1)%3;
        Toast toast = null;
        if(mRepeatCount==MusicBean.REPEAT_MODEL){
            mPlayService.setPlayModel(MusicBean.REPEAT_MODEL);
            iButton.setImageResource(R.drawable.vector_drawable_repeat);
            toast = Toast.makeText(this,"已切换到顺序播放",Toast.LENGTH_SHORT);
        }else if(mRepeatCount==MusicBean.REPEAT_ONCE_MODEL){
            mPlayService.setPlayModel(MusicBean.REPEAT_ONCE_MODEL);
            iButton.setImageResource(R.drawable.vector_drawable_repeatonce);
            toast = Toast.makeText(this,"已切换到单曲循环",Toast.LENGTH_SHORT);
        }else if(mRepeatCount==MusicBean.SHUFFLE){
            mPlayService.setPlayModel(MusicBean.SHUFFLE);
            iButton.setImageResource(R.drawable.vector_drawable_shuffle);
            toast = Toast.makeText(this,"已切换到随机播放",Toast.LENGTH_SHORT);
        }
        if(toast!=null){
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context,PlayActivity.class);
        context.startActivity(intent);
    }


    private void  updatePlayUI(){
        if(mSongList==null||mSongList.size()==0){
            LogCat.i("无正在播放的歌曲",TAG);
            return;
        }
        if(mSongTextView!=null&&mSingerTextView!=null){
            mSongTextView.setText(mSongList.get(mPosition).getName());
            mSingerTextView.setText(mSongList.get(mPosition).getSinger());
        }
        if(mImageButtonPlay!=null){
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
        if(mImageSong!=null&&mSongList.get(mPosition).getBitmap()!=null){
            Bitmap bitmap = MusicUtils.changeBitmapSize(mSongList.get(mPosition).getBitmap(),700,700);
            mImageSong.setImageBitmap(bitmap);
        }
    }
}