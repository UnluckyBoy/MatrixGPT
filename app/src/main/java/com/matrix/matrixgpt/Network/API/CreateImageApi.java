package com.matrix.matrixgpt.Network.API;

import com.matrix.matrixgpt.Network.Service.CreateImageService;

import retrofit2.Retrofit;

/**
 * @ClassName CreateImageApi
 * @Author Create By Administrator
 * @Date 2023/4/8 0008 22:52
 */
public class CreateImageApi extends WebApi{
    String url="https://api.openai.com/v1/images/";//绘画API
    Retrofit retrofit=getApi(url);

    @Override
    public <T> T getService() {
        return (T) retrofit.create(CreateImageService.class);
    }
}
