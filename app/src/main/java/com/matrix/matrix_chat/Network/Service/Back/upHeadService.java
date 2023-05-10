package com.matrix.matrix_chat.Network.Service.Back;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.IsTrueBean;
import com.matrix.matrix_chat.Network.ResponseBean.BackService.LoginBean;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * @ClassName upHeadService
 * @Author Create By matrix
 * @Date 2023/5/10 0010 20:59
 */
public interface upHeadService {
    @Multipart
    @POST("upHead")
    Call<LoginBean> getState(
            @Query("account") String account,
            @Part MultipartBody.Part file);
}
