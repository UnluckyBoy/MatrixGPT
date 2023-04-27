package com.matrix.matrix_chat.Network.Service.Back;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.LoginBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @ClassName RegisterService
 * @Author Create By Administrator
 * @Date 2023/4/11 0011 16:37
 */
public interface RegisterService {
    @POST("register")
    Call<LoginBean> getState(
            @Query("name") String name,
            @Query("account") String account,
            @Query("password") String password
    );
}
