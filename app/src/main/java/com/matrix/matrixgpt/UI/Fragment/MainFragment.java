package com.matrix.matrixgpt.UI.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.DislikeInfo;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.matrix.matrixgpt.Network.API.Back.BackChatApi;
import com.matrix.matrixgpt.Network.API.Back.BackCreateImageApi;
import com.matrix.matrixgpt.Network.API.Back.DoGptTransApi;
import com.matrix.matrixgpt.Network.ResponseBean.BackService.BackChatBean;
import com.matrix.matrixgpt.Network.ResponseBean.BackService.LoginBean;
import com.matrix.matrixgpt.Network.Service.Back.BackChatService;
import com.matrix.matrixgpt.Network.Service.Back.BackCreateImageService;
import com.matrix.matrixgpt.Network.Service.Back.DoGptTransService;
import com.matrix.matrixgpt.R;
import com.matrix.matrixgpt.UI.Activity.LoginActivity;
import com.matrix.matrixgpt.UI.Activity.testCustomBanner;
import com.matrix.matrixgpt.UITool.MatrixDialog;
import com.matrix.matrixgpt.UITool.MatrixDialogManager;
import com.matrix.matrixgpt.UtilTool.AdverUtil.Dialog.MatrixDislikeDialog;
import com.matrix.matrixgpt.UtilTool.AdverUtil.MatrixToast;
import com.matrix.matrixgpt.UtilTool.AdverUtil.config.TTAdManagerHolder;
import com.matrix.matrixgpt.UtilTool.ImageTool;
import com.matrix.matrixgpt.UtilTool.TimeTool;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("EmptyMethod")
public class MainFragment extends Fragment {
    private final String mImagePath="/sdcard/Download";

    private Intent intent_MainFragment;
    private View view;

    private EditText mEditText;
    //private Button mForecastBtn,mReadBtn,mChatBtn,mPaintBbtn;
    private TextView mShow_View;
    private ImageView mImage_View;

    private int visitor_Num=2;

    private FrameLayout mBanner;//广告视图
    private TTNativeExpressAd mTTAd;
    private TTAdNative mTTAdNative;
    private long startTime = 0;
    private boolean mHasShowDownloadActive = false;


    public static MainFragment newInstance(String param1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("args_account",param1);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("RedundantCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //mContext = this.getContext();

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
        mBanner=view.findViewById(R.id.banner_container);
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(view.getContext());
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(view.getContext());
        //加载广告
        loadExpressAd("952047590",300,150);

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

//        Button testBtn=view.findViewById(R.id.get_adv);
//        testBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickShowBanner();
//            }
//        });
    }

    //按钮响应事件
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
            //游客
            MatrixDialogManager.hintLoginDialog(view.getContext(),intent_MainFragment,getActivity(), LoginActivity.class);//提示登录
//            if(visitor_Num<=0){
//                //Toast.makeText(view.getContext(),"次数使用完",Toast.LENGTH_SHORT).show();
//                ShowAdvertise(view.getContext());
//            }else{
//                //visitor_Num--;//测试
//                VisitorHandBackChat(content,openAi_type);
//            }
        }else{
            //用户登录
            int mLevel=intent_MainFragment.getIntExtra("U_level",0);
            int mGptNum=intent_MainFragment.getIntExtra("U_gptNum",0);
            UserHandBackChat(content,openAi_type,mLevel,mGptNum,intent_MainFragment.getStringExtra("U_account"));
        }
    }

    /**游客调用后台API获取Chat数据**/
    private void VisitorHandBackChat(String content,String openAi_type){
        SetShowViewLoad();
        switch (openAi_type){
            case "chat":
                BackChatApi mBackChatApi=new BackChatApi();
                mBackChatApi.SetUrl(view.getContext().getString(R.string.BackUrl)
                        +view.getContext().getString(R.string.Url_Gpt));
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
                mBackCreateImageApi.SetUrl(view.getContext().getString(R.string.BackUrl)
                        +view.getContext().getString(R.string.Url_Gpt));
                BackCreateImageService mBackCreateImageService=mBackCreateImageApi.getService();
                Call<BackChatBean> callCreateImg=mBackCreateImageService.getState(content);
                callCreateImg.enqueue(new Callback<BackChatBean>() {
                    @Override
                    public void onResponse(Call<BackChatBean> call, Response<BackChatBean> response) {
                        if(!(response.body().equals(null))){
                            if(response.body().getResult().equals("success")){
                                /** 自定义工具类将url资源显示**/
                                ImageTool.SetImageView(mImage_View,response.body().getContent());
                                SetShowViewNull();
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
    private void UserHandBackChat(String content,String openAi_type,int level,int gptnum,String account){
        switch (openAi_type){
            case "chat":
                switch (level){
                    case 1:
                        //Toast.makeText(view.getContext(),"管理员,无限次使用",Toast.LENGTH_SHORT).show();
                        SetShowViewLoad();
                        BackChatApi mAdimnChatApi=new BackChatApi();
                        mAdimnChatApi.SetUrl(view.getContext().getString(R.string.BackUrl)
                                +view.getContext().getString(R.string.Url_Gpt));
                        BackChatService mAdminChatService=mAdimnChatApi.getService();
                        Call<BackChatBean> callAdminChat=mAdminChatService.getState(content);
                        callAdminChat.enqueue(new Callback<BackChatBean>() {
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
                            SetShowViewLoad();
                            BackChatApi mUserChatApi=new BackChatApi();
                            mUserChatApi.SetUrl(view.getContext().getString(R.string.BackUrl)
                                    +view.getContext().getString(R.string.Url_Gpt));
                            BackChatService mUserChatService=mUserChatApi.getService();
                            Call<BackChatBean> callUserChat=mUserChatService.getState(content);
                            callUserChat.enqueue(new Callback<BackChatBean>() {
                                @Override
                                public void onResponse(Call<BackChatBean> call, Response<BackChatBean> response) {
                                    if(response.body()==null){
                                        mShow_View.setText(view.getContext().getString(R.string.ResponseBodyNull));
                                    }else {
                                        if(response.body().getResult().equals("success")){
                                            mShow_View.setText(response.body().getContent());
                                            DoGptTransApi mDoGptTransApi=new DoGptTransApi();
                                            mDoGptTransApi.SetUrl(view.getContext().getString(R.string.BackUrl)
                                                    +view.getContext().getString(R.string.Url_UserInfo));
                                            DoGptTransService mDoGptTransService=mDoGptTransApi.getService();
                                            Call<LoginBean> callDoGpt=mDoGptTransService.getState(account);
                                            callDoGpt.enqueue(new Callback<LoginBean>() {
                                                @Override
                                                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                                                    if(response.body()==null){
                                                        mShow_View.setText(view.getContext().getString(R.string.ResponseBodyNull));
                                                    }else {
                                                        switch (response.body().getResult()){
                                                            case "Permission":
                                                                intent_MainFragment.putExtra("U_gptNum",response.body().getGptNum());//将数据刷新
                                                                break;
                                                            case "NullPermission":
                                                                //Toast.makeText(view.getContext(),view.getContext().getString(R.string.NullPermission),Toast.LENGTH_SHORT).show();
                                                                ShowAdvertise(view.getContext());
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
                        }else{
                            //Toast.makeText(view.getContext(),view.getContext().getString(R.string.NullPermission),Toast.LENGTH_SHORT).show();
                            ShowAdvertise(view.getContext());
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
                        mBackCreateImageApi.SetUrl(view.getContext().getString(R.string.BackUrl)
                                +view.getContext().getString(R.string.Url_Gpt));
                        BackCreateImageService mBackCreateImageService=mBackCreateImageApi.getService();
                        Call<BackChatBean> callCreateImg=mBackCreateImageService.getState(content);
                        callCreateImg.enqueue(new Callback<BackChatBean>() {
                            @Override
                            public void onResponse(Call<BackChatBean> call, Response<BackChatBean> response) {
                                if(!(response.body().equals(null))){
                                    if(response.body().getResult().equals("success")){
                                        /** 自定义工具类将url资源显示**/
                                        ImageTool.SetImageView(mImage_View,response.body().getContent());
                                        SetShowViewNull();
                                    }else{
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
                        //Toast.makeText(view.getContext(),"普通用户,可用次数:"+gptnum,Toast.LENGTH_SHORT).show();
                        /**普通用户使用createImage**/
                        if(gptnum>0){
                            SetShowViewLoad();
                            BackCreateImageApi mUserBackCreateImage=new BackCreateImageApi();
                            mUserBackCreateImage.SetUrl(view.getContext().getString(R.string.BackUrl)
                                    +view.getContext().getString(R.string.Url_Gpt));
                            BackCreateImageService mUserBackCreateImageService=mUserBackCreateImage.getService();
                            Call<BackChatBean> callUserCreate=mUserBackCreateImageService.getState(content);
                            callUserCreate.enqueue(new Callback<BackChatBean>() {
                                @Override
                                public void onResponse(Call<BackChatBean> call, Response<BackChatBean> response) {
                                    if(!(response.body().equals(null))){
                                        if(response.body().getResult().equals("success")){
                                            /** 自定义工具类将url资源显示**/
                                            ImageTool.SetImageView(mImage_View,response.body().getContent());
                                            SetShowViewNull();
                                            /**更改用户使用次数**/
                                            DoGptTransApi mDoGptTransApi=new DoGptTransApi();
                                            mDoGptTransApi.SetUrl(view.getContext().getString(R.string.BackUrl)
                                                    +view.getContext().getString(R.string.Url_UserInfo));
                                            DoGptTransService mDoGptTransService=mDoGptTransApi.getService();
                                            Call<LoginBean> callDoGpt=mDoGptTransService.getState(account);
                                            callDoGpt.enqueue(new Callback<LoginBean>() {
                                                @Override
                                                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                                                    if(response.body()==null){
                                                        mShow_View.setText(view.getContext().getString(R.string.ResponseBodyNull));
                                                    }else {
                                                        switch (response.body().getResult()){
                                                            case "Permission":
                                                                intent_MainFragment.putExtra("U_gptNum",response.body().getGptNum());//将数据刷新
                                                                break;
                                                            case "NullPermission":
                                                                //Toast.makeText(view.getContext(),view.getContext().getString(R.string.NullPermission),Toast.LENGTH_SHORT).show();
                                                                ShowAdvertise(view.getContext());
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
                        }else{
                            //Toast.makeText(view.getContext(),view.getContext().getString(R.string.NullPermission),Toast.LENGTH_SHORT).show();
                            ShowAdvertise(view.getContext());
                        }

                        break;
                }
                break;
        }
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

    /**显示广告弹窗**/
    public void ShowAdvertise(final Context mContext){
        String[] names = { mContext.getString(R.string.SystemTitle),
                mContext.getString(R.string.ShowAdvertise),
                mContext.getString(R.string.Confirm),
                mContext.getString(R.string.Cancel) };
        MatrixDialog mDialog = new MatrixDialog(mContext, names, true);
        mDialog.setOnClickListener2LastTwoItems(new MatrixDialog.OnClickListener2LastTwoItem() {
            /**取消按钮**/
            @Override
            public void onClickListener2LastItem() {

                mDialog.dismiss();
            }
            /**确定按钮**/
            @Override
            public void onClickListener2SecondLastItem() {
                //Toast.makeText(mContext, "点击了确定", Toast.LENGTH_SHORT).show();

                mDialog.dismiss();
            }
        });
        mDialog.show();
    }


//    public void onClickShowBanner() {
//        //loadExpressAd("952047590",300,150);
//        if (mTTAd != null) {
//            mTTAd.render();
//        } else {
//            //MatrixToast.show(view.getContext(), "请先加载广告..");
//        }
//    }

    //加载自定义广告视图
    private void loadExpressAd(String codeId, int expressViewWidth, int expressViewHeight) {
        mBanner.removeAllViews();
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                MatrixToast.show(view.getContext(), "load error : " + code + ", " + message);
                mBanner.removeAllViews();
            }
            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                /*******************
                 * 如果旧广告对象不使用了，在替换成新广告对象前，必须进行销毁，否则可能导致多个广告对象同时存在，影响SSR
                 */
                if (mTTAd != null) {
                    mTTAd.destroy();
                }
                /********************/
                mTTAd = ads.get(0);
                mTTAd.setSlideIntervalTime(30 * 1000);
                bindAdListener(mTTAd);
                startTime = System.currentTimeMillis();
                //MatrixToast.show(view.getContext(), "load success!");
                /**加载成功，直接渲染出来**/
                mTTAd.render();
            }
        });
    }

    /**绑定数据到视图**/
    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                //MatrixToast.show(view.getContext(), "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                //MatrixToast.show(view.getContext(), "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
                //MatrixToast.show(view.getContext(), msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                //返回view的宽高 单位 dp
                //MatrixToast.show(view.getContext(), "渲染成功");
                mBanner.removeAllViews();
                mBanner.addView(view);
            }
        });
        //dislike设置
        bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                MatrixToast.show(view.getContext(), "点击开始下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    MatrixToast.show(view.getContext(), "下载中，点击暂停", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                MatrixToast.show(view.getContext(), "下载暂停，点击继续", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                MatrixToast.show(view.getContext(), "下载失败，点击重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                MatrixToast.show(view.getContext(), "安装完成，点击图片打开", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                MatrixToast.show(view.getContext(), "点击安装", Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
        if (customStyle) {
            //使用自定义样式
            final DislikeInfo dislikeInfo = ad.getDislikeInfo();
            if (dislikeInfo == null || dislikeInfo.getFilterWords() == null || dislikeInfo.getFilterWords().isEmpty()) {
                return;
            }
            final MatrixDislikeDialog dislikeDialog = new MatrixDislikeDialog(view.getContext(), dislikeInfo);
            dislikeDialog.setOnDislikeItemClick(new MatrixDislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
                    //MatrixToast.show(view.getContext(), "点击 " + filterWord.getName());
                    //用户选择不喜欢原因后，移除广告展示
                    mBanner.removeAllViews();
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(getActivity(), new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onShow() {

            }
            @Override
            public void onSelected(int position, String value, boolean enforce) {
                //MatrixToast.show(view.getContext(), "点击 " + value);
                mBanner.removeAllViews();
                //用户选择不喜欢原因后，移除广告展示
//                if (enforce) {
//                    MatrixToast.show(view.getContext(), "模版Banner 穿山甲sdk强制将view关闭了");
//                }
            }
            @Override
            public void onCancel() {
                //MatrixToast.show(view.getContext(), "点击取消 ");
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTTAd != null) {
            mTTAd.destroy();
        }
    }
}
