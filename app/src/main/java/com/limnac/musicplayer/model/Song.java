package com.limnac.musicplayer.model;

import android.graphics.Bitmap;

import com.limnac.musicplayer.R;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 10:54
 * @description com.limnac.musicplayer
 */

public class Song {

    private static final String TAG = "Song";

    private String name; //歌曲名
    private String singer; //歌手名
    private long size; // 歌曲所占空间大小
    private int duration; // 歌曲时间长度
    private String path; // 歌曲地址
    private long albumId; // 图片id
    private long id; // 歌曲id
    private int icon = R.drawable.vector_drawable_music; // 默认音乐图标

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public Song setSinger(String singer) {
        this.singer = singer;
        return this;
    }

    public long getSize() {
        return size;
    }

    public Song setSize(long size) {
        this.size = size;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public Song setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public String getPath() {
        return path;
    }

    public Song setPath(String path) {
        this.path = path;
        return this;
    }

    public long getAlbumId() {
        return albumId;
    }

    public Song setAlbumId(long albumId) {
        this.albumId = albumId;
        return this;
    }

    public long getId() {
        return id;
    }

    public Song setId(long id) {
        this.id = id;
        return this;
    }

    public int getIcon() {
        return icon;
    }

    public Song setIcon(int icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", singer='" + singer + '\'' +
                '}';
    }
}
