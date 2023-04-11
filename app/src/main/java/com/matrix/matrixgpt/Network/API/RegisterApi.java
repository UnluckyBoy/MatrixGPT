package com.matrix.matrixgpt.Network.API;

import com.matrix.matrixgpt.Network.Service.RegisterService;

import retrofit2.Retrofit;

/**
 * @ClassName RegisterApi
 * @Author Create By Administrator
 * @Date 2023/4/11 0011 16:34
 */
public class RegisterApi extends WebApi{
    String url="https://5841dcdd.r5.cpolar.top/UserInfo/";
    Retrofit retrofit=getApi(url);

    @Override
    public <T> T getService() {
        return (T) retrofit.create(RegisterService.class);
    }
}
