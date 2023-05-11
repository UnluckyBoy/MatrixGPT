package com.matrix.matrix_chat.UI.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.matrix.matrix_chat.Network.API.Back.getArticlesApi;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.ArticlesBean;
import com.matrix.matrix_chat.Network.Service.Back.getArticleService;
import com.matrix.matrix_chat.R;
import com.matrix.matrix_chat.UI.Adapter.AllArticlesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {
    private View view;
    private String args=null;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<ArticlesBean.ArticleBean> articleList=new ArrayList<ArticlesBean.ArticleBean>();;

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
        mRecyclerView=view.findViewById(R.id.article_list_view);

        InitArticlesData();
        initRefresh(view);
    }

    /**加载数据**/
    private void InitArticlesData(){
        getArticlesApi mgetArticlesApi=new getArticlesApi();
        mgetArticlesApi.SetUrl(view.getContext().getString(R.string.BackUrl)+view.getContext().getString(R.string.Url_Article));
        getArticleService mgetArticleService=mgetArticlesApi.getService();
        Call<ArticlesBean> call_articles=mgetArticleService.getState();
        call_articles.enqueue(new Callback<ArticlesBean>() {
            @Override
            public void onResponse(Call<ArticlesBean> call, Response<ArticlesBean> response) {
                if(response.body()!=null){
                    if(response.body().getArticles()!=null){
                        Toast.makeText(view.getContext(),"获取数据成功:"+response.body().getArticles().get(0).getmTitle(),Toast.LENGTH_SHORT).show();
//                        for(int i=0;i<response.body().getArticles().size();i++){
//                            articleList.add(response.body().getArticles().get(i));
//                        }
//                        bindDataView(articleList);
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
    /**绑定数据到视图**/
    private void bindDataView(final List<ArticlesBean.ArticleBean> articles){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(linearLayoutManager);
                AllArticlesAdapter articlesAdapter=new AllArticlesAdapter(getActivity(),articles);
                articlesAdapter.SetOnItemClickListener(new AllArticlesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(view.getContext(),"点击了"+position,Toast.LENGTH_SHORT).show();
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
            }
        },1000);
    }

    private void initRefresh(View view){
        mSwipeRefreshLayout=view.findViewById(R.id.swipeRefreshLay);
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
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(view.getContext(),"刷新成功", Toast.LENGTH_SHORT).show();
                }
            },1500);
        }
    }
}
