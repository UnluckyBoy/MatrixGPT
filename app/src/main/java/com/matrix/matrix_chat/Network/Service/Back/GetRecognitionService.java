package com.matrix.matrix_chat.Network.Service.Back;

import com.matrix.matrix_chat.Network.ResponseBean.BackService.WordsResult;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @ClassName GetRecognitionService
 * @Author Create By matrix
 * @Date 2023/5/13 0013 21:24
 */
public interface GetRecognitionService {
    @POST("handwriting")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
    Call<WordsResult> getState(
            @Query("access_token") String access_token,
            @Body() RequestBody image
    );
}
