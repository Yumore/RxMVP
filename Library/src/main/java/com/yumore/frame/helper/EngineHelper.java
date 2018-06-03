package com.yumore.frame.helper;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;

public interface EngineHelper {

    Retrofit getRetrofit();

    OkHttpClient.Builder setInterceptor(Interceptor interceptor);

    Retrofit.Builder setConverterFactory(Converter.Factory factory);

}
