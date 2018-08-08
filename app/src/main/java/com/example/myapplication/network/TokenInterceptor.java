package com.example.myapplication.network;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by jarod on 10/23/2017.
 */

public class TokenInterceptor implements Interceptor {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();
        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("key", "AIzaSyCLOYZXtk0p9HTIVtGGaWd9FG_MVh8mMGY")
                .build();
        Request.Builder requestBuilder = original.newBuilder().url(url);
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
