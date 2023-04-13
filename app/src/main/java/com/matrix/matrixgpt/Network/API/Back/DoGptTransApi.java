package com.matrix.matrixgpt.Network.API.Back;

import com.matrix.matrixgpt.Network.API.WebApi;
import com.matrix.matrixgpt.Network.Service.Back.DoGptTransService;

import retrofit2.Retrofit;

/**
 * @ClassName DoGptTransApi
 * @Author Create By Administrator
 * @Date 2023/4/13 0013 17:09
 */
public class DoGptTransApi extends WebApi {
    String url="https://4b301a04.r10.cpolar.top/UserInfo/";//image
    Retrofit retrofit=getApi(url);

    @Override
    public <T> T getService() {
        return (T) retrofit.create(DoGptTransService.class);
    }
}
