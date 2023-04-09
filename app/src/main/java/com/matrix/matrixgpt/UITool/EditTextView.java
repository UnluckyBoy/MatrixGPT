package com.matrix.matrixgpt.UITool;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

/**
 * @ClassName EditTextView
 * @Author Create By Administrator
 * @Date 2023/4/9 0009 17:11
 */
@SuppressLint("AppCompatCustomView")
public class EditTextView extends EditText {
    public EditTextView(Context context) {
        super(context);
    }

    public EditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EditTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean getDefaultEditable() {
        return false;//禁止EditText被编辑
    }
}
