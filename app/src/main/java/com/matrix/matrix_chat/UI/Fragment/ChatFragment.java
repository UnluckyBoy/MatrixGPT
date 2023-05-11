package com.matrix.matrix_chat.UI.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.matrix.matrix_chat.R;

public class ChatFragment extends Fragment {
    private View view;
    private String args=null;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

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
        bindDataView(view);
        initRefresh(view);
    }

    private void bindDataView(View view){
        mRecyclerView=view.findViewById(R.id.article_list_view);
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
