package com.limnac.musicplayer.activitys;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.limnac.musicplayer.R;
import com.limnac.musicplayer.fragments.CommunityFragment;
import com.limnac.musicplayer.fragments.HomePageFragment;
import com.limnac.musicplayer.fragments.ListFragment;
import com.limnac.musicplayer.fragments.MyFragment;
import com.limnac.musicplayer.services.PlayService;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 10:54
 * @description com.limnac.musicplayer
 */

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    private View homePageBtn;
    private View listBtn;
    private View communityBtn;
    private View myBtn;
    private HomePageFragment homePageFragment;
    private ListFragment listFragment;
    private CommunityFragment communityFragment;
    private MyFragment myFragment;
    private FragmentManager manager;

    private PlayService mPlayService;

    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            PlayService.MyBinder myBinder = (PlayService.MyBinder) iBinder;
            mPlayService = myBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    //读写权限
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static final int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermission();
        initView();

        Intent intent = new Intent(MainActivity.this, PlayService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private void getPermission(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }

    private void initView() {
        manager = this.getSupportFragmentManager();

        homePageBtn = findViewById(R.id.homepage_layout);
        listBtn = findViewById(R.id.list_layout);
        communityBtn = findViewById(R.id.community_layout);
        myBtn = findViewById(R.id.my_layout);

        homePageBtn.setOnClickListener(v->setSelectionTab(0));
        listBtn.setOnClickListener(v->setSelectionTab(1));
        communityBtn.setOnClickListener(v->setSelectionTab(2));
        myBtn.setOnClickListener(v->setSelectionTab(3));
    }


    public  void setSelectionTab(int index){
        FragmentTransaction trans = manager.beginTransaction();
        setUnSelect();
        hideFragments(trans);

        switch (index){
            case 0:
                this.homePageBtn.setBackgroundColor(0xff0000ff);
                if(homePageFragment==null) {
                    homePageFragment = new HomePageFragment();
                    trans.add(R.id.content,homePageFragment);
                }else{
                    trans.show(homePageFragment);
                }
                break;
            case 1:
                this.listBtn.setBackgroundColor(0xff0000ff);
                if(listFragment==null) {
                    listFragment = new ListFragment();
                    trans.add(R.id.content,listFragment);
                }else{
                    trans.show(listFragment);
                }
                break;
            case 2:
                this.communityBtn.setBackgroundColor(0xff0000ff);
                if(communityFragment==null) {
                    communityFragment = new CommunityFragment();
                    trans.add(R.id.content,communityFragment);
                }else{
                    trans.show(communityFragment);
                }
                break;
            case 3:
                this.myBtn.setBackgroundColor(0xff0000ff);
                if(myFragment==null) {
                    myFragment = new MyFragment();
                    trans.add(R.id.content,myFragment);
                }else{
                    trans.show(myFragment);
                }
                break;
        }
        trans.commit();
    }

    /**
     * 隐藏所有的Fragment
     */
    private void hideFragments(FragmentTransaction trans) {
        hideFragment(trans,homePageFragment);
        hideFragment(trans,listFragment);
        hideFragment(trans,communityFragment);
        hideFragment(trans,myFragment);
    }

    private void hideFragment(FragmentTransaction trans, Fragment fragment){
        if(fragment !=null){
            trans.hide(fragment);
        }
    }

    /**
     * 将所有按钮设置为非选择状态
     */
    private void setUnSelect() {
        this.homePageBtn.setBackgroundColor(0xffffffff);
        this.listBtn.setBackgroundColor(0xffffffff);
        this.communityBtn.setBackgroundColor(0xffffffff);
        this.myBtn.setBackgroundColor(0xffffffff);
    }

    public PlayService getPlayService(){
        return mPlayService;
    }

}