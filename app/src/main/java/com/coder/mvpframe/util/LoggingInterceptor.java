package com.coder.mvpframe.util;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.i("http", String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Log.i("http", request.body().contentType().toString());

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.i("http", String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            String string = body.string();

            Log.i("http", string);
        }


        return response;
    }
}