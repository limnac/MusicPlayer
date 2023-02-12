package com.limnac.musicplayer.utils;//package com.limnac.musicplayer.utils;
//
//import com.drake.logcat.LogCat;
//import com.google.gson.reflect.TypeToken;
//import com.limnac.musicplayer.data.constant.MusicBean;
//import com.limnac.musicplayer.data.model.Song;
//import com.tencent.mmkv.MMKV;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * @author chuanghu
// * @email chuanghu@iflytek.com
// * @date 2023/1/12 15:41
// * @description
// */
//public class MMKVUtils {
//
//    private static final String TAG = "MMKVUtils";
//
//    private static MMKVUtils instance;
//    private MMKVUtils(){}
//    public synchronized static MMKVUtils getInstance() {
//        if (instance == null) {
//            instance = new MMKVUtils();
//        }
//        return instance;
//    }
//
//    private final MMKV kv = MMKV.defaultMMKV();
//
//    public void setSongList(List<Song> songList){
//        if(songList!=null){
//            kv.encode(MusicBean.SONG_LIST_KEY,GsonUtils.toJson(songList));
//        }
//    }
//
//    public List<Song> getSongList(){
//        String json = kv.decodeString(MusicBean.SONG_LIST_KEY);
//        if(json==null){
//            LogCat.i("内存中没有歌曲信息列表",TAG);
//            return null;
//        }
//        try{
//            return GsonUtils.fromJson(json,new TypeToken<List<Song>>(){}.getType());
//        }catch (Throwable throwable){
//            return null;
//        }
//    }
//
//    public void setSingerMap(Map<String, List<Song>> singerMap){
//        if(singerMap!=null){
//            kv.encode(MusicBean.SINGER_MAP_KEY,GsonUtils.toJson(singerMap));
//        }
//    }
//
//    public Map<String, List<Song>> getSingerMap(){
//        String json = kv.decodeString(MusicBean.SINGER_MAP_KEY);
//        if(json==null){
//            LogCat.i("内存中没有歌曲信息列表",TAG);
//            return null;
//        }
//        try{
//            return GsonUtils.fromJson(json,new TypeToken<Map<String, List<Song>>>(){}.getType());
//        }catch (Throwable throwable){
//            return null;
//        }
//    }
//
//    public void setAlbumMap(Map<String, List<Song>> albumMap){
//        if(albumMap!=null){
//            kv.encode(MusicBean.ALBUM_MAP_KEY,GsonUtils.toJson(albumMap));
//        }
//    }
//
//    public Map<String, List<Song>> getAlbumMap(){
//        String json = kv.decodeString(MusicBean.ALBUM_MAP_KEY);
//        if(json==null){
//            LogCat.i("内存中没有歌曲信息列表",TAG);
//            return null;
//        }
//        try{
//            return GsonUtils.fromJson(json,new TypeToken<Map<String, List<Song>>>(){}.getType());
//        }catch (Throwable throwable){
//            return null;
//        }
//    }
//}
