package com.matrix.matrix_chat.UI.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.matrix.matrix_chat.Network.API.Back.RegisterApi;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.LoginBean;
import com.matrix.matrix_chat.Network.Service.Back.RegisterService;
import com.matrix.matrix_chat.R;
import com.matrix.matrix_chat.UITool.MatrixDialogManager;
import com.matrix.matrix_chat.UITool.PwdEditView;
import com.matrix.matrix_chat.UtilTool.DataSharaPreferenceManager;
import com.matrix.matrix_chat.UtilTool.Pwd3DESUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ClassName RegisterActivity
 * @Author Create By Administrator
 * @Date 2023/4/11 0011 15:45
 */
public class RegisterActivity extends Activity {
    private final RegisterActivity TGA=RegisterActivity.this;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anctivity_register);
        InitRun();
    }

    private void InitRun() {
        Button okBtn,cancelBtn;
        okBtn=findViewById(R.id.OK);
        cancelBtn=findViewById(R.id.Cacel);

        okBtn.setOnClickListener(new RegisterClickListener());
        cancelBtn.setOnClickListener(new RegisterClickListener());
    }

    private class RegisterClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.OK:
                    RegisterTrans();
                    break;
                case R.id.Cacel:
                    BackMain();
                    break;
            }
        }
    }

    private void RegisterTrans(){

        PwdEditView name = (PwdEditView) findViewById(R.id.use_register);
        PwdEditView account = (PwdEditView) findViewById(R.id.account_register);
        PwdEditView pwd = (PwdEditView) findViewById(R.id.pwd_register);
        PwdEditView confirmPwd = (PwdEditView) findViewById(R.id.pwd_confirm);

        final String user_name = name.getText().toString();
        final String user_account = account.getText().toString();
        final String user_pwd = pwd.getText().toString();
        final String ispwd=confirmPwd.getText().toString();
        if (!(ispwd.equals(user_pwd))) {
            Toast.makeText(RegisterActivity.this,TGA.getString(R.string.pwdConfirmError),Toast.LENGTH_SHORT).show();
        }
        else {
            RegisterApi userApi = new RegisterApi();
            userApi.SetUrl(TGA.getString(R.string.BackUrl)+TGA.getString(R.string.Url_UserInfo));
            RegisterService registerService = userApi.getService();
            Call<LoginBean> call_Register = registerService.getState(user_name,user_account,user_pwd);
            call_Register.enqueue(new Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                    if(response.body()!=null){
                        if (response.body().getResult().equals("success")) {
                            Toast.makeText(RegisterActivity.this,
                                    "恭喜注册成功！", Toast.LENGTH_SHORT).show();

                            DataSharaPreferenceManager.setSharaPreferenceData(RegisterActivity.this,response);

                            Intent main_intent = new Intent(TGA, MainActivity.class);
                            setExtra(main_intent,response);
                            startActivity(main_intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, TGA.getString(R.string.accountIsRegis),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this,TGA.getString(R.string.ResponseBodyNull), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginBean> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this,TGA.getString(R.string.NetworkFailure), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        BackMain();
    }

    private void BackMain(){
        String[] names={TGA.getString(R.string.SystemTitle),
                TGA.getString(R.string.cancelRegister),
                TGA.getString(R.string.Confirm), TGA.getString(R.string.Cancel)};

        new MatrixDialogManager().ShowMatrixDialog(names,TGA,MainActivity.class);
    }

    private void setExtra(Intent intent,Response<LoginBean> response){
        intent.putExtra("U_id",response.body().getId());
        intent.putExtra("U_head",response.body().getHead());
        intent.putExtra("U_name",response.body().getName());
        intent.putExtra("U_password",response.body().getPassword());
        intent.putExtra("U_sex",response.body().getSex());
        intent.putExtra("U_account",response.body().getAccount());
        intent.putExtra("U_phone",response.body().getPhone());
        intent.putExtra("U_email",response.body().getEmail());
        intent.putExtra("U_gptNum",response.body().getGptNum());
        intent.putExtra("U_level",response.body().getLevel());
    }
}
