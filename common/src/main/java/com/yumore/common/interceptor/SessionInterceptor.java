package com.yumore.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.yumore.provider.utility.EmptyUtils;
import com.yumore.provider.utility.LoggerUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;

/**
 * @author Nathaniel
 * @date 2019/6/9 - 9:27
 */
public class SessionInterceptor implements Interceptor {
    private static final String TAG = SessionInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return new Response.Builder()
                .body(newResponseBody(response))
                .headers(response.headers())
                .message(response.message())
                .code(response.code())
                .request(response.request())
                .protocol(response.protocol())
                .build();
    }

    private ResponseBody newResponseBody(final Response response) {
        final String defaultResponse = "{\"error_code\":500,\"success\":false,\"data\":null, \"reason\":\"请求参数错误或者服务器异常\"}";
        return new ResponseBody() {
            @Override
            public MediaType contentType() {
                if (EmptyUtils.isEmpty(response) || EmptyUtils.isEmpty(response.body())) {
                    return MediaType.parse("application/json");
                }
                return response.body().contentType();
            }

            @Override
            public long contentLength() {
                if (EmptyUtils.isEmpty(response) || EmptyUtils.isEmpty(response.body())) {
                    return 0;
                }
                return response.body().contentLength();
            }

            @Override
            public BufferedSource source() {
                try {
                    if (EmptyUtils.isEmpty(response) || EmptyUtils.isEmpty(response.body())) {
                        ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(defaultResponse.getBytes());
                        return Okio.buffer(Okio.source(tInputStringStream));
                    }
                    String result = response.body().string();
                    LoggerUtils.logger(TAG, "result from server: " + result);
                    if (result.contains("code") && JSON.parseObject(result).containsKey("code") && JSON.parseObject(result).getInteger("code") == 500) {
                        /*
                         *这里改变返回的数据，如果服务器返回的是一个HTML网页，
                         *那么移动端也能拿到一个Json数据，用于保证数据可解析不至于崩溃
                         */
                        ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(defaultResponse.getBytes());
                        return Okio.buffer(Okio.source(tInputStringStream));
                    } else {
                        ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(result.getBytes());
                        return Okio.buffer(Okio.source(tInputStringStream));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(defaultResponse.getBytes());
                    return Okio.buffer(Okio.source(tInputStringStream));
                }
            }
        };
    }
}
