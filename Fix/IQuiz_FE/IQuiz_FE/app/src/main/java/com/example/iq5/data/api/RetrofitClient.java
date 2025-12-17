package com.example.iq5.data.api;

import com.example.iq5.utils.DateDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Retrofit Client Singleton
 * Quản lý kết nối API với backend
 */
public class RetrofitClient {
    
    // ⚠️ QUAN TRỌNG: BASE_URL cho Android Emulator
    // - Android Emulator: 10.0.2.2 = localhost của máy host
    // - Thiết bị thật: Dùng IP thật của máy (ví dụ: 192.168.1.100)
    
    // ✅ ĐÚNG CHO EMULATOR:
    // Option 1: Use 10.0.2.2 (requires firewall rule)
    // private static final String BASE_URL = "http://10.0.2.2:7092/";
    
    // Option 2: Use machine IP (works without firewall rule if on same network)
    private static final String BASE_URL = "http://192.168.214.1:7092/";
    
    // ❌ SAI - KHÔNG DÙNG localhost trong Android:
    // private static final String BASE_URL = "http://localhost:7092/";
    // private static final String BASE_URL = "http://127.0.0.1:7092/";
    
    private static Retrofit retrofit = null;
    private static ApiService apiService = null;

    /**
     * Lấy instance của Retrofit (Singleton pattern)
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            // Tạo logging interceptor để debug
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Tạo OkHttp client với timeout và logging
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(60, TimeUnit.SECONDS)  // Increased from 30 to 60
                    .readTimeout(60, TimeUnit.SECONDS)     // Increased from 30 to 60
                    .writeTimeout(60, TimeUnit.SECONDS)    // Increased from 30 to 60
                    .retryOnConnectionFailure(true)        // Enable retry on connection failure
                    .build();

            // Tạo custom Gson với DateDeserializer
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateDeserializer())
                    .create();

            // Tạo Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    /**
     * Lấy API Service instance
     */
    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = getClient().create(ApiService.class);
        }
        return apiService;
    }

    /**
     * Reset client (dùng khi cần thay đổi cấu hình)
     */
    public static void resetClient() {
        retrofit = null;
        apiService = null;
    }
}
