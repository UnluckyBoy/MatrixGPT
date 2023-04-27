package com.matrix.matrix_chat.Network.Service.Back;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.LoginBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @ClassName DoGptTransService
 * @Author Create By Administrator
 * @Date 2023/4/13 0013 17:10
 */
public interface DoGptTransService {
    @POST("doGptTrans")
    Call<LoginBean> getState(
            @Query("account") String account
    );
}
