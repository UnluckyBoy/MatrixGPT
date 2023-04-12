package com.matrix.matrixgpt.Network.API.Back;

import com.matrix.matrixgpt.Network.API.WebApi;
import com.matrix.matrixgpt.Network.Service.Back.LoginService;

import retrofit2.Retrofit;

/**
 * @ClassName LoginApi
 * @Author Create By Administrator
 * @Date 2023/4/11 0011 14:48
 */
public class LoginApi extends WebApi {
    String url="https://628945ad.r5.cpolar.top/UserInfo/";//login
    Retrofit retrofit=getApi(url);

    @Override
    public <T> T getService() {
        return (T) retrofit.create(LoginService.class);
    }
}
