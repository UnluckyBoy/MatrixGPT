package com.matrix.matrix_chat.Network.API.Back;

import com.matrix.matrix_chat.Network.API.WebApi;
import com.matrix.matrix_chat.Network.Service.Back.LoginService;

import retrofit2.Retrofit;

/**
 * @ClassName LoginApi
 * @Author Create By Administrator
 * @Date 2023/4/11 0011 14:48
 */
public class LoginApi extends WebApi {
//    String url="https://4b301a04.r10.cpolar.top/UserInfo/";//login
//    Retrofit retrofit=getApi(url);
    Retrofit retrofit;
    public void SetUrl(String url){
        retrofit=getApi(url);
    }

    @Override
    public <T> T getService() {
        return (T) retrofit.create(LoginService.class);
    }
}
