package com.matrix.matrixgpt.UtilTool;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.matrix.matrixgpt.UtilTool.AdverUtil.config.TTAdManagerHolder;

/**
 * @ClassName MatrixApplication
 * @Author Create By Administrator
 * @Date 2023/4/17 0017 19:01
 */
public class MatrixApplication extends MultiDexApplication {
    public static String PROCESS_NAME_XXXX = "process_name_xxxx";
    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        MatrixApplication.context = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //穿山甲SDK初始化
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现context为null的异常
        TTAdManagerHolder.init(this);

    }

    public static Context getAppContext() {
        return MatrixApplication.context;
    }
}
