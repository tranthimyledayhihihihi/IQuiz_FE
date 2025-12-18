package com.example.iq5.core.network;

import com.example.iq5.core.prefs.PrefsManager;

import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

   
    private static final String BASE_URL = "http://10.0.2.2:5048/api/"; // Android Emulator
    // private static final String BASE_URL = "http://192.168.1.6:5048/api/"; // Thiết bị thật - WiFi
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

    // Tạo unsafe TrustManager cho development (KHÔNG dùng trong production)
    private static X509TrustManager getUnsafeTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        };
    }

    // Tạo unsafe SSL context cho development
    private static SSLSocketFactory getUnsafeSSLSocketFactory() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{getUnsafeTrustManager()};
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Phương thức khởi tạo Retrofit, yêu cầu PrefsManager để tạo OkHttpClient
    private static Retrofit initializeRetrofit(PrefsManager prefsManager) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(getAuthInterceptor(prefsManager))
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
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