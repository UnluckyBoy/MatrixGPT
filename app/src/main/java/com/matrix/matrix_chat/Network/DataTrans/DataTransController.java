package com.matrix.matrix_chat.Network.DataTrans;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.matrix_chat.Network.API.Back.ArticleContentApi;
import com.matrix.matrix_chat.Network.API.Back.BackChatApi;
import com.matrix.matrix_chat.Network.API.Back.BackCreateImageApi;
import com.matrix.matrix_chat.Network.API.Back.DoGptTransApi;
import com.matrix.matrix_chat.Network.API.Back.LogoutApi;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.ArticleContentBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.BackChatBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.LoginBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.LogoutBean;
import com.matrix.matrix_chat.Network.Service.Back.ArticleContentService;
import com.matrix.matrix_chat.Network.Service.Back.BackChatService;
import com.matrix.matrix_chat.Network.Service.Back.BackCreateImageService;
import com.matrix.matrix_chat.Network.Service.Back.DoGptTransService;
import com.matrix.matrix_chat.Network.Service.Back.LogoutService;
import com.matrix.matrix_chat.R;
import com.matrix.matrix_chat.UITool.MatrixDialogManager;
import com.matrix.matrix_chat.UtilTool.ImageTool;

import org.w3c.dom.Text;

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

    /**
     * 管理员身份获取chat数据
     * @param context
     * @param content
     * @param tempTextView
     */
    public static void adminGetChatData(Context context, String content, TextView tempTextView){
        BackChatApi mAdimnChatApi=new BackChatApi();
        mAdimnChatApi.SetUrl(context.getString(R.string.BackUrl)
                +context.getString(R.string.Url_Gpt));
        BackChatService mAdminChatService=mAdimnChatApi.getService();
        Call<BackChatBean> callAdminChat=mAdminChatService.getState(content);
        callAdminChat.enqueue(new Callback<BackChatBean>() {
            @Override
            public void onResponse(Call<BackChatBean> call, Response<BackChatBean> response) {
                if(response.body()==null){
                    tempTextView.setText(context.getString(R.string.ResponseBodyNull));
                }else {
                    if(response.body().getResult().equals("success")){
                        tempTextView.setText(response.body().getContent());
                    }else{
                        tempTextView.setText(context.getString(R.string.ResponseBodyNull));
                    }
                }
            }
            @Override
            public void onFailure(Call<BackChatBean> call, Throwable t) {
                //SetFailure_ShowView();
                Toast.makeText(context,context.getString(R.string.NetworkFailure),Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *管理员身份获取图片
     * @param context
     * @param content
     * @param tempImageView
     * @param textView
     */
    public static void adminGetImage(Context context, String content, ImageView tempImageView,TextView textView){
        BackCreateImageApi mBackCreateImageApi=new BackCreateImageApi();
        mBackCreateImageApi.SetUrl(context.getString(R.string.BackUrl)
                +context.getString(R.string.Url_Gpt));
        BackCreateImageService mBackCreateImageService=mBackCreateImageApi.getService();
        Call<BackChatBean> callCreateImg=mBackCreateImageService.getState(content);
        callCreateImg.enqueue(new Callback<BackChatBean>() {
            @Override
            public void onResponse(Call<BackChatBean> call, Response<BackChatBean> response) {
                if(!(response.body().equals(null))){
                    if(response.body().getResult().equals("success")){
                        /** 自定义工具类将url资源显示**/
                        ImageTool.SetImageView(tempImageView,response.body().getContent());
                        setTextViewEmpty(textView);
                    }else{
                        setTextViewEmpty(textView);
                        Toast.makeText(context,context.getString(R.string.ResponseBodyNull),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    setTextViewEmpty(textView);
                    Toast.makeText(context,context.getString(R.string.ResponseBodyNull),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BackChatBean> call, Throwable t) {
                SetFailure_ShowView(context);
            }
        });
    }

    /**
     * 用户身份获取chat数据
     * @param activity
     * @param content
     * @param tempView
     * @param account
     * @param temp_intent
     */
    public static void getChatData(Activity activity, String content, TextView tempView, String account, Intent temp_intent){
        BackChatApi mUserChatApi=new BackChatApi();
        mUserChatApi.SetUrl(activity.getString(R.string.BackUrl)
                +activity.getString(R.string.Url_Gpt));
        BackChatService mUserChatService=mUserChatApi.getService();
        Call<BackChatBean> callUserChat=mUserChatService.getState(content);
        callUserChat.enqueue(new Callback<BackChatBean>() {
            @Override
            public void onResponse(Call<BackChatBean> call, Response<BackChatBean> response) {
                if(response.body()==null){
                    tempView.setText(activity.getString(R.string.ResponseBodyNull));
                }else {
                    if(response.body().getResult().equals("success")){
                        tempView.setText(response.body().getContent());
                        DoGptTransApi mDoGptTransApi=new DoGptTransApi();
                        mDoGptTransApi.SetUrl(activity.getString(R.string.BackUrl)
                                +activity.getString(R.string.Url_UserInfo));
                        DoGptTransService mDoGptTransService=mDoGptTransApi.getService();
                        Call<LoginBean> callDoGpt=mDoGptTransService.getState(account);
                        callDoGpt.enqueue(new Callback<LoginBean>() {
                            @Override
                            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                                if(response.body()==null){
                                    tempView.setText(activity.getString(R.string.ResponseBodyNull));
                                }else {
                                    switch (response.body().getResult()){
                                        case "Permission":
                                            temp_intent.putExtra("U_gptNum",response.body().getGptNum());//将数据刷新
                                            break;
                                        case "NullPermission":
                                            //Toast.makeText(view.getContext(),view.getContext().getString(R.string.NullPermission),Toast.LENGTH_SHORT).show();
                                            MatrixDialogManager.ShowAdvertise(activity,account,temp_intent);
                                            break;
                                        case "error":
                                            Toast.makeText(activity,activity.getString(R.string.ResponseBodyNull),Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<LoginBean> call, Throwable t) {
                                SetFailure_ShowView(activity);
                                //Toast.makeText(activity,activity.getString(R.string.NetworkFailure),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        tempView.setText(activity.getString(R.string.ResponseBodyNull));
                    }
                }
            }
            @Override
            public void onFailure(Call<BackChatBean> call, Throwable t) {
                SetFailure_ShowView(activity);
                //Toast.makeText(activity,activity.getString(R.string.NetworkFailure),Toast.LENGTH_SHORT).show();
            }
        });
    }

    /****
     * 普通用户获取图片
     * @param activity
     * @param content
     * @param tempImageView
     * @param textView
     * @param account
     * @param temp_intent
     */
    public static void getImage(Activity activity, String content, ImageView tempImageView,
                                TextView textView,String account, Intent temp_intent){
        BackCreateImageApi mUserBackCreateImage=new BackCreateImageApi();
        mUserBackCreateImage.SetUrl(activity.getString(R.string.BackUrl)
                +activity.getString(R.string.Url_Gpt));
        BackCreateImageService mUserBackCreateImageService=mUserBackCreateImage.getService();
        Call<BackChatBean> callUserCreate=mUserBackCreateImageService.getState(content);
        callUserCreate.enqueue(new Callback<BackChatBean>() {
            @Override
            public void onResponse(Call<BackChatBean> call, Response<BackChatBean> response) {
                if(!(response.body().equals(null))){
                    if(response.body().getResult().equals("success")){
                        /** 自定义工具类将url资源显示**/
                        ImageTool.SetImageView(tempImageView,response.body().getContent());
                        setTextViewEmpty(textView);
                        /**更改用户使用次数**/
                        DoGptTransApi mDoGptTransApi=new DoGptTransApi();
                        mDoGptTransApi.SetUrl(activity.getString(R.string.BackUrl)
                                +activity.getString(R.string.Url_UserInfo));
                        DoGptTransService mDoGptTransService=mDoGptTransApi.getService();
                        Call<LoginBean> callDoGpt=mDoGptTransService.getState(account);
                        callDoGpt.enqueue(new Callback<LoginBean>() {
                            @Override
                            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                                if(response.body()==null){
                                    textView.setText(activity.getString(R.string.ResponseBodyNull));
                                }else {
                                    switch (response.body().getResult()){
                                        case "Permission":
                                            temp_intent.putExtra("U_gptNum",response.body().getGptNum());//将数据刷新
                                            break;
                                        case "NullPermission":
                                            //Toast.makeText(view.getContext(),view.getContext().getString(R.string.NullPermission),Toast.LENGTH_SHORT).show();
                                            //ShowAdvertise(getActivity(),account);
                                            MatrixDialogManager.ShowAdvertise(activity,account,temp_intent);
                                            break;
                                        case "error":
                                            Toast.makeText(activity,activity.getString(R.string.ResponseBodyNull),Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<LoginBean> call, Throwable t) {
                                setTextViewEmpty(textView);
                                SetFailure_ShowView(activity);
                                //Toast.makeText(view.getContext(),"网络错误!请检查!!!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        setTextViewEmpty(textView);
                        Toast.makeText(activity,activity.getString(R.string.ResponseBodyNull),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    setTextViewEmpty(textView);
                    Toast.makeText(activity,activity.getString(R.string.ResponseBodyNull),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BackChatBean> call, Throwable t) {
                SetFailure_ShowView(activity);
                setTextViewEmpty(textView);
            }
        });
    }

    public static void SetFailure_ShowView(Context context){
        Toast.makeText(context,context.getString(R.string.NetworkFailure),Toast.LENGTH_SHORT).show();
    }

    public static void setTextViewEmpty(TextView textView){
        textView.setText("");
    }
}
