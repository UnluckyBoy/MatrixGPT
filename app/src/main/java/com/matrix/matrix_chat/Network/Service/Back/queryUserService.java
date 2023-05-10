package com.matrix.matrix_chat.Network.Service.Back;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.UserBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @ClassName queryUserService
 * @Author Create By matrix
 * @Date 2023/5/10 0010 14:11
 */
public interface queryUserService {
    @POST("queryinfo")
    Call<UserBean> getState(
            @Query("account") String account
    );
}
