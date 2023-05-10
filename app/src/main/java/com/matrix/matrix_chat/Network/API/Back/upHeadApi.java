package com.matrix.matrix_chat.Network.API.Back;

import com.matrix.matrix_chat.Network.API.WebApi;
import com.matrix.matrix_chat.Network.Service.Back.upHeadService;

import retrofit2.Retrofit;

/**
 * @ClassName upHeadApi
 * @Author Create By matrix
 * @Date 2023/5/10 0010 20:58
 */
public class upHeadApi extends WebApi {
    Retrofit retrofit;
    public void SetUrl(String url){
        retrofit=getApi(url);
    }

    @Override
    public <T> T getService() {
        return (T) retrofit.create(upHeadService.class);
    }
}
