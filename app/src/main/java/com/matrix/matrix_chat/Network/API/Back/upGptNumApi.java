package com.matrix.matrix_chat.Network.API.Back;

import com.matrix.matrix_chat.Network.API.WebApi;
import com.matrix.matrix_chat.Network.Service.Back.upGptNumService;

import retrofit2.Retrofit;

/**
 * @ClassName upGptNumApi
 * @Author Create By matrix
 * @Date 2023/5/10 0010 13:08
 */
public class upGptNumApi extends WebApi {
    Retrofit retrofit;
    public void SetUrl(String url){
        retrofit=getApi(url);
    }

    @Override
    public <T> T getService() {
        return (T) retrofit.create(upGptNumService.class);
    }
}
