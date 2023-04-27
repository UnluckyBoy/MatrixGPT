package com.matrix.matrix_chat.Network.API.Back;

import com.matrix.matrix_chat.Network.API.WebApi;
import com.matrix.matrix_chat.Network.Service.Back.BackCreateImageService;

import retrofit2.Retrofit;

/**
 * @ClassName BackCreateImageApi
 * @Author Create By Administrator
 * @Date 2023/4/13 0013 15:38
 */
public class BackCreateImageApi extends WebApi {
//    String url="https://4b301a04.r10.cpolar.top/MatrixGPT/";//image
//    Retrofit retrofit=getApi(url);
    Retrofit retrofit;
    public void SetUrl(String url){
        retrofit=getApi(url);
    }

    @Override
    public <T> T getService() {
        return (T) retrofit.create(BackCreateImageService.class);
    }
}
