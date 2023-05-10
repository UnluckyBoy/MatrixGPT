package com.matrix.matrix_chat.UITool;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.matrix.matrix_chat.R;

/**
 * @ClassName PwdEditView
 * @Author Create By Administrator
 * @Date 2023/4/11 0011 17:01
 */
public class PwdEditView extends LinearLayout {

    private final EditText mEtData;

    public PwdEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_pwd_edit_text, this, true);
        //ImageView mImLeft = findViewById(R.id.im_left);
        mEtData = findViewById(R.id.et_data);
        CheckBox mImEye = findViewById(R.id.im_eye);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PwdEditTextView);
        if (attributes != null) {
            int drawableLeft = attributes.getResourceId(R.styleable.PwdEditTextView_drawableLeft, Color.WHITE);
            //mImLeft.setImageDrawable(context.getDrawable(drawableLeft));

            String hint = attributes.getString(R.styleable.PwdEditTextView_hint);
            mEtData.setHint(hint);

            int maxLenth = attributes.getInt(R.styleable.PwdEditTextView_maxLength, 32);
            mEtData.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLenth)});
            int inputType = attributes.getInt(R.styleable.PwdEditTextView_inputType, EditorInfo.TYPE_NULL);
            mEtData.setInputType(inputType);

            boolean needEys = attributes.getBoolean(R.styleable.PwdEditTextView_needEye, false);
            if (needEys) {
                mImEye.setVisibility(VISIBLE);
                mImEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mEtData.setTransformationMethod(isChecked ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
                        mEtData.setSelection(mEtData.getText().length());
                    }
                });
            } else {
                mImEye.setVisibility(GONE);
                mEtData.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mEtData.setSelection(mEtData.getText().length());
            }
        }
    }

    public Editable getText() {
        return mEtData.getText();
    }

    public void setText(String text){
        mEtData.setText(text);
    }
}
