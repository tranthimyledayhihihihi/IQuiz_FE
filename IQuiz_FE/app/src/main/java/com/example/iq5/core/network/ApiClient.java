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

    // Base URLs cho các port khác nhau
    private static final String BASE_URL_5048 = "http://10.0.2.2:5048/api/"; // Port mặc định
    private static final String BASE_URL_8084 = "http://10.0.2.2:8084/";     // Cho checkToday và history
    private static final String BASE_URL_8088 = "http://10.0.2.2:8088/";     // Cho claim reward

    private static Retrofit retrofitInstance5048;
    private static Retrofit retrofitInstance8084;
    private static Retrofit retrofitInstance8088;

    // Helper method để lấy base URL cho từng port
    public static String getBaseUrl5048() {
        return BASE_URL_5048;
    }

    public static String getBaseUrl8084() {
        return BASE_URL_8084;
    }

    public static String getBaseUrl8088() {
        return BASE_URL_8088;
    }

    // PHƯƠNG THỨC CHUNG ĐỂ DEBUG - THÊM MỚI
    /**
     * Trả về Base URL mặc định (PORT 5048) cho mục đích debug.
     * Đây là API chính của ứng dụng.
     */
    public static String getBaseUrl() {
        return BASE_URL_5048; // Trả về base URL của API chính
    }

    // Interceptor để thêm JWT Token
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

    // Interceptor KHÔNG thêm token (cho các API không cần authentication)
    private static Interceptor getNoAuthInterceptor() {
        return chain -> chain.proceed(chain.request());
    }

    // Tạo unsafe TrustManager cho development
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

    // ========== PHƯƠNG THỨC CHO PORT 5048 (API chính) ==========

    private static Retrofit initializeRetrofit5048(PrefsManager prefsManager) {
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
                .baseUrl(BASE_URL_5048)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getClient(PrefsManager prefsManager) {
        if (retrofitInstance5048 == null) {
            retrofitInstance5048 = initializeRetrofit5048(prefsManager);
        }
        return retrofitInstance5048;
    }

    // ========== PHƯƠNG THỨC CHO PORT 8084 (Daily Reward Check/History) ==========

    private static Retrofit initializeRetrofit8084() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL_8084)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getClient8084() {
        if (retrofitInstance8084 == null) {
            retrofitInstance8084 = initializeRetrofit8084();
        }
        return retrofitInstance8084;
    }

    // Phiên bản có thêm auth interceptor cho port 8084
    public static Retrofit getClient8084(PrefsManager prefsManager) {
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
                .baseUrl(BASE_URL_8084)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // ========== PHƯƠNG THỨC CHO PORT 8088 (Daily Reward Claim) ==========

    private static Retrofit initializeRetrofit8088() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL_8088)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getClient8088() {
        if (retrofitInstance8088 == null) {
            retrofitInstance8088 = initializeRetrofit8088();
        }
        return retrofitInstance8088;
    }

    // Phiên bản có thêm auth interceptor cho port 8088
    public static Retrofit getClient8088(PrefsManager prefsManager) {
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
                .baseUrl(BASE_URL_8088)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // ========== PHƯƠNG THỨC GENERIC ==========

    // Phương thức Generic để tạo bất kỳ Service Interface nào
    public static <T> T createService(Retrofit retrofit, Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

    // Phương thức tạo service với client mặc định (port 5048)
    public static <T> T createService(PrefsManager prefsManager, Class<T> serviceClass) {
        return getClient(prefsManager).create(serviceClass);
    }

    // Phương thức tạo service cho daily reward (port 8084)
    public static <T> T createDailyRewardService(PrefsManager prefsManager, Class<T> serviceClass) {
        return getClient8084(prefsManager).create(serviceClass);
    }

    // Phương thức tạo service cho claim reward (port 8088)
    public static <T> T createClaimRewardService(PrefsManager prefsManager, Class<T> serviceClass) {
        return getClient8088(prefsManager).create(serviceClass);
    }

    // Reset các client (dùng khi cần thay đổi cấu hình)
    public static void resetClients() {
        retrofitInstance5048 = null;
        retrofitInstance8084 = null;
        retrofitInstance8088 = null;
    }
}