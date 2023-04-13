package com.matrix.matrixgpt.UI.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.matrix.matrixgpt.Network.API.Back.BackChatApi;
import com.matrix.matrixgpt.Network.API.Back.BackCreateImageApi;
import com.matrix.matrixgpt.Network.API.Back.DoGptTransApi;
import com.matrix.matrixgpt.Network.API.GPT.ChatApi;
import com.matrix.matrixgpt.Network.API.GPT.CreateImageApi;
import com.matrix.matrixgpt.Network.GptRequestBody.ChatRequestBody;
import com.matrix.matrixgpt.Network.GptRequestBody.CreImgRequestBody;
import com.matrix.matrixgpt.Network.ResponseBean.BackService.BackChatBean;
import com.matrix.matrixgpt.Network.ResponseBean.BackService.LoginBean;
import com.matrix.matrixgpt.Network.ResponseBean.GPT.ChatBean;
import com.matrix.matrixgpt.Network.ResponseBean.GPT.CreateImageBean;
import com.matrix.matrixgpt.Network.Service.Back.BackChatService;
import com.matrix.matrixgpt.Network.Service.Back.BackCreateImageService;
import com.matrix.matrixgpt.Network.Service.Back.DoGptTransService;
import com.matrix.matrixgpt.Network.Service.GPT.ChatService;
import com.matrix.matrixgpt.Network.Service.GPT.CreateImageService;
import com.matrix.matrixgpt.R;
import com.matrix.matrixgpt.UITool.MatrixDialog;
import com.matrix.matrixgpt.UtilTool.ImageTool;
import com.matrix.matrixgpt.UtilTool.TimeTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {
    private final String mImagePath="/sdcard/Download";
    private final Context TGA=getContext();

    private Intent intent_MainFragment;
    private View view;

    private EditText mEditText;
    //private Button mForecastBtn,mReadBtn,mChatBtn,mPaintBbtn;
    private TextView mShow_View;
    private ImageView mImage_View;

    private int visitor_Num=2;

    public static MainFragment newInstance(String param1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("args_account",param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view!=null){
            ViewGroup parent=(ViewGroup) view.getParent();
            if(parent!=null){
                parent.removeView(view);
            }
        }else{
            view = inflater.inflate(R.layout.main_fragment, container, false);

            Bundle bundle = getArguments();
            String args_account = bundle.getString("args_account");
            Init_Component(view,args_account);
        }

        return view;
    }

    private void Init_Component(View view,String account) {
        Button mChatBtn,mPaintBtn;
        mChatBtn=view.findViewById(R.id.chat_btn);
        mPaintBtn=view.findViewById(R.id.paint_btn);
        mShow_View=view.findViewById(R.id.show_View);
        //mShow_View.setMovementMethod(ScrollingMovementMethod.getInstance());//添加文本视图滚动条

        mEditText=view.findViewById(R.id.edit_text);
        mImage_View=view.findViewById(R.id.image_View);

        mImage_View.setOnClickListener(new ImageClickListener());

        mChatBtn.setOnClickListener(new ClickListener());
        mPaintBtn.setOnClickListener(new ClickListener());
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //Toast.makeText(view.getContext(),"点击了聊天按钮",Toast.LENGTH_SHORT).show();
            final int mBtnId=v.getId();
            switch(mBtnId){
                case R.id.chat_btn:
                    /**解答**/
                    String content=mEditText.getText().toString();//获取输入内容
                    if(content.equals(null)||content.equals("")){
                        Toast.makeText(view.getContext(),view.getContext().getString(R.string.EditIsNull),Toast.LENGTH_SHORT).show();
                    }else{
                        //OnAndroidGetChat(content);
                        GetBackChatData(content,"chat");
                    }
                    mShow_View.setMovementMethod(ScrollingMovementMethod.getInstance());//添加文本视图滚动条
                    mImage_View.setVisibility(View.GONE);//显示文本时，图片隐藏
                    //mImage_View.setVisibility(View.VISIBLE);//图片显示
                    break;
                case R.id.paint_btn:
                    /**绘画**/
                    String prompt=mEditText.getText().toString();//获取输入内容
                    if(prompt.equals(null)||prompt.equals("")){
                        Toast.makeText(view.getContext(),view.getContext().getString(R.string.EditIsNull),Toast.LENGTH_SHORT).show();
                    }else{
                        //OnAndroidGetCreateImage(prompt);
                        GetBackChatData(prompt,"createImage");
                    }
                    //mShow_View.setVisibility(View.GONE);//显示图片时，文本隐藏
                    break;
            }
        }
    }

    /**
     * @param content 内容
     * @param openAi_type 获取的接口类型
     */
    private void GetBackChatData(String content,String openAi_type){
        intent_MainFragment=getActivity().getIntent();
        if(intent_MainFragment.getStringExtra("U_account").equals(null)||
                intent_MainFragment.getStringExtra("U_account").equals("")){
            //游客登录
            //Toast.makeText(view.getContext(),"游客登录",Toast.LENGTH_SHORT).show();
            if(visitor_Num<=0){
                Toast.makeText(view.getContext(),"次数使用完",Toast.LENGTH_SHORT).show();
//                String[] names = {TGA.getString(R.string.SystemTitle),
//                        TGA.getString(R.string.visitor_NumAdd),
//                        TGA.getString(R.string.Confirm), TGA.getString(R.string.Cancel)};
//                /**调用窗口方法**/
//                new MatrixDialogManager().ShowMatrixDialog(names,getActivity(), MainActivity.class);
            }else{
                //visitor_Num--;//测试
                VisitorHandBackChat(content,openAi_type);
            }
        }else{
            //用户登录
            //Toast.makeText(view.getContext(),"用户登录"+intent_MainFragment.getStringExtra("U_account"),Toast.LENGTH_SHORT).show();
            int mLevel=intent_MainFragment.getIntExtra("U_level",0);
            int mGptNum=intent_MainFragment.getIntExtra("U_gptNum",0);
            UserHandBackChat(content,openAi_type,mLevel,mGptNum);
        }
    }

    /**游客调用后台API获取Chat数据**/
    private void VisitorHandBackChat(String content,String openAi_type){
        SetShowViewLoad();
        switch (openAi_type){
            case "chat":
                BackChatApi mBackChatApi=new BackChatApi();
                BackChatService mBackChatService=mBackChatApi.getService();
                Call<BackChatBean> callBackChat=mBackChatService.getState(content);
                callBackChat.enqueue(new Callback<BackChatBean>() {
                    @Override
                    public void onResponse(Call<BackChatBean> call, Response<BackChatBean> response) {
                        if(response.body()==null){
                            //Toast.makeText(view.getContext(),"意料之外的错误!!!",Toast.LENGTH_SHORT).show();
                            mShow_View.setText(view.getContext().getString(R.string.ResponseBodyNull));
                        }else {
                            if(response.body().getResult().equals("success")){
                                mShow_View.setText(response.body().getContent());
                                visitor_Num--;
                            }else{
                                //Toast.makeText(view.getContext(),"意料之外的错误!!!",Toast.LENGTH_SHORT).show();
                                mShow_View.setText(view.getContext().getString(R.string.ResponseBodyNull));
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<BackChatBean> call, Throwable t) {
                        SetFailure_ShowView();
                        //Toast.makeText(view.getContext(),"网络错误!请检查!!!",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "createImage":
                //Toast.makeText(view.getContext(),"绘画功能暂未实现",Toast.LENGTH_SHORT).show();
                BackCreateImageApi mBackCreateImageApi=new BackCreateImageApi();
                BackCreateImageService mBackCreateImageService=mBackCreateImageApi.getService();
                Call<BackChatBean> callCreateImg=mBackCreateImageService.getState(content);
                callCreateImg.enqueue(new Callback<BackChatBean>() {
                    @Override
                    public void onResponse(Call<BackChatBean> call, Response<BackChatBean> response) {
                        if(!(response.body().equals(null))){
                            if(response.body().getResult().equals("success")){
                                /** 自定义工具类将url资源显示**/
                                ImageTool.SetImageView(mImage_View,response.body().getContent());
                                visitor_Num--;
                            }else {
                                Toast.makeText(view.getContext(),view.getContext().getString(R.string.ResponseBodyNull),Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(view.getContext(),view.getContext().getString(R.string.ResponseBodyNull),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BackChatBean> call, Throwable t) {
                        SetFailure_ShowView();
                    }
                });
                break;
        }
    }

    /**用户调用后台API获取Chat数据**/
    private void UserHandBackChat(String content,String openAi_type,int level,int gptnum){
        switch (openAi_type){
            case "chat":
                switch (level){
                    case 1:
                        //Toast.makeText(view.getContext(),"管理员,无限次使用",Toast.LENGTH_SHORT).show();
                        SetShowViewLoad();
                        BackChatApi mBackChatApi=new BackChatApi();
                        BackChatService mBackChatService=mBackChatApi.getService();
                        Call<BackChatBean> callBackChat=mBackChatService.getState(content);
                        callBackChat.enqueue(new Callback<BackChatBean>() {
                            @Override
                            public void onResponse(Call<BackChatBean> call, Response<BackChatBean> response) {
                                if(response.body()==null){
                                    mShow_View.setText(view.getContext().getString(R.string.ResponseBodyNull));
                                }else {
                                    if(response.body().getResult().equals("success")){
                                        mShow_View.setText(response.body().getContent());
                                    }else{
                                        mShow_View.setText(view.getContext().getString(R.string.ResponseBodyNull));
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<BackChatBean> call, Throwable t) {
                                SetFailure_ShowView();
                                //Toast.makeText(view.getContext(),"网络错误!请检查!!!",Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 0:
                        /**普通用户使用Chat**/
                        //Toast.makeText(view.getContext(),"普通用户,可用次数:"+gptnum,Toast.LENGTH_SHORT).show();
                        if(gptnum>0){
                            DoGptTransApi mDoGptTransApi=new DoGptTransApi();
                            DoGptTransService mDoGptTransService=mDoGptTransApi.getService();
                            Call<LoginBean> callDoGpt=mDoGptTransService.getState(content);
                            callDoGpt.enqueue(new Callback<LoginBean>() {
                                @Override
                                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                                    if(response.body()==null){
                                        mShow_View.setText(view.getContext().getString(R.string.ResponseBodyNull));
                                    }else {
                                        switch (response.body().getResult()){
                                            case "Permission":

                                                break;
                                            case "NullPermission":
                                                Toast.makeText(view.getContext(),view.getContext().getString(R.string.NullPermission),Toast.LENGTH_SHORT).show();
                                                break;
                                            case "error":
                                                Toast.makeText(view.getContext(),view.getContext().getString(R.string.ResponseBodyNull),Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<LoginBean> call, Throwable t) {
                                    SetFailure_ShowView();
                                    //Toast.makeText(view.getContext(),"网络错误!请检查!!!",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(view.getContext(),view.getContext().getString(R.string.NullPermission),Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                break;
            case "createImage":
                switch (level){
                    case 1:
                        /**管理员**/
                        SetShowViewLoad();
                        BackCreateImageApi mBackCreateImageApi=new BackCreateImageApi();
                        BackCreateImageService mBackCreateImageService=mBackCreateImageApi.getService();
                        Call<BackChatBean> callCreateImg=mBackCreateImageService.getState(content);
                        callCreateImg.enqueue(new Callback<BackChatBean>() {
                            @Override
                            public void onResponse(Call<BackChatBean> call, Response<BackChatBean> response) {
                                if(!(response.body().equals(null))){
                                    if(response.body().getResult().equals("success")){
                                        /** 自定义工具类将url资源显示**/
                                        ImageTool.SetImageView(mImage_View,response.body().getContent());
                                    }else {
                                        Toast.makeText(view.getContext(),view.getContext().getString(R.string.ResponseBodyNull),Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(view.getContext(),view.getContext().getString(R.string.ResponseBodyNull),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BackChatBean> call, Throwable t) {
                                SetFailure_ShowView();
                            }
                        });
                        break;
                    case 0:
                        Toast.makeText(view.getContext(),"普通用户,可用次数:"+gptnum,Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    /**直接获取OpenAi数据**/
    private void OnAndroidGetChat(String content){
        SetShowViewNull();
        //Toast.makeText(view.getContext(),"点击了聊天按钮",Toast.LENGTH_SHORT).show();
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String,String> temp=new HashMap<>();
        temp.put("role","user");
        temp.put("content",content);
        dataList.add(temp);

        ChatRequestBody param=new ChatRequestBody();
        param.setModel("gpt-3.5-turbo");
        param.setMessages(dataList);

        ChatApi mChatApi=new ChatApi();
        ChatService mChatService=mChatApi.getService();
        Call<ChatBean> callChat=mChatService.getState(param);
        callChat.enqueue(new Callback<ChatBean>() {
            @Override
            public void onResponse(Call<ChatBean> call, Response<ChatBean> response) {
                if(!(response.body().equals(null))){
                    mShow_View.setText(response.body().getChoices().get(0).getMessage().getContent());
                }else{
                    Toast.makeText(view.getContext(),"意料之外的错误!!!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChatBean> call, Throwable t) {
                SetFailure_ShowView();
                //Toast.makeText(view.getContext(),"网络错误!请检查!!!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**直接获取OpenAi数据**/
    private void OnAndroidGetCreateImage(String prompt){
        SetShowViewNull();

        CreImgRequestBody mCreateImg=new CreImgRequestBody();
        mCreateImg.setPrompt(prompt);
        mCreateImg.setSize("1024x1024");

        CreateImageApi mCreateImageApi=new CreateImageApi();
        CreateImageService mCreateImageService=mCreateImageApi.getService();
        Call<CreateImageBean> callCreateImg=mCreateImageService.getState(mCreateImg);
        callCreateImg.enqueue(new Callback<CreateImageBean>() {
            @Override
            public void onResponse(Call<CreateImageBean> call, Response<CreateImageBean> response) {
                if(!(response.body().equals(null))){
                    /**
                     * 自定义工具类将url资源显示
                     */
                    ImageTool.SetImageView(mImage_View,response.body().getData().get(0).getUrl());

                }else{
                    Toast.makeText(view.getContext(),"意料之外的错误!!!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateImageBean> call, Throwable t) {
                SetFailure_ShowView();
            }
        });
    }

    private void SetShowViewLoad(){
        mShow_View.setText(view.getContext().getString(R.string.Onload));
    }

    private void SetShowViewNull(){
        mShow_View.setText("");
    }

    private void SetFailure_ShowView(){
        mShow_View.setText(view.getContext().getString(R.string.NetworkFailure));
    }

    /**
     * ImageView长按响应方法
     */
    private class ImageClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(mImage_View.getDrawable()!=null){
                Bitmap bm =((BitmapDrawable) ((ImageView) mImage_View).getDrawable()).getBitmap();
                ShowDialog(bm,v.getContext());
            }
        }
    }

    /**
     * 保存确认
     * @param mContext
     */
    public void ShowDialog(Bitmap bitmap,final Context mContext) {
        String[] names = { mContext.getString(R.string.SystemTitle),
                mContext.getString(R.string.saveTitle),
                mContext.getString(R.string.Confirm),
                mContext.getString(R.string.Cancel) };
        /**MatrixDialog中最后两个按钮的顺序与names的文本顺序相反**/
        MatrixDialog mDialog = new MatrixDialog(mContext, names, true);
        mDialog.setOnClickListener2LastTwoItems(new MatrixDialog.OnClickListener2LastTwoItem() {
            /**取消按钮**/
            @Override
            public void onClickListener2LastItem() {
                Toast.makeText(mContext, "取消保存", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
            /**确定按钮**/
            @Override
            public void onClickListener2SecondLastItem() {
                //Toast.makeText(mContext, "点击了确定", Toast.LENGTH_SHORT).show();
                ImageTool.saveBitmap(bitmap,mImagePath,mContext, TimeTool.GetSystemTime());
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }
}
