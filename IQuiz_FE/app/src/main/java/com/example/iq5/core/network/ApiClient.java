package com.example.iq5.core.network;

import android.util.Log;

import com.example.iq5.BuildConfig;
import com.example.iq5.core.prefs.PrefsManager;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {

    private static volatile Retrofit retrofitInstance;
    private static volatile String lastBaseUrl;

    private ApiClient() {
        // no instance
    }

    // Interceptor gắn JWT
    private static Interceptor authInterceptor(PrefsManager prefsManager) {
        return chain -> {
            Request original = chain.request();
            Request.Builder builder = original.newBuilder();

            if (prefsManager != null) {
                String token = prefsManager.getAuthToken();
                if (token != null && !token.trim().isEmpty()) {
                    builder.header("Authorization", "Bearer " + token.trim());
                }
            }
            return chain.proceed(builder.build());
        };
    }

    // OkHttp client
    private static OkHttpClient okHttp(PrefsManager prefsManager) {
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(log)
                .addInterceptor(authInterceptor(prefsManager))
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    // Build Retrofit
    private static Retrofit buildRetrofit(PrefsManager prefsManager, String baseUrl) {

        // LOG kiểm tra base URL lúc runtime
        Log.e("API_BASE_URL", baseUrl);

        return new Retrofit.Builder()
                .baseUrl(baseUrl) // 🔥 CHỈ DÙNG BUILDCONFIG
                .client(okHttp(prefsManager))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // Lấy Retrofit instance (theo BASE_URL hiện tại)
    public static Retrofit getClient(PrefsManager prefsManager) {

        String baseUrl = BuildConfig.BASE_URL;

        if (retrofitInstance == null || lastBaseUrl == null || !lastBaseUrl.equals(baseUrl)) {
            synchronized (ApiClient.class) {
                if (retrofitInstance == null || lastBaseUrl == null || !lastBaseUrl.equals(baseUrl)) {
                    lastBaseUrl = baseUrl;
                    retrofitInstance = buildRetrofit(prefsManager, baseUrl);
                }
            }
        }
        return retrofitInstance;
    }

    // Tạo service
    public static <T> T createService(Retrofit retrofit, Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

    // Reset khi logout / đổi môi trường
    public static void reset() {
        retrofitInstance = null;
        lastBaseUrl = null;
    }
}
