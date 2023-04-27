package com.matrix.matrix_chat.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * @ClassName BaseActivity
 * @Author Create By Administrator
 * @Date 2023/4/18 0018 13:23
 */
public abstract class BaseActivity extends Activity {

    ArrayList<Activity> activities = new ArrayList<Activity>();
    /**屏幕宽*/
    public static int SCREEN_WIDTH;
    /**屏幕高*/
    public static int SCREEN_HEIGHT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        addActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity();
    }

    public void addActivity() {
        activities.add(this);
    }

    public void removeActivity() {
        activities.remove(this);
    }

    /**
     * 结束所有活动
     */
    public void finishAllActivity() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 跳转
     */
    public void startAction(@SuppressWarnings("rawtypes") Class cls,
                            String name, String value) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(name, value);
        startActivity(intent);
    }
}
