package com.matrix.matrixgpt.Network.API.Back;

import com.matrix.matrixgpt.Network.API.WebApi;
import com.matrix.matrixgpt.Network.Service.Back.BackChatService;

import retrofit2.Retrofit;

/**
 * @ClassName BackChatApi
 * @Author Create By Administrator
 * @Date 2023/4/12 0012 14:06
 */
public class BackChatApi extends WebApi {
    String url="https://4b301a04.r10.cpolar.top/MatrixGPT/";//chat
    Retrofit retrofit=getApi(url);

    @Override
    public <T> T getService() {
        return (T) retrofit.create(BackChatService.class);
    }
}
