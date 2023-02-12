package com.limnac.musicplayer.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.drake.logcat.LogCat;
import com.limnac.musicplayer.R;
import com.limnac.musicplayer.data.adapter.SongAdapter;
import com.limnac.musicplayer.data.model.Song;
import com.limnac.musicplayer.services.PlayService;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "SingerFragment";

    private PlayService mPlayService;
    private static List<Song> mSongList;

    private final ServiceConnection conn = new ServiceConnection() {
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
        setContentView(R.layout.activity_list);
        Intent intent = new Intent(ListActivity.this, PlayService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
        initView();

    }

    private void initView() {
        ListView listView = findViewById(R.id.listview_activity_list);
        if(mSongList==null){
            LogCat.e("无法获取歌曲列表",TAG);
            return;
        }
        SongAdapter adapter = new SongAdapter(mSongList,getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPlayService.playNewMusic(i,mSongList);
                ListActivity.this.finish();
            }
        });
    }


    public static void startActivity(Context context,List<Song> mSongList){
        Intent intent = new Intent(context,ListActivity.class);
        context.startActivity(intent);
        ListActivity.mSongList = mSongList;
    }
}