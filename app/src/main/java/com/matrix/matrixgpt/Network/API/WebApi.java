package com.matrix.matrixgpt.Network.API;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class WebApi {
    Retrofit getApi(String url) {
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        // 设置超时，超过 300s 视为超时
        OkHttpClient client = httpBuilder.readTimeout(300, TimeUnit.SECONDS)
                .connectTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public abstract <T> T getService();
}
