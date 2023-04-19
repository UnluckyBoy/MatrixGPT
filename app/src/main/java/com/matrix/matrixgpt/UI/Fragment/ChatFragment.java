package com.matrix.matrixgpt.UI.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.bytedance.sdk.openadsdk.TTAdNative;
import com.matrix.matrixgpt.R;
import com.matrix.matrixgpt.UI.Activity.FullScreenAdvActivity;
import com.matrix.matrixgpt.UI.Activity.testNativeActivity;
import com.matrix.matrixgpt.UtilTool.AdverUtil.View.ShowFullScreeAdvClass;

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
        //testView();


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
//    private void testView(){
//        Button mAdvBtn=view.findViewById(R.id.adv_btn);
//        mAdvBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getContext(), testNativeActivity.class);
//                startActivity(intent);
//                //getActivity().finish();
//            }
//        });
//
//        Button mCustomAdvBtn=view.findViewById(R.id.adv_custom_btn);
//        mCustomAdvBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Intent intent=new Intent(getContext(), FullScreenAdvActivity.class);
//                //startActivity(intent);
//                //getActivity().finish();
//
//                ShowFullScreeAdvClass.loadAdv(getActivity());
//            }
//        });
//    }
}
