package com.matrix.matrix_chat.UI.Activity;

import android.text.method.ScrollingMovementMethod;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.matrix_chat.Network.DataTrans.DataTransController;
import com.matrix.matrix_chat.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName ReadAcitivity
 * @Author Create By matrix
 * @Date 2023/5/12 0012 14:25
 */
public class ReadActivity extends BaseActivity{
    private boolean hideTitleBar=false;
    private boolean mPressed=false;

    private RelativeLayout lay_read_info;
    private FrameLayout lay_read_content_back;
    private TextView read_view_title,read_view_author,read_view_time,read_view_hot;
    private WebView read_view_content;

    private static final long DOUBLE_CLICK_TIME_DELTA = 300; //双击事件间隔时间
    private long lastClickTime = 0;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_read;
    }

    @Override
    protected void initView() {
        lay_read_info=findViewById(R.id.lay_read_info);
        lay_read_content_back=findViewById(R.id.lay_read_content_back);

        read_view_title=findComponent(R.id.read_view_title);
        read_view_author=findComponent(R.id.read_view_author);
        read_view_time=findComponent(R.id.read_view_time);
        read_view_hot=findComponent(R.id.read_view_hot);
        read_view_content=findComponent(R.id.read_view_content);
        // 启用WebView的支持JavaScript代码
        read_view_content.getSettings().setJavaScriptEnabled(true);
        // 设置WebView的缩放模式为适应屏幕
        read_view_content.getSettings().setLoadWithOverviewMode(true);
        read_view_content.getSettings().setUseWideViewPort(true);

        bindViewTrans();
        bindDataTrans();
    }

    private void bindDataTrans() {
        String title=TAG_Intent.getStringExtra(mContext.getString(R.string.mTitle));
        String author=TAG_Intent.getStringExtra(mContext.getString(R.string.mAuthor));
        read_view_title.setText(title);
        read_view_author.setText(author);
        read_view_time.setText(TAG_Intent.getStringExtra(mContext.getString(R.string.mCreateTime)));
        read_view_hot.setText(String.valueOf(TAG_Intent.getIntExtra(mContext.getString(R.string.mHot),0)));
        //read_view_content.setText("");//先置空,防止出现视图错误
        //String test=mContext.getString(R.string.BackUrl)+mContext.getString(R.string.Url_GetFile)+ TAG_Intent.getStringExtra(mContext.getString(R.string.mContent));
        //read_view_content.setText(test);
        //showToast(test);
        DataTransController.getArticleContent(mContext,title,author,read_view_content);
    }

    private void bindViewTrans() {
        lay_read_content_back.setOnClickListener(new ArticleViewOnClick());
        //read_view_content.setOnClickListener(new ArticleViewOnClick());

        // 创建GestureDetector实例
        final GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // 双击事件处理逻辑
                if(!hideTitleBar){
                    lay_read_info.setVisibility(View.GONE);
                    hideTitleBar=!hideTitleBar;
                }else{
                    lay_read_info.setVisibility(View.VISIBLE);
                    hideTitleBar=!hideTitleBar;
                }
                return true;
            }
        });

        // 设置WebView的OnTouchListener
        read_view_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 将触摸事件传递给GestureDetector进行处理
                gestureDetector.onTouchEvent(event);

                // 处理滑动事件
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        // 在此处处理滑动事件
                        // 可以根据需要进行相应的处理逻辑
                        break;
                }

                // 返回false表示继续处理其他触摸事件
                return false;
            }
        });
    }

    private class ArticleViewOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(!mPressed){
                mPressed = true;
                new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                    @Override
                    public void run() {
                        mPressed = false;
                    }
                }, 1000);
            }
            else{
                if(!hideTitleBar){
                    lay_read_info.setVisibility(View.GONE);
                    hideTitleBar=!hideTitleBar;
                }else{
                    lay_read_info.setVisibility(View.VISIBLE);
                    hideTitleBar=!hideTitleBar;
                }
            }
        }
    }

    @Override
    protected String initTitle() {
        return null;
    }
}
