package com.matrix.matrix_chat.UI.Fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.matrix.matrix_chat.Network.API.Back.getArticlesApi;
import com.matrix.matrix_chat.Network.API.Back.upArticleContentApi;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.ArticleBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.ArticlesBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.UpArticleBean;
import com.matrix.matrix_chat.Network.Service.Back.getArticleService;
import com.matrix.matrix_chat.Network.Service.Back.upArticleContentService;
import com.matrix.matrix_chat.R;
import com.matrix.matrix_chat.UI.Activity.ReadActivity;
import com.matrix.matrix_chat.UI.Adapter.AllArticlesAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {
    private View view;
    private String args=null;
    private static Intent intent_Chat;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RelativeLayout add_article_lay;
    private LinearLayout add_image_lay;

    private List<ArticleBean> articleList;
    private Button add_article,hide_add_view_btn,add_image_btn,add_video_btn,up_btn;
    private EditText add_title,add_description,add_content;

//    private TTAdNative mTTAdNative;
//    private AdLoadListener mAdLoadListener;


    public static ChatFragment newInstance(String param1) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString("args", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {

        if(view!=null){
            ViewGroup parent=(ViewGroup) view.getParent();
            if(parent!=null){
                parent.removeView(view);
            }
        }else{
            view = inflater.inflate(R.layout.fragment_chat, container, false);

            Bundle bundle = getArguments();
            args = bundle.getString("args");
        }

        InitView(view);
        return view;
    }

    private void InitView(View view){
        intent_Chat=getActivity().getIntent();

        mSwipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLay);
        mRecyclerView=(RecyclerView) view.findViewById(R.id.article_list_view);
        add_article_lay=(RelativeLayout)view.findViewById(R.id.add_article_lay);
        add_article_lay.setVisibility(View.GONE);//先隐藏视图
        add_image_lay=(LinearLayout)view.findViewById(R.id.add_image_lay);

        add_title=(EditText)view.findViewById(R.id.add_title);
        add_description=(EditText)view.findViewById(R.id.add_description);
        add_content=(EditText)view.findViewById(R.id.add_content);

        add_article=(Button)view.findViewById(R.id.add_article);
        hide_add_view_btn=(Button)view.findViewById(R.id.hide_add_view_btn);

        add_image_btn=(Button)view.findViewById(R.id.add_image_btn);
        add_video_btn=(Button)view.findViewById(R.id.add_video_btn);
        up_btn=(Button)view.findViewById(R.id.up_btn);

        InitArticlesData();
        initRefresh(view);
        btnClickTrans();
    }

    /**加载数据**/
    private void InitArticlesData(){
        articleList=new ArrayList<ArticleBean>();

        getArticlesApi mgetArticlesApi=new getArticlesApi();
        mgetArticlesApi.SetUrl(view.getContext().getString(R.string.BackUrl)+view.getContext().getString(R.string.Url_Article));
        getArticleService mgetArticleService=mgetArticlesApi.getService();
        Call<ArticlesBean> call_articles=mgetArticleService.getState();
        call_articles.enqueue(new Callback<ArticlesBean>() {
            @Override
            public void onResponse(Call<ArticlesBean> call, Response<ArticlesBean> response) {
                if(response.body()!=null){
                    if(response.body().getArticles()!=null){
                        //Toast.makeText(view.getContext(),"获取数据成功:"+response.body().getArticles().get(0).getmTitle(),Toast.LENGTH_SHORT).show();
                        for(int i=0;i<response.body().getArticles().size();i++){
                            articleList.add(response.body().getArticles().get(i));
                        }
                        bindDataView(articleList);
                    }else{
                        Toast.makeText(view.getContext(),"获取数据失败",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(view.getContext(),view.getContext().getString(R.string.ResponseBodyNull),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArticlesBean> call, Throwable t) {
                Toast.makeText(view.getContext(),view.getContext().getString(R.string.NetworkFailure),Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**创建文章**/
    public void btnClickTrans(){
        add_article.setOnClickListener(new setBtnClick());
        hide_add_view_btn.setOnClickListener(new setBtnClick());
    }
    /**绑定数据到视图**/
    private void bindDataView(final List<ArticleBean> articles){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(linearLayoutManager);
                AllArticlesAdapter articlesAdapter=new AllArticlesAdapter(getActivity(),articles);
                /**点击响应事件**/
                articlesAdapter.SetOnItemClickListener(new AllArticlesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(view.getContext(),"点击了"+position,Toast.LENGTH_SHORT).show();
                        Intent readIntent=new Intent(getActivity(), ReadActivity.class);
                        setExtra(readIntent,articles,position);
                        startActivity(readIntent);
//                        if(args==null){
//
//                        }else{
//
//                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                mRecyclerView.setAdapter(articlesAdapter);
            }
        },1000);
    }

    private void initRefresh(View view){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class SwipeRefresh implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    InitArticlesData();
                    mSwipeRefreshLayout.setRefreshing(false);
                    //Toast.makeText(view.getContext(),"刷新成功", Toast.LENGTH_SHORT).show();
                }
            },1500);
        }
    }

    private void setExtra(Intent intent, List<ArticleBean> list, int index){
        intent.putExtra(view.getContext().getString(R.string.mTitle),list.get(index).getmTitle());
        intent.putExtra(view.getContext().getString(R.string.mAuthor),list.get(index).getmAuthor());
        intent.putExtra(view.getContext().getString(R.string.mCreateTime),list.get(index).getmCreateTime());
        intent.putExtra(view.getContext().getString(R.string.mHot),list.get(index).getmHot());
        intent.putExtra(view.getContext().getString(R.string.mContent),list.get(index).getmContent());
    }

    private class setBtnClick implements View.OnClickListener {
        private String cover;
        private LocalMedia localMedia;
        //private Bitmap[] bitmap;
        //private int mapIndex=0;

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_article:
                    /**使用ViewTreeObserver确保在完全测量和绘制RelativeLayout后获取正确的宽度和高度**/
                    ViewTreeObserver vto_show = add_article_lay.getViewTreeObserver();
                    vto_show.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            // 获取宽度和高度
                            int width = add_article_lay.getWidth();
                            int height = add_article_lay.getHeight();
                            // 在这里进行你的操作
                            //Toast.makeText(view.getContext(), "height:"+height+"\twidth:"+width, Toast.LENGTH_SHORT).show();
                            setShowAnim(width,height);
                            add_image_btn.setOnClickListener(new setBtnClick());
                            add_video_btn.setOnClickListener(new setBtnClick());
                            up_btn.setOnClickListener(new setBtnClick());

                            // 在获取到宽度和高度后,移除监听器以避免多次回调
                            add_article_lay.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
                    add_article_lay.setVisibility(View.VISIBLE);//显示视图
                    add_article.setVisibility(View.GONE);//隐藏按钮
                    break;
                case R.id.hide_add_view_btn:
                    ViewTreeObserver vto_hide = add_article_lay.getViewTreeObserver();
                    vto_hide.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            // 获取宽度和高度
                            int width = add_article_lay.getWidth();
                            int height = add_article_lay.getHeight();
                            // 在这里进行你的操作
                            //Toast.makeText(view.getContext(), "height:"+height+"\twidth:"+width, Toast.LENGTH_SHORT).show();

                            setHideAnim(width,height);

                            // 在获取到宽度和高度后,移除监听器以避免多次回调
                            add_article_lay.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
                    add_article_lay.setVisibility(View.GONE);

                    AlphaAnimation show_btn_article_alpha = new AlphaAnimation(0, 1);
                    show_btn_article_alpha.setDuration(1000);
                    add_article.startAnimation(show_btn_article_alpha);//开始动画
                    add_article.setVisibility(View.VISIBLE);
                    break;
                case R.id.add_image_btn:
                    //final int[] image_start_index = {0};
                    PictureSelector.create(getActivity())
                            .openSystemGallery(SelectMimeType.ofImage())
                            .forSystemResult(new OnResultCallbackListener<LocalMedia>() {
                                @Override
                                public void onResult(ArrayList<LocalMedia> result) {
                                    //Toast.makeText(getContext(), "打开相册"+result.get(0).getRealPath(), Toast.LENGTH_SHORT).show();
                                    localMedia = result.get(0);
                                    //Toast.makeText(view.getContext(), "本地文件:"+localMedia.getFileName(), Toast.LENGTH_SHORT).show();

                                    cover=localMedia.getRealPath();

//                                    Bitmap bitmap = BitmapFactory.decodeFile(cover);// 获取要添加的图片资源
//                                    BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);// 将图片转换为Drawable
//                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//                                    SpannableStringBuilder builder = new SpannableStringBuilder();// 创建SpannableStringBuilder，并将图片添加到文本中
//                                    builder.append(add_content.getText().toString()+" ");
//                                    // 在指定位置插入图片
//                                    builder.setSpan(new ImageSpan(drawable), builder.length() - 1, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                    add_content.setText(builder);// 设置EditText的文本为SpannableStringBuilder

                                    //String temp=add_content.getText().toString();
                                    //image_start_index[0] =temp.length()+1;
                                    //Toast.makeText(view.getContext(), "add_edit内容长度:"+image_start_index[0], Toast.LENGTH_SHORT).show();
                                    //add_content.setText(temp+"{image/"+localMedia.getFileName()+":"+image_start_index[0]+"}");

                                    int image_width=add_image_lay.getWidth();
                                    Toast.makeText(view.getContext(), "图片区域高度:"+image_width, Toast.LENGTH_SHORT).show();

                                    for (int i = 0; i < result.size(); i++) {
                                        //建ImageView对象
                                        ImageView imageView = new ImageView(view.getContext());
                                        // 设置ImageView的宽高、图片资源等属性
                                        imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
                                        Bitmap bitmap = BitmapFactory.decodeFile(result.get(i).getRealPath());
                                        // 设置ImageView的图片资源
                                        imageView.setImageBitmap(bitmap);

                                        // 将ImageView添加到布局中
                                        add_image_lay.addView(imageView);
                                    }
                                    // 将ImageView添加到布局中
                                    //add_image_lay.addView(imageView);

                                }
                                @Override
                                public void onCancel() {
                                    //closePopupWindow();
                                }
                            });


                    break;
                case R.id.add_video_btn:
                    final int[] video_start_index = {0};
                    PictureSelector.create(getActivity())
                            .openSystemGallery(SelectMimeType.ofVideo())
                            .forSystemResult(new OnResultCallbackListener<LocalMedia>() {
                                @Override
                                public void onResult(ArrayList<LocalMedia> result) {
                                    //Toast.makeText(getContext(), "打开相册"+result.get(0).getRealPath(), Toast.LENGTH_SHORT).show();
                                    final LocalMedia localMedia = result.get(0);
                                    Toast.makeText(view.getContext(), "本地文件:"+localMedia.getFileName(), Toast.LENGTH_SHORT).show();

                                    String temp=add_content.getText().toString();
                                    video_start_index[0] =temp.length()+1;
                                    //Toast.makeText(view.getContext(), "add_edit内容长度:"+image_start_index[0], Toast.LENGTH_SHORT).show();
                                    add_content.setText(temp+"{video/"+localMedia.getFileName()+":"+video_start_index[0]+"}");

                                }
                                @Override
                                public void onCancel() {
                                    //closePopupWindow();
                                }
                            });
                    break;

                case R.id.up_btn:
                    //Toast.makeText(view.getContext(), "上传内容:"+add_edit.getText().toString(), Toast.LENGTH_SHORT).show();
                    upArticleContentApi mUpArticleContentApi=new upArticleContentApi();
                    mUpArticleContentApi.SetUrl(view.getContext().getString(R.string.BackUrl)+view.getContext().getString(R.string.Url_Article));
                    upArticleContentService mUpArticleContentService=mUpArticleContentApi.getService();

                    Toast.makeText(view.getContext(), "author:"+intent_Chat.getStringExtra(view.getContext().getString(R.string.info_name)), Toast.LENGTH_SHORT).show();
                    UpArticleBean articleBean=new UpArticleBean();
                    articleBean.setTitle(add_title.getText().toString());
                    articleBean.setCover("测试.png");
                    articleBean.setDescription(add_description.getText().toString());
                    articleBean.setContent(add_content.getText().toString());
                    articleBean.setAuthor(intent_Chat.getStringExtra(view.getContext().getString(R.string.info_name)));
                    Gson gson = new Gson();
                    String requestJson=gson.toJson(articleBean);

                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),requestJson);
                    Call<UpArticleBean> call = mUpArticleContentService.getState(requestBody);
                    call.enqueue(new Callback<UpArticleBean>() {
                        @Override
                        public void onResponse(Call<UpArticleBean> call, Response<UpArticleBean> response) {
                            // 处理响应结果
                        }

                        @Override
                        public void onFailure(Call<UpArticleBean> call, Throwable t) {
                            // 处理请求失败
                        }
                    });

                    break;
            }
        }
    }

    /**弹出框动画**/
    private void setShowAnim(final int width,final int height){
        //Toast.makeText(view.getContext(), "height:"+height, Toast.LENGTH_SHORT).show();
        AnimationSet animationSet=new AnimationSet(false);
        AlphaAnimation show_alpha = new AlphaAnimation(0, 1);
        TranslateAnimation show_translate=new TranslateAnimation(0, 0, height, 0);
        ScaleAnimation show_scale = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_PARENT,0.5f);

        animationSet.addAnimation(show_alpha);
        animationSet.addAnimation(show_translate);
        animationSet.addAnimation(show_scale);
        animationSet.setDuration(1000);
        animationSet.setRepeatMode(AnimationSet.REVERSE);
        animationSet.reset();//释放资源
        add_article_lay.startAnimation(animationSet);//开始动画
    }

    private void setHideAnim(final int width,final int height){
        AnimationSet animationSet=new AnimationSet(false);
        AlphaAnimation hide_alpha = new AlphaAnimation(1, 0);
        TranslateAnimation hide_translate=new TranslateAnimation(0, 0, 0, height);
        ScaleAnimation hide_scale = new ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_PARENT,0.5f);

        animationSet.addAnimation(hide_alpha);
        animationSet.addAnimation(hide_translate);
        animationSet.addAnimation(hide_scale);
        animationSet.setDuration(1000);
        animationSet.setRepeatMode(AnimationSet.REVERSE);
        animationSet.reset();//释放资源
        add_article_lay.startAnimation(animationSet);//开始动画
    }
}
