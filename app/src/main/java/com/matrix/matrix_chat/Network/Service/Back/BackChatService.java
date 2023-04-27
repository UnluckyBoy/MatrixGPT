package com.matrix.matrix_chat.Network.Service.Back;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.BackChatBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @ClassName BackChatService
 * @Author Create By Administrator
 * @Date 2023/4/12 0012 14:08
 */
public interface BackChatService {
    @POST("chat")
    Call<BackChatBean> getState(
            @Query("content") String content
    );
}
