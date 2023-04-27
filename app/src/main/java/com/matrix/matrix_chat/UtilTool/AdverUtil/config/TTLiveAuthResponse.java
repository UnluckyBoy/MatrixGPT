package com.matrix.matrix_chat.UtilTool.AdverUtil.config;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName TTLiveAuthResponse
 * @Author Create By Administrator
 * @Date 2023/4/17 0017 18:58
 */
public class TTLiveAuthResponse {
    public static class AuthAccessToken {
        @SerializedName("refresh_token")
        String refreshToken;
        @SerializedName("scope")
        String scope;
        @SerializedName("access_token")
        String accessToken;
        @SerializedName("description")
        String description;
        @SerializedName("error_code")
        long errorCode;
        @SerializedName("expires_in")
        long expiresIn;// 单位s
        @SerializedName("open_id")
        String openId;
        @SerializedName("refresh_expires_in")
        long refreshExpiresIn;
    }

    public static class AuthAccessTokenResponse {
        @SerializedName("data")
        JsonObject data;
        @SerializedName("message")
        String message;
    }

    public static class RefreshToken {
        @SerializedName("description")
        String description;
        @SerializedName("error_code")
        long errorCode;
        @SerializedName("expires_in")
        long expiresIn;
        @SerializedName("refresh_token")
        String refreshToken;
        @SerializedName("open_id")
        String openId;
        @SerializedName("refresh_expires_in")
        long refreshExpiresIn;
        @SerializedName("scope")
        String scope;
        @SerializedName("access_token")
        String accessToken;
    }

    public static class RefreshTokenResponse {
        @SerializedName("data")
        JsonObject data;
        @SerializedName("message")
        String message;
    }
}
