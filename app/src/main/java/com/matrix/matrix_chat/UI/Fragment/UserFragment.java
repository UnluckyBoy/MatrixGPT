package com.matrix.matrix_chat.UI.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.matrix.matrix_chat.Network.API.Back.upHeadApi;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.IsTrueBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.LoginBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.UserBean;
import com.matrix.matrix_chat.Network.Service.Back.upHeadService;
import com.matrix.matrix_chat.R;
import com.matrix.matrix_chat.UI.Activity.LoginActivity;
import com.matrix.matrix_chat.UITool.CircleImageView;
import com.matrix.matrix_chat.UtilTool.DataSharaPreferenceManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {
    private Intent intent_UserFragment;
    private View view;
    private Button mInfoBtn,mEditInfoBtn;
    private String args=null;
    private CircleImageView imageButton=null;
    private static int openPickCode=99;

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
        args = bundle.getString("args");

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
                userLevel.setText("用户");
                break;
            default:
                userLevel.setText("游客");
                break;
        }

        imageButton=view.findViewById(R.id.user_head);
        SetHead(intent_UserFragment.getStringExtra("U_head"));

        mInfoBtn=view.findViewById(R.id.info_btn);
        mEditInfoBtn=view.findViewById(R.id.btn_info_edit);
        //mInfoBtn.setOnClickListener(new UserFraClickListener());
        //imageButton.setOnClickListener(new UserFraClickListener());
        //mEditInfoBtn.setOnClickListener(new UserFraClickListener());
        if(args.equals(null)||args.equals("")||args==null||args==""){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }else{
            mInfoBtn.setOnClickListener(new UserFraClickListener());
            imageButton.setOnClickListener(new UserFraClickListener());
            mEditInfoBtn.setOnClickListener(new UserFraClickListener());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == openPickCode) {
            final String localPicturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
            upHead(localPicturePath);
        }
    }

    private void upHead(String headPath) {
        //Toast.makeText(view.getContext(),"图片地址"+headPath,Toast.LENGTH_SHORT).show();
        upHeadApi mUpHeadApi=new upHeadApi();
        mUpHeadApi.SetUrl(view.getContext().getString(R.string.BackUrl)+view.getContext().getString(R.string.Url_UserInfo));
        upHeadService upHeadService=mUpHeadApi.getService();
        File file=new File(headPath);
        RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body=MultipartBody.Part.createFormData("file",file.getName(),requestBody);
        Call<LoginBean> call=upHeadService.getState(args,body);
        call.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                if(response.body()!=null){
                    if(response.body().getResult().equals("success")){
                        SetHead(response.body().getHead());
                        //freshData();
                        DataSharaPreferenceManager.setExtra(response,intent_UserFragment);
                        Toast.makeText(view.getContext(),"头像更新成功",Toast.LENGTH_SHORT).show();
                    }else{
                        SetHead(intent_UserFragment.getStringExtra("U_head"));
                    }
                }else{
                    SetHead(intent_UserFragment.getStringExtra("U_head"));
                }
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                Toast.makeText(view.getContext(),view.getContext().getString(R.string.NetworkFailure),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SetHead(String headUrl) {
        String path=getResources().getText(R.string.BackUrl)+"/get_file/"+headUrl;
        Picasso.get()
                .load(path)
                .memoryPolicy(MemoryPolicy.NO_CACHE)//跳过缓存
                .networkPolicy(NetworkPolicy.NO_CACHE)//跳过网络缓存
                .error(R.drawable.no_user)
                .into(imageButton);
    }

    /**按钮响应逻辑**/
    private class UserFraClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.info_btn:
//                    String account=intent_UserFragment.getStringExtra("U_account");
//                    if(account.equals(null)||account.equals("")||account==null||account==""){
//                        Intent intent=new Intent(getActivity(), LoginActivity.class);
//                        startActivity(intent);
//                        getActivity().finish();
//                    }else{
//                        //Toast.makeText(view.getContext(),"已登录:"+account,Toast.LENGTH_SHORT).show();
//                        popUpWindow(v);
//                    }
                    popUpWindow(v);
                break;
                case R.id.user_head:
                    startActivityForResult(BGAPhotoPickerActivity.newIntent(getActivity(), null,
                            1, null, false), openPickCode);

                    //Intent intent = new  Intent(Intent.ACTION_PICK);
                    //intent.setType("image/*");//指定获取的是图片
                    //startActivityForResult(intent, 55);
                    break;
                case R.id.btn_info_edit:
                    Toast.makeText(getContext(),"功能未实装,敬请期待",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void popUpWindow(View view){
        //创建窗口
        PopupWindow popupWindow=new PopupWindow(view);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View popUpView=LayoutInflater.from(view.getContext()).inflate(R.layout.view_info_window, null);
        popupWindow.setContentView(popUpView);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(view.getResources().getColor(R.color.transparent)));

        //popupWindow.showAsDropDown(view);//弹出创建显示在按钮下面
        popupWindow.showAsDropDown(view,100,0, Gravity.CENTER);//显示在view控件的正左上方
        //popupWindow.showAsDropDown(view,100,100, Gravity.BOTTOM);//显示在view控件的正左下方

        TextView accountText,nameText,emailText,phoneText,genderText,gpt_numText;
        Button getNumBtn=popUpView.findViewById(R.id.top_up_btn);
        accountText=popUpView.findViewById(R.id.info_account);
        nameText=popUpView.findViewById(R.id.info_name);
        emailText=popUpView.findViewById(R.id.info_email);
        phoneText=popUpView.findViewById(R.id.info_phone);
        genderText=popUpView.findViewById(R.id.info_gender);
        gpt_numText=popUpView.findViewById(R.id.info_gpt_num);

        accountText.setText(intent_UserFragment.getStringExtra("U_account"));
        nameText.setText(intent_UserFragment.getStringExtra("U_name"));
        emailText.setText(intent_UserFragment.getStringExtra("U_email"));
        phoneText.setText(intent_UserFragment.getStringExtra("U_phone"));
        genderText.setText(intent_UserFragment.getStringExtra("U_sex"));
        gpt_numText.setText(String.valueOf(intent_UserFragment.getIntExtra("U_gptNum",0)));

        getNumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"充值功能未实装,敬请期待",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**开启线程刷新数据**/
    private void freshData(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SetHead(intent_UserFragment.getStringExtra("U_head"));
            }
        },1500);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            //Toast.makeText(getContext(),"UserFragment",Toast.LENGTH_SHORT).show();
            //SetHead(intent_UserFragment.getStringExtra("U_head"));
            //freshData();
        }
    }
}
