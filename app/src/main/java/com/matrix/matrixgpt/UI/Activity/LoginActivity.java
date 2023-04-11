package com.matrix.matrixgpt.UI.Activity;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.matrix.matrixgpt.R;
import com.matrix.matrixgpt.UITool.MatrixDialog;

/**
 * @ClassName LoginActivity
 * @Author Create By Administrator
 * @Date 2023/4/11 0011 12:34
 */
public class LoginActivity extends Activity {
    private final Context TGA=LoginActivity.this;

    private static boolean mBackKeyPressed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        InitRun();
    }

    private void InitRun() {
        TextView mOther_login_title;
        mOther_login_title=findViewById(R.id.other_login_title);
        mOther_login_title.setText(Html.fromHtml(TGA.getString(R.string.other_login),FROM_HTML_MODE_COMPACT));
    }

    @Override
    public void onBackPressed() {
        String[] names = {TGA.getString(R.string.SystemTitle),
                TGA.getString(R.string.cancelLogin),
                TGA.getString(R.string.Confirm), TGA.getString(R.string.Cancel) };
        /**MatrixDialog中最后两个按钮的顺序与names的文本顺序相反**/
        MatrixDialog mDialog = new MatrixDialog(TGA, names, true);
        mDialog.setOnClickListener2LastTwoItems(new MatrixDialog.OnClickListener2LastTwoItem() {
            /**取消按钮**/
            @Override
            public void onClickListener2LastItem() {
                mDialog.dismiss();
            }
            /**确定按钮**/
            @Override
            public void onClickListener2SecondLastItem() {
                //Toast.makeText(mContext, "点击了确定", Toast.LENGTH_SHORT).show();
                Intent main_Intent=new Intent(TGA,MainActivity.class);
                startActivity(main_Intent);
                finish();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }
}
