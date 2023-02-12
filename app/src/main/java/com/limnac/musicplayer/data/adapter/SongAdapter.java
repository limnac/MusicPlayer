package com.limnac.musicplayer.data.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.limnac.musicplayer.R;
import com.limnac.musicplayer.data.model.Song;
import com.limnac.musicplayer.data.viewholder.SongViewHolder;

import java.util.List;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 11:29
 * @description com.limnac.musicplayer.adapter
 */
public class SongAdapter extends BaseAdapter {

    private final List<Song> mSongList;
    private final Context mContext;

    public SongAdapter(List<Song> mSongList,Context mContext){
        this.mSongList = mSongList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mSongList.size();
    }

    @Override
    public Object getItem(int i) {
        return mSongList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SongViewHolder songViewHolder;
        if(view==null){
            view = View.inflate(mContext, R.layout.item_song,null);
            songViewHolder = new SongViewHolder();
            songViewHolder.setNameTextView(view.findViewById(R.id.name));
            songViewHolder.setSingerTextView(view.findViewById(R.id.singer));
            songViewHolder.setCoverImageView(view.findViewById(R.id.cover));

            songViewHolder.getNameTextView().setText(mSongList.get(i).getName());
            songViewHolder.getSingerTextView().setText(mSongList.get(i).getSinger());
            if(mSongList.get(i).getBitmap()!=null){
                songViewHolder.getCoverImageView().setImageBitmap(mSongList.get(i).getBitmap());
            }else{
                songViewHolder.getCoverImageView().setBackgroundResource(mSongList.get(i).getIcon());
            }

            view.setTag(songViewHolder);
        }else{
            songViewHolder = (SongViewHolder) view.getTag();

            songViewHolder.getNameTextView().setText(mSongList.get(i).getName());
            songViewHolder.getSingerTextView().setText(mSongList.get(i).getSinger());
            if(mSongList.get(i).getBitmap()!=null){
                songViewHolder.getCoverImageView().setImageBitmap(mSongList.get(i).getBitmap());
            }else{
                songViewHolder.getCoverImageView().setBackgroundResource(mSongList.get(i).getIcon());
            }

        }
        return view;
    }
}
