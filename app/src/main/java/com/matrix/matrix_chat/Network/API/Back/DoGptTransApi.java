package com.matrix.matrix_chat.Network.API.Back;

import com.matrix.matrix_chat.Network.API.WebApi;
import com.matrix.matrix_chat.Network.Service.Back.DoGptTransService;

import retrofit2.Retrofit;

/**
 * @ClassName DoGptTransApi
 * @Author Create By Administrator
 * @Date 2023/4/13 0013 17:09
 */
public class DoGptTransApi extends WebApi {
//    String url="https://4b301a04.r10.cpolar.top/UserInfo/";
//    Retrofit retrofit=getApi(url);
    Retrofit retrofit;
    public void SetUrl(String url){
        retrofit=getApi(url);
    }

    @Override
    public <T> T getService() {
        return (T) retrofit.create(DoGptTransService.class);
    }
}
