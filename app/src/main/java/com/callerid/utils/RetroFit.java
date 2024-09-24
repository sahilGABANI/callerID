package com.callerid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.callerid.BuildConfig;
import com.callerid.api.Api;
import com.callerid.api.CarHeroInterceptorHeaders;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFit {
    private static class RF {
        static volatile Retrofit retrofit = null;
        static volatile OkHttpClient okHttpClient = null;
        static volatile Api api = null;


        static Retrofit getRetrofit(Context context) {
            if (okHttpClient == null) getOkhttp(context);


            retrofit = new Retrofit.Builder()
//                        .baseUrl("http://api.3sigmacrm.com/")
                    .baseUrl("https://mapi2.3sigmacrm.com/api/v1/")
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return retrofit;
        }

        static Retrofit getRetrofit1(Context context) {
            if (okHttpClient == null) getOkhttp(context);

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://13.232.254.20:1337/")
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return retrofit;
        }

        static String str = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InN1YiI6IjY0OTA0ZmExYTQxMTdkZTEzMTA4NzZmOCIsInJvbGUiOiJzdXBlcl9hZG1pbiJ9LCJpYXQiOjE3MjU3OTIyNzN9.BnHIpiMuEYeb_VZ1joIkZklABraErBpftokvyldH7w0";

        static void getOkhttp(Context context) {

            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            File cacheDir = new File(context.getCacheDir(), "HttpCache");
            Cache cache = new Cache(cacheDir, (long) cacheSize);

            CarHeroInterceptorHeaders carHeroInterceptorHeaders = new CarHeroInterceptorHeaders();


            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(240, TimeUnit.SECONDS)
                    .addInterceptor(carHeroInterceptorHeaders)
                    .cache(cache);


            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(loggingInterceptor);
            }


//            int timeOut = 60;
//            OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
//                    .connectTimeout(timeOut, TimeUnit.SECONDS)
//                    .readTimeout(timeOut, TimeUnit.SECONDS)
//                    .cache(null)
//                    .writeTimeout(timeOut, TimeUnit.SECONDS)
//                    .retryOnConnectionFailure(true);
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
////            httpClient.addInterceptor(interceptor);
//            SharedPreferences prf = context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
//            httpClient.addInterceptor(chain -> {
//                Request original = chain.request();
//                Request.Builder requestBuilder = original.newBuilder()
//                        .addHeader("Accept", "application/json")
//                        .addHeader("Content-Type", "application/json")
//                        .addHeader("Authorization", str);
////                        .addHeader("Cache-Control", "no-cache");
//                        //  .addHeader("Cache-Control", "max-age=0")
////                        .addHeader("Cookie", "sails.sid=s%3ACXO_v2DdBwPabM9lLjEkew8bvvX2ddX7.aMstjjLRhUZnaeVbQKgaGW7VTHdBzY%2FSw%2BpDqnamq98; Path=/; Expires=Fri, 14 May 2032 09:18:22 GMT; HttpOnly");
//                Request request = requestBuilder.build();
//                return chain.proceed(request);
//            });
            okHttpClient = httpClient.build();
        }

        static Api getApi(Context context) {
            api = getRetrofit(context).create(Api.class);
            return api;
        }

        static Api getApi1(Context context) {
            api = getRetrofit1(context).create(Api.class);
            return api;
        }
    }

    public static Api get1(Context context) {
        return RF.getApi(context);
    }

    public static Api get(Context context) {
        return RF.getApi(context);
    }


}