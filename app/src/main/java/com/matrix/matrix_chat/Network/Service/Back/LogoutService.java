package com.matrix.matrix_chat.Network.Service.Back;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.LogoutBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @ClassName LogoutService
 * @Author Create By matrix
 * @Date 2023/5/8 0008 18:40
 */
public interface LogoutService {
    @POST("logout")
    Call<LogoutBean> getState(
            @Query("account") String account,
            @Query("password") String password
    );
}
