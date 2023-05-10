package com.matrix.matrix_chat.Network.Service.Back;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.LoginBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @ClassName upGptNumService
 * @Author Create By matrix
 * @Date 2023/5/10 0010 13:08
 */
public interface upGptNumService {
    @POST("update_gpt_num")
    Call<LoginBean> getState(
            @Query("account") String account
    );
}
