package com.limnac.musicplayer.data.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.limnac.musicplayer.R;

/**
 * @author chuanghu
 * @email chuanghu@iflytek.com
 * @date 2023/1/12 14:02
 * @description
 */
public class AlbumViewHolder extends RecyclerView.ViewHolder{
    private View mAlbumRecyclerView;
    private ImageView mAlbumImage;
    private TextView mAlbumName;
    public AlbumViewHolder(@NonNull View itemView) {
        super(itemView);
        mAlbumRecyclerView = itemView;
        mAlbumImage = (ImageView) itemView.findViewById(R.id.image_item_album);
        mAlbumName = (TextView) itemView.findViewById(R.id.content_item_album);
    }

    public View getAlbumRecyclerView() {
        return mAlbumRecyclerView;
    }

    public void setAlbumRecyclerView(View mAlbumRecyclerView) {
        this.mAlbumRecyclerView = mAlbumRecyclerView;
    }

    public ImageView getAlbumImage() {
        return mAlbumImage;
    }

    public void setAlbumImage(ImageView mAlbumImage) {
        this.mAlbumImage = mAlbumImage;
    }

    public TextView getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(TextView mAlbumName) {
        this.mAlbumName = mAlbumName;
    }
}
