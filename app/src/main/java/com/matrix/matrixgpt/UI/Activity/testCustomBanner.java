package com.matrix.matrixgpt.UI.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.DislikeInfo;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.matrix.matrixgpt.R;
import com.matrix.matrixgpt.UITool.testView.LoadMoreRecyclerView;
import com.matrix.matrixgpt.UtilTool.AdverUtil.Dialog.MatrixDislikeDialog;
import com.matrix.matrixgpt.UtilTool.AdverUtil.MatrixToast;
import com.matrix.matrixgpt.UtilTool.AdverUtil.config.TTAdManagerHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName testCustomBanner
 * @Author Create By Administrator
 * @Date 2023/4/18 0018 1:56
 */
public class testCustomBanner extends Activity {

    private TTAdNative mTTAdNative;
    private FrameLayout mExpressContainer;
    private Context mContext;

    private TTAdDislike mTTAdDislike;

    private TTNativeExpressAd mTTAd;
    private LoadMoreRecyclerView mListView;
    private List<AdSizeModel> mBannerAdSizeModelList;
    private long startTime = 0;
    private boolean mHasShowDownloadActive = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_native_express_banner);
        mContext = this.getApplicationContext();

        initView();
        //initData();
        //initRecycleView();
        initTTSDKConfig();
        Button button = (Button) findViewById(R.id.btn_eb_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        Button mCustBtn=findViewById(R.id.test_showBanner);
//        mCustBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadExpressAd("952047590",300,150);
//            }
//        });

        //在onCreate中调用加载广告数据
        loadExpressAd("952047590",300,150);

    }

    private void initTTSDKConfig() {
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);
    }

//    private void initRecycleView() {
//        GridLayoutManager layoutManager = new GridLayoutManager(testCustomBanner.this, 4);
//        mListView.setLayoutManager(layoutManager);
//        AdapterForBannerType adapterForBannerType = new AdapterForBannerType(this, mBannerAdSizeModelList);
//        mListView.setAdapter(adapterForBannerType);
//
//        //loadExpressAd("952047590",300,150);
//
//    }

    private void initView() {
        mExpressContainer = (FrameLayout) findViewById(R.id.express_container);
        mListView = findViewById(R.id.my_list);

        //loadExpressAd("952047590",300,150);

        findViewById(R.id.showBanner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickShowBanner();
            }
        });
    }

//    private void initData() {
//        mBannerAdSizeModelList = new ArrayList<>();
//        mBannerAdSizeModelList.add(new AdSizeModel("600*90", 300, 45, "901121246"));
//        mBannerAdSizeModelList.add(new AdSizeModel("600*150", 300, 75, "901121700"));
//        mBannerAdSizeModelList.add(new AdSizeModel("600*260", 300, 130, "901121148"));
//        mBannerAdSizeModelList.add(new AdSizeModel("600*300", 300, 150, "952047590"));//个人952047590,官方:945509757
//        mBannerAdSizeModelList.add(new AdSizeModel("600*400", 300, 200, "945509751"));
//        mBannerAdSizeModelList.add(new AdSizeModel("640*100", 320, 50, "901121223"));
//        mBannerAdSizeModelList.add(new AdSizeModel("690*388", 345, 194, "945509738"));
//        mBannerAdSizeModelList.add(new AdSizeModel("600*500", 300, 250, "945509744"));
//    }
//
//
//    public static class AdapterForBannerType extends RecyclerView.Adapter<AdapterForBannerType.ViewHolder> {
//        private List<AdSizeModel> mBannerSizeList;
//        private testCustomBanner mActivity;
//
//        public AdapterForBannerType(testCustomBanner activity, List<AdSizeModel> bannerSize) {
//            this.mActivity = activity;
//            this.mBannerSizeList = bannerSize;
//        }
//
//        @NonNull
//        @Override
//        public AdapterForBannerType.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.express_banner_list_item, viewGroup, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull AdapterForBannerType.ViewHolder viewHolder, int i) {
//            final AdSizeModel bannerSize = mBannerSizeList == null ? null : mBannerSizeList.get(i);
//            if (bannerSize != null) {
//                viewHolder.btnSize.setText(bannerSize.adSizeName);
//                viewHolder.btnSize.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //请求banner广告
//                        mActivity.loadExpressAd(bannerSize.codeId, bannerSize.width, bannerSize.height);
//                    }
//                });
//            }
//        }
//        @Override
//        public int getItemCount() {
//            return mBannerSizeList != null ? mBannerSizeList.size() : 0;
//        }
//        public static class ViewHolder extends RecyclerView.ViewHolder {
//            private Button btnSize;
//
//            public ViewHolder(View view) {
//                super(view);
//                btnSize = view.findViewById(R.id.btn_banner_size);
//            }
//
//        }
//    }

    public void onClickShowBanner() {
        //loadExpressAd("952047590",300,150);

        if (mTTAd != null) {
            mTTAd.render();
        } else {
            MatrixToast.show(mContext, "请先加载广告..");
        }
    }

    //加载自定义广告视图
    private void loadExpressAd(String codeId, int expressViewWidth, int expressViewHeight) {
        mExpressContainer.removeAllViews();
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
                MatrixToast.show(testCustomBanner.this, "load error : " + code + ", " + message);
                mExpressContainer.removeAllViews();
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
                MatrixToast.show(mContext, "load success!");
            }
        });
    }


    private void bindAdListener(TTNativeExpressAd ad) {

        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override

            public void onAdClicked(View view, int type) {
                MatrixToast.show(mContext, "广告被点击");
            }

            @Override

            public void onAdShow(View view, int type) {
                MatrixToast.show(mContext, "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
                MatrixToast.show(mContext, msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                //返回view的宽高 单位 dp
                MatrixToast.show(mContext, "渲染成功");
                mExpressContainer.removeAllViews();
                mExpressContainer.addView(view);
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
                MatrixToast.show(testCustomBanner.this, "点击开始下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    MatrixToast.show(testCustomBanner.this, "下载中，点击暂停", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                MatrixToast.show(testCustomBanner.this, "下载暂停，点击继续", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                MatrixToast.show(testCustomBanner.this, "下载失败，点击重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                MatrixToast.show(testCustomBanner.this, "安装完成，点击图片打开", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                MatrixToast.show(testCustomBanner.this, "点击安装", Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
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
            final MatrixDislikeDialog dislikeDialog = new MatrixDislikeDialog(this, dislikeInfo);
            dislikeDialog.setOnDislikeItemClick(new MatrixDislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
                    MatrixToast.show(mContext, "点击 " + filterWord.getName());
                    //用户选择不喜欢原因后，移除广告展示
                    mExpressContainer.removeAllViews();
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式

        ad.setDislikeCallback(testCustomBanner.this, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onShow() {

            }

            @Override
            public void onSelected(int position, String value, boolean enforce) {
                MatrixToast.show(mContext, "点击 " + value);
                mExpressContainer.removeAllViews();
                //用户选择不喜欢原因后，移除广告展示
                if (enforce) {
                    MatrixToast.show(mContext, "模版Banner 穿山甲sdk强制将view关闭了");
                }
            }

            @Override
            public void onCancel() {
                MatrixToast.show(mContext, "点击取消 ");
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTTAd != null) {
            mTTAd.destroy();
        }
    }


    public static class AdSizeModel {
        public AdSizeModel(String adSizeName, int width, int height, String codeId) {
            this.adSizeName = adSizeName;
            this.width = width;
            this.height = height;
            this.codeId = codeId;
        }

        public String adSizeName;
        public int width;
        public int height;
        public String codeId;
    }
}