package com.matrix.matrix_chat.Network.API.Back;

import com.matrix.matrix_chat.Network.API.WebApi;
import com.matrix.matrix_chat.Network.Service.Back.GetRecognitionService;

import retrofit2.Retrofit;

/**
 * @ClassName GetRecogApi
 * @Author Create By matrix
 * @Date 2023/5/13 0013 21:18
 */
public class GetRecognitionApi extends WebApi {
    Retrofit retrofit;
    public void SetUrl(String url){
        retrofit=getApi(url);
    }

    @Override
    public <T> T getService() {
        return (T) retrofit.create(GetRecognitionService.class);
    }
}
