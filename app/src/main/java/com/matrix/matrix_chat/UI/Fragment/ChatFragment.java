package com.matrix.matrix_chat.UI.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.matrix.matrix_chat.R;

public class ChatFragment extends Fragment {
    private View view;
    private String args=null;

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
            view = inflater.inflate(R.layout.chat_fragment, container, false);

            Bundle bundle = getArguments();
            args = bundle.getString("args");
        }

        InitView();
        return view;
    }

    private void InitView(){
        testView();


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**测试**/
    private void testView(){
    }
}
