package com.example.iq5.core.network;

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

    private ApiClient() {}

    private static Interceptor authInterceptor(PrefsManager prefsManager) {
        return chain -> {
            Request original = chain.request();
            Request.Builder b = original.newBuilder();

            if (prefsManager != null) {
                String token = prefsManager.getAuthToken();
                if (token != null && !token.trim().isEmpty()) {
                    b.header("Authorization", "Bearer " + token.trim());
                }
            }
            return chain.proceed(b.build());
        };
    }

    private static OkHttpClient okHttp(PrefsManager prefsManager) {
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(log)
                .addInterceptor(authInterceptor(prefsManager))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private static Retrofit buildRetrofit(PrefsManager prefsManager, String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttp(prefsManager))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

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

    public static <T> T createService(Retrofit retrofit, Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
