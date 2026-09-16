package com.deepcoder.hbalum.datalayers.retrofit;


import static com.deepcoder.hbalum.BuildConfig.BASE_URL;

import android.content.Context;
import android.util.Log;

import com.deepcoder.hbalum.BuildConfig;
import com.deepcoder.hbalum.datalayers.storage.AppPref;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.logger.CustomLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {

    public static final String PREF_ACCESS_TOKEN_TYPE = "Bearer";
    public static final int API_CALL_TIMEOUT = 60;
    public static final int API_CALL_UPLOAD_IMAGE_FILE_TIMEOUT = 240;
    private static Retrofit retrofitWithToken = null, retrofitWithoutToken = null;
    private static final Gson gson = new GsonBuilder().setLenient().create();

    /**
     * Method to return retrofit instance. This method will retrofit retrofit instance with Base url.
     *
     * @return instance of ad retrofit.
     */

    public static Retrofit getClient() {
        if (retrofitWithoutToken == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(API_CALL_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(API_CALL_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(API_CALL_TIMEOUT, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> {
                    CustomLog.error("response", message);
                }).setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(interceptor);
            }

            OkHttpClient okHttpClient = httpClient.build();

            retrofitWithoutToken = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitWithoutToken;
    }

    /**
     * Method to return retrofit instance. This method will retrofit retrofit instance with Base url.
     *
     * @return instance of ad retrofit.
     */

    public static Retrofit getClientWithToken(final Context context) {
        if (retrofitWithToken == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(API_CALL_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(API_CALL_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(API_CALL_TIMEOUT, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> {
                    CustomLog.error("response", message);
                }).setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(interceptor);
            }

            httpClient.addInterceptor(chain -> {
                String token = AppPref.getInstance(context).getValue(StaticData.PREF_API_ACCESS_TOKEN, "");

                Log.i("Token", ": " + token);

                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", PREF_ACCESS_TOKEN_TYPE + " " + token)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build();

                return chain.proceed(newRequest);
            });

            OkHttpClient okHttpClient = httpClient.build();

            retrofitWithToken = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitWithToken;
    }


    public static Retrofit getClientUploadImage(final Context context) {
        if (retrofitWithToken == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(API_CALL_UPLOAD_IMAGE_FILE_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(API_CALL_UPLOAD_IMAGE_FILE_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(API_CALL_UPLOAD_IMAGE_FILE_TIMEOUT, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(interceptor);
            }

            httpClient.addInterceptor(chain -> {
                String token = AppPref.getInstance(context).getValue(StaticData.PREF_API_ACCESS_TOKEN, "");

                Log.i("Token", ": " + token);

                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", PREF_ACCESS_TOKEN_TYPE + " " + token)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build();

                return chain.proceed(newRequest);
            });

            OkHttpClient okHttpClient = httpClient.build();

            retrofitWithToken = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitWithToken;
    }

    public static Converter<ResponseBody, APIError> getConverter() {
        return retrofitWithToken.responseBodyConverter(APIError.class, new Annotation[0]);
    }

}
