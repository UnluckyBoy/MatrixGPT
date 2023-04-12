package com.matrix.matrixgpt.UI.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.matrix.matrixgpt.Network.ResponseBean.BackService.UserBean;
import com.matrix.matrixgpt.R;
import com.matrix.matrixgpt.UI.Fragment.ChatFragment;
import com.matrix.matrixgpt.UI.Fragment.MainFragment;
import com.matrix.matrixgpt.UI.Fragment.UserFragment;
import com.matrix.matrixgpt.UITool.MatrixDialog;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private final Context TGA=MainActivity.this;
    private Intent intent_MainActivity;

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

    private static boolean mBackKeyPressed = false;//记录是否有首次按键

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
        /**获取用户信息**/
        intent_MainActivity=getIntent();
//        UserBean mUser_MainActivity=new UserBean();
//        mUser_MainActivity.setId(Integer.parseInt(intent_MainActivity.getStringExtra("U_id")));
//        mUser_MainActivity.setImage(intent_MainActivity.getStringExtra("U_image"));
//        mUser_MainActivity.setName(intent_MainActivity.getStringExtra("U_name"));
//        mUser_MainActivity.setPassword(intent_MainActivity.getStringExtra("U_pwd"));
//        mUser_MainActivity.setSex(intent_MainActivity.getStringExtra("U_sex"));
//        mUser_MainActivity.setAccount(intent_MainActivity.getStringExtra("U_account"));
//        mUser_MainActivity.setPhone(intent_MainActivity.getStringExtra("U_phone"));
//        mUser_MainActivity.setEmail(intent_MainActivity.getStringExtra("U_email"));
//        mUser_MainActivity.setGptNum(Integer.parseInt(intent_MainActivity.getStringExtra("U_gptNum")));
        String account_id=intent_MainActivity.getStringExtra("U_account");

        if(account_id==null){
            ShowDialog(TGA);//提示登录

            mMainFragment = MainFragment.newInstance("");
            mChatFragment= ChatFragment.newInstance("");
            mUserFragment=UserFragment.newInstance("");
        }else{
            mMainFragment = MainFragment.newInstance(account_id);
            mChatFragment= ChatFragment.newInstance(account_id);
            mUserFragment=UserFragment.newInstance(account_id);
        }
        mFragmentContainer = new Fragment[]{mMainFragment, mChatFragment, mUserFragment};
        mainFrame = (FrameLayout) findViewById(R.id.fragment_container);
        //设置fragment到布局
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mMainFragment)
                .show(mMainFragment)
                .commit();

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
                    /**
                     * 这里因为需要对3个fragment进行切换
                     * 如果只是想测试按钮点击，不管fragment的切换，可以把start到end里面的内容去掉
                     */
                    //start
                    if (mLastFragmentTag != 0) {
                        switchFragment(mLastFragmentTag, 0);
                        mLastFragmentTag = 0;
                    }
                    //end
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

    /**
     * 退出检测
     */
    @Override
    public void onBackPressed() {
        if(!mBackKeyPressed){
            Toast.makeText(this, "再滑一次退出", Toast.LENGTH_SHORT).show();
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        }
        else{
            //退出程序
            this.finish();
            System.exit(0);
        }
    }

    public void ShowDialog(final Context mContext) {
        String[] names = {mContext.getString(R.string.SystemTitle),
                mContext.getString(R.string.loginTitle),
                mContext.getString(R.string.Confirm),mContext.getString(R.string.Cancel) };

        /**MatrixDialog中最后两个按钮的顺序与names的文本顺序相反**/
        MatrixDialog mDialog = new MatrixDialog(mContext, names, true);
        mDialog.setOnClickListener2LastTwoItems(new MatrixDialog.OnClickListener2LastTwoItem() {
            /**取消按钮**/
            @Override
            public void onClickListener2LastItem() {
                intent_MainActivity.putExtra("U_account","");
                mDialog.dismiss();
            }
            /**确定按钮**/
            @Override
            public void onClickListener2SecondLastItem() {
                //Toast.makeText(mContext, "点击了确定", Toast.LENGTH_SHORT).show();
                Intent login_Intent=new Intent(TGA,LoginActivity.class);
                startActivity(login_Intent);
                finish();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }
}