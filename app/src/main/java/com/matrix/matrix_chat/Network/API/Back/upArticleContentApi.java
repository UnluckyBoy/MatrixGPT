package com.matrix.matrix_chat.Network.API.Back;

import com.matrix.matrix_chat.Network.API.WebApi;
import com.matrix.matrix_chat.Network.Service.Back.upArticleContentService;

import retrofit2.Retrofit;

/**
 * @ClassName up_ArticleContent
 * @Author Create By matrix
 * @Date 2023/5/17 0017 16:24
 */
public class upArticleContentApi extends WebApi {
    Retrofit retrofit;
    public void SetUrl(String url){
        retrofit=getApi(url);
    }

    @Override
    public <T> T getService() {
        return (T) retrofit.create(upArticleContentService.class);
    }
}
