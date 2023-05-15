package com.matrix.matrix_chat.UI.Activity;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.matrix.matrix_chat.Network.API.Back.LoginApi;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.LoginBean;
import com.matrix.matrix_chat.Network.Service.Back.LoginService;
import com.matrix.matrix_chat.R;
import com.matrix.matrix_chat.UITool.MatrixDialogManager;
import com.matrix.matrix_chat.UITool.PwdEditView;
import com.matrix.matrix_chat.UtilTool.DataSharaPreferenceManager;
import com.matrix.matrix_chat.UtilTool.Pwd3DESUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ClassName LoginActivity
 * @Author Create By Administrator
 * @Date 2023/4/11 0011 12:34
 */
public class LoginActivity extends Activity {
    private final LoginActivity TGA=LoginActivity.this;

    private PwdEditView Uid,Upwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitRun();
    }

    private void InitRun() {
        SharedPreferences sharedPreferences =
                LoginActivity.this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String account = sharedPreferences.getString("account", null);
        String password = sharedPreferences.getString("password", null);
        Uid=findViewById(R.id.uId_edit);
        Upwd=findViewById(R.id.uPwd_edit);
        Uid.setText(account);
        Upwd.setText(Pwd3DESUtil.decode3Des(this.getResources().getString(R.string.encryptionkey),password));

        Button login,cancel,register;
        ImageButton biliBtn,qqBtn,wechatBtn;
        TextView mOther_login_title;
        mOther_login_title=findViewById(R.id.other_login_title);
        mOther_login_title.setText(Html.fromHtml(TGA.getString(R.string.other_login),FROM_HTML_MODE_COMPACT));

        login=findViewById(R.id.login);
        cancel=findViewById(R.id.cancel);
        register=findViewById(R.id.register);
        biliBtn=findViewById(R.id.bili_Btn);
        qqBtn=findViewById(R.id.qq_Btn);
        wechatBtn=findViewById(R.id.wechat_Btn);

        login.setOnClickListener(new BtnClickListener());
        cancel.setOnClickListener(new BtnClickListener());
        register.setOnClickListener(new BtnClickListener());
        biliBtn.setOnClickListener(new BtnClickListener());
        qqBtn.setOnClickListener(new BtnClickListener());
        wechatBtn.setOnClickListener(new BtnClickListener());
    }

    private class BtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login:
                    LoginTrans();
                    //testlogin();
                    break;
                case R.id.cancel:
                    CancelLoginDialog();
                    break;
                case R.id.register:
                    Intent register=new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(register);
                    finish();
                    break;
                case R.id.bili_Btn:
                    Toast.makeText(TGA,"功能尚未实现,敬请期待!",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.qq_Btn:
                    Toast.makeText(TGA,"功能尚未实现,敬请期待!",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.wechat_Btn:_Btn:
                    Toast.makeText(TGA,"功能尚未实现,敬请期待!",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**登录**/
    private void LoginTrans(){

        final String name=Uid.getText().toString();
        final String pwd=Upwd.getText().toString();
        if(name==null||pwd==null||name==""||pwd==""){
            Toast.makeText(this,"请输入账号信息",Toast.LENGTH_SHORT).show();
        }else{
            LoginApi loginApi=new LoginApi();
            loginApi.SetUrl(TGA.getString(R.string.BackUrl)+TGA.getString(R.string.Url_UserInfo));
            LoginService loginService=loginApi.getService();
            Call<LoginBean> call_login=loginService.getState(name,pwd);
            call_login.enqueue(new Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                    if(response.body()!=null){
                        if(response.body().getResult().equals("success")){
                            /**使用SharaPreference记录数据**/
                            DataSharaPreferenceManager.setSharaPreferenceData(LoginActivity.this,response);

                            Intent intent=new Intent(TGA,MainActivity.class);
                            setExtra(response,intent);
                            startActivity(intent);
                            finish();
                        }else if(response.body().getResult().equals("login_lock")){
                            //MatrixDialogManager.ShowSystemDialog(TGA);
                            Toast.makeText(TGA, "账号已登录,请检查是否密码泄露",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(TGA, TGA.getString(R.string.userInfoError),Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(TGA, TGA.getString(R.string.ResponseBodyNull),Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<LoginBean> call, Throwable t) {
                    Toast.makeText(TGA, TGA.getString(R.string.NetworkFailure),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    /** 返回逻辑*/
    @Override
    public void onBackPressed() {
        CancelLoginDialog();
    }

    private void CancelLoginDialog(){
        String[] names = {TGA.getString(R.string.SystemTitle),
                TGA.getString(R.string.cancelLogin),
                TGA.getString(R.string.Confirm), TGA.getString(R.string.Cancel)};

        /**调用窗口方法**/
        new MatrixDialogManager().ShowMatrixDialog(names,TGA,MainActivity.class);
    }

    private void setExtra(Response<LoginBean> response,Intent intent){
        intent.putExtra(this.getString(R.string.info_id),response.body().getId());
        intent.putExtra(this.getString(R.string.info_head),response.body().getHead());
        intent.putExtra(this.getString(R.string.info_name),response.body().getName());
        intent.putExtra(this.getString(R.string.info_password),response.body().getPassword());
        intent.putExtra(this.getString(R.string.info_sex),response.body().getSex());
        intent.putExtra(this.getString(R.string.info_account),response.body().getAccount());
        intent.putExtra(this.getString(R.string.info_phone),response.body().getPhone());
        intent.putExtra(this.getString(R.string.info_email),response.body().getEmail());
        intent.putExtra(this.getString(R.string.info_gptNum),response.body().getGptNum());
        intent.putExtra(this.getString(R.string.info_level),response.body().getLevel());
    }

//    /**
//     * 将数据储存
//     * @param response
//     */
//    public void setSharaPreferenceData(Response<LoginBean> response){
//        SharedPreferences sharedPreferences =
//                LoginActivity.this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("account", response.body().getAccount());
//        editor.putString("password", response.body().getPassword());
//        editor.commit();
//    }
//
//    public void getSharaPreferenceData(){
//        SharedPreferences sharedPreferences =
//                LoginActivity.this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
//        String account = sharedPreferences.getString("account", null);
//        String password = sharedPreferences.getString("password", null);
//        Toast.makeText(this,"记录的账号信息:"+account+"___"+password,Toast.LENGTH_SHORT).show();
//    }
}
