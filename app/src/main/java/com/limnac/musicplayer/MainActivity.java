package com.limnac.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private View homePageBtn;
    private View listBtn;
    private View communityBtn;
    private View myBtn;
    private HomePageFragment homePageFragment;
    private ListFragment listFragment;
    private CommunityFragment communityFragment;
    private MyFragment myFragment;
    private FragmentManager manager;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        manager = this.getSupportFragmentManager();

        homePageBtn = findViewById(R.id.homepage_layout);
        listBtn = findViewById(R.id.list_layout);
        communityBtn = findViewById(R.id.community_layout);
        myBtn = findViewById(R.id.my_layout);

        homePageBtn.setOnClickListener(this);
        listBtn.setOnClickListener(this);
        communityBtn.setOnClickListener(this);
        myBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.homepage_layout:
                setSelectionTab(0);
                break;
            case R.id.list_layout:
                setSelectionTab(1);
                break;
            case R.id.community_layout:
                setSelectionTab(2);
                break;
            case R.id.my_layout:
                setSelectionTab(3);
                break;
        }
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
     * @param trans
     */
    private void hideFragments(FragmentTransaction trans) {
        if(homePageFragment != null) {
            trans.hide(homePageFragment);
        }
        if(listFragment != null) {
            trans.hide(listFragment);
        }
        if(communityFragment != null) {
            trans.hide(communityFragment);
        }
        if(myFragment != null) {
            trans.hide(myFragment);
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
}