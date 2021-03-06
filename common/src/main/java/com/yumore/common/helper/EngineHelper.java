package com.yumore.common.helper;

import okhttp3.Interceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;

public interface EngineHelper {

    /**
     * initialize retrofit
     *
     * @return retrofit
     */
    Retrofit getRetrofit();

    /**
     * initialize interceptor
     *
     * @return interceptor
     */
    Interceptor getInterceptor();

    /**
     * initialize server url
     *
     * @return serverUrl
     */
    String getBaseUrl();

    Converter.Factory getFactory();

}
