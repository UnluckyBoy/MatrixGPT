package com.matrix.matrix_chat.Network.Service.Back;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.LoginBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @ClassName LoginService
 * @Author Create By Administrator
 * @Date 2023/4/11 0011 14:55
 */
public interface LoginService {
    @POST("login")
    Call<LoginBean> getState(
            @Query("account") String account,
            @Query("password") String password
    );
}
