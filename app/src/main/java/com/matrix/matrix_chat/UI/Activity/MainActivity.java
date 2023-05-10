package com.matrix.matrix_chat.UI.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.matrix.matrix_chat.Network.API.Back.LogoutApi;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.LogoutBean;
import com.matrix.matrix_chat.Network.Service.Back.LogoutService;
import com.matrix.matrix_chat.R;
import com.matrix.matrix_chat.UI.Fragment.ChatFragment;
import com.matrix.matrix_chat.UI.Fragment.MainFragment;
import com.matrix.matrix_chat.UI.Fragment.UserFragment;
import com.matrix.matrix_chat.UITool.MatrixDialogManager;
import com.matrix.matrix_chat.UtilTool.Pwd3DESUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final Context TGA=MainActivity.this;
    private Intent intent_MainActivity;
    private static String PASSWORD_EncryKEY = "EncryptionKey_By-WuMing";//自定义密钥:EncryptionKey_By-WuMing

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

        WebView.setWebContentsDebuggingEnabled(true);

        setContentView(R.layout.activity_main);

        ConfirmFirstStart();
        intent_MainActivity=getIntent();
        initView();
        setActionBar();
    }

    /**设置状态栏**/
    private void setActionBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void initView() {
        /**获取用户信息**/
        String account_id=intent_MainActivity.getStringExtra("U_account");

        if(account_id==null){
            MatrixDialogManager.hintLoginDialog(TGA,intent_MainActivity,this,LoginActivity.class);//提示登录

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

        //bottomNavigation的点击事件
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
        //transaction.replace();
        if (mFragmentContainer[index].isAdded() == false) {
            transaction.add(R.id.fragment_container, mFragmentContainer[index]);
        }
        transaction.show(mFragmentContainer[index]).commitAllowingStateLoss();
    }

    private void ConfirmFirstStart(){
        SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
        //默认false
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //如果是true就表示已经不是第一次运行app，跳转相应界面就可以

        if (isFirstRun) {
            //不是第一次运行

        } else {
            //第一次运行
            getPermission();

            editor.putBoolean("isFirstRun", true);
            editor.commit();
        }
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
            logoutTrans();
            this.finish();
            //System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void logoutTrans(){
        /**
         * 登出
         * 结果:success,error,unlogin
         */
        String account=intent_MainActivity.getStringExtra("U_account");
        String password= Pwd3DESUtil.decode3Des(PASSWORD_EncryKEY,intent_MainActivity.getStringExtra("U_password"));
        LogoutApi logoutApi=new LogoutApi();
        logoutApi.SetUrl(TGA.getString(R.string.BackUrl)
                +TGA.getString(R.string.Url_UserInfo));
        LogoutService logoutService=logoutApi.getService();
        Call<LogoutBean> callLogout=logoutService.getState(account,password);
        callLogout.enqueue(new Callback<LogoutBean>() {
            @Override
            public void onResponse(Call<LogoutBean> call, Response<LogoutBean> response) {
                if(response.body()!=null){
                    if(response.body().getResult()=="success"){
                        Toast.makeText(TGA,"登出",Toast.LENGTH_SHORT).show();
                        getSystemExit();
                    }else if(response.body().getResult()=="unlogin"){
                        Toast.makeText(TGA,"尚未登录",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(TGA,"登出异常",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LogoutBean> call, Throwable t) {
                Toast.makeText(TGA,TGA.getString(R.string.NetworkFailure),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSystemExit(){
        System.exit(0);
    }

    /**
     * 判断app是否处于前台
     * @param context
     * @return
     */
    public static boolean isAppForeground(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Service.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfoList==null){
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfoList) {
            if (processInfo.processName.equals(context.getPackageName()) &&
                    processInfo.importance==ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                return true;
            }
        }
        return false;
    }
}