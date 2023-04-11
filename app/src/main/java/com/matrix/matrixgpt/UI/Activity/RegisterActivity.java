package com.matrix.matrixgpt.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.matrix.matrixgpt.Network.API.RegisterApi;
import com.matrix.matrixgpt.Network.ResponseBean.BackService.LoginBean;
import com.matrix.matrixgpt.Network.Service.RegisterService;
import com.matrix.matrixgpt.R;
import com.matrix.matrixgpt.UITool.MatrixDialogManager;

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
        EditText name = (EditText) findViewById(R.id.use_register);
        EditText account = (EditText) findViewById(R.id.account_register);
        EditText pwd = (EditText) findViewById(R.id.pwd_register);
        EditText confirmPwd = (EditText) findViewById(R.id.pwd_confirm);
        final String user_name = name.getText().toString();
        final String user_account = account.getText().toString();
        final String user_pwd = pwd.getText().toString();
        final String ispwd=confirmPwd.getText().toString();
        if (!(ispwd.equals(user_pwd))) {
            Toast.makeText(RegisterActivity.this,TGA.getString(R.string.pwdConfirmError),Toast.LENGTH_SHORT).show();
        }
        else {
            RegisterApi userApi = new RegisterApi();
            RegisterService registerService = userApi.getService();
            Call<LoginBean> call_Register = registerService.getState(user_name,user_account,user_pwd);
            call_Register.enqueue(new Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                    if (response.body().getResult().equals("success")) {
                        Toast.makeText(RegisterActivity.this,
                                "恭喜注册成功！", Toast.LENGTH_SHORT).show();
                        Intent main_intent = new Intent(TGA, MainActivity.class);
                        main_intent.putExtra("U_id",response.body().getId());
                        main_intent.putExtra("U_image",response.body().getImage());
                        main_intent.putExtra("U_name",response.body().getName());
                        main_intent.putExtra("U_pwd",response.body().getPassword());
                        main_intent.putExtra("U_sex",response.body().getSex());
                        main_intent.putExtra("U_account",response.body().getAccount());
                        main_intent.putExtra("U_phone",response.body().getPhone());
                        main_intent.putExtra("U_email",response.body().getEmail());
                        main_intent.putExtra("U_gptNum",response.body().getGptNum());
                        startActivity(main_intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, TGA.getString(R.string.accountIsRegis),
                                Toast.LENGTH_SHORT).show();
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
}
