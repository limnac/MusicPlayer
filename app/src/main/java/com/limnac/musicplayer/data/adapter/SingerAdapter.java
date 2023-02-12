package com.limnac.musicplayer.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.limnac.musicplayer.R;
import com.limnac.musicplayer.activitys.ListActivity;
import com.limnac.musicplayer.data.model.Song;
import com.limnac.musicplayer.data.viewholder.SingerViewHolder;
import com.limnac.musicplayer.manager.MusicManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chuanghu
 * @email chuanghu@iflytek.com
 * @date 2023/1/12 10:08
 * @description
 */
public class SingerAdapter extends RecyclerView.Adapter<SingerViewHolder> {

    private final List<String> mSingerList;
    private final Map<String,List<Song>> mSingerMap;

    public SingerAdapter(Map<String,List<Song>> mSingerMap) {
        this.mSingerMap = mSingerMap;
        this.mSingerList = new ArrayList<>(mSingerMap.keySet());
    }

    @NonNull
    @Override
    public SingerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singers, parent, false);
        SingerViewHolder holder = new SingerViewHolder(view);
        holder.getSingerRecyclerView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                String singer = mSingerList.get(position);
                Map<String,List<Song>> songMap = MusicManager.getInstance().getSingerMap();
                List<Song> songs = songMap.get(singer);
                ListActivity.startActivity(parent.getContext(),songs);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SingerViewHolder holder, int position) {
        String singer = mSingerList.get(position);
        holder.getSingerImage().setBackgroundResource(R.drawable.vector_drawable_headportrait);
        holder.getSingerName().setText(singer);
    }

    @Override
    public int getItemCount() {
        return mSingerList.size();
    }
}
