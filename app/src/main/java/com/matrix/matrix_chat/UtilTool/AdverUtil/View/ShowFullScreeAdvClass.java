package com.matrix.matrix_chat.UtilTool.AdverUtil.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdLoadType;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.matrix.matrix_chat.Network.API.Back.LoginApi;
import com.matrix.matrix_chat.Network.API.Back.upGptNumApi;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.LoginBean;
import com.matrix.matrix_chat.Network.Service.Back.LoginService;
import com.matrix.matrix_chat.Network.Service.Back.upGptNumService;
import com.matrix.matrix_chat.R;
import com.matrix.matrix_chat.UI.Activity.LoginActivity;
import com.matrix.matrix_chat.UtilTool.AdverUtil.MatrixToast;
import com.matrix.matrix_chat.UtilTool.AdverUtil.config.TTAdManagerHolder;
import com.matrix.matrix_chat.UtilTool.DataSharaPreferenceManager;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ClassName ShowFullScreeAdvClass
 * @Author Create By Administrator
 * @Date 2023/4/18 0018 18:47
 */
public class ShowFullScreeAdvClass {
    private static TTAdNative mTTAdNative;
    private static AdLoadListener mAdLoadListener;
    //private Activity activity;

    public ShowFullScreeAdvClass(){
    }

    public static void loadAdv(Activity activity,String account,Intent intent){
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限
        TTAdManagerHolder.get().requestPermissionIfNecessary(activity);
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(activity);

        //step5:创建广告请求参数AdSlot
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("952058327") //广告代码位Id,个人:952058327,官方:947747681
                .setAdLoadType(TTAdLoadType.LOAD) //本次广告用途:TTAdLoadType.LOAD实时;TTAdLoadType.PRELOAD预请求
                .build();
        //step6:注册广告加载生命周期监听，请求广告
        mAdLoadListener = new AdLoadListener(activity,account,intent);
        mTTAdNative.loadFullScreenVideoAd(adSlot, mAdLoadListener);
    }

    /**
     * 【必须】广告加载期间生命周期监听
     */
    private static class AdLoadListener implements TTAdNative.FullScreenVideoAdListener {

        private final Activity mActivity;
        private final String current_account;
        private final Intent current_intent;

        private TTFullScreenVideoAd mAd;

        public AdLoadListener(Activity activity,String account,Intent intent) {
            mActivity = activity;
            current_account=account;
            current_intent=intent;
        }

        @Override
        public void onError(int code, String message) {
            Log.e(mActivity.toString(), "Callback --> onError: " + code + ", " + message);
            //MatrixToast.show(mActivity, message);
        }

        @Override
        public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
            Log.e(mActivity.toString(), "Callback --> onFullScreenVideoAdLoad");
            //MatrixToast.show(mActivity, "FullVideoAd loaded  广告类型：" + getAdType(ad.getFullVideoAdType()));
            handleAd(ad);
        }

        @Override
        public void onFullScreenVideoCached() {
            // 已废弃 请使用 onRewardVideoCached(TTRewardVideoAd ad) 方法
        }

        @Override
        public void onFullScreenVideoCached(TTFullScreenVideoAd ad) {
            Log.e(mActivity.toString(), "Callback --> onFullScreenVideoCached");
            //MatrixToast.show(mActivity, "FullVideoAd video cached");
            handleAd(ad);
        }

        /**
         * 处理广告对象
         */
        public void handleAd(TTFullScreenVideoAd ad) {
            if (mAd != null) {
                return;
            }
            mAd = ad;
            //【必须】广告展示时的生命周期监听
            mAd.setFullScreenVideoAdInteractionListener(new AdLifeListener(mActivity,current_account,current_intent));
            //【可选】监听下载状态
            mAd.setDownloadListener(new DownloadStatusListener());

            //加载完后直接显示
            mAd.showFullScreenVideoAd(mActivity, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
            // 广告使用后应废弃
            mAd = null;
        }

        /**
         * 触发展示广告
         */
//        public void showAd(TTAdConstant.RitScenes ritScenes, String scenes) {
//            if (mAd == null) {
//                MatrixToast.show(mActivity, "当前广告未加载好，请先点击加载广告");
//                return;
//            }
//
//            mAd.showFullScreenVideoAd(mActivity, ritScenes, scenes);
//            // 广告使用后应废弃
//            mAd = null;
//        }
    }

    /**
     * 【必须】广告生命状态监听器
     */
    private static class AdLifeListener implements TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {
        private final WeakReference<Context> mContextRef;
        private Context mContext;
        private String mAccount;
        private Intent mIntent;
        public AdLifeListener(Context context,String account,Intent intent) {
            mContextRef = new WeakReference<>(context);
            mContext=context;
            mAccount=account;
            mIntent=intent;
        }

        @Override

        public void onAdShow() {
            Log.d(mContext.toString(), "Callback --> FullVideoAd show");
            //MatrixToast.show(mContextRef.get(), "FullVideoAd show");
        }

        @Override

        public void onAdVideoBarClick() {
            Log.d(mContext.toString(), "Callback --> FullVideoAd bar click");
            //MatrixToast.show(mContextRef.get(), "FullVideoAd bar click");
        }

        @Override

        public void onAdClose() {
            Log.d(mContext.toString(), "Callback --> FullVideoAd close");
            /*关闭*/
            //MatrixToast.show(mContextRef.get(), "FullVideoAd close");
        }

        @Override
        public void onVideoComplete() {
            Log.d(mContext.toString(), "Callback --> FullVideoAd complete");

            /**完整关闭**/
            //MatrixToast.show(mContextRef.get(), "FullVideoAd complete");
            upGptNumApi upGptNumApi=new upGptNumApi();
            upGptNumApi.SetUrl(mContext.getString(R.string.BackUrl)+mContext.getString(R.string.Url_UserInfo));
            upGptNumService upGptNumService=upGptNumApi.getService();
            Call<LoginBean> call_upGptNum=upGptNumService.getState(mAccount);
            call_upGptNum.enqueue(new Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                    if(response.body()!=null){
                        if(response.body().getResult().equals("success")){
                            Toast.makeText(mContext, "获取次数成功", Toast.LENGTH_SHORT).show();
                            DataSharaPreferenceManager.setExtra(response,mIntent);
                        }else{
                            Toast.makeText(mContext, "获取次数失败", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(mContext,mContext.getString(R.string.ResponseBodyNull), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginBean> call, Throwable t) {
                    Toast.makeText(mContext,mContext.getString(R.string.NetworkFailure), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSkippedVideo() {
            Log.d(mContext.toString(), "Callback --> FullVideoAd skipped");
            /*跳过*/
            //MatrixToast.show(mContextRef.get(), "FullVideoAd skipped");
        }
    }

    /**
     * 【可选】下载状态监听器
     */
    private static class DownloadStatusListener implements TTAppDownloadListener {
        @Override
        public void onIdle() {
        }
        @Override
        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
            Log.d("DML", "onDownloadActive==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
        }

        @Override
        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
            Log.d("DML", "onDownloadPaused===totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
        }

        @Override
        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
            Log.d("DML", "onDownloadFailed==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
        }

        @Override
        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
            Log.d("DML", "onDownloadFinished==totalBytes=" + totalBytes + ",fileName=" + fileName + ",appName=" + appName);
        }

        @Override
        public void onInstalled(String fileName, String appName) {
            Log.d("DML", "onInstalled==" + ",fileName=" + fileName + ",appName=" + appName);
        }
    }

    private static String getAdType(int type) {
        switch (type) {

            case TTAdConstant.AD_TYPE_COMMON_VIDEO:
                return "普通全屏视频，type=" + type;

            case TTAdConstant.AD_TYPE_PLAYABLE_VIDEO:
                return "Playable全屏视频，type=" + type;

            case TTAdConstant.AD_TYPE_PLAYABLE:
                return "纯Playable，type=" + type;

            case TTAdConstant.AD_TYPE_LIVE:
                return "直播流，type=" + type;
        }
        return "未知类型+type=" + type;
    }
}
