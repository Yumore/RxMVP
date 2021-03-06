package com.yumore.common.interceptor;

import com.yumore.provider.utility.LoggerUtils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Nathaniel
 * @date 2019/4/13 - 20:13
 */
public class LoggerInterceptor implements Interceptor {
    private static final String TAG = LoggerInterceptor.class.getSimpleName();
    private static final boolean ENABLE = true;
    private final boolean enable;

    public LoggerInterceptor() {
        this.enable = ENABLE;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long requestTime = System.currentTimeMillis();
        Response response = chain.proceed(request);
        if (request.body() instanceof MultipartBody || request.body() instanceof FormBody) {

        }
        if (enable) {
            //这里不能直接使用response.body().string()的方式输出日志
            //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
            //个新的response给应用层处理
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            long responseTime = System.currentTimeMillis();
            LoggerUtils.logger(TAG, response.request().url() + " , use-timeMillis: " + (responseTime - requestTime) + " , data: " + responseBody.string());
        }
        return response;
    }
}
