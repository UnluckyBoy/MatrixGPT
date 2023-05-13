package com.matrix.matrix_chat.UI.Activity;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
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
    private TextView read_view_title,read_view_author,read_view_time,read_view_hot,read_view_content;
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
        read_view_content.setText("");//先置空,防止出现视图错误
        //String test=mContext.getString(R.string.BackUrl)+mContext.getString(R.string.Url_GetFile)+ TAG_Intent.getStringExtra(mContext.getString(R.string.mContent));
        //read_view_content.setText(test);
        //showToast(test);
        DataTransController.getArticleContent(mContext,title,author,read_view_content);
        read_view_content.setMovementMethod(ScrollingMovementMethod.getInstance());//实现滑动条
    }

    private void bindViewTrans() {
        lay_read_content_back.setOnClickListener(new ArticleViewOnClick());
        read_view_content.setOnClickListener(new ArticleViewOnClick());
        read_view_content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //showToast("长按");
                read_view_content.setTextIsSelectable(true);//长按复制
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