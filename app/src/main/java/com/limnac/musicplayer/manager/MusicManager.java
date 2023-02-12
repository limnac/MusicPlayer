package com.limnac.musicplayer.manager;

import com.limnac.musicplayer.callback.UpdateCallBack;
import com.limnac.musicplayer.data.congfig.MusicConfig;
import com.limnac.musicplayer.data.constant.errorcode.UpdateErrorCode;
import com.limnac.musicplayer.data.model.Song;
import com.limnac.musicplayer.utils.MusicUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author chuanghu
 * @email chuanghu@iflytek.com
 * @date 2023/1/10 11:09
 * @description
 */
public class MusicManager {

    private static MusicManager instance;
    private MusicManager(){
    }
    public synchronized static MusicManager getInstance(){
        if(instance==null){
            instance = new MusicManager();
        }
        return instance;
    }

    private List<Song> mSongList;
    private HashMap<String,List<Song>> mSingerMap;
    private HashMap<String,List<Song>> mAlbumMap;

    public void update(UpdateCallBack updateCallBack){
        new Thread(){
            @Override
            public void run() {
                mSongList = MusicUtils.getMusic(MusicConfig.getInstance().getContext());
                if(mSongList==null){
                    updateCallBack.onFailed(UpdateErrorCode.GET_SONG_LIST_EXCEPTION,"无法获取本地音乐列表");
                    return;
                }
                if(mSongList.size()==0){
                    updateCallBack.onFailed(UpdateErrorCode.NO_SONG,"本地音乐数量为0");
                    return;
                }
                mSingerMap=new HashMap<>();
                mAlbumMap = new HashMap<>();
                for(int i = 0;i<mSongList.size();i++){
                    Song song = mSongList.get(i);
                    if(song.getSinger()==null||song.getSinger().length()==0){
                        if(!mSingerMap.containsKey("未知")){
                            mSingerMap.put("未知", new ArrayList<>());
                        }
                        Objects.requireNonNull(mSingerMap.get("未知")).add(song);
                    }else{
                        if(!mSingerMap.containsKey(song.getSinger())){
                            mSingerMap.put(song.getSinger(),new ArrayList<>());
                        }
                        Objects.requireNonNull(mSingerMap.get(song.getSinger())).add(song);
                    }

                    if (!mAlbumMap.containsKey(song.getAlbumName())) {
                        mAlbumMap.put(song.getAlbumName(), new ArrayList<>());
                    }
                    Objects.requireNonNull(mAlbumMap.get(song.getAlbumName())).add(song);
                }

                updateCallBack.success();
            }
        }.start();
    }

    public HashMap<String, List<Song>> getAlbumMap() {
        return mAlbumMap;
    }

    public HashMap<String, List<Song>> getSingerMap() {
        return mSingerMap;
    }

    public List<Song> getList() {
        return mSongList;
    }
}
