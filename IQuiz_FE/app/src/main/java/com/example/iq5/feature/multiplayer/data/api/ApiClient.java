package com.example.iq5.feature.multiplayer.data.api;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static ApiClient instance;
    private final OkHttpClient client;
    private final Gson gson;
    private final Context context;

    private ApiClient(Context context) {
        this.context = context.getApplicationContext();
        this.gson = new GsonBuilder()
                .setLenient()
                .create();

        // Configure OkHttp client
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new AuthInterceptor(context))
                .addInterceptor(new LoggingInterceptor())
                .build();
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public Gson getGson() {
        return gson;
    }

    // ===============================
    // INTERCEPTOR: AUTO ADD TOKEN
    // ===============================
    private static class AuthInterceptor implements Interceptor {
        private final Context context;

        public AuthInterceptor(Context context) {
            this.context = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            // Get token from SharedPreferences
            SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            String token = prefs.getString("auth_token", "");

            // Add Authorization header if token exists
            Request.Builder builder = original.newBuilder();
            if (!token.isEmpty()) {
                builder.addHeader("Authorization", "Bearer " + token);
            }
            builder.addHeader("Content-Type", "application/json");

            Request request = builder.build();
            return chain.proceed(request);
        }
    }

    // ===============================
    // INTERCEPTOR: LOGGING
    // ===============================
    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            android.util.Log.d("API_REQUEST", String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            android.util.Log.d("API_RESPONSE", String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
}