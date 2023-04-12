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
import com.matrix.matrixgpt.Network.API.GPT.ChatApi;
import com.matrix.matrixgpt.Network.API.GPT.CreateImageApi;
import com.matrix.matrixgpt.Network.GptRequestBody.ChatRequestBody;
import com.matrix.matrixgpt.Network.GptRequestBody.CreImgRequestBody;
import com.matrix.matrixgpt.Network.ResponseBean.BackService.BackChatBean;
import com.matrix.matrixgpt.Network.ResponseBean.BackService.Gpt.ChatBean;
import com.matrix.matrixgpt.Network.ResponseBean.BackService.Gpt.CreateImageBean;
import com.matrix.matrixgpt.Network.Service.Back.BackChatService;
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
        mShow_View.setMovementMethod(ScrollingMovementMethod.getInstance());//添加文本视图滚动条

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
                    String content=mEditText.getText().toString();//获取输入内容
                    //OnAndroidGetChat(content);
                    GetBackChatData(content);
//                    if(content!=null||content!=""){
//                        //OnAndroidGetChat(content);
//                        GetBackChatData(content);
//                    }else{
//                        Toast.makeText(view.getContext(),"请输入内容",Toast.LENGTH_SHORT).show();
//                    }

                    break;
                case R.id.paint_btn:
                    String prompt=mEditText.getText().toString();//获取输入内容
                    //OnAndroidGetCreateImage(prompt);
                    break;
            }
        }
    }

    private void GetBackChatData(String content){
        intent_MainFragment=getActivity().getIntent();
        if(intent_MainFragment.getStringExtra("U_account").equals(null)||
                intent_MainFragment.getStringExtra("U_account").equals("")){
            //游客登录
            //Toast.makeText(view.getContext(),"游客登录",Toast.LENGTH_SHORT).show();
            if(visitor_Num<=0){
                Toast.makeText(view.getContext(),"次数使用完",Toast.LENGTH_SHORT).show();
                String[] names = {TGA.getString(R.string.SystemTitle),
                        TGA.getString(R.string.visitor_NumAdd),
                        TGA.getString(R.string.Confirm), TGA.getString(R.string.Cancel)};

                /**调用窗口方法**/
                //new MatrixDialogManager().ShowMatrixDialog(names,getActivity(), MainActivity.class);
            }else{
                BackChatApi mBackChatApi=new BackChatApi();
                BackChatService mBackChatService=mBackChatApi.getService();
                Call<BackChatBean> callBackChat=mBackChatService.getState(content);
                callBackChat.enqueue(new Callback<BackChatBean>() {
                    @Override
                    public void onResponse(Call<BackChatBean> call, Response<BackChatBean> response) {
                        if(!(response.body().equals(null))){
                            mShow_View.setText(response.body().getResult());
                            visitor_Num--;
                        }else{
                            Toast.makeText(view.getContext(),"意料之外的错误!!!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BackChatBean> call, Throwable t) {
                        SetFailure_ShowView("网络错误!请检查!!!");
                        //Toast.makeText(view.getContext(),"网络错误!请检查!!!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else{
            //用户登录
            //Toast.makeText(view.getContext(),"用户登录"+intent_MainFragment.getStringExtra("U_account"),Toast.LENGTH_SHORT).show();


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
                SetFailure_ShowView("网络错误!请检查!!!");
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
                SetFailure_ShowView("网络错误!请检查!!!");
            }
        });
    }

    private void SetShowViewNull(){
        mShow_View.setText("");
    }

    private void SetFailure_ShowView(String failure){
        mShow_View.setText(failure);
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
