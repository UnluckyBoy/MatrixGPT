package com.matrix.matrixgpt.UtilTool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
                // 子线程runnable将会执行
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
}
