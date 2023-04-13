package com.matrix.matrixgpt.Network.Service.Back;

import com.matrix.matrixgpt.Network.ResponseBean.BackService.BackChatBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @ClassName BackCreateImageService
 * @Author Create By Administrator
 * @Date 2023/4/13 0013 15:39
 */
public interface BackCreateImageService {
    @POST("createImage")
    Call<BackChatBean> getState(
            @Query("content") String content
    );
}
