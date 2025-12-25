package com.example.iq5.data.api;

import android.util.Log;

import com.example.iq5.BuildConfig;
import com.example.iq5.utils.DateDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit Client Singleton
 * Dùng chung cho toàn bộ app
 * BASE_URL lấy từ BuildConfig (productFlavors / buildVariants)
 */
public final class RetrofitClient {

    private static Retrofit retrofit = null;
    private static ApiService apiService = null;

    private RetrofitClient() {
        // no instance
    }

    /**
     * Khởi tạo Retrofit instance
     */
    private static Retrofit createRetrofit() {

        // LOG để xác nhận BASE_URL khi chạy (rất quan trọng lúc debug)
        Log.e("RETROFIT_BASE_URL", BuildConfig.BASE_URL);

        // Logging interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // OkHttp client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        // Gson (custom Date deserializer)
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL) // 🔥 CHỈ DÙNG BUILDCONFIG
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    /**
     * Lấy Retrofit instance (Singleton)
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    retrofit = createRetrofit();
                }
            }
        }
        return retrofit;
    }

    /**
     * Lấy ApiService
     */
    public static ApiService getApiService() {
        if (apiService == null) {
            synchronized (RetrofitClient.class) {
                if (apiService == null) {
                    apiService = getClient().create(ApiService.class);
                }
            }
        }
        return apiService;
    }

    /**
     * Reset client (dùng khi logout / đổi account / đổi env)
     */
    public static void reset() {
        retrofit = null;
        apiService = null;
    }
}
