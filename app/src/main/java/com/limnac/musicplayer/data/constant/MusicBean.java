package com.limnac.musicplayer.data.constant;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/12/4 16:55
 * @description com.limnac.musicplayer.bean
 */
public class MusicBean {
    /**
     * 绑定服务
     */
    public static final int MSG_BIND = 1000;
    /**
     * 更新UI
     */
    public static final int UPDATE_UI = 2001;
    /**
     * 准备歌词
     */
    public static final int PREPARE_LYRICS  = 2002;
    /**
     * 更新歌词
     */
    public static final int UPDATE_LYRICS  = 2003;
    /**
     * 退出歌词更新
     */
    public static final int STOP_UPDATE_LYRICS  = 2004;
    /**
     * 更新歌曲播放位置
     */
    public static final int SET_PLAY_POSITION  = 2005;
    /**
     * 歌曲循环方式
     */
    public static final int REPEAT_MODEL = 0;
    public static final int REPEAT_ONCE_MODEL = 1;
    public static final int SHUFFLE = 2;
    /**
     * 歌曲列表存储KEY
     */
    public static final String SONG_LIST_KEY = "song_list_key";
    /**
     * 歌手-歌曲Map存储KEY
     */
    public static final String SINGER_MAP_KEY = "singer_map_key";
    /**
     * 专辑-歌曲Map存储KEY
     */
    public static final String ALBUM_MAP_KEY = "album_map_key";

}
