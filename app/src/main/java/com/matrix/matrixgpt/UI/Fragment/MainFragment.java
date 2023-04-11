package com.matrix.matrixgpt.UI.Fragment;

import android.content.Context;
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

import com.matrix.matrixgpt.Network.API.ChatApi;
import com.matrix.matrixgpt.Network.API.CreateImageApi;
import com.matrix.matrixgpt.Network.GptRequestBody.ChatRequestBody;
import com.matrix.matrixgpt.Network.GptRequestBody.CreImgRequestBody;
import com.matrix.matrixgpt.Network.ResponseBean.BackService.UserBean;
import com.matrix.matrixgpt.Network.ResponseBean.Gpt.ChatBean;
import com.matrix.matrixgpt.Network.ResponseBean.Gpt.CreateImageBean;
import com.matrix.matrixgpt.Network.Service.ChatService;
import com.matrix.matrixgpt.Network.Service.CreateImageService;
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

    private View view;

    private EditText mEditText;
    //private Button mForecastBtn,mReadBtn,mChatBtn,mPaintBbtn;
    private TextView mShow_View,mAi_info_View;
    private ImageView mImage_View;
    private String agrs1=null;

    public static MainFragment newInstance(String param1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("agrs1",param1);
        fragment.setArguments(args);
        return fragment;
    }

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
            agrs1 = bundle.getString("agrs1");
            Init_Component(view);
        }
        return view;
    }

    private void Init_Component(View view) {
        Button mChatBtn,mPaintBtn;
        mChatBtn=view.findViewById(R.id.chat_btn);
        mPaintBtn=view.findViewById(R.id.paint_btn);
        //mSaveImgBtn=view.findViewById(R.id.saveImg_btn);
        mShow_View=view.findViewById(R.id.show_View);
        mShow_View.setMovementMethod(ScrollingMovementMethod.getInstance());//添加文本视图滚动条
        mAi_info_View=view.findViewById(R.id.ai_info_View);

        mEditText=view.findViewById(R.id.edit_text);
        mImage_View=view.findViewById(R.id.image_View);

        mImage_View.setOnClickListener(new ImageClickListener());

        mChatBtn.setOnClickListener(new ClickListener());
        mPaintBtn.setOnClickListener(new ClickListener());
        //mSaveImgBtn.setOnClickListener(new ClickListener());
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //Toast.makeText(view.getContext(),"点击了聊天按钮",Toast.LENGTH_SHORT).show();
            final int mBtnId=v.getId();
            switch(mBtnId){
                case R.id.chat_btn:
                    String content=mEditText.getText().toString();//获取输入内容
                    SetShowViewNull();

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

                    break;
                case R.id.paint_btn:
                    String prompt=mEditText.getText().toString();//获取输入内容
                    SetShowViewNull();

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
                                //mShow_View.setText(response.body().getData().get(0).getUrl());
                                //mEditTextView.setText(response.body().getData().get(0).getUrl());
                                //Picasso.get().load(response.body().getData().get(0).getUrl()).into(mImage_View);

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
                    break;
            }
        }
    }

    private void SetLoad_ShowView(){
        mAi_info_View.setText("请稍后...");
    }
    private void SetShowViewNull(){
        mShow_View.setText("");
    }

    private void SetFailure_ShowView(String failure){
        mAi_info_View.setText("");
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
