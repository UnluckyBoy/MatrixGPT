package com.matrix.matrixgpt.Network.Service;

import com.matrix.matrixgpt.Network.CustomRequestModel.ChatRequestBody;
import com.matrix.matrixgpt.Network.ReplyBean.ChatBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChatService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:Bearer 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"
    })
    @POST("completions")
    Call<ChatBean> getState(
            @Body ChatRequestBody requestData
    );
}
