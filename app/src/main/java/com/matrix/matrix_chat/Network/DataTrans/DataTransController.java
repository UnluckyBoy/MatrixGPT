package com.matrix.matrix_chat.Network.DataTrans;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.matrix_chat.Network.API.Back.ArticleContentApi;
import com.matrix.matrix_chat.Network.API.Back.LogoutApi;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.ArticleContentBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.LogoutBean;
import com.matrix.matrix_chat.Network.Service.Back.ArticleContentService;
import com.matrix.matrix_chat.Network.Service.Back.LogoutService;
import com.matrix.matrix_chat.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ClassName UserDataController
 * @Author Create By matrix
 * @Date 2023/5/8 0008 21:44
 */
public class DataTransController {

    /**
     * 登出
     * @param context
     * @param account
     * @param password
     *
     * 结果:success,error,unlogin
     */
    public static void doLogout(Context context,String account,String password){
        //boolean result;
        LogoutApi logoutApi=new LogoutApi();
        logoutApi.SetUrl(context.getString(R.string.BackUrl)
                +context.getString(R.string.Url_UserInfo));
        LogoutService logoutService=logoutApi.getService();
        Call<LogoutBean> callLogout=logoutService.getState(account,password);
        callLogout.enqueue(new Callback<LogoutBean>() {
            @Override
            public void onResponse(Call<LogoutBean> call, Response<LogoutBean> response) {
                if(response.body()!=null){
                    if(response.body().getResult()=="success"){

                    }
                }
            }

            @Override
            public void onFailure(Call<LogoutBean> call, Throwable t) {

            }
        });
    }

    public static void getArticleContent(Context context, String title, String author, TextView tempView){
        ArticleContentApi articleContentApi=new ArticleContentApi();
        articleContentApi.SetUrl(context.getString(R.string.BackUrl)+context.getString(R.string.Url_Article));
        ArticleContentService articleContentService=articleContentApi.getService();
        Call<ArticleContentBean> call_content=articleContentService.getState(title,author);
        call_content.enqueue(new Callback<ArticleContentBean>() {
            @Override
            public void onResponse(Call<ArticleContentBean> call, Response<ArticleContentBean> response) {
                if(response.body()!=null){
                    tempView.setText(response.body().getArticle_content());
                }else{
                    Toast.makeText(context, context.getString(R.string.ResponseBodyNull), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArticleContentBean> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.NetworkFailure), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
