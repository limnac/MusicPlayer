package com.limnac.musicplayer.model;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 10:54
 * @description com.limnac.musicplayer
 */

public class ViewHolder {

    private TextView nameTextView;
    private ImageView coverImageView ;
    private TextView singerTextView;

    public TextView getNameTextView() {
        return nameTextView;
    }

    public void setNameTextView(TextView nameTextView) {
        this.nameTextView = nameTextView;
    }

    public ImageView getCoverImageView() {
        return coverImageView;
    }

    public void setCoverImageView(ImageView coverImageView) {
        this.coverImageView = coverImageView;
    }

    public TextView getSingerTextView() {
        return singerTextView;
    }

    public void setSingerTextView(TextView singerTextView) {
        this.singerTextView = singerTextView;
    }
}
