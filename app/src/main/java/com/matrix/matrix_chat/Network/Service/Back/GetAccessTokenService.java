package com.matrix.matrix_chat.Network.Service.Back;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.AccessTokenBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @ClassName GetAccessTokenService
 * @Author Create By matrix
 * @Date 2023/5/13 0013 23:17
 */
public interface GetAccessTokenService {
    @POST("token")
    Call<AccessTokenBean> getState(
            @Query("client_id") String client_id,
            @Query("client_secret") String client_secret,
            @Query("grant_type") String grant_type
    );
}
