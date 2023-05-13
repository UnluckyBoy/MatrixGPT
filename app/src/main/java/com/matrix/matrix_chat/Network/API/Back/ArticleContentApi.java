package com.matrix.matrix_chat.Network.API.Back;

import com.matrix.matrix_chat.Network.API.WebApi;
import com.matrix.matrix_chat.Network.Service.Back.ArticleContentService;

import retrofit2.Retrofit;

/**
 * @ClassName ArticleContentApi
 * @Author Create By matrix
 * @Date 2023/5/13 0013 9:44
 */
public class ArticleContentApi extends WebApi {
    Retrofit retrofit;
    public void SetUrl(String url){
        retrofit=getApi(url);
    }

    @Override
    public <T> T getService() {
        return (T) retrofit.create(ArticleContentService.class);
    }
}
