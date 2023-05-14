package com.matrix.matrix_chat.UI.Fragment;

import static android.app.Activity.RESULT_OK;

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
import android.view.WindowManager;
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

import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
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
import java.util.ArrayList;

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

    private ActivityResultLauncher<Intent> launcher;
    private PopupWindow selectImageWindow;//弹窗


    public static UserFragment newInstance(String param1) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString("args", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**launcher必须在onCreate初始化并处理结果**/
        //initLauncher();

    }

    /**初始化launcher打开相册,已用其他工具替代**/
    private void initLauncher(){
        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    /**处理成功,上传头像**/
                    final String localPicturePath = BGAPhotoPickerActivity.getSelectedImages(result.getData()).get(0);
                    upHead(localPicturePath);
                }else{
                    //失败处理
                    Toast.makeText(getContext(),"打开失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        String path=getResources().getText(R.string.BackUrl)+getResources().getString(R.string.Url_GetFile)+headUrl;
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
                    popUpWindow(v);
                break;
                case R.id.user_head:
                    /**使用launcher.launch()打开图库**/
                    //launcher.launch(BGAPhotoPickerActivity.newIntent(getActivity(), null, 1, null, false));
                    selectHeadWindow(v);
                    break;
                case R.id.btn_info_edit:
                    Toast.makeText(getContext(),"功能未实装,敬请期待",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**信息视图弹窗**/
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
        //popupWindow.showAsDropDown(view,100,100, Gravity.BOTTOM);//显示在view控件的正左下方
        popupWindow.showAsDropDown(view,100,0, Gravity.CENTER);//显示在view控件的正左上方

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


    private void selectHeadWindow(View view){
        View popUpView=LayoutInflater.from(view.getContext()).inflate(R.layout.view_selectimage_window, null);
        selectImageWindow=new PopupWindow(view);
        selectImageWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        selectImageWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        selectImageWindow.setContentView(popUpView);
        selectImageWindow.setOutsideTouchable(true);
        selectImageWindow.setFocusable(true);
        //popupWindow.setAnimationStyle(5);//设置动画效果
        //selectImageWindow.setTouchable(true);
        selectImageWindow.setBackgroundDrawable(new ColorDrawable(view.getResources().getColor(R.color.transparent,null)));
        /**设置背景为暗**/
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        selectImageWindow.showAtLocation(getActivity().getWindow().getDecorView(),Gravity.CENTER,0,0);
        /**点击外部时，关闭遮罩**/
        selectImageWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });

        Button btn_photo,btn_camera,btn_cancel;
        btn_photo=(Button)popUpView.findViewById(R.id.btn_photo);
        btn_camera=(Button)popUpView.findViewById(R.id.btn_camera);
        btn_cancel=(Button)popUpView.findViewById(R.id.btn_cancel);

        btn_photo.setOnClickListener(new headWinClickListener());
        btn_camera.setOnClickListener(new headWinClickListener());
        btn_cancel.setOnClickListener(new headWinClickListener());
    }
    private class headWinClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_photo:
                    //相册
                    PictureSelector.create(getActivity())
                            .openSystemGallery(SelectMimeType.ofImage())
                            .forSystemResult(new OnResultCallbackListener<LocalMedia>() {
                                @Override
                                public void onResult(ArrayList<LocalMedia> result) {
                                    //Toast.makeText(getContext(), "打开相册"+result.get(0).getRealPath(), Toast.LENGTH_SHORT).show();
                                    final String localPicturePath = result.get(0).getRealPath();
                                    upHead(localPicturePath);
                                }
                                @Override
                                public void onCancel() {
                                    //closePopupWindow();
                                }
                            });
                    closePopupWindow();
                    break;
                case R.id.btn_camera:
                    //拍照
                    //Toast.makeText(getContext(), "打开相机", Toast.LENGTH_SHORT).show();
                    PictureSelector.create(getActivity())
                            .openCamera(SelectMimeType.ofImage())
                            .forResultActivity(new OnResultCallbackListener<LocalMedia>() {
                                @Override
                                public void onResult(ArrayList<LocalMedia> result) {
                                    //Toast.makeText(getContext(), "拍照:"+result.get(0).getRealPath(), Toast.LENGTH_SHORT).show();
                                    final String localPicturePath = result.get(0).getRealPath();
                                    upHead(localPicturePath);
                                }

                                @Override
                                public void onCancel() {
                                    //closePopupWindow();
                                }
                            });
                    closePopupWindow();
                    break;
                case R.id.btn_cancel:
                    //取消
                    closePopupWindow();
                    break;
            }
        }
    }
    /**关闭弹窗**/
    private void closePopupWindow() {
        if (selectImageWindow != null && selectImageWindow.isShowing()) {
            selectImageWindow.dismiss();
            selectImageWindow = null;
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 1f;
            getActivity().getWindow().setAttributes(lp);
        }
    }
}
