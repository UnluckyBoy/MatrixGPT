package com.matrix.matrixgpt.UI.Fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.matrix.matrixgpt.Network.API.ChatApi;
import com.matrix.matrixgpt.Network.API.CreateImageApi;
import com.matrix.matrixgpt.Network.CustomRequestModel.ChatRequestBody;
import com.matrix.matrixgpt.Network.CustomRequestModel.CreImgRequestBody;
import com.matrix.matrixgpt.Network.ReplyBean.ChatBean;
import com.matrix.matrixgpt.Network.ReplyBean.CreateImageBean;
import com.matrix.matrixgpt.Network.Service.ChatService;
import com.matrix.matrixgpt.Network.Service.CreateImageService;
import com.matrix.matrixgpt.R;
import com.matrix.matrixgpt.UtilTool.ImageTool;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private View view;

    private EditText mEditText;
    //private Button mForecastBtn,mReadBtn,mChatBtn,mPaintBbtn;
    private TextView mShow_View,mAi_info_View;
    private ImageView mImage_View;

    private Map<String,Integer> mNumMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.main_fragment,container,false);
//        // Inflate the layout for this fragment
//        return view;
        if(view!=null){
            ViewGroup parent=(ViewGroup) view.getParent();
            if(parent!=null){
                parent.removeView(view);
            }
        }else{
            view = inflater.inflate(R.layout.main_fragment, container, false);

            Bundle bundle = getArguments();

            Init_Component(view);
        }
        return view;
    }

    private void Init_Component(View view) {
        Button mChatBtn,mPaintBbtn;
        mChatBtn=view.findViewById(R.id.chat_btn);
        mPaintBbtn=view.findViewById(R.id.paint_btn);
        mShow_View=view.findViewById(R.id.show_View);
        mAi_info_View=view.findViewById(R.id.ai_info_View);
        mShow_View.setMovementMethod(ScrollingMovementMethod.getInstance());//添加文本视图滚动条

        mEditText=view.findViewById(R.id.edit_text);
        mImage_View=view.findViewById(R.id.image_View);

        mChatBtn.setOnClickListener(new ClickListener());
        mPaintBbtn.setOnClickListener(new ClickListener());
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //Toast.makeText(view.getContext(),"点击了聊天按钮",Toast.LENGTH_SHORT).show();
            final int mBtnId=v.getId();
            switch(mBtnId){
                case R.id.chat_btn:
                    String content=mEditText.getText().toString();//获取输入内容

                    //Toast.makeText(view.getContext(),"点击了聊天按钮",Toast.LENGTH_SHORT).show();
                    SetLoad_ShowView();

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
                                mAi_info_View.setText("AI模型:"+response.body().getModel());
                                mShow_View.setText(response.body().getChoices().get(0).getMessage().getContent());
                            }
                        }

                        @Override
                        public void onFailure(Call<ChatBean> call, Throwable t) {
                            SetFailure_ShowView("网络错误!请检查!!!");
                            //Toast.makeText(view.getContext(),"网络错误!请检查!!!",Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;
                case R.id.paint_btn:
                    String prompt=mEditText.getText().toString();//获取输入内容

                    //Toast.makeText(view.getContext(),"点击了绘画按钮",Toast.LENGTH_SHORT).show();
                    SetLoad_ShowView();
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
                                mAi_info_View.setText("");
                                mShow_View.setText(response.body().getData().get(0).getUrl());
                                //Picasso.get().load(response.body().getData().get(0).getUrl()).into(mImage_View);
                            }
                        }

                        @Override
                        public void onFailure(Call<CreateImageBean> call, Throwable t) {
                            SetFailure_ShowView("网络错误!请检查!!!");
                        }
                    });
                    break;
            }
        }
    }

    private void SetLoad_ShowView(){
        mAi_info_View.setText("请稍后...");
    }

    private void SetFailure_ShowView(String failure){
        mAi_info_View.setText("");
        mShow_View.setText(failure);
    }
}
