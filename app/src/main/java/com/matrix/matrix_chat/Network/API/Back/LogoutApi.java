package com.matrix.matrix_chat.Network.API.Back;

import com.matrix.matrix_chat.Network.API.WebApi;
import com.matrix.matrix_chat.Network.Service.Back.LogoutService;

import retrofit2.Retrofit;

/**
 * @ClassName LogoutApi
 * @Author Create By matrix
 * @Date 2023/5/8 0008 18:39
 */
public class LogoutApi extends WebApi {
    Retrofit retrofit;
    public void SetUrl(String url){
        retrofit=getApi(url);
    }

    @Override
    public <T> T getService() {
        return (T) retrofit.create(LogoutService.class);
    }
}
