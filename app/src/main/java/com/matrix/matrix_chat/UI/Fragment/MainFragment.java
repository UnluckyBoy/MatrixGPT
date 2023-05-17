package com.matrix.matrix_chat.UI.Fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.DislikeInfo;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.matrix.matrix_chat.Network.DataTrans.DataTransController;
import com.matrix.matrix_chat.R;
import com.matrix.matrix_chat.UI.Activity.LoginActivity;
import com.matrix.matrix_chat.UITool.MatrixDialog;
import com.matrix.matrix_chat.UITool.MatrixDialogManager;
import com.matrix.matrix_chat.UtilTool.AdverUtil.Dialog.MatrixDislikeDialog;
import com.matrix.matrix_chat.UtilTool.AdverUtil.MatrixToast;
import com.matrix.matrix_chat.UtilTool.AdverUtil.config.TTAdManagerHolder;
import com.matrix.matrix_chat.UtilTool.ImageTool;
import com.matrix.matrix_chat.UtilTool.TimeTool;


import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;

@SuppressWarnings("EmptyMethod")
public class MainFragment extends Fragment {
    private final String mImagePath="/sdcard/Download";

    private Intent intent_MainFragment;
    private View view;

    private EditText mEditText;
    //private Button mForecastBtn,mReadBtn,mChatBtn,mPaintBbtn;
    private TextView mShow_View;
    private ImageView mImage_View;

    private FrameLayout mBanner;//广告视图
    private TTNativeExpressAd mTTAd;
    private TTAdNative mTTAdNative;
    private long startTime = 0;
    private boolean mHasShowDownloadActive = false;

    private PopupWindow popupWindow;//弹窗

    private ActivityResultLauncher<Intent> mLauncher;//启动activity对象,必须要在onCreate中初始化

    private static String args_account;

    private static final String API_KEY ="lTOov0EPAKvHDrB66z6G4U7Z";
    private static final String SECRET_KEY ="HqD8XwBF57v6mE3uY5ayLjdHDszyTZtb";
    private static final String grant_type="client_credentials";

    public static MainFragment newInstance(String param1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("args_account",param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**ActivityResultLauncher<Intent>必须要在onCreate中启动**/
        //setLauncher();
    }

    /**使用registerForActivityResult打开相册,已使用新组件PictureSelector替换**/
    private void setLauncher(){
        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // 处理Activity结果
                        if (result.getResultCode() == RESULT_OK) {
                            // 处理成功结果
                            //Toast.makeText(view.getContext(),"打开成功"+result,Toast.LENGTH_SHORT).show();
                            //mShow_View.setText(BGAPhotoPickerActivity.getSelectedImages(result.getData()).get(0));
                            final String localPicturePath = BGAPhotoPickerActivity.getSelectedImages(result.getData()).get(0);
                            getImageBaseData(localPicturePath);
                        } else {
                            // 处理失败结果
                            Toast.makeText(getContext(),"打开失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
            view = inflater.inflate(R.layout.fragment_main, container, false);
            Bundle bundle = getArguments();
            args_account = bundle.getString("args_account");
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

        Button mChatBtn,mPaintBtn,recognition_btn;
        mChatBtn=view.findViewById(R.id.chat_btn);
        mPaintBtn=view.findViewById(R.id.paint_btn);
        recognition_btn=view.findViewById(R.id.recognition_btn);
        mShow_View=view.findViewById(R.id.show_View);

        /**测试**/
//        SpannableString spannableString = new SpannableString("文字和图片 ");
//        // 创建一个ImageSpan，并设置要显示的图片
//        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = getResources().getDrawable(R.drawable.back); // 替换为你的图片资源
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//        ImageSpan imageSpan = new ImageSpan(drawable);
//        // 将ImageSpan应用到SpannableString的指定位置
//        spannableString.setSpan(imageSpan, 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        // 设置TextView的文本为SpannableString
//        mShow_View.setText(spannableString);

        mEditText=view.findViewById(R.id.edit_text);
        mImage_View=view.findViewById(R.id.image_View);

        mImage_View.setOnClickListener(new ImageClickListener());

        mChatBtn.setOnClickListener(new ClickListener());
        mPaintBtn.setOnClickListener(new ClickListener());
        recognition_btn.setOnClickListener(new ClickListener());
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
//                        if(args_account==""||args_account==null|| args_account.equals("")||args_account.equals(null)){
//                            MatrixDialogManager.hintLoginDialog(view.getContext(),intent_MainFragment,getActivity(),LoginActivity.class);
//                        }else{
//                            GetBackChatData(content,"chat");
//                        }
                        GetBackChatData(content,"chat");
                    }
                    //mShow_View.setMovementMethod(ScrollingMovementMethod.getInstance());//添加文本视图滚动条
                    mImage_View.setVisibility(View.GONE);//显示文本时，图片隐藏
                    //mImage_View.setVisibility(View.VISIBLE);//图片显示
                    break;
                case R.id.paint_btn:
                    /**绘画**/
                    String prompt=mEditText.getText().toString();//获取输入内容
                    if(prompt.equals(null)||prompt.equals("")){
                        Toast.makeText(view.getContext(),view.getContext().getString(R.string.EditIsNull),Toast.LENGTH_SHORT).show();
                    }else{
                        if(args_account==""||args_account==null|| args_account.equals("")||args_account.equals(null)){
                            MatrixDialogManager.hintLoginDialog(view.getContext(),intent_MainFragment,getActivity(),LoginActivity.class);
                        }else{
                            GetBackChatData(prompt,"createImage");
                        }
                        //GetBackChatData(prompt,"createImage");
                    }
                    mImage_View.setVisibility(View.VISIBLE);//图片显示
                    //mShow_View.setVisibility(View.GONE);//显示图片时，文本隐藏
                    break;
                case R.id.recognition_btn:
                    /**打开识图activity**/
                    //mLauncher.launch(BGAPhotoPickerActivity.newIntent(getActivity(), null, 1, null, false));//原Picker工具使用

                    selectImageWindow(view);//项目使用
                    //test();//测试使用
                    break;
            }
        }
    }

    private void test(){
        PictureSelector.create(getActivity())
                .openSystemGallery(SelectMimeType.ofImage())
                .forSystemResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        //Toast.makeText(getContext(), "打开相册"+result.get(0).getRealPath(), Toast.LENGTH_SHORT).show();
                        final String localPicturePath = result.get(0).getRealPath();
                        final long mid=8388608;
                        final long testPath = result.get(0).getSize();

                        String temp=ImageTool.getImagePath2BaseCode(localPicturePath,true);
                        String temp_bitmap=ImageTool.bitmap2Base64(localPicturePath,true);

                        //mShow_View.setText(String.valueOf(testPath));
                        if(testPath>mid){
                            mShow_View.setText(String.valueOf(testPath)+":大于8m");
                        }else{
                            mShow_View.setText(String.valueOf(testPath)+":小于8m");
                        }
                    }
                    @Override
                    public void onCancel() {
                        //closePopupWindow();
                    }
                });
    }

    /**弹窗选择相片**/
    public void selectImageWindow(View view){
        View popUpView=LayoutInflater.from(view.getContext()).inflate(R.layout.view_selectimage_window, null);
        popupWindow=new PopupWindow(view);
        //popupWindow=new PopupWindow(popUpView,-1, -2);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(popUpView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.popup_selector_image_anim_style);//设置动画效果
        //popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(view.getResources().getColor(R.color.transparent,null)));
        /**设置背景为暗**/
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.showAtLocation(getActivity().getWindow().getDecorView(),Gravity.CENTER,0,0);

        /**点击外部时，关闭遮罩**/
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });

        Button btn_photo,btn_camera,btn_cancel;
        btn_photo=(Button)popUpView.findViewById(R.id.btn_photo);
        btn_camera=(Button)popUpView.findViewById(R.id.btn_camera);
        btn_cancel=(Button)popUpView.findViewById(R.id.btn_cancel);

        btn_photo.setOnClickListener(new popClickListener());
        btn_camera.setOnClickListener(new popClickListener());
        btn_cancel.setOnClickListener(new popClickListener());
    }
    private class popClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_photo:
                    //相册
                    PictureSelector.create(getActivity())
                            .openSystemGallery(SelectMimeType.ofImage())
                            .forSystemResult(new OnResultCallbackListener<LocalMedia>() {
                                @Override
                                public void onResult(ArrayList<LocalMedia> result) {
                                    //Toast.makeText(getContext(), "打开相册"+result.get(0).getRealPath(), Toast.LENGTH_SHORT).show();
                                    final String localPicturePath = result.get(0).getRealPath();
                                    final long fileSize = result.get(0).getSize();//文件大小
                                    getRecognitionTrans(fileSize,localPicturePath);
                                    //getImageBaseData(localPicturePath);//调用文字识别api
                                }
                                @Override
                                public void onCancel() {
                                    //closePopupWindow();
                                }
                            });
                    closePopupWindow();
                    break;
                case R.id.btn_camera:
                    //拍照
                    //Toast.makeText(getContext(), "打开相机", Toast.LENGTH_SHORT).show();
                    PictureSelector.create(getActivity())
                            .openCamera(SelectMimeType.ofImage())
                            .forResultActivity(new OnResultCallbackListener<LocalMedia>() {
                                @Override
                                public void onResult(ArrayList<LocalMedia> result) {
                                    //Toast.makeText(getContext(), "拍照:"+result.get(0).getRealPath(), Toast.LENGTH_SHORT).show();
                                    final String localPicturePath = result.get(0).getRealPath();
                                    final long fileSize = result.get(0).getSize();//文件大小
                                    getRecognitionTrans(fileSize,localPicturePath);
                                }

                                @Override
                                public void onCancel() {
                                    //closePopupWindow();
                                }
                            });
                    closePopupWindow();
                    break;
                case R.id.btn_cancel:
                    //取消
                    closePopupWindow();
                    break;
            }
        }
    }
    /**关闭弹窗**/
    public void closePopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;

            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 1f;
            getActivity().getWindow().setAttributes(lp);
        }
    }

    /**判断是否压缩**/
    private void getRecognitionTrans(final long fileSize,String localPicturePath){
        final long midSize=8388608;
        /**识别内容要10m以内,此处设置8m以内**/
        if(fileSize>midSize){
            //大于8m,需要压缩
            String tempCode=ImageTool.bitmap2Base64(localPicturePath,false);
            String body_Code=ImageTool.getBase2Urlencode(tempCode);
            DataTransController.getBaiduRecognitionData(view.getContext(),body_Code,API_KEY,SECRET_KEY,grant_type,mShow_View);//调取api获得识别的内容
        }else{
            getImageBaseData(localPicturePath);//否则直接调用文字识别api
        }
    }

    /**
     * 将图片转为base64格式
     * @param filePath
     */
    private void getImageBaseData(String filePath){
        String temp=ImageTool.getRecognitionImageBase(filePath);
        //mShow_View.setText(temp);
        DataTransController.getBaiduRecognitionData(view.getContext(),temp,API_KEY,SECRET_KEY,grant_type,mShow_View);//调取api获得识别的内容
    }


    /**
     * @param content 内容
     * @param openAi_type 获取的接口类型
     */
    private void GetBackChatData(String content,String openAi_type){
        intent_MainFragment=getActivity().getIntent();
        if(intent_MainFragment.getStringExtra(view.getContext().getString(R.string.info_account)).equals(null)||
                intent_MainFragment.getStringExtra(view.getContext().getString(R.string.info_account)).equals("")){
            //游客
            /*提示登录*/
            MatrixDialogManager.hintLoginDialog(view.getContext(),intent_MainFragment,getActivity(), LoginActivity.class);
        }else{
            //用户登录
            int mLevel=intent_MainFragment.getIntExtra(view.getContext().getString(R.string.info_level),0);
            int mGptNum=intent_MainFragment.getIntExtra(view.getContext().getString(R.string.info_gptNum),0);
            UserHandBackChat(content,openAi_type,mLevel,mGptNum,intent_MainFragment.getStringExtra(view.getContext().getString(R.string.info_account)));
        }
    }

    /**用户调用后台API获取Chat数据**/
    private void UserHandBackChat(String content,String openAi_type,int level,int gptnum,String account){
        //SetShowViewLoad();
        switch (openAi_type){
            case "chat":
                switch (level){
                    case 9:
                        //Toast.makeText(view.getContext(),"管理员,无限次使用",Toast.LENGTH_SHORT).show();
                        SetShowViewLoad();
                        DataTransController.adminGetChatData(view.getContext(),content,mShow_View);
                        break;
                    case 1:
                        /**普通用户使用Chat**/
                        //Toast.makeText(view.getContext(),"普通用户,可用次数:"+gptnum,Toast.LENGTH_SHORT).show();
                        if(gptnum>0){
                            SetShowViewLoad();
                            DataTransController.getChatData(getActivity(),content,mShow_View,account,intent_MainFragment);
                        }
                        else{
                            //Toast.makeText(view.getContext(),view.getContext().getString(R.string.NullPermission),Toast.LENGTH_SHORT).show();
                            MatrixDialogManager.ShowAdvertise(getActivity(),account,intent_MainFragment);
                        }
                        break;
                }
                break;
            case "createImage":
                switch (level){
                    case 9:
                        /**管理员**/
                        SetShowViewLoad();
                        DataTransController.adminGetImage(getActivity(),content,mImage_View,mShow_View);
                        break;
                    case 1:
                        //Toast.makeText(view.getContext(),"普通用户,可用次数:"+gptnum,Toast.LENGTH_SHORT).show();
                        /**普通用户使用createImage**/
                        if(gptnum>0){
                            SetShowViewLoad();
                            DataTransController.getImage(getActivity(),content,mImage_View,mShow_View,account,intent_MainFragment);
                        }else{
                            //Toast.makeText(view.getContext(),view.getContext().getString(R.string.NullPermission),Toast.LENGTH_SHORT).show();
                            MatrixDialogManager.ShowAdvertise(getActivity(),account,intent_MainFragment);
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
                //MatrixToast.show(view.getContext(), "点击开始下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    //MatrixToast.show(view.getContext(), "下载中，点击暂停", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                //MatrixToast.show(view.getContext(), "下载暂停，点击继续", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                //MatrixToast.show(view.getContext(), "下载失败，点击重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                //MatrixToast.show(view.getContext(), "安装完成，点击图片打开", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                //MatrixToast.show(view.getContext(), "点击安装", Toast.LENGTH_LONG);
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
                //if (enforce) {
                //    MatrixToast.show(view.getContext(), "模版Banner 穿山甲sdk强制将view关闭了");
                //}
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
        getActivity().finish();
    }

    /**
     * 每次重启MainFragment
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        SetShowViewNull();
    }
}
