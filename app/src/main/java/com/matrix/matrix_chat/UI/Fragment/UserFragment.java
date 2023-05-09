package com.matrix.matrix_chat.UI.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.matrix.matrix_chat.R;
import com.matrix.matrix_chat.UI.Activity.LoginActivity;
import com.matrix.matrix_chat.UITool.CircleImageView;
import com.squareup.picasso.Picasso;

public class UserFragment extends Fragment {
    private Intent intent_UserFragment;
    private View view;
    private Button mInfoBtn;
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
        view = inflater.inflate(R.layout.fragment_user, container, false);
        Bundle bundle = getArguments();
        String args = bundle.getString("args");

        InitData(view);
        return view;
    }

    private void InitData(View view) {
        TextView userName,userLevel;

        userName=view.findViewById(R.id.userName);
        userLevel=view.findViewById(R.id.userLevel);

        userName.setText(intent_UserFragment.getStringExtra("U_name"));
        switch (intent_UserFragment.getIntExtra("U_level",0)){
            case 9:
                userLevel.setText("管理员");
                break;
            case 1:
                userLevel.setText("普通用户");
                break;
            default:
                userLevel.setText("游客");
                break;
        }

        imageButton=view.findViewById(R.id.user_head);
        SetHead(intent_UserFragment.getStringExtra("U_head"));

        mInfoBtn=view.findViewById(R.id.info_btn);
        mInfoBtn.setOnClickListener(new UserFraClickListener());
    }

    private void SetHead(String headUrl) {
        String path=getResources().getText(R.string.BackUrl)+"/getImage"+headUrl;
        Picasso.get()
                .load(path)
                .error(R.drawable.no_user)
                .into(imageButton);
    }

    private class UserFraClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.info_btn:
                    String account=intent_UserFragment.getStringExtra("U_account");
                    if(account.equals(null)||account.equals("")){
                        Intent intent=new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }else{
                        Toast.makeText(view.getContext(),"已登录:"+account,Toast.LENGTH_SHORT).show();
                    }
                break;
            }
        }
    }
}
