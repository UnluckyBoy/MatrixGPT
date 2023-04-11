package com.matrix.matrixgpt.UI.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.matrix.matrixgpt.R;
import com.matrix.matrixgpt.UITool.CircleImageView;
import com.squareup.picasso.Picasso;

public class UserFragment extends Fragment {
    private Intent intent_UserFragment;
    private View view;
    private String args=null;

    private CircleImageView imageButton=null;
    private Button editBt;
    private TextView userName;
    private EditText A_Name;

    public static UserFragment newInstance(String param1) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString("args", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        if (view != null) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            if (parent != null) {
//                parent.removeView(view);
//            }
//        } else {
//            view = inflater.inflate(R.layout.user_fragment, container, false);
//            Bundle bundle = getArguments();
//            agrs1 = bundle.getString("agrs1");
//        }

        intent_UserFragment=getActivity().getIntent();

        view = inflater.inflate(R.layout.user_fragment, container, false);
        Bundle bundle = getArguments();
        args = bundle.getString("args");

        if(args!=null){
            Log.i("MyFragment用户模块",args);
            InitData(view,intent_UserFragment);
        }

        return view;
    }

    private void InitData(View view,Intent args_Intent) {
        String test_head=args_Intent.getStringExtra("U_image");
        //String test_name=args_Intent.getStringExtra("U_name");
        userName=view.findViewById(R.id.userName);
        userName.setText(args_Intent.getStringExtra("U_name"));
        SetHead(test_head);
    }

    private void SetHead(String headUrl) {
        Picasso.get()
                .load("https://5841dcdd.r5.cpolar.top/getImage"+headUrl)
                .error(R.drawable.no_user)
                .into(imageButton);
    }
}
