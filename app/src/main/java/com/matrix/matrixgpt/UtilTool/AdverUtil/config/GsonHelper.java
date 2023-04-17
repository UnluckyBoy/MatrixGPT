package com.matrix.matrixgpt.UtilTool.AdverUtil.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

/**
 * @ClassName GsonHelper
 * @Author Create By Administrator
 * @Date 2023/4/17 0017 18:12
 */
public class GsonHelper {
    public static Gson get() {
        return builder().create();
    }

    public static Gson getDefault() {
        return new Gson();
    }

    public static JsonParser parser() {
        return new JsonParser();
    }

    public static GsonBuilder builder() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    }
}
