package com.matrix.matrix_chat.UITool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.matrix.matrix_chat.R;
import com.matrix.matrix_chat.UI.Fragment.MainFragment;
import com.matrix.matrix_chat.UtilTool.AdverUtil.View.ShowFullScreeAdvClass;

import java.util.ArrayList;

/**
 * @ClassName MatrixDialogManager
 * @Author Create By Administrator
 * @Date 2023/4/11 0011 16:01
 */
public class MatrixDialogManager {
    private static PopupWindow popupWindow;//弹窗

    public MatrixDialogManager(){
    }

    public <T> void ShowMatrixDialog(String[] names, Activity currentTGA, Class<T> targetTGA){
        /**MatrixDialog中最后两个按钮的顺序与names的文本顺序相反**/
        MatrixDialog mDialog = new MatrixDialog(currentTGA, names, true);
        mDialog.setOnClickListener2LastTwoItems(new MatrixDialog.OnClickListener2LastTwoItem() {
            /**取消按钮**/
            @Override
            public void onClickListener2LastItem() {
                mDialog.dismiss();
            }
            /**确定按钮**/
            @Override
            public void onClickListener2SecondLastItem() {
                //Class<MainActivity> tset=MainActivity.class;
                Intent main_Intent=new Intent(currentTGA, targetTGA);
                main_Intent.putExtra(currentTGA.getString(R.string.info_account),"");
                currentTGA.startActivity(main_Intent);
                currentTGA.finish();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public static <T> void hintLoginDialog(final Context mContext, Intent intent, Activity currentTGA, Class<T> targetTGA) {
        String[] names = {mContext.getString(R.string.SystemTitle),
                mContext.getString(R.string.loginTitle),
                mContext.getString(R.string.Confirm),mContext.getString(R.string.Cancel) };

        /**MatrixDialog中最后两个按钮的顺序与names的文本顺序相反**/
        MatrixDialog mDialog = new MatrixDialog(mContext, names, true);
        mDialog.setOnClickListener2LastTwoItems(new MatrixDialog.OnClickListener2LastTwoItem() {
            /**取消按钮**/
            @Override
            public void onClickListener2LastItem() {
                intent.putExtra("U_account","");
                mDialog.dismiss();
            }
            /**确定按钮**/
            @Override
            public void onClickListener2SecondLastItem() {
                //Toast.makeText(mContext, "点击了确定", Toast.LENGTH_SHORT).show();
                Intent login_Intent=new Intent(currentTGA, targetTGA);
                currentTGA.startActivity(login_Intent);
                currentTGA.finish();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    /**显示视频窗口**/
    public static void showVideoView(String[] names, Activity activity,String account,Intent intent){
        MatrixDialog mDialog = new MatrixDialog(activity, names, true);
        mDialog.setOnClickListener2LastTwoItems(new MatrixDialog.OnClickListener2LastTwoItem() {
            /**取消按钮**/
            @Override
            public void onClickListener2LastItem() {
                mDialog.dismiss();
            }
            /**确定按钮**/
            @Override
            public void onClickListener2SecondLastItem() {
                ShowFullScreeAdvClass.loadAdv(activity,account,intent);
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public static void ShowSystemDialog(final Context mContext){
        String[] names={"系统提示","账户已在他处登录\n详情请前往\"我的\"->\"帮助\"查看","确定","取消"};
        MatrixDialog mDialog = new MatrixDialog(mContext, names, true);
        mDialog.setOnClickListener2LastTwoItems(new MatrixDialog.OnClickListener2LastTwoItem() {
            @Override
            public void onClickListener2LastItem() {
                mDialog.dismiss();
            }

            @Override
            public void onClickListener2SecondLastItem() {
                mDialog.dismiss();
            }
        });
    }

    public static void ShowAdvertise(final Activity activity,String account,Intent tempIntent){
        String[] names = { activity.getString(R.string.SystemTitle),
                activity.getString(R.string.ShowAdvertise),
                activity.getString(R.string.Confirm),
                activity.getString(R.string.Cancel) };
        MatrixDialogManager.showVideoView(names,activity,account,tempIntent);
    }

    /**弹窗选择相片**/
//    public static void selectImageWindow(Activity activity,View view){
//        View popUpView=LayoutInflater.from(view.getContext()).inflate(R.layout.view_selectimage_window, null);
//        popupWindow=new PopupWindow(view);
//        //popupWindow=new PopupWindow(popUpView,-1, -2);
//        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setContentView(popUpView);
//        popupWindow.setOutsideTouchable(false);
//        popupWindow.setFocusable(false);
//        //popupWindow.setAnimationStyle(5);//设置动画效果
//        popupWindow.setTouchable(true);
//        popupWindow.setBackgroundDrawable(new ColorDrawable(view.getResources().getColor(R.color.transparent,null)));
//        /**设置背景为暗**/
//        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//        lp.alpha = 0.5f;
//        activity.getWindow().setAttributes(lp);
//
//        popupWindow.showAtLocation(activity.getWindow().getDecorView(),Gravity.CENTER,0,0);
//
//        Button btn_photo,btn_camera,btn_cancel;
//        btn_photo=(Button)popUpView.findViewById(R.id.btn_photo);
//        btn_camera=(Button)popUpView.findViewById(R.id.btn_camera);
//        btn_cancel=(Button)popUpView.findViewById(R.id.btn_cancel);
//
//        btn_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PictureSelector.create(activity)
//                        .openSystemGallery(SelectMimeType.ofImage())
//                        .forSystemResult(new OnResultCallbackListener<LocalMedia>() {
//                            @Override
//                            public void onResult(ArrayList<LocalMedia> result) {
//                                //Toast.makeText(getContext(), "打开相册"+result.get(0).getRealPath(), Toast.LENGTH_SHORT).show();
//                                final String localPicturePath = result.get(0).getRealPath();
//                                getImageBaseData(localPicturePath);//调用文字识别api
//                            }
//
//                            @Override
//                            public void onCancel() {
//                                //closePopupWindow();
//                            }
//                        });
//                closePopupWindow(activity,popupWindow);
//            }
//        });
//        btn_camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PictureSelector.create(activity)
//                        .openCamera(SelectMimeType.ofImage())
//                        .forResultActivity(new OnResultCallbackListener<LocalMedia>() {
//                            @Override
//                            public void onResult(ArrayList<LocalMedia> result) {
//                                Toast.makeText(activity, "拍照:"+result.get(0).getRealPath(), Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onCancel() {
//                                //closePopupWindow();
//                            }
//                        });
//                closePopupWindow(activity,popupWindow);
//            }
//        });
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closePopupWindow(activity,popupWindow);
//            }
//        });
//    }
//    /**关闭弹窗**/
//    public static void closePopupWindow(Activity activity,PopupWindow popupWindow) {
//        if (popupWindow != null && popupWindow.isShowing()) {
//            popupWindow.dismiss();
//            popupWindow = null;
//
//            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//            lp.alpha = 1f;
//            activity.getWindow().setAttributes(lp);
//        }
//    }
}
