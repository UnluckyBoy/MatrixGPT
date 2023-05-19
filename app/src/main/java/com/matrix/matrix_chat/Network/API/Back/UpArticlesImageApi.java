package com.matrix.matrix_chat.Network.API.Back;

import com.matrix.matrix_chat.Network.API.WebApi;
import com.matrix.matrix_chat.Network.Service.Back.UpArticlesImageService;

import retrofit2.Retrofit;

/**
 * @ClassName UpArticlesImageApi
 * @Author Create By matrix
 * @Date 2023/5/19 0019 10:19
 */
public class UpArticlesImageApi extends WebApi {
    Retrofit retrofit;
    public void SetUrl(String url){
        retrofit=getApi(url);
    }

    @Override
    public <T> T getService() {
        return (T) retrofit.create(UpArticlesImageService.class);
    }
}
