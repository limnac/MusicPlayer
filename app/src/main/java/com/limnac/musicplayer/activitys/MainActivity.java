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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.drake.logcat.LogCat;
import com.limnac.musicplayer.R;
import com.limnac.musicplayer.data.adapter.SongAdapter;
import com.limnac.musicplayer.data.constant.MusicBean;
import com.limnac.musicplayer.data.constant.PlayStatus;
import com.limnac.musicplayer.fragments.ClassifyFragment;
import com.limnac.musicplayer.fragments.ListFragment;
import com.limnac.musicplayer.fragments.MyFragment;
import com.limnac.musicplayer.fragments.PlayingFragment;
import com.limnac.musicplayer.data.model.Song;
import com.limnac.musicplayer.services.PlayService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 10:54
 * @description com.limnac.musicplayer
 */

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    private View mPlayingBtn;
    private View mListBtn;
    private View mClassifyBtn;
    private View mMyBtn;
    private PlayingFragment mPlayingFragment;
    private ListFragment mListFragment;
    private ClassifyFragment mClassifyFragment;
    private MyFragment mMyFragment;
    private FragmentManager mFragmentManager;

    private  TextView mSongTextView;
    private  TextView mSingerTextView;
    private  ImageButton mBtnPlay;
    private  ImageView mImageSong;
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
                updateMainUI();

            }
        }
    };

    private PlayService mPlayService;

    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            PlayService.MyBinder myBinder = (PlayService.MyBinder) iBinder;
            mPlayService = myBinder.getService();

            Messenger mServiceMessenger = myBinder.getMessengerToMainActivity();
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
        setContentView(R.layout.activity_main);

        initView();
        setSelectionTab(0);
        Intent intent = new Intent(MainActivity.this, PlayService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void  updateMainUI(){
        if(mSongList==null||mSongList.size()==0){
            LogCat.e("读取不到歌曲",TAG);
            return;
        }
        if(mSongTextView!=null&&mSingerTextView!=null){
            mSongTextView.setText(mSongList.get(mPosition).getName());
            mSingerTextView.setText(mSongList.get(mPosition).getSinger());
        }
        if(mBtnPlay!=null){
            switch (mPlayStatus){
                case PlayStatus.IN_PAUSE:
                    mBtnPlay    .setImageResource(R.drawable.vector_drawable_play);
                    break;
                case PlayStatus.IN_PLAY:
                    mBtnPlay.setImageResource(R.drawable.vector_drawable_pause);
                    break;
                default:
                    break;
            }
        }
        if(mImageSong!=null&&mSongList.get(mPosition).getBitmap()!=null){
            mImageSong.setImageBitmap(mSongList.get(mPosition).getBitmap());
        }
        updatePlayingFragment();
    }
    private List<Song> beforeSongList = new ArrayList<>();
    private void updatePlayingFragment(){
        if(beforeSongList.equals(mSongList)){
            return;
        }else{
            beforeSongList = mSongList;
        }
        if(mPlayingFragment==null){
            LogCat.e("playingFragment==null",TAG);
            return;
        }
        ListView playingListView = mPlayingFragment.requireView().findViewById(R.id.playing_listview_fragment_playing);
        if(playingListView==null){
            LogCat.e("playingListView==null",TAG);
            return;
        }
        SongAdapter adapter = new SongAdapter(mSongList,getApplicationContext());
        playingListView.setAdapter(adapter);
        playingListView.setOnItemClickListener((adapterView, view, i, l) -> {
            if(mPlayService!=null){
                mPlayService.playNewMusic(i,mSongList);
            }else{
                LogCat.e("无法获取服务",TAG);
            }
        });
    }





    private void initView() {
        mFragmentManager = this.getSupportFragmentManager();

        mPlayingBtn = findViewById(R.id.playing_layout_activity_main);
        mListBtn = findViewById(R.id.list_layout_activity_main);
        mClassifyBtn = findViewById(R.id.classify_layout_activity_main);
        mMyBtn = findViewById(R.id.my_layout_activity_main);

        mPlayingBtn.setOnClickListener(v->setSelectionTab(0));
        mListBtn.setOnClickListener(v->setSelectionTab(1));
        mClassifyBtn.setOnClickListener(v->setSelectionTab(2));
        mMyBtn.setOnClickListener(v->setSelectionTab(3));

        mSongTextView = findViewById(R.id.song_name_activity_main);
        mSingerTextView = findViewById(R.id.song_singer_activity_main);
        mImageSong = findViewById(R.id.song_image_activity_main);
        mImageSong.setOnClickListener(v->startPlayActivity());

        mBtnPlay = findViewById(R.id.music_play_activity_main);
        mBtnPlay.setOnClickListener(v->mPlayService.playMusic());

        ImageButton btnNextMusic = findViewById(R.id.music_next_activity_main);
        btnNextMusic.setOnClickListener(v->mPlayService.nextMusic());

        ImageButton btnPreMusic = findViewById(R.id.music_previous_activity_main);
        btnPreMusic.setOnClickListener(v->mPlayService.preMusic());
    }

    private long mFlag;
    private void startPlayActivity(){
        if(System.currentTimeMillis()-mFlag<300){
            mFlag = System.currentTimeMillis();
            return;
        }
        mFlag = System.currentTimeMillis();
        PlayActivity.startActivity(MainActivity.this);

    }

    public void setSelectionTab(int index){
        FragmentTransaction trans = mFragmentManager.beginTransaction();
        setUnSelect();
        hideFragments(trans);

        switch (index){
            case 0:
                this.mPlayingBtn.setBackgroundColor(0xff0000ff);
                if(mPlayingFragment==null) {
                    mPlayingFragment = new PlayingFragment();
                    trans.add(R.id.content,mPlayingFragment);
                }else{
                    trans.show(mPlayingFragment);
                }
                break;
            case 1:
                this.mListBtn.setBackgroundColor(0xff0000ff);
                if(mListFragment==null) {
                    mListFragment = new ListFragment();
                    trans.add(R.id.content,mListFragment);
                }else{
                    trans.show(mListFragment);
                }
                break;
            case 2:
                this.mClassifyBtn.setBackgroundColor(0xff0000ff);
                if(mClassifyFragment==null) {
                    mClassifyFragment = new ClassifyFragment();
                    trans.add(R.id.content,mClassifyFragment);
                }else{
                    trans.show(mClassifyFragment);
                }
                break;
            case 3:
                this.mMyBtn.setBackgroundColor(0xff0000ff);
                if(mMyFragment==null) {
                    mMyFragment = new MyFragment();
                    trans.add(R.id.content,mMyFragment);
                }else{
                    trans.show(mMyFragment);
                }
                break;
        }
        trans.commit();
    }


    /**
     * 隐藏所有的Fragment
     */
    private void hideFragments(FragmentTransaction trans) {
        hideFragment(trans,mPlayingFragment);
        hideFragment(trans,mListFragment);
        hideFragment(trans,mClassifyFragment);
        hideFragment(trans,mMyFragment);
    }

    private void hideFragment(FragmentTransaction trans, Fragment fragment){
        if(fragment !=null){
            trans.hide(fragment);
        }
    }

    /**
     * 将所有按钮设置为非选择状态
     */
    private void setUnSelect() {
        this.mPlayingBtn.setBackgroundColor(0xffffffff);
        this.mListBtn.setBackgroundColor(0xffffffff);
        this.mClassifyBtn.setBackgroundColor(0xffffffff);
        this.mMyBtn.setBackgroundColor(0xffffffff);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    public PlayService getPlayService(){
        return mPlayService;
    }

}