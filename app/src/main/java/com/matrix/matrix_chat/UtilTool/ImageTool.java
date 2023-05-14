package com.matrix.matrix_chat.UtilTool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * @ClassName GetBitMap
 * @Author Create By Administrator
 * @Date 2023/4/8 0008 23:53
 */

/**
 * 打开线程将url的图片显示到ImageView
 */
public class ImageTool{
    private static byte[] data;
    private static Bitmap bitmap;
    private static Handler handler = null;

    public static void SetImageView(ImageView imageView,String url){
        // 创建属于主线程的handler
        handler = new Handler();
        /*
         * 在新开的线程中设置图片显示
         */
        Runnable runnable = new Runnable() {
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        };

        new Thread() {
            public void run() {
                try {
                    // 通过url获得图片数据
                    data = GetUserHead(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //子线程runnable将会执行
                handler.post(runnable);
            }
        }.start();
    }

    /**
     * @param urlpath String类型
     * @return byte[]类型
     * @throws IOException
     */
    public static byte[] GetUserHead(String urlpath) throws IOException {
        URL url = new URL(urlpath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET"); // 设置请求方法为GET
        conn.setReadTimeout(5 * 1000); // 设置请求过时时间为5秒
        InputStream inputStream = conn.getInputStream(); // 通过输入流获得图片数据
        byte[] data = StreamTool.readInputStream(inputStream); // 获得图片的二进制数据
        return data;
    }

    /**
     * 保存bitmap到本地
     *
     * @param bitmap Bitmap
     */
    public static void saveBitmap(Bitmap bitmap, String path, Context mContext,String fileName) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savePath = path;
        } else {
            //Log.e("tag", "saveBitmap failure : sdcard not mounted");
            return;
        }
        try {
            filePic = new File(savePath,fileName);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic+".png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            //Toast.makeText(mContext, "保存成功!!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("tag", "saveBitmap: " + e.getMessage());
            Toast.makeText(mContext, "保存失败!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("tag", "saveBitmap success: " + filePic.getAbsolutePath());
        Toast.makeText(mContext, "保存成功!!!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 将图片转base64编码
     * @param path
     * @return
     */
    public static String getImageBase(String path,boolean add_head){
        File file=new File(path);
        byte[] data = null;
        try {
            InputStream in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filename=file.getName();
        String suffixName=filename.substring(filename.lastIndexOf(".")+1);//获取没有.的后缀名
        String imageBaseType="data:image/"+suffixName+";base64,";
        Base64.Encoder encoder = Base64.getEncoder();
        if(add_head){
            return imageBaseType+encoder.encodeToString(data);
        }else{
            return encoder.encodeToString(data);//若加入图片头,则使用:imageBaseType+encoder.encodeToString(data)
        }
    }

    /***
     * 获取文字识别规定body类型
     * @param path
     * @return
     */
    public static String getRecognitionImageBase(String path){
        String base64code=getImageBase(path,false);
        return getBase2Urlencode(base64code);
    }

    public static String getBase2Urlencode(String base64code){
        String result=null;
        try {
            result=URLEncoder.encode(base64code, "utf-8");//将base64进行urlencode
            //String result4 = URLDecoder.decode(result, "utf-8");//URLDecoder解码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /***
     * 压缩
     * @param base64Img
     * @return
     */
    public static String resizeImageTo40K(String base64Img) {

        return null;
    }
}
