package com.matrix.matrix_chat.Network.API.Back;

import com.matrix.matrix_chat.Network.API.WebApi;
import com.matrix.matrix_chat.Network.Service.Back.BackChatService;

import retrofit2.Retrofit;

/**
 * @ClassName BackChatApi
 * @Author Create By Administrator
 * @Date 2023/4/12 0012 14:06
 */
public class BackChatApi extends WebApi {
    Retrofit retrofit;
    public void SetUrl(String url){
        retrofit=getApi(url);
    }
//    String url="https://3671d84e.r8.cpolar.top/MatrixGPT/";//chat
//    Retrofit retrofit=getApi(url);

    @Override
    public <T> T getService() {
        return (T) retrofit.create(BackChatService.class);
    }
}
