package com.matrix.matrix_chat.UtilTool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.LoginBean;
import com.matrix.matrix_chat.UI.Activity.LoginActivity;

import retrofit2.Response;

/**
 * @ClassName DataSharaPreferenceManager
 * @Author Create By matrix
 * @Date 2023/5/10 0010 11:21
 */
public class DataSharaPreferenceManager {
    /**
     * 将数据储存
     * @param response
     */
    public static void setSharaPreferenceData(Activity activity, Response<LoginBean> response){
        SharedPreferences sharedPreferences =
                activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("account", response.body().getAccount());
        editor.putString("password", response.body().getPassword());
        editor.commit();
    }

    public static void getSharaPreferenceData(Activity activity){
        SharedPreferences sharedPreferences =
                activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String account = sharedPreferences.getString("account", null);
        String password = sharedPreferences.getString("password", null);
        Toast.makeText(activity,"记录的账号信息:"+account+"___"+password,Toast.LENGTH_SHORT).show();
    }
}
