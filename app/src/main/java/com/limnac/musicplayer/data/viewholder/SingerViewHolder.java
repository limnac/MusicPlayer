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
 * @date 2023/1/12 10:02
 * @description
 */
public class SingerViewHolder extends RecyclerView.ViewHolder {
    private View mSingerRecyclerView;
    private ImageView mSingerImage;
    private TextView mSingerName;
    public SingerViewHolder(@NonNull View itemView) {
        super(itemView);
        mSingerRecyclerView = itemView;
        mSingerImage = (ImageView) itemView.findViewById(R.id.image_item_singers);
        mSingerName = (TextView) itemView.findViewById(R.id.content_item_singers);
    }

    public View getSingerRecyclerView() {
        return mSingerRecyclerView;
    }

    public void setSingerRecyclerView(View mSingerRecyclerView) {
        this.mSingerRecyclerView = mSingerRecyclerView;
    }

    public ImageView getSingerImage() {
        return mSingerImage;
    }

    public void setSingerImage(ImageView mSingerImage) {
        this.mSingerImage = mSingerImage;
    }

    public TextView getSingerName() {
        return mSingerName;
    }

    public void setSingerName(TextView mSingerName) {
        this.mSingerName = mSingerName;
    }
}
