package com.matrix.matrixgpt.UI.Activity;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.matrix.matrixgpt.Network.API.LoginApi;
import com.matrix.matrixgpt.Network.ResponseBean.BackService.LoginBean;
import com.matrix.matrixgpt.Network.Service.LoginService;
import com.matrix.matrixgpt.R;
import com.matrix.matrixgpt.UITool.MatrixDialog;
import com.matrix.matrixgpt.UITool.MatrixDialogManager;
import com.matrix.matrixgpt.UITool.PwdEditView;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitRun();
    }

    private void InitRun() {
        Button login,cacel,register;
        TextView mOther_login_title;
        mOther_login_title=findViewById(R.id.other_login_title);
        mOther_login_title.setText(Html.fromHtml(TGA.getString(R.string.other_login),FROM_HTML_MODE_COMPACT));

        login=findViewById(R.id.login);
        cacel=findViewById(R.id.cancel);
        register=findViewById(R.id.register);
        login.setOnClickListener(new BtnClickListener());
        cacel.setOnClickListener(new BtnClickListener());
        register.setOnClickListener(new BtnClickListener());
    }

    private class BtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login:
                    LoginTrans();
                    break;
                case R.id.cancel:
                    CancelLoginDialog();
                    break;
                case R.id.register:
                    Intent register=new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(register);
                    finish();
                    break;
            }
        }
    }

    /**登录**/
    private void LoginTrans(){
        PwdEditView Uid=findViewById(R.id.uId_edit);
        PwdEditView Upwd=findViewById(R.id.uPwd_edit);
        final String name=Uid.getText().toString();
        final String pwd=Upwd.getText().toString();
        LoginApi loginApi=new LoginApi();
        LoginService loginService=loginApi.getService();
        Call<LoginBean> call_login=loginService.getState(name,pwd);
        call_login.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                if(response.body().getResult().equals("success")){
                    Intent intent=new Intent(TGA,MainActivity.class);
                    intent.putExtra("U_id",response.body().getId());
                    intent.putExtra("U_image",response.body().getImage());
                    intent.putExtra("U_name",response.body().getName());
                    intent.putExtra("U_pwd",response.body().getPassword());
                    intent.putExtra("U_sex",response.body().getSex());
                    intent.putExtra("U_account",response.body().getAccount());
                    intent.putExtra("U_phone",response.body().getPhone());
                    intent.putExtra("U_email",response.body().getEmail());
                    intent.putExtra("U_gptNum",response.body().getGptNum());
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(TGA, TGA.getString(R.string.userInfoError),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                Toast.makeText(TGA, TGA.getString(R.string.NetworkFailure),Toast.LENGTH_SHORT).show();
            }
        });
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
}
