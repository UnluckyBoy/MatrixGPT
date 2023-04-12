package com.matrix.matrixgpt.Network.API.GPT;

import com.matrix.matrixgpt.Network.API.WebApi;
import com.matrix.matrixgpt.Network.Service.GPT.ChatService;

import retrofit2.Retrofit;

public class ChatApi extends WebApi {
    String url="https://api.openai.com/v1/chat/";//聊天API
    Retrofit retrofit=getApi(url);

    @Override
    public <T> T getService() {
        return (T) retrofit.create(ChatService.class);
    }
}
