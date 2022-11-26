package com.limnac.musicplayer.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.limnac.musicplayer.R;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 10:54
 * @description com.limnac.musicplayer
 */

public class PlayActivity extends AppCompatActivity {

    private final int REPEAT_MODEL = 0;
    private final int REPEAR_ONCE_MODEL = 1;
    private final int SHUFFLE = 2;
    private int repeat_count = -1;
    private boolean isPlay = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ImageButton btn_repeat_model = findViewById(R.id.repeat_model_ibtn);
        btn_repeat_model.setOnClickListener(v-> changeRepeatModel(btn_repeat_model));
        ImageButton btn_play_pause = findViewById(R.id.play_pause_ibtn);
        btn_play_pause.setOnClickListener(v-> play2pause(btn_play_pause));
    }

    private void play2pause(ImageButton ibtn){
        if(isPlay){
            isPlay = false;
            ibtn.setImageResource(R.drawable.vector_drawable_pause);
        }else{
            isPlay = true;
            ibtn.setImageResource(R.drawable.vector_drawable_play);
        }
    }

    private void changeRepeatModel(ImageButton ibtn){
        repeat_count = (repeat_count+1)%3;
        if(repeat_count==REPEAT_MODEL){
            ibtn.setImageResource(R.drawable.vector_drawable_repeatonce);
        }else if(repeat_count==REPEAR_ONCE_MODEL){
            ibtn.setImageResource(R.drawable.vector_drawable_shuffle);
        }else if(repeat_count==SHUFFLE){
            ibtn.setImageResource(R.drawable.vector_drawable_repeat);
        }
    }

    public static void startPlayActivity(Context context){
        Intent intent = new Intent(context,PlayActivity.class);
        context.startActivity(intent);
    }
}