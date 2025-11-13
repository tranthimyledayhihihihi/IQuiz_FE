package com.example.iq5.core.network;

import com.example.iq5.core.prefs.PrefsManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://your-backend-api.com/api/";

    // Interceptor để thêm JWT Token (Sử dụng cú pháp Anonymous Inner Class chuẩn Java)
    private static Interceptor getAuthInterceptor(PrefsManager prefsManager) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();

                String token = prefsManager.getAuthToken();
                if (token != null) {
                    requestBuilder.header("Authorization", "Bearer " + token);
                }

                return chain.proceed(requestBuilder.build());
            }
        };
    }

    public static Retrofit create(PrefsManager prefsManager) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(getAuthInterceptor(prefsManager))
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // Phương thức tạo các Service
    public static AuthApiService getAuthService(Retrofit retrofit) {
        return retrofit.create(AuthApiService.class);
    }

    public static QuizApiService getQuizService(Retrofit retrofit) {
        return retrofit.create(QuizApiService.class);
    }

    public static ResultApiService getResultService(Retrofit retrofit) {
        return retrofit.create(ResultApiService.class);
    }

    // ... thêm các phương thức getter khác
}