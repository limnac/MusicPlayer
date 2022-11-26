package com.limnac.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.limnac.musicplayer.R;
import com.limnac.musicplayer.model.Song;
import com.limnac.musicplayer.model.ViewHolder;

import java.util.List;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 11:29
 * @description com.limnac.musicplayer.adapter
 */
public class SongAdapter extends BaseAdapter {

    private List<Song> mSongList;
    private Context mContext;

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
        ViewHolder viewHolder = null;
        if(view==null){
            view = View.inflate(mContext, R.layout.item_song,null);
            viewHolder = new ViewHolder();
            viewHolder.setNameTextView(view.findViewById(R.id.name));
            viewHolder.setSingerTextView(view.findViewById(R.id.singer));
            viewHolder.setCoverImageView(view.findViewById(R.id.cover));

            viewHolder.getNameTextView().setText(mSongList.get(i).getName());
            viewHolder.getSingerTextView().setText(mSongList.get(i).getSinger());
            viewHolder.getCoverImageView().setBackgroundResource(mSongList.get(i).getIcon());

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();

            viewHolder.getNameTextView().setText(mSongList.get(i).getName());
            viewHolder.getSingerTextView().setText(mSongList.get(i).getSinger());
            viewHolder.getCoverImageView().setBackgroundResource(mSongList.get(i).getIcon());

        }
        return view;
    }
}
