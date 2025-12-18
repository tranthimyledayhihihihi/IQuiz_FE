# 🚀 Hướng dẫn sử dụng API

## 📦 Cấu trúc API đã tạo

### Models (Data Classes)
- `LoginRequest` - Dữ liệu đăng nhập
- `LoginResponse` - Phản hồi đăng nhập  
- `RegisterRequest` - Dữ liệu đăng ký
- `ApiResponse` - Phản hồi chung
- `Question` - Câu hỏi quiz
- `GameStartOptions` - Tùy chọn bắt đầu quiz
- `AnswerSubmit` - Nộp đáp án

### API Services
- `AuthApiService` - Đăng nhập/đăng ký/đăng xuất
- `QuizApiService` - Quiz (bắt đầu, nộp bài, kết thúc)
- `UserApiService` - Profile và settings

## 🔧 Cách sử dụng trong Activity

### 1. Test kết nối server
```java
// Thêm vào onCreate() của Activity
NetworkTestHelper.testConnection(prefsManager);
```

### 2. Đăng nhập
```java
ApiUsageExample.loginExample(prefsManager, "username", "password");
```

### 3. Đăng ký
```java
ApiUsageExample.registerExample(prefsManager, "username", "email@test.com", 
                               "password", "password", "Họ Tên");
```

### 4. Lấy thông tin profile
```java
ApiUsageExample.getProfileExample(prefsManager);
```

### 5. Bắt đầu quiz
```java
ApiUsageExample.startQuizExample(prefsManager);
```

### 6. Đăng xuất
```java
ApiUsageExample.logoutExample(prefsManager);
```

## 🎯 Sử dụng trực tiếp API Service

### Đăng nhập
```java
AuthApiService authService = ApiServiceFactory.getAuthService(prefsManager);
LoginRequest request = new LoginRequest("username", "password");
Call<LoginResponse> call = authService.login(request);

call.enqueue(new Callback<LoginResponse>() {
    @Override
    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
        if (response.isSuccessful()) {
            LoginResponse loginResponse = response.body();
            prefsManager.saveAuthToken(loginResponse.getToken());
            // Chuyển đến MainActivity
        }
    }
    
    @Override
    public void onFailure(Call<LoginResponse> call, Throwable t) {
        // Xử lý lỗi
    }
});
```

## ⚠️ Lưu ý quan trọng

1. **Server phải chạy** trên `http://localhost:5048` (hoặc IP tương ứng)
2. **Token tự động** được thêm vào header qua `getAuthInterceptor`
3. **Error handling** cho 401 (Unauthorized), 404, 500
4. **Reset services** khi đăng xuất: `ApiServiceFactory.resetServices()`

## 🔍 Debug

- Xem Logcat với filter `NetworkTestHelper` để test kết nối
- Xem Logcat với filter `ApiUsageExample` để debug API calls
- HTTP requests được log chi tiết qua `HttpLoggingInterceptor`