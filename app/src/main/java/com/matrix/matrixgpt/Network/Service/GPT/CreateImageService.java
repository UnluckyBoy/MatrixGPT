package com.matrix.matrixgpt.Network.Service.GPT;

import com.matrix.matrixgpt.Network.GptRequestBody.CreImgRequestBody;
import com.matrix.matrixgpt.Network.ResponseBean.GPT.CreateImageBean;

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
            "Authorization:Bearer 你的API-KEY"
    })
    @POST("generations")
    Call<CreateImageBean> getState(
            @Body CreImgRequestBody requestData
    );
}