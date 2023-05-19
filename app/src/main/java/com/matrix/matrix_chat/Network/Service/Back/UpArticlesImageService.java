package com.matrix.matrix_chat.Network.Service.Back;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.IsTrueBean;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * @ClassName UpArticlesImageService
 * @Author Create By matrix
 * @Date 2023/5/19 0019 10:20
 */
public interface UpArticlesImageService {
    @Multipart
    @POST("article_image")
    Call<IsTrueBean> up_Article_Image(
            @Query("account") String account,
            @Query("title") String title,
            @Part List<MultipartBody.Part> files);
}
