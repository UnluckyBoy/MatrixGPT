package com.matrix.matrix_chat.Network.DataTrans;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.matrix_chat.Network.API.Back.ArticleContentApi;
import com.matrix.matrix_chat.Network.API.Back.BackChatApi;
import com.matrix.matrix_chat.Network.API.Back.BackCreateImageApi;
import com.matrix.matrix_chat.Network.API.Back.DoGptTransApi;
import com.matrix.matrix_chat.Network.API.Back.GetAccessTokenApi;
import com.matrix.matrix_chat.Network.API.Back.GetRecognitionApi;
import com.matrix.matrix_chat.Network.API.Back.LogoutApi;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.AccessTokenBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.ArticleContentBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.BackChatBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.LoginBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.LogoutBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.WordsResult;
import com.matrix.matrix_chat.Network.Service.Back.ArticleContentService;
import com.matrix.matrix_chat.Network.Service.Back.BackChatService;
import com.matrix.matrix_chat.Network.Service.Back.BackCreateImageService;
import com.matrix.matrix_chat.Network.Service.Back.DoGptTransService;
import com.matrix.matrix_chat.Network.Service.Back.GetAccessTokenService;
import com.matrix.matrix_chat.Network.Service.Back.GetRecognitionService;
import com.matrix.matrix_chat.Network.Service.Back.LogoutService;
import com.matrix.matrix_chat.R;
import com.matrix.matrix_chat.UITool.MatrixDialogManager;
import com.matrix.matrix_chat.UtilTool.ImageTool;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
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

    public static void getArticleContent(Context context, String title, String author, WebView tempView){
        ArticleContentApi articleContentApi=new ArticleContentApi();
        articleContentApi.SetUrl(context.getString(R.string.BackUrl)+context.getString(R.string.Url_Article));
        ArticleContentService articleContentService=articleContentApi.getService();
        Call<ArticleContentBean> call_content=articleContentService.getState(title,author);
        call_content.enqueue(new Callback<ArticleContentBean>() {
            @Override
            public void onResponse(Call<ArticleContentBean> call, Response<ArticleContentBean> response) {
                if(response.body()!=null){
                    //tempView.setText(response.body().getArticle_content());
                    String htmlContent=null;
                    if(response.body().getArticle_content().split("&").length>1){
                        String content_temp=response.body().getArticle_content().split("&")[0];
                        String[] html_p_num=content_temp.split("\n");

                        StringBuilder stringBuilder = new StringBuilder();
                        for (String string : html_p_num) {
                            stringBuilder.append("<p style=\"text-indent: 2em;\">").append(string).append("</p>");
                        }

                        StringBuilder imgBuilder = new StringBuilder();
                        String image_Str=response.body().getArticle_content().split("&")[1];
                        List<String> image_temp=extractList(image_Str);
                        for (String list : image_temp) {
                            imgBuilder.append("<img src=\""+context.getString(R.string.BackUrl)+
                                    context.getString(R.string.Url_GetFile)+context.getString(R.string.Url_Art_image)).append(list).append("\"/>");
                        }
                        htmlContent = "<html><head><style>"+"p{font-size:46px;text-indent:2em;}"+
                                "div.container { display: flex; }"+"div.container img {width: 33.33%;}"+"</style></head><body>"
                                +stringBuilder+"<div class=\"container\">"+imgBuilder+"</div>"+"</body></html>";
                    }else{
                        //tempView.setText(response.body().getArticle_content());
                        String[] html_p_num=response.body().getArticle_content().split("\n");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String string : html_p_num) {
                            stringBuilder.append("<p>").append(string).append("</p>");
                        }
                        htmlContent = "<html><head><style>"+"p{font-size:46px;text-indent:2em;}"+"</style></head><body>"
                                +stringBuilder.toString() + "</body></html>";
                    }
                    tempView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
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
                                            temp_intent.putExtra(activity.getString(R.string.info_gptNum),response.body().getGptNum());//将数据刷新
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
                                            temp_intent.putExtra(activity.getString(R.string.info_gptNum),response.body().getGptNum());//将数据刷新
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

    public static void getBaiduRecognitionData(Context context,String image_url_code,String api_key,String secret_key,String grant_type,TextView textView){
        /**获取access_token**/
        GetAccessTokenApi getAccessTokenApi=new GetAccessTokenApi();
        getAccessTokenApi.SetUrl(context.getString(R.string.token_url));
        GetAccessTokenService getAccessTokenService=getAccessTokenApi.getService();
        Call<AccessTokenBean> call_token=getAccessTokenService.getState(api_key,secret_key,grant_type);
        call_token.enqueue(new Callback<AccessTokenBean>() {
            @Override
            public void onResponse(Call<AccessTokenBean> call, Response<AccessTokenBean> response) {
                if(response.body()!=null){
                    //mShow_View.setText(response.body().getAccess_token());
                    /**获取识别内容**/
                    GetRecognitionApi getRecognitionApi=new GetRecognitionApi();
                    getRecognitionApi.SetUrl(context.getString(R.string.recognition_url));
                    GetRecognitionService getRecognitionService=getRecognitionApi.getService();
                    //MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                    //RequestBody body = RequestBody.create(mediaType,"image="+temp);
                    RequestBody requestBody=RequestBody.create(
                            MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"),"image="+image_url_code);
                    Call<WordsResult> call_recognition=getRecognitionService.getState(response.body().getAccess_token(),requestBody);
                    call_recognition.enqueue(new Callback<WordsResult>() {
                        @Override
                        public void onResponse(Call<WordsResult> call, Response<WordsResult> response) {
                            if(response.body()!=null){
                                if(response.body().getWords_result()!=null){
                                    String word_temp="";
                                    for(int i=0;i<response.body().getWords_result().size();i++){
                                        word_temp+=response.body().getWords_result().get(i).getWords()+"\n";
                                    }
                                    textView.setText(word_temp);
                                }else {
                                    //Toast.makeText(context, "识别失败", Toast.LENGTH_SHORT).show();
                                    textView.setText(context.getString(R.string.recognition_list_null));
                                }
                            }else{
                                //Toast.makeText(context, "识别失败,请检查图片是否符合", Toast.LENGTH_SHORT).show();
                                textView.setText(context.getString(R.string.recognition_list_null));
                            }
                        }

                        @Override
                        public void onFailure(Call<WordsResult> call, Throwable t) {
                            SetFailure_ShowView(context);
                        }
                    });

                }else{
                    Toast.makeText(context, "获取失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccessTokenBean> call, Throwable t) {
                SetFailure_ShowView(context);
            }
        });
    }

    public static void SetFailure_ShowView(Context context){
        Toast.makeText(context,context.getString(R.string.NetworkFailure),Toast.LENGTH_SHORT).show();
    }

    public static void setTextViewEmpty(TextView textView){
        textView.setText("");
    }

    /***
     * 正则表达式将字符串的list.tostring提取为list
     * @param input
     * @return
     */
    private static List<String> extractList(String input) {
        List<String> fileList = new ArrayList<>();
        // 使用正则表达式提取文件名
        Pattern pattern = Pattern.compile("\\{([^{}]+)\\}");
        Matcher matcher = pattern.matcher(input);
        // 遍历匹配结果，并将文件名添加到列表中
        while (matcher.find()) {
            String fileName = matcher.group(1);
            fileList.add(fileName);
        }
        return fileList;
    }
}
