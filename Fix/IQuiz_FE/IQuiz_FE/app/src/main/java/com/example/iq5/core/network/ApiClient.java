package com.example.iq5.core.network;

import com.example.iq5.core.prefs.PrefsManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://your-backend-api.com/api/";
    private static Retrofit retrofitInstance; // Dùng cho Singleton

    // Interceptor để thêm JWT Token (Có thể sử dụng lambda thay vì Anonymous Inner Class)
    private static Interceptor getAuthInterceptor(PrefsManager prefsManager) {
        return chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();

            String token = prefsManager.getAuthToken();
            if (token != null) {
                requestBuilder.header("Authorization", "Bearer " + token);
            }

            return chain.proceed(requestBuilder.build());
        };
    }

    // Phương thức khởi tạo Retrofit, yêu cầu PrefsManager để tạo OkHttpClient
    private static Retrofit initializeRetrofit(PrefsManager prefsManager) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(getAuthInterceptor(prefsManager)) // Đã sửa lỗi kiểu dữ liệu
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // Phương thức Singleton để lấy đối tượng Retrofit
    // LƯU Ý: Phương thức này cần nhận PrefsManager từ Activity/Application
    public static Retrofit getClient(PrefsManager prefsManager) {
        if (retrofitInstance == null) {
            retrofitInstance = initializeRetrofit(prefsManager);
        }
        return retrofitInstance;
    }

    // Phương thức Generic để tạo bất kỳ Service Interface nào
    public static <T> T createService(Retrofit retrofit, Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

    // === XÓA CÁC PHƯƠNG THỨC GÂY LỖI TRÙNG LẶP VÀ SAI KIỂU DỮ LIỆU ===
    /*
    public static Retrofit create(Class<QuizApiService> prefsManager) { ... } // LỖI SAI KIỂU DỮ LIỆU
    public static AuthApiService getAuthService(Retrofit retrofit) { ... } // THAY THẾ BỞI createService
    public static QuizApiService getQuizService(Retrofit retrofit) { ... }   // THAY THẾ BỞI createService
    public static ResultApiService getResultService(Retrofit retrofit) { ... } // THAY THẾ BỞI createService
    public static ApiClient getClient() { } // LỖI MISSING RETURN
    public Object create(Class<QuizApiService> prefsManager) { } // LỖI TRÙNG LẶP
    public Object create(Class<QuizApiService> quizApiServiceClass) { } // LỖI TRÙNG LẶP
    */
}