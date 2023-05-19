package com.matrix.matrix_chat.Network.Service.Back;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.IsTrueBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @ClassName upArticleContentService
 * @Author Create By matrix
 * @Date 2023/5/17 0017 16:25
 */
public interface upArticleContentService {
    @POST("up_article_content")
    Call<IsTrueBean> getState(@Body RequestBody fileContent);
}
