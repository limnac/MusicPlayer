package com.limnac.musicplayer.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.limnac.musicplayer.R;
import com.limnac.musicplayer.activitys.ListActivity;
import com.limnac.musicplayer.data.viewholder.AlbumViewHolder;
import com.limnac.musicplayer.data.model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author chuanghu
 * @email chuanghu@iflytek.com
 * @date 2023/1/12 14:07
 * @description
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumViewHolder> {
    private final Map<String,List<Song>> mAlbumMap;
    private final List<String> mAlbumNameList;

    public AlbumAdapter(Map<String,List<Song>> mAlbumMap) {
        this.mAlbumMap = mAlbumMap;
        this.mAlbumNameList = new ArrayList<>(mAlbumMap.keySet());
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        AlbumViewHolder holder = new AlbumViewHolder(view);
        holder.getAlbumRecyclerView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                String album = mAlbumNameList.get(position);
                ListActivity.startActivity(parent.getContext(),mAlbumMap.get(album));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        String albumName = mAlbumNameList.get(position);
        if(Objects.requireNonNull(mAlbumMap.get(albumName)).get(0).getBitmap()!=null){
            holder.getAlbumImage().setImageBitmap(Objects.requireNonNull(mAlbumMap.get(albumName)).get(0).getBitmap());
        }else{
            holder.getAlbumImage().setBackgroundResource(R.drawable.vector_drawable_music);
        }

        holder.getAlbumName().setText(albumName);
    }

    @Override
    public int getItemCount() {
        return mAlbumNameList.size();
    }
}
