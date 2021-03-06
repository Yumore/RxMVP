package com.yumore.common.interceptor;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 实际链接次数 1+重连次数
 *
 * @author nathaniel
 */
public class RetryInterceptor implements Interceptor {
    private static final int MAX_RETRY = 1;

    /**
     * 重连次数
     * 重连计数器
     */
    private final int maxRetry;
    private int retryNumber;

    public RetryInterceptor() {
        this.maxRetry = MAX_RETRY;
    }

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        while (!response.isSuccessful() && retryNumber < maxRetry) {
            retryNumber++;
            response = chain.proceed(request);
        }
        // response .body(ResponseBody.create(MediaType.parse("application/json"), response.toString().getBytes())).addHeader("content-type", "application/json").build();
        return response;
    }
}
