package com.matrix.matrixgpt.Network.API.Back;

import com.matrix.matrixgpt.Network.API.WebApi;
import com.matrix.matrixgpt.Network.Service.Back.RegisterService;

import retrofit2.Retrofit;

/**
 * @ClassName RegisterApi
 * @Author Create By Administrator
 * @Date 2023/4/11 0011 16:34
 */
public class RegisterApi extends WebApi {
//    String url="https://4b301a04.r10.cpolar.top/UserInfo/";
//    Retrofit retrofit=getApi(url);
    Retrofit retrofit;
    public void SetUrl(String url){
        retrofit=getApi(url);
    }

    @Override
    public <T> T getService() {
        return (T) retrofit.create(RegisterService.class);
    }
}
