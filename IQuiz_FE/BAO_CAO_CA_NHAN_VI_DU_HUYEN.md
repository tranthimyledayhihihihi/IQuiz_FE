# BÁO CÁO CÁ NHÂN - DỰ ÁN IQUIZ

**Họ và tên:** Nguyễn Thị Huyền  
**MSSV:** 125XXXXX  
**Lớp:** 125LTTD02  
**Vai trò trong nhóm:** Full-stack Developer (Backend + Frontend)  
**Ngày báo cáo:** 15/01/2025

---

## MỤC LỤC

1. [Thông tin cá nhân và vai trò](#1-thông-tin-cá-nhân-và-vai-trò)
2. [Nhiệm vụ được phân công](#2-nhiệm-vụ-được-phân-công)
3. [Công việc đã thực hiện](#3-công-việc-đã-thực-hiện)
4. [Kết quả đạt được](#4-kết-quả-đạt-được)
5. [Khó khăn và giải pháp](#5-khó-khăn-và-giải-pháp)
6. [Bài học kinh nghiệm](#6-bài-học-kinh-nghiệm)
7. [Đóng góp cho dự án](#7-đóng-góp-cho-dự-án)
8. [Kết luận](#8-kết-luận)

---

## 1. THÔNG TIN CÁ NHÂN VÀ VAI TRÒ

### 1.1. Thông tin cá nhân
- **Họ và tên:** Nguyễn Thị Huyền
- **Mã số sinh viên:** 125XXXXX
- **Lớp:** 125LTTD02
- **Email:** huyen.nguyen@example.com
- **Số điện thoại:** 090XXXXXXX

### 1.2. Vai trò trong dự án
- [x] **Frontend Developer (Android):** Phát triển giao diện và logic ứng dụng Android
- [x] **Backend Developer:** Phát triển API server và database
- [x] **Full-stack Developer:** Làm việc với cả frontend và backend
- [ ] **UI/UX Designer:** Thiết kế giao diện và trải nghiệm người dùng
- [x] **Database Designer:** Thiết kế và quản lý cơ sở dữ liệu
- [x] **API Integration:** Tích hợp API vào ứng dụng Android
- [x] **Tester:** Kiểm thử và đảm bảo chất lượng

### 1.3. Kỹ năng và kiến thức áp dụng
- **Ngôn ngữ lập trình:** Java, C#, SQL, TSQL
- **Framework/Thư viện:** 
  - Android SDK, AndroidX, Material Design
  - ASP.NET Core, Entity Framework Core
  - Retrofit, OkHttp, Gson
  - Room Database
- **Công cụ:** Android Studio, Visual Studio 2022, SQL Server Management Studio, Git, Postman, Swagger
- **Kiến thức khác:** RESTful API Design, Database Design, JWT Authentication, WebSocket, Clean Architecture, SOLID Principles

---

## 2. NHIỆM VỤ ĐƯỢC PHÂN CÔNG

### 2.1. Nhiệm vụ chính

**Backend (ASP.NET Core):**
- Thiết kế và xây dựng database schema cho hệ thống IQuiz
- Phát triển API endpoints cho module Authentication (Account Controller)
- Phát triển API endpoints cho module Quiz Game (Choi Controller)
- Phát triển API endpoints cho module Profile và Settings
- Phát triển API endpoints cho module Achievement và Rewards
- Thiết lập JWT Authentication và Authorization
- Implement WebSocket cho chế độ multiplayer real-time
- Viết stored procedures và optimize database queries

**Frontend (Android):**
- Phát triển module Authentication (Login, Register, Logout)
- Phát triển module Profile và Settings
- Tích hợp API Authentication vào ứng dụng Android
- Tích hợp API Profile và Settings
- Phát triển giao diện cho các màn hình Profile, Settings
- Implement Retrofit client và API service interfaces
- Xử lý error handling và loading states

### 2.2. Nhiệm vụ phụ
- Review code của các thành viên khác
- Fix bugs và optimize performance cho cả backend và frontend
- Viết tài liệu API (Swagger documentation)
- Hỗ trợ testing và debug
- Thiết lập Git workflow và code review process
- Hỗ trợ các thành viên khác trong việc tích hợp API

### 2.3. Thời gian thực hiện
- **Bắt đầu:** 01/10/2024
- **Kết thúc:** 15/01/2025
- **Tổng thời gian:** ~15 tuần (khoảng 300-400 giờ làm việc)

---

## 3. CÔNG VIỆC ĐÃ THỰC HIỆN

### 3.1. Giai đoạn 1: Phân tích và thiết kế hệ thống

**Thời gian:** 01/10/2024 - 15/10/2024

**Công việc đã làm:**
- [x] Phân tích yêu cầu và thiết kế database schema
- [x] Thiết kế API endpoints và request/response models
- [x] Thiết kế authentication flow với JWT
- [x] Thiết kế architecture cho Android app
- [x] Tạo project structure cho Backend (ASP.NET Core)
- [x] Tạo project structure cho Frontend (Android)

**Kết quả:**
- Database schema hoàn chỉnh với các bảng: Users, Quiz, Questions, Attempts, Results, Achievements
- API design document với Swagger specification
- Architecture diagram cho cả backend và frontend
- Project structure đã được setup

**Files/Code đã tạo:**
- `IQuiz_BE/QUIZ (1).sql` - Database schema và initial data
- `IQuiz_BE/Controllers/AccountController.cs` - Structure cơ bản
- `IQuiz_FE/app/src/main/java/com/example/iq5/data/api/ApiService.java` - API interface

---

### 3.2. Giai đoạn 2: Phát triển Backend API

**Thời gian:** 16/10/2024 - 30/11/2024

**Công việc đã làm:**
- [x] Implement Account Controller (Login, Register, Logout, Change Password)
- [x] Implement JWT Authentication middleware
- [x] Implement Profile Controller
- [x] Implement Settings Controller
- [x] Implement Achievement Controller
- [x] Implement Quiz Game Controller (Choi Controller)
- [x] Setup Entity Framework Core và database context
- [x] Implement repository pattern
- [x] Setup Swagger documentation

**Kết quả:**
- Hoàn thành 5 controllers chính với đầy đủ endpoints
- JWT authentication hoạt động ổn định
- Database operations được optimize
- API documentation đầy đủ trên Swagger

**Files/Code đã tạo:**
- `IQuiz_BE/Controllers/AccountController.cs` - ~300 dòng
- `IQuiz_BE/Controllers/ProfileController.cs` - ~200 dòng
- `IQuiz_BE/Controllers/AchievementController.cs` - ~250 dòng
- `IQuiz_BE/Controllers/ChoiController.cs` - ~400 dòng
- `IQuiz_BE/Services/JwtService.cs` - ~150 dòng
- `IQuiz_BE/Data/ApplicationDbContext.cs` - ~200 dòng
- `IQuiz_BE/Models/*.cs` - ~500 dòng (các model classes)

---

### 3.3. Giai đoạn 3: Phát triển Frontend Android

**Thời gian:** 01/12/2024 - 31/12/2024

**Công việc đã làm:**
- [x] Implement Retrofit client và API service
- [x] Implement Authentication module (Login, Register)
- [x] Implement Profile Activity và Settings Activity
- [x] Tích hợp API Profile và Settings
- [x] Implement SharedPreferences để lưu JWT token
- [x] Implement error handling và loading states
- [x] Design UI cho Profile và Settings screens

**Kết quả:**
- Authentication flow hoàn chỉnh
- Profile và Settings screens hoạt động tốt
- API integration thành công
- User experience mượt mà

**Files/Code đã tạo:**
- `IQuiz_FE/app/src/main/java/com/example/iq5/data/api/ApiService.java` - ~200 dòng
- `IQuiz_FE/app/src/main/java/com/example/iq5/data/api/RetrofitClient.java` - ~100 dòng
- `IQuiz_FE/app/src/main/java/com/example/iq5/feature/auth/ui/LoginActivity.java` - ~250 dòng
- `IQuiz_FE/app/src/main/java/com/example/iq5/feature/auth/ui/RegisterActivity.java` - ~200 dòng
- `IQuiz_FE/app/src/main/java/com/example/iq5/feature/auth/ui/ProfileActivity.java` - ~300 dòng
- `IQuiz_FE/app/src/main/java/com/example/iq5/feature/auth/ui/SettingsActivity.java` - ~250 dòng
- `IQuiz_FE/app/src/main/java/com/example/iq5/utils/PrefsManager.java` - ~150 dòng
- Layout XML files - ~500 dòng

---

### 3.4. Giai đoạn 4: Tích hợp và Testing

**Thời gian:** 01/01/2025 - 15/01/2025

**Công việc đã làm:**
- [x] Test tích hợp giữa Frontend và Backend
- [x] Fix bugs phát hiện được
- [x] Optimize API performance
- [x] Optimize Android app performance
- [x] Viết API documentation
- [x] Test trên nhiều thiết bị Android

**Kết quả:**
- Tất cả tính năng hoạt động ổn định
- Performance được cải thiện đáng kể
- Documentation đầy đủ

---

### 3.5. Chi tiết các tính năng đã implement

#### 3.5.1. Authentication Module (Backend + Frontend)

**Mô tả:**
Xây dựng hệ thống xác thực người dùng hoàn chỉnh với JWT token, bao gồm đăng nhập, đăng ký, đăng xuất và đổi mật khẩu.

**Backend - Các file đã tạo/chỉnh sửa:**
```
IQuiz_BE/Controllers/AccountController.cs
- Chức năng: Xử lý các request đăng nhập, đăng ký, đăng xuất, đổi mật khẩu
- Số dòng code: ~300 dòng

IQuiz_BE/Services/JwtService.cs
- Chức năng: Tạo và validate JWT tokens
- Số dòng code: ~150 dòng

IQuiz_BE/Models/LoginRequest.cs, RegisterRequest.cs
- Chức năng: Data models cho authentication requests
- Số dòng code: ~100 dòng
```

**Frontend - Các file đã tạo/chỉnh sửa:**
```
IQuiz_FE/app/src/main/java/com/example/iq5/feature/auth/ui/LoginActivity.java
- Chức năng: Màn hình đăng nhập, xử lý form và gọi API
- Số dòng code: ~250 dòng

IQuiz_FE/app/src/main/java/com/example/iq5/feature/auth/ui/RegisterActivity.java
- Chức năng: Màn hình đăng ký với validation
- Số dòng code: ~200 dòng

IQuiz_FE/app/src/main/java/com/example/iq5/utils/PrefsManager.java
- Chức năng: Quản lý SharedPreferences để lưu JWT token
- Số dòng code: ~150 dòng
```

**API đã tích hợp:**
- `POST /api/Account/login` - Đăng nhập và nhận JWT token
- `POST /api/Account/register` - Đăng ký tài khoản mới
- `POST /api/Account/logout` - Đăng xuất
- `POST /api/Account/change-password` - Đổi mật khẩu

**Screenshot/Demo:**
- Login screen với validation
- Register screen với form validation
- Success/Error messages

---

#### 3.5.2. Profile & Settings Module (Backend + Frontend)

**Mô tả:**
Module quản lý thông tin cá nhân và cài đặt người dùng, cho phép xem và cập nhật profile, thay đổi settings.

**Backend - Các file đã tạo/chỉnh sửa:**
```
IQuiz_BE/Controllers/ProfileController.cs
- Chức năng: API endpoints cho profile (GET, PUT)
- Số dòng code: ~200 dòng

IQuiz_BE/Controllers/SettingsController.cs
- Chức năng: API endpoints cho settings (GET, PUT)
- Số dòng code: ~150 dòng

IQuiz_BE/Models/UserProfileModel.cs
- Chức năng: Data model cho user profile
- Số dòng code: ~80 dòng
```

**Frontend - Các file đã tạo/chỉnh sửa:**
```
IQuiz_FE/app/src/main/java/com/example/iq5/feature/auth/ui/ProfileActivity.java
- Chức năng: Hiển thị và chỉnh sửa thông tin profile
- Số dòng code: ~300 dòng

IQuiz_FE/app/src/main/java/com/example/iq5/feature/auth/ui/SettingsActivity.java
- Chức năng: Quản lý cài đặt ứng dụng
- Số dòng code: ~250 dòng

IQuiz_FE/app/src/main/res/layout/activity_profile.xml
- Chức năng: Layout cho màn hình profile
- Số dòng code: ~150 dòng

IQuiz_FE/app/src/main/res/layout/activity_settings.xml
- Chức năng: Layout cho màn hình settings
- Số dòng code: ~120 dòng
```

**API đã tích hợp:**
- `GET /api/user/Profile/me` - Lấy thông tin profile của user hiện tại
- `PUT /api/user/Profile/me` - Cập nhật thông tin profile
- `PUT /api/user/Profile/settings` - Cập nhật settings

**Screenshot/Demo:**
- Profile screen với avatar, thông tin cá nhân
- Settings screen với các tùy chọn
- Edit profile dialog/form

---

#### 3.5.3. Quiz Game Module (Backend)

**Mô tả:**
Module xử lý logic chơi quiz, bao gồm bắt đầu quiz, lấy câu hỏi, nộp đáp án và kết thúc quiz.

**Backend - Các file đã tạo/chỉnh sửa:**
```
IQuiz_BE/Controllers/ChoiController.cs
- Chức năng: API endpoints cho quiz game (start, submit, next, end)
- Số dòng code: ~400 dòng

IQuiz_BE/Services/QuizService.cs
- Chức năng: Business logic cho quiz game
- Số dòng code: ~300 dòng

IQuiz_BE/Models/StartQuizRequest.cs, SubmitAnswerModel.cs
- Chức năng: Data models cho quiz requests
- Số dòng code: ~150 dòng
```

**API đã tích hợp:**
- `POST /api/Choi/start` - Bắt đầu phiên làm bài quiz
- `GET /api/Choi/next/{attemptId}` - Lấy câu hỏi tiếp theo
- `POST /api/Choi/submit` - Nộp đáp án
- `POST /api/Choi/end/{attemptId}` - Kết thúc quiz và xem kết quả

---

#### 3.5.4. Achievement Module (Backend)

**Mô tả:**
Module quản lý thành tích và phần thưởng của người dùng, bao gồm streak, daily rewards, achievements.

**Backend - Các file đã tạo/chỉnh sửa:**
```
IQuiz_BE/Controllers/AchievementController.cs
- Chức năng: API endpoints cho achievements và rewards
- Số dòng code: ~250 dòng

IQuiz_BE/Services/AchievementService.cs
- Chức năng: Business logic cho achievements
- Số dòng code: ~200 dòng
```

**API đã tích hợp:**
- `GET /api/user/Achievement/me` - Lấy thành tựu của user
- `GET /api/user/Achievement/streak` - Lấy chuỗi ngày chơi
- `POST /api/user/Achievement/daily-reward` - Nhận thưởng hằng ngày
- `GET /api/user/Achievement/my-rewards` - Danh sách quà tặng

---

### 3.6. Code snippets quan trọng

#### 3.6.1. JWT Authentication Service (Backend)

```csharp
// IQuiz_BE/Services/JwtService.cs
public class JwtService
{
    private readonly IConfiguration _configuration;
    
    public string GenerateToken(User user)
    {
        var claims = new[]
        {
            new Claim(ClaimTypes.NameIdentifier, user.UserId.ToString()),
            new Claim(ClaimTypes.Name, user.Username),
            new Claim(ClaimTypes.Email, user.Email)
        };
        
        var key = new SymmetricSecurityKey(
            Encoding.UTF8.GetBytes(_configuration["Jwt:Key"]));
        var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
        
        var token = new JwtSecurityToken(
            issuer: _configuration["Jwt:Issuer"],
            audience: _configuration["Jwt:Audience"],
            claims: claims,
            expires: DateTime.Now.AddDays(7),
            signingCredentials: creds);
        
        return new JwtSecurityTokenHandler().WriteToken(token);
    }
}
```

**Giải thích:**
- Service này tạo JWT token cho user sau khi đăng nhập thành công
- Sử dụng SymmetricSecurityKey để ký token
- Token có thời hạn 7 ngày
- Chứa thông tin user ID, username, email trong claims

---

#### 3.6.2. Retrofit API Client Setup (Frontend)

```java
// IQuiz_FE/app/src/main/java/com/example/iq5/data/api/RetrofitClient.java
public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:5048/";
    private static Retrofit retrofit = null;
    
    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    String token = PrefsManager.getInstance(context).getToken();
                    if (token != null) {
                        Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + token);
                        return chain.proceed(requestBuilder.build());
                    }
                    return chain.proceed(original);
                })
                .build();
            
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }
}
```

**Giải thích:**
- Setup Retrofit client với singleton pattern
- Thêm interceptor để tự động thêm JWT token vào header
- Sử dụng HttpLoggingInterceptor để debug API calls
- Base URL sử dụng 10.0.2.2 cho Android emulator (map tới localhost)

---

#### 3.6.3. Login Activity với API Integration (Frontend)

```java
// IQuiz_FE/app/src/main/java/com/example/iq5/feature/auth/ui/LoginActivity.java
private void performLogin(String username, String password) {
    showLoading(true);
    
    LoginRequestModel request = new LoginRequestModel(username, password);
    ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
    
    apiService.login(request).enqueue(new Callback<LoginResponseModel>() {
        @Override
        public void onResponse(Call<LoginResponseModel> call, 
                              Response<LoginResponseModel> response) {
            showLoading(false);
            
            if (response.isSuccessful() && response.body() != null) {
                String token = response.body().getToken();
                PrefsManager.getInstance(this).saveToken(token);
                
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                showError("Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.");
            }
        }
        
        @Override
        public void onFailure(Call<LoginResponseModel> call, Throwable t) {
            showLoading(false);
            showError("Lỗi kết nối. Vui lòng thử lại.");
        }
    });
}
```

**Giải thích:**
- Gọi API login với username và password
- Lưu JWT token vào SharedPreferences khi đăng nhập thành công
- Xử lý success và error cases
- Navigate tới HomeActivity sau khi login thành công

---

### 3.7. Thống kê công việc

**Tổng số file đã tạo/chỉnh sửa:** ~45 files
- Files mới: ~35 files
- Files chỉnh sửa: ~10 files

**Tổng số dòng code:** ~5,500 dòng (ước tính)
- Java (Android): ~2,500 dòng
- C# (Backend): ~2,000 dòng
- XML (Layout): ~800 dòng
- SQL (Database): ~200 dòng

**Số commit trên Git:** ~120 commits
**Số pull request:** ~15 PRs
**Số issue đã fix:** ~25 issues

---

## 4. KẾT QUẢ ĐẠT ĐƯỢC

### 4.1. Tính năng đã hoàn thành
- [x] Authentication Module (Backend + Frontend) - Đăng nhập, đăng ký, đăng xuất, đổi mật khẩu
- [x] Profile Module (Backend + Frontend) - Xem và cập nhật thông tin cá nhân
- [x] Settings Module (Backend + Frontend) - Quản lý cài đặt ứng dụng
- [x] Quiz Game Module (Backend) - Logic chơi quiz (start, submit, end)
- [x] Achievement Module (Backend) - Thành tích và phần thưởng
- [x] JWT Authentication System - Bảo mật API với JWT tokens
- [x] API Documentation - Swagger documentation đầy đủ
- [x] Database Design - Schema hoàn chỉnh và optimized

### 4.2. Chất lượng code
- [x] Code đã được review và approved
- [x] Code tuân thủ coding standards (C# conventions, Java conventions)
- [x] Đã viết comments và documentation
- [x] Đã refactor code để tối ưu
- [x] Đã fix các lỗi được phát hiện

### 4.3. Testing
- [x] Đã test thủ công các tính năng
- [x] Đã test tích hợp với API (Postman, Android app)
- [x] Đã test trên nhiều thiết bị Android (emulator và real device)
- [x] Đã fix các bugs được phát hiện
- [ ] Đã viết unit tests (chưa hoàn thành đầy đủ)

### 4.4. Đóng góp cho team
- [x] Đã hỗ trợ các thành viên khác trong việc tích hợp API
- [x] Đã review code của team members
- [x] Đã chia sẻ kiến thức về JWT, Retrofit, Entity Framework
- [x] Đã tham gia meetings và discussions
- [x] Đã viết API documentation để team dễ sử dụng

---

## 5. KHÓ KHĂN VÀ GIẢI PHÁP

### 5.1. Khó khăn 1: Tích hợp JWT Authentication giữa Backend và Frontend

**Mô tả:**
Ban đầu gặp khó khăn trong việc xử lý JWT token ở Android app, đặc biệt là việc tự động thêm token vào header của mọi API request và xử lý token expiration.

**Nguyên nhân:**
- Chưa quen với cách Retrofit interceptor hoạt động
- Chưa hiểu rõ cách lưu trữ và quản lý token trong Android
- Token expiration chưa được xử lý đúng cách

**Giải pháp đã áp dụng:**
- Nghiên cứu OkHttp Interceptor để tự động thêm token vào header
- Tạo PrefsManager class để quản lý token trong SharedPreferences
- Implement token refresh mechanism
- Sử dụng Retrofit Callback để xử lý 401 Unauthorized và redirect về login

**Kết quả:**
- JWT authentication hoạt động mượt mà
- Token được tự động thêm vào mọi request
- Xử lý token expiration một cách tự động

**Bài học:**
- Hiểu rõ hơn về HTTP interceptors và cách sử dụng trong Android
- Nắm vững cách quản lý authentication state trong mobile app
- Biết cách xử lý error responses và token refresh

---

### 5.2. Khó khăn 2: Optimize Database Queries trong Backend

**Mô tả:**
Ban đầu các API endpoints chạy chậm do N+1 query problem và thiếu indexing trong database.

**Nguyên nhân:**
- Sử dụng Entity Framework Core chưa đúng cách
- Thiếu Include() statements dẫn đến multiple queries
- Database chưa có indexes phù hợp
- Chưa sử dụng async/await đúng cách

**Giải pháp đã áp dụng:**
- Sử dụng Include() và ThenInclude() để eager loading
- Thêm indexes cho các foreign keys và columns thường query
- Chuyển sang async/await cho tất cả database operations
- Sử dụng Select() để chỉ lấy các fields cần thiết
- Implement pagination cho các endpoints trả về danh sách

**Kết quả:**
- API response time giảm từ ~500ms xuống ~100ms
- Database queries được optimize đáng kể
- App chạy mượt mà hơn

**Bài học:**
- Hiểu rõ về Entity Framework Core performance best practices
- Biết cách identify và fix N+1 query problems
- Nắm vững database indexing và optimization

---

### 5.3. Khó khăn 3: Xử lý Error Handling và Loading States trong Android

**Mô tả:**
Ban đầu app không có error handling tốt, dẫn đến crash khi API fail hoặc network error. Loading states cũng chưa được xử lý đúng cách.

**Nguyên nhân:**
- Chưa có try-catch blocks đầy đủ
- Chưa xử lý các trường hợp network error, timeout
- Loading indicators chưa được hiển thị đúng lúc
- Chưa có user-friendly error messages

**Giải pháp đã áp dụng:**
- Tạo ErrorHandler utility class để xử lý các loại errors
- Implement loading states với ProgressDialog/ProgressBar
- Sử dụng try-catch trong tất cả API calls
- Hiển thị user-friendly error messages (Toast, Snackbar)
- Implement retry mechanism cho failed requests
- Sử dụng onFailure callback trong Retrofit để xử lý network errors

**Kết quả:**
- App không còn crash khi có lỗi
- User experience được cải thiện đáng kể
- Error messages rõ ràng và dễ hiểu

**Bài học:**
- Luôn phải xử lý error cases trong mobile development
- User experience quan trọng hơn technical details
- Cần có error handling strategy từ đầu dự án

---

## 6. BÀI HỌC KINH NGHIỆM

### 6.1. Kiến thức kỹ thuật đã học được

**Backend Development:**
- ASP.NET Core Web API development
- Entity Framework Core và database operations
- JWT Authentication và Authorization
- RESTful API design principles
- Async/await programming
- Repository pattern và dependency injection
- Swagger/OpenAPI documentation

**Frontend Development:**
- Android app development với Java
- Retrofit và OkHttp cho API integration
- SharedPreferences cho local storage
- Activity lifecycle management
- Material Design components
- Error handling và loading states
- Navigation và Intent handling

**Database:**
- SQL Server database design
- Entity relationships và foreign keys
- Database indexing và optimization
- Stored procedures
- Query optimization techniques

**API Integration:**
- RESTful API consumption
- JWT token management
- HTTP interceptors
- Error handling strategies
- Request/response models

### 6.2. Kỹ năng mềm đã phát triển

**Làm việc nhóm:**
- Communication: Giao tiếp hiệu quả với team members
- Collaboration: Làm việc cùng nhau trên Git
- Code review: Review code của người khác và nhận feedback
- Knowledge sharing: Chia sẻ kiến thức với team

**Quản lý thời gian:**
- Planning: Lập kế hoạch cho từng giai đoạn
- Prioritization: Ưu tiên các tasks quan trọng
- Time estimation: Ước tính thời gian chính xác hơn
- Deadline management: Quản lý deadline hiệu quả

**Giải quyết vấn đề:**
- Debugging: Kỹ năng debug cả backend và frontend
- Research: Tìm kiếm và học hỏi từ documentation, Stack Overflow
- Problem analysis: Phân tích vấn đề một cách có hệ thống
- Solution design: Thiết kế giải pháp tối ưu

### 6.3. Best practices đã áp dụng

- **Clean Code:** Viết code dễ đọc, dễ maintain
- **SOLID Principles:** Áp dụng các nguyên tắc SOLID trong design
- **Git Workflow:** Sử dụng feature branches, commit messages rõ ràng
- **Code Review:** Review code trước khi merge
- **Error Handling:** Xử lý errors một cách đầy đủ
- **Documentation:** Viết comments và documentation
- **API Design:** Thiết kế API RESTful, consistent
- **Database Design:** Normalize database, optimize queries

### 6.4. Tài liệu và nguồn tham khảo

- **Android Developer Documentation:** https://developer.android.com
- **Retrofit Documentation:** https://square.github.io/retrofit/
- **ASP.NET Core Documentation:** https://docs.microsoft.com/aspnet/core
- **Entity Framework Core Docs:** https://docs.microsoft.com/ef/core
- **JWT.io:** https://jwt.io (để test và debug JWT tokens)
- **Stack Overflow:** Nhiều câu hỏi và giải đáp hữu ích
- **GitHub Repositories:** Tham khảo các open-source projects
- **Material Design Guidelines:** https://material.io/design

---

## 7. ĐÓNG GÓP CHO DỰ ÁN

### 7.1. Đóng góp về code

**Backend:**
- Implemented 5 controllers chính với đầy đủ endpoints
- Created JWT authentication system
- Designed và implemented database schema
- Optimized database queries và performance
- Wrote comprehensive API documentation

**Frontend:**
- Implemented Authentication module hoàn chỉnh
- Implemented Profile và Settings modules
- Created Retrofit client với JWT interceptor
- Implemented error handling và loading states
- Designed UI cho Profile và Settings screens

### 7.2. Đóng góp về thiết kế

- Designed database schema cho toàn bộ hệ thống
- Designed API endpoints structure
- Designed authentication flow
- Created UI/UX cho Profile và Settings screens
- Designed error handling strategy

### 7.3. Đóng góp về tài liệu

- Wrote Swagger API documentation
- Created API integration guide cho team
- Documented database schema
- Wrote code comments và JavaDoc
- Created troubleshooting guide

### 7.4. Đóng góp về quy trình

- Established Git workflow với feature branches
- Set up code review process
- Created API testing checklist
- Established coding standards cho team
- Set up project structure cho cả backend và frontend

### 7.5. Đóng góp khác

- Hỗ trợ các thành viên khác trong việc tích hợp API
- Fix bugs và optimize performance
- Conducted knowledge sharing sessions về JWT, Retrofit
- Tested và verified các tính năng của team members

---

## 8. KẾT LUẬN

### 8.1. Tóm tắt

Trong suốt quá trình tham gia dự án IQuiz, tôi đã có cơ hội làm việc với cả Backend (ASP.NET Core) và Frontend (Android), điều này giúp tôi phát triển kỹ năng full-stack development. Tôi đã hoàn thành các module quan trọng như Authentication, Profile, Settings, và Quiz Game logic. 

Qua dự án này, tôi đã học được rất nhiều về:
- Xây dựng RESTful API với ASP.NET Core
- Tích hợp API vào Android app với Retrofit
- JWT Authentication và bảo mật
- Database design và optimization
- Error handling và user experience
- Làm việc nhóm và collaboration

Dự án đã giúp tôi trưởng thành hơn cả về mặt kỹ thuật lẫn kỹ năng mềm. Tôi cảm thấy tự tin hơn trong việc phát triển các ứng dụng full-stack và sẵn sàng cho các dự án lớn hơn trong tương lai.

### 8.2. Đánh giá bản thân

**Điểm mạnh:**
- Có khả năng học hỏi nhanh các công nghệ mới
- Có thể làm việc độc lập và tự giải quyết vấn đề
- Code quality tốt, tuân thủ best practices
- Giao tiếp tốt với team members
- Có khả năng làm việc với cả backend và frontend

**Điểm cần cải thiện:**
- Cần viết nhiều unit tests hơn
- Cần cải thiện kỹ năng ước tính thời gian
- Cần học thêm về advanced Android architecture (MVVM, LiveData)
- Cần cải thiện kỹ năng debugging phức tạp hơn
- Cần học thêm về performance optimization techniques

### 8.3. Hướng phát triển cá nhân

- **Android Architecture Components:** Học và áp dụng ViewModel, LiveData, Room
- **Advanced Backend:** Học thêm về microservices, Docker, CI/CD
- **Testing:** Cải thiện kỹ năng viết unit tests và integration tests
- **Performance:** Học thêm về app performance optimization
- **Security:** Nâng cao kiến thức về application security
- **Cloud:** Học về cloud deployment (Azure, AWS)

### 8.4. Lời cảm ơn

Tôi xin chân thành cảm ơn:
- **Giảng viên hướng dẫn** đã tận tình hỗ trợ và đưa ra những góp ý quý báu
- **Các thành viên trong nhóm** đã làm việc cùng nhau, hỗ trợ lẫn nhau trong suốt dự án
- **Team leader** đã phân công công việc hợp lý và quản lý tiến độ tốt
- Tất cả những người đã hỗ trợ và đóng góp cho sự thành công của dự án

---

## PHỤ LỤC

### A. Danh sách files đã tạo/chỉnh sửa

**Backend (ASP.NET Core):**
```
IQuiz_BE/Controllers/AccountController.cs - Authentication endpoints
IQuiz_BE/Controllers/ProfileController.cs - Profile management
IQuiz_BE/Controllers/SettingsController.cs - Settings management
IQuiz_BE/Controllers/ChoiController.cs - Quiz game logic
IQuiz_BE/Controllers/AchievementController.cs - Achievements và rewards
IQuiz_BE/Services/JwtService.cs - JWT token generation và validation
IQuiz_BE/Services/QuizService.cs - Quiz business logic
IQuiz_BE/Services/AchievementService.cs - Achievement business logic
IQuiz_BE/Data/ApplicationDbContext.cs - Database context
IQuiz_BE/Models/*.cs - Data models
IQuiz_BE/QUIZ (1).sql - Database schema
```

**Frontend (Android):**
```
IQuiz_FE/app/src/main/java/com/example/iq5/data/api/ApiService.java - API interfaces
IQuiz_FE/app/src/main/java/com/example/iq5/data/api/RetrofitClient.java - Retrofit setup
IQuiz_FE/app/src/main/java/com/example/iq5/feature/auth/ui/LoginActivity.java - Login screen
IQuiz_FE/app/src/main/java/com/example/iq5/feature/auth/ui/RegisterActivity.java - Register screen
IQuiz_FE/app/src/main/java/com/example/iq5/feature/auth/ui/ProfileActivity.java - Profile screen
IQuiz_FE/app/src/main/java/com/example/iq5/feature/auth/ui/SettingsActivity.java - Settings screen
IQuiz_FE/app/src/main/java/com/example/iq5/utils/PrefsManager.java - SharedPreferences manager
IQuiz_FE/app/src/main/res/layout/activity_login.xml - Login layout
IQuiz_FE/app/src/main/res/layout/activity_register.xml - Register layout
IQuiz_FE/app/src/main/res/layout/activity_profile.xml - Profile layout
IQuiz_FE/app/src/main/res/layout/activity_settings.xml - Settings layout
```

### B. Screenshots/Demo

[Chèn các screenshot sau đây nếu có:]
- Login screen
- Register screen
- Profile screen
- Settings screen
- API Swagger documentation
- Database schema diagram

### C. Links và tài liệu tham khảo

- **GitHub Repository:**
  - Frontend: https://github.com/tranthimyledayhihihihi/IQuiz_FE
  - Backend: https://github.com/tranthimyledayhihihihi/IQuiz_BE

- **Tài liệu tham khảo:**
  - Android Developer Documentation: https://developer.android.com
  - Retrofit Documentation: https://square.github.io/retrofit/
  - ASP.NET Core Documentation: https://docs.microsoft.com/aspnet/core
  - Entity Framework Core: https://docs.microsoft.com/ef/core
  - JWT.io: https://jwt.io
  - Material Design: https://material.io/design

---

**Người viết:** Nguyễn Thị Huyền  
**Ngày hoàn thành:** 15/01/2025  
**Chữ ký:** [Chữ ký]

