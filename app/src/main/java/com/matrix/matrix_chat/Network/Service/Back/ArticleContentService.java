package com.matrix.matrix_chat.Network.Service.Back;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.ArticleContentBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @ClassName ArticleContentService
 * @Author Create By matrix
 * @Date 2023/5/13 0013 9:44
 */
public interface ArticleContentService {
    @POST("get_file_content")
    Call<ArticleContentBean> getState(
            @Query("title") String title,
            @Query("author") String author
    );
}
