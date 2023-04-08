package com.matrix.matrixgpt.UI.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.matrix.matrixgpt.R;
import com.matrix.matrixgpt.UI.Fragment.ChatFragment;
import com.matrix.matrixgpt.UI.Fragment.MainFragment;
import com.matrix.matrixgpt.UI.Fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private FrameLayout mainFrame;
    // 持有对应Fragment的对象
    private MainFragment mMainFragment;
    private ChatFragment mChatFragment;
    private UserFragment mUserFragment;
    // 用于存放fragment的数组
    private Fragment[] mFragmentContainer;
    // 用于标记最后一个fragment的标签
    public int mLastFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermission();
        initView();
    }

    /***
     * 动态获取申请读写权限
     */
    private void getPermission(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        //申请写权限
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        //申请创建文件
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);
        }

        //网络权限
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }
    }

    private void initView() {
        mMainFragment = new MainFragment();
        mChatFragment = new ChatFragment();
        mUserFragment = new UserFragment();
        mFragmentContainer = new Fragment[]{mMainFragment, mChatFragment, mUserFragment};
        mainFrame = (FrameLayout) findViewById(R.id.fragment_container);
        //设置fragment到布局
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mMainFragment).show(mMainFragment).commit();

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        //这里是bottomnavigationview的点击事件
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_mainIcon:
                    //这里因为需要对3个fragment进行切换
                    //start
                    if (mLastFragmentTag != 0) {
                        switchFragment(mLastFragmentTag, 0);
                        mLastFragmentTag = 0;
                    }
                    //end
                    //如果只是想测试按钮点击，不管fragment的切换，可以把start到end里面的内容去掉
                    return true;
                case R.id.action_chatIcon:
                    if (mLastFragmentTag != 1) {
                        switchFragment(mLastFragmentTag, 1);
                        mLastFragmentTag = 1;
                    }
                    return true;
                case R.id.action_userIcon:
                    if (mLastFragmentTag != 2) {
                        switchFragment(mLastFragmentTag, 2);
                        mLastFragmentTag = 2;
                    }
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    /**
     *切换fragment
     */
    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(mFragmentContainer[lastfragment]);
        if (mFragmentContainer[index].isAdded() == false) {
            transaction.add(R.id.fragment_container, mFragmentContainer[index]);
        }
        transaction.show(mFragmentContainer[index]).commitAllowingStateLoss();
    }

    /** 半透明状态栏*/
    protected void setHalfTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}