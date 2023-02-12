package com.limnac.musicplayer.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.drake.logcat.LogCat;
import com.limnac.musicplayer.R;
import com.limnac.musicplayer.callback.UpdateCallBack;
import com.limnac.musicplayer.manager.MusicManager;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        setContentView(R.layout.activity_start);

        checkAndGetPermission();

    }

    //读写权限
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static final int REQUEST_PERMISSION_CODE = 1;

    private void checkAndGetPermission(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }else{
                loadMusicList();
            }
        }else{
            loadMusicList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadMusicList();
            } else {
                Toast.makeText(getApplicationContext(), "请检查应用权限后重试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadMusicList(){
        MusicManager.getInstance().update(new UpdateCallBack() {
            @Override
            public void success() {
                MainActivity.startActivity(StartActivity.this);
                finish();
            }

            @Override
            public void onFailed(int errorCode, String msg) {
                LogCat.e("errorCode: "+errorCode+", msg: "+msg,TAG);
            }
        });
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context,StartActivity.class);
        context.startActivity(intent);
    }
}