package com.matrix.matrix_chat.UtilTool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.LoginBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.UserBean;
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
    public static void setSharaPreferenceData(Context context, Response<LoginBean> response){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("account", response.body().getAccount());
        editor.putString("password", response.body().getPassword());
        editor.commit();
    }

    public static void getSharaPreferenceData(Context context){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String account = sharedPreferences.getString("account", null);
        String password = sharedPreferences.getString("password", null);
        Toast.makeText(context,"记录的账号信息:"+account+"___"+password,Toast.LENGTH_SHORT).show();
    }

    /**当前Activity刷新数据时调用**/
    public static void setExtra(Response<LoginBean> response, Intent intent){
        intent.putExtra("U_id",response.body().getId());
        intent.putExtra("U_head",response.body().getHead());
        intent.putExtra("U_name",response.body().getName());
        intent.putExtra("U_password",response.body().getPassword());
        intent.putExtra("U_sex",response.body().getSex());
        intent.putExtra("U_account",response.body().getAccount());
        intent.putExtra("U_phone",response.body().getPhone());
        intent.putExtra("U_email",response.body().getEmail());
        intent.putExtra("U_gptNum",response.body().getGptNum());
        intent.putExtra("U_level",response.body().getLevel());
    }
}
