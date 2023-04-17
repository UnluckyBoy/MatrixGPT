package com.matrix.matrixgpt.UI.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.DislikeInfo;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.matrix.matrixgpt.R;
import com.matrix.matrixgpt.UI.Activity.testActivity;
import com.matrix.matrixgpt.UtilTool.AdverUtil.Dialog.MatrixDislikeDialog;
import com.matrix.matrixgpt.UtilTool.AdverUtil.MatrixToast;
import com.matrix.matrixgpt.UtilTool.AdverUtil.config.TTAdManagerHolder;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private View view;
    private String agrs1=null;


    public static ChatFragment newInstance(String param1) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
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
            agrs1 = bundle.getString("agrs1");
        }

        InitView();
        return view;
    }

    private void InitView(){
        Button mAdvBtn=view.findViewById(R.id.adv_btn);
        mAdvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), testActivity.class);
                startActivity(intent);
                //getActivity().finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
