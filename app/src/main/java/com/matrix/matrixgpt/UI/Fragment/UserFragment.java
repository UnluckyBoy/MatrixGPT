package com.matrix.matrixgpt.UI.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

    private CircleImageView imageButton;

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
        String args = bundle.getString("args");

        if(args.equals(null)||args.equals("")){
            //游客方式登录

        }else{
            Log.i("MyFragment用户模块",args);
            InitData(view,intent_UserFragment);
        }

        return view;
    }

    private void InitData(View view,Intent args) {
        TextView userName,userLevel;

        userName=view.findViewById(R.id.userName);
        userLevel=view.findViewById(R.id.userLevel);

        userName.setText(args.getStringExtra("U_name"));
        switch (args.getIntExtra("U_level",0)){
            case 1:
                userLevel.setText("管理员");
                break;
            case 0:
                userLevel.setText("普通用户");
                break;
            default:
                userLevel.setText("游客");
                break;
        }

        imageButton=view.findViewById(R.id.user_head);
        SetHead(args.getStringExtra("U_image"));
    }

    private void SetHead(String headUrl) {
        Picasso.get()
                .load("https://60fb829.r10.cpolar.top/getImage"+headUrl)
                .error(R.drawable.no_user)
                .into(imageButton);
    }
}
