package com.matrix.matrix_chat.Network.API.Back;

import com.matrix.matrix_chat.Network.API.WebApi;
import com.matrix.matrix_chat.Network.Service.Back.getArticleService;

import retrofit2.Retrofit;

/**
 * @ClassName getAllArticles
 * @Author Create By matrix
 * @Date 2023/5/11 0011 19:48
 */
public class getArticlesApi extends WebApi {
    Retrofit retrofit;
    public void SetUrl(String url){
        retrofit=getApi(url);
    }

    @Override
    public <T> T getService() {
        return (T) retrofit.create(getArticleService.class);
    }
}
