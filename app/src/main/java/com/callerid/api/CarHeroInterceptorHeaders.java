package com.callerid.api;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CarHeroInterceptorHeaders implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder();
        requestBuilder.header("Content-Type", "application/json");
        requestBuilder.header("Accept", "application/json");
        requestBuilder.header("token-check-disable", "true");
        requestBuilder.header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InN1YiI6IjY0OTA0ZmExYTQxMTdkZTEzMTA4NzZmOCIsInJvbGUiOiJzdXBlcl9hZG1pbiJ9LCJpYXQiOjE3MjU3OTIyNzN9.BnHIpiMuEYeb_VZ1joIkZklABraErBpftokvyldH7w0");


        Response response;
        try {
            response = chain.proceed(requestBuilder.build());
        } catch (Throwable t) {
            Log.e("CarHeroInterceptorHeaders", "error in InterceptorHeaders:\n" + t.getMessage() );
            throw new IOException(t.getMessage());
        }
        return response;
    }
}