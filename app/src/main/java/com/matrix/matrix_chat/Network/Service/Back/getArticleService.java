package com.matrix.matrix_chat.Network.Service.Back;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.ArticlesBean;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * @ClassName getArticleService
 * @Author Create By matrix
 * @Date 2023/5/11 0011 19:49
 */
public interface getArticleService {
    @POST("get_all")
    Call<ArticlesBean> getState();
}
