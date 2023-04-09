package com.matrix.matrixgpt.Network.Service;

import com.matrix.matrixgpt.Network.CustomRequestModel.CreImgRequestBody;
import com.matrix.matrixgpt.Network.ReplyBean.CreateImageBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * @ClassName CreateImageService
 * @Author Create By Administrator
 * @Date 2023/4/8 0008 22:52
 */
public interface CreateImageService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:Bearer 你的API-Key"
    })
    @POST("generations")
    Call<CreateImageBean> getState(
            @Body CreImgRequestBody requestData
    );
}