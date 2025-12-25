# ỨNG DỤNG IQUIZ - HỆ THỐNG QUIZ TRỰC TUYẾN

**Nhóm thực hiện:** [Tên nhóm]  
**Thành viên:** [Danh sách thành viên]  
**Lớp:** [Tên lớp]  
**Môn học:** [Tên môn học]  
**Ngày thực hiện:** [Ngày/Tháng/Năm]

---

## MỤC LỤC

1. [Giới thiệu chung](#1-giới-thiệu-chung)
2. [Mục tiêu dự án](#2-mục-tiêu-dự-án)
3. [Phạm vi và đối tượng sử dụng](#3-phạm-vi-và-đối-tượng-sử-dụng)
4. [Công nghệ sử dụng](#4-công-nghệ-sử-dụng)
5. [Kiến trúc hệ thống](#5-kiến-trúc-hệ-thống)
6. [Tính năng chính](#6-tính-năng-chính)
7. [Giao diện ứng dụng](#7-giao-diện-ứng-dụng)
8. [Kết quả đạt được](#8-kết-quả-đạt-được)
9. [Hướng phát triển](#9-hướng-phát-triển)
10. [Kết luận](#10-kết-luận)

---

## 1. GIỚI THIỆU CHUNG

IQuiz là một ứng dụng Android được phát triển để tạo ra một nền tảng học tập và kiểm tra kiến thức thông qua hình thức quiz trực tuyến. Ứng dụng được xây dựng với mục tiêu cung cấp một công cụ học tập hiệu quả, thú vị và dễ sử dụng cho người dùng.

Ứng dụng cho phép người dùng:
- Tham gia các bài quiz theo nhiều danh mục khác nhau
- Theo dõi tiến độ học tập thông qua hệ thống thống kê chi tiết
- Cạnh tranh với người chơi khác thông qua bảng xếp hạng và chế độ multiplayer
- Học tập mọi lúc mọi nơi với chế độ offline và online

IQuiz được phát triển bằng Java, sử dụng các công nghệ Android hiện đại và tích hợp với API backend để đảm bảo trải nghiệm người dùng mượt mà và đồng bộ dữ liệu tốt.

---

## 2. MỤC TIÊU DỰ ÁN

### 2.1. Mục tiêu chính
- Tạo ra một nền tảng quiz học tập thân thiện và dễ sử dụng
- Hỗ trợ người dùng ôn tập và kiểm tra kiến thức một cách hiệu quả
- Xây dựng hệ thống thống kê và theo dõi tiến độ học tập chi tiết

### 2.2. Mục tiêu phụ
- Tạo môi trường cạnh tranh lành mạnh thông qua bảng xếp hạng và chế độ multiplayer
- Hỗ trợ cả chế độ offline và online để người dùng có thể học mọi lúc mọi nơi
- Tích hợp hệ thống thành tích và phần thưởng để khuyến khích người dùng học tập

---

## 3. PHẠM VI VÀ ĐỐI TƯỢNG SỬ DỤNG

### 3.1. Đối tượng sử dụng
- **Học sinh, sinh viên:** Muốn ôn tập kiến thức các môn học
- **Giáo viên:** Muốn tạo bài kiểm tra và theo dõi tiến độ học sinh
- **Người dùng tổng quát:** Muốn kiểm tra và mở rộng kiến thức của mình

### 3.2. Phạm vi dự án
- **Nền tảng:** Android (minSdk 24, targetSdk 36)
- **Chức năng:** Hỗ trợ nhiều danh mục quiz khác nhau
- **Kết nối:** Tích hợp API backend để đồng bộ dữ liệu
- **Lưu trữ:** Hỗ trợ lưu trữ local và cloud

---

## 4. CÔNG NGHỆ SỬ DỤNG

### 4.1. Frontend (Android Application)

#### 4.1.1. Ngôn ngữ lập trình
- **Java 11:** Ngôn ngữ chính để phát triển ứng dụng Android

#### 4.1.2. Framework và thư viện Android

**Android SDK:**
- **AndroidX AppCompat:** Hỗ trợ tương thích ngược
- **Material Design Components:** Tạo giao diện hiện đại
- **Activity & Fragment:** Quản lý lifecycle và navigation

**Database:**
- **Room Database:** Lưu trữ dữ liệu local
  - Room Runtime
  - Room Compiler (annotation processor)

**Network:**
- **Retrofit 2.9.0:** RESTful API client
- **OkHttp 4.11.0:** HTTP client
- **OkHttp Logging Interceptor:** Debug và theo dõi network requests
- **Gson 2.10.1:** JSON serialization/deserialization

**UI Components:**
- **RecyclerView 1.3.2:** Hiển thị danh sách hiệu quả
- **Fragment 1.6.2:** Quản lý UI components
- **ViewPager2 1.1.0:** Swipeable views
- **ConstraintLayout:** Layout linh hoạt
- **CircleImageView 3.1.0:** Hiển thị avatar tròn
- **Glide 4.15.1:** Tải và cache hình ảnh

**Build Tools:**
- **Gradle (Kotlin DSL):** Build automation
- **Android Studio:** IDE chính

### 4.2. Backend (API Server)

#### 4.2.1. Ngôn ngữ và Framework
- **C#:** Ngôn ngữ lập trình chính
- **ASP.NET Core:** Framework web API
- **Entity Framework Core:** ORM cho database

#### 4.2.2. Database
- **SQL Server:** Hệ quản trị cơ sở dữ liệu chính
- **TSQL:** Ngôn ngữ truy vấn

#### 4.2.3. Real-time Communication
- **WebSocket:** Kết nối real-time cho chế độ đối kháng multiplayer
- **SignalR (nếu có):** Framework hỗ trợ WebSocket

#### 4.2.4. Authentication & Security
- **JWT (JSON Web Token):** Xác thực người dùng
- **Bearer Token Authentication:** Bảo mật API endpoints

#### 4.2.5. Deployment
- **Docker:** Containerization (có .dockerignore)
- **IIS/Cloud:** Hosting API server

### 4.3. Kiến trúc tổng thể
- **Client-Server Architecture:** Android app (client) giao tiếp với ASP.NET Core API (server)
- **RESTful API:** Giao tiếp qua HTTP/HTTPS
- **WebSocket:** Giao tiếp real-time cho multiplayer
- **Database:** SQL Server lưu trữ dữ liệu persistent

---

## 5. KIẾN TRÚC HỆ THỐNG

### 5.1. Kiến trúc tổng thể hệ thống

Hệ thống IQuiz được xây dựng theo mô hình **Client-Server** với các thành phần chính:

```
┌─────────────────┐         ┌──────────────────┐         ┌──────────────┐
│  Android App    │◄───────►│  ASP.NET Core API │◄───────►│  SQL Server  │
│   (Frontend)    │  HTTP   │     (Backend)     │   SQL   │  (Database)  │
└─────────────────┘         └──────────────────┘         └──────────────┘
       │                              │
       │                              │
       └──────────WebSocket───────────┘
            (Real-time Multiplayer)
```

**Thành phần:**
- **Frontend (Android):** Ứng dụng di động, giao diện người dùng
- **Backend (ASP.NET Core):** API server xử lý logic nghiệp vụ
- **Database (SQL Server):** Lưu trữ dữ liệu persistent
- **WebSocket:** Kết nối real-time cho multiplayer

### 5.2. Kiến trúc ứng dụng Android
Ứng dụng Android được xây dựng theo mô hình **MVP/MVVM** với pattern **BaseActivity** để tái sử dụng code và quản lý lifecycle hiệu quả.

**Các layer chính:**
- **UI Layer:** Activities, Fragments - Xử lý giao diện người dùng
- **Business Logic Layer:** ViewModels, Presenters - Xử lý logic nghiệp vụ
- **Data Layer:** Repository, API Service, Local Database - Quản lý dữ liệu

### 5.3. Kiến trúc Backend API

Backend được xây dựng bằng ASP.NET Core với kiến trúc Controller-Service-Repository:

**Các Controller chính:**
- **AccountController:** Xác thực (login, register, logout, change-password)
- **HomeController:** Health check và smoke test
- **ChoiController:** Quản lý phiên làm bài quiz (start, submit, next, end)
- **QuizNgayController:** Quiz hàng ngày
- **QuizTuyChinhController:** Quiz tùy chỉnh
- **QuizChiaSeController:** Chia sẻ quiz
- **CauHoiController:** Quản lý câu hỏi
- **LichSuChoiController:** Lịch sử chơi và thống kê
- **ProfileController:** Quản lý hồ sơ người dùng
- **AchievementController:** Thành tích và phần thưởng
- **TranDauController:** Trận đấu online (multiplayer)
- **RankingController:** Bảng xếp hạng

**WebSocket Endpoint:**
- `/ws/game`: Kết nối WebSocket cho chế độ đối kháng real-time

### 5.4. Cấu trúc thư mục Frontend

```
com.example.iq5/
├── core/
│   └── base/              # BaseActivity, BaseFragment
│       └── BaseActivity.java
├── data/                  # Data models, API services
├── feature/               # Các tính năng chính
│   ├── auth/              # Xác thực người dùng
│   │   └── ui/
│   │       ├── SplashActivity
│   │       ├── LoginActivity
│   │       ├── RegisterActivity
│   │       ├── HomeActivity
│   │       ├── ProfileActivity
│   │       └── SettingsActivity
│   ├── quiz/              # Quiz game
│   │   └── ui/
│   │       ├── SelectCategoryActivity
│   │       ├── QuizActivity
│   │       └── ReviewQuestionActivity
│   ├── result/            # Kết quả và thống kê
│   │   └── ui/
│   │       ├── ResultActivity
│   │       ├── StatsActivity
│   │       └── StreakActivity
│   ├── reward/            # Phần thưởng
│   ├── achievement/       # Thành tích
│   ├── history/           # Lịch sử
│   ├── social/            # Xã hội (leaderboard)
│   ├── multiplayer/       # Chế độ nhiều người chơi
│   │   └── ui/
│   │       ├── FindMatchActivity
│   │       ├── RoomLobbyActivity
│   │       └── PvPBattleActivity
│   └── specialmode/       # Chế độ đặc biệt
├── utils/                 # Tiện ích
├── notification/          # Thông báo
└── debug/                 # Debug tools
```

### 5.5. BaseActivity Pattern
Ứng dụng sử dụng `BaseActivity` để:
- Chuẩn hóa lifecycle của các Activity
- Tái sử dụng code setup views và observe data
- Cung cấp các utility methods chung (như showToast)

---

## 6. TÍNH NĂNG CHÍNH

### 6.1. Xác thực người dùng (Authentication)
- **Đăng nhập/Đăng ký:** Người dùng có thể tạo tài khoản mới hoặc đăng nhập vào hệ thống
- **Quản lý hồ sơ:** Xem và chỉnh sửa thông tin cá nhân
- **Cài đặt:** Tùy chỉnh các thiết lập ứng dụng
- **Tích hợp API:** Đồng bộ dữ liệu với backend server

### 6.2. Quiz Game
- **Chọn danh mục:** Người dùng chọn chủ đề quiz muốn làm
- **Làm bài quiz:** Trả lời các câu hỏi với giao diện thân thiện
- **Xem lại câu hỏi:** Sau khi hoàn thành, có thể xem lại các câu đã trả lời
- **Hỗ trợ offline/online:** Có thể làm quiz offline hoặc kết nối API để lấy câu hỏi mới

### 6.3. Kết quả và Thống kê
- **Hiển thị kết quả:** Xem điểm số và đánh giá sau mỗi bài quiz
- **Thống kê tiến độ:** Biểu đồ theo dõi tiến độ học tập theo thời gian
- **Chuỗi ngày học (Streak):** Theo dõi số ngày liên tiếp học tập
- **Phần thưởng hàng ngày:** Nhận phần thưởng khi đăng nhập hàng ngày

### 6.4. Thành tích (Achievements)
- **Hệ thống thành tích:** Thu thập các huy hiệu và thành tựu
- **Theo dõi mốc:** Xem các mốc đã đạt được và mục tiêu tiếp theo
- **Thành tích offline/online:** Hỗ trợ cả hai chế độ

### 6.5. Lịch sử (History)
- **Lịch sử quiz:** Xem tất cả các bài quiz đã làm
- **Chi tiết bài quiz:** Xem chi tiết từng bài quiz, câu hỏi và câu trả lời
- **Lịch sử câu sai:** Xem lại và học từ các câu hỏi đã trả lời sai

### 6.6. Tính năng xã hội
- **Bảng xếp hạng (Leaderboard):** Xem vị trí của mình so với người chơi khác
- **So sánh điểm:** So sánh kết quả với bạn bè

### 6.7. Chế độ Multiplayer
- **Tìm trận đấu:** Tìm kiếm đối thủ để thi đấu
- **Phòng chờ (Room Lobby):** Chờ đợi và chuẩn bị trước khi bắt đầu
- **Đấu PvP:** Thi đấu trực tiếp với người chơi khác
- **So sánh kết quả:** Xem kết quả và so sánh sau khi thi đấu
- **Quản lý bạn bè:** Thêm và quản lý danh sách bạn bè

### 6.8. Chế độ đặc biệt
- **Xem lại câu sai:** Tập trung vào các câu hỏi đã trả lời sai
- **Quiz tùy chỉnh:** Tạo bài quiz theo ý muốn
- **Quiz hàng ngày:** Bài quiz đặc biệt mỗi ngày
- **Lịch sử câu sai:** Theo dõi và ôn tập các câu hỏi sai

---

## 7. GIAO DIỆN ỨNG DỤNG

### 7.1. Thiết kế giao diện
- **Material Design:** Sử dụng Material Design guidelines để tạo giao diện hiện đại, nhất quán
- **Responsive Design:** Hỗ trợ nhiều kích thước màn hình khác nhau
- **Navigation:** Hệ thống điều hướng rõ ràng, dễ sử dụng
- **Color Scheme:** Sử dụng màu sắc hài hòa, dễ nhìn

### 7.2. Các màn hình chính

#### 7.2.1. Splash Screen
- Màn hình khởi động với logo ứng dụng
- Kiểm tra trạng thái đăng nhập và điều hướng phù hợp

#### 7.2.2. Authentication
- **Login Activity:** Đăng nhập với username/password
- **Register Activity:** Đăng ký tài khoản mới

#### 7.2.3. Home
- Màn hình chính sau khi đăng nhập
- Hiển thị các tùy chọn: Quiz, History, Profile, Settings

#### 7.2.4. Select Category
- Danh sách các danh mục quiz có sẵn
- Cho phép chọn danh mục muốn làm

#### 7.2.5. Quiz Activity
- Giao diện làm bài quiz
- Hiển thị câu hỏi, các lựa chọn, và timer (nếu có)
- Navigation giữa các câu hỏi

#### 7.2.6. Result Screen
- Hiển thị điểm số và kết quả
- Phân tích đúng/sai
- Các nút: Xem lại, Làm lại, Về trang chủ

#### 7.2.7. Profile & Settings
- Thông tin cá nhân
- Cài đặt ứng dụng
- Thống kê tổng quan

#### 7.2.8. Leaderboard
- Bảng xếp hạng người chơi
- Vị trí của người dùng hiện tại

#### 7.2.9. History
- Danh sách các bài quiz đã làm
- Lọc và tìm kiếm

#### 7.2.10. Multiplayer Lobby
- Phòng chờ trước khi thi đấu
- Danh sách người chơi trong phòng

### 7.3. UI Components
- **RecyclerView:** Hiển thị danh sách hiệu quả (Leaderboard, History, Friends)
- **ViewPager2:** Swipeable tabs (Friends/Leaderboard)
- **CircleImageView:** Avatar người dùng dạng tròn
- **Glide:** Tải và cache hình ảnh mượt mà

---

## 8. KẾT QUẢ ĐẠT ĐƯỢC

### 8.1. Tính năng đã hoàn thành
✅ Xây dựng thành công ứng dụng quiz với đầy đủ tính năng cơ bản  
✅ Tích hợp API backend để đồng bộ dữ liệu  
✅ Hỗ trợ cả chế độ offline và online  
✅ Giao diện người dùng thân thiện, dễ sử dụng  
✅ Hệ thống thống kê và theo dõi tiến độ học tập  
✅ Chế độ multiplayer cho phép người chơi cạnh tranh với nhau  
✅ Hệ thống thành tích và phần thưởng  
✅ Lưu trữ lịch sử và cho phép xem lại  

### 8.2. Chất lượng code
- Sử dụng BaseActivity pattern để tái sử dụng code
- Tách biệt rõ ràng giữa các layer (UI, Business Logic, Data)
- Code dễ đọc, dễ bảo trì và mở rộng

### 8.3. Hiệu suất
- Sử dụng Room Database cho truy vấn local nhanh
- Sử dụng Retrofit và OkHttp cho network requests hiệu quả
- Sử dụng Glide để tối ưu hóa việc tải hình ảnh

---

## 9. HƯỚNG PHÁT TRIỂN

### 9.1. Tính năng mới
- Mở rộng thêm nhiều danh mục quiz
- Cải thiện thuật toán matching cho multiplayer
- Thêm tính năng chat trong phòng chờ
- Tích hợp push notification để thông báo quiz mới, thách đấu
- Thêm tính năng tạo quiz tùy chỉnh cho người dùng
- Hỗ trợ quiz theo thời gian thực (real-time)

### 9.2. Cải thiện UI/UX
- Thiết kế lại một số màn hình để tối ưu trải nghiệm
- Thêm animations và transitions mượt mà hơn
- Cải thiện accessibility cho người dùng khuyết tật

### 9.3. Tối ưu hóa
- Tối ưu hóa hiệu suất ứng dụng
- Giảm thời gian load và cải thiện responsiveness
- Tối ưu hóa việc sử dụng bộ nhớ

### 9.4. Mở rộng
- Hỗ trợ nhiều ngôn ngữ (i18n)
- Phát triển phiên bản iOS
- Tích hợp với các nền tảng học tập khác

---

## 10. KẾT LUẬN

IQuiz là một ứng dụng quiz học tập toàn diện với nhiều tính năng hữu ích, được xây dựng bằng các công nghệ Android hiện đại. Ứng dụng đã đạt được các mục tiêu ban đầu và có tiềm năng phát triển mạnh trong tương lai.

Với kiến trúc rõ ràng, code dễ bảo trì và các tính năng phong phú, IQuiz không chỉ là một công cụ học tập hiệu quả mà còn là một nền tảng giải trí và cạnh tranh lành mạnh cho người dùng.

Dự án đã thể hiện được khả năng áp dụng các kiến thức về lập trình Android, thiết kế giao diện, quản lý dữ liệu và tích hợp API vào thực tế. Với những cải tiến và mở rộng trong tương lai, IQuiz có thể trở thành một ứng dụng học tập phổ biến.

---

## PHỤ LỤC

### A. Thông tin kỹ thuật
- **Package name:** com.example.iq5
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 36
- **Version Code:** 1
- **Version Name:** 1.0

### B. Cấu trúc API Backend

**Base URL:** `http://[server-ip]:5048/api/` hoặc `http://10.0.2.2:5048/api/` (cho emulator)

**Các API Endpoints chính:**

#### Authentication (Account API)
- `POST /api/Account/login` - Đăng nhập và nhận JWT Token
- `POST /api/Account/register` - Đăng ký tài khoản mới
- `POST /api/Account/change-password` - Đổi mật khẩu
- `POST /api/Account/logout` - Đăng xuất

#### Quiz Game (Choi API)
- `POST /api/Choi/start` - Bắt đầu phiên làm bài quiz
- `POST /api/Choi/submit` - Nộp đáp án cho câu hỏi
- `POST /api/Choi/end/{attemptId}` - Kết thúc phiên và xem kết quả
- `GET /api/Choi/next/{attemptId}` - Lấy câu hỏi tiếp theo

#### Quiz Ngày
- `GET /api/QuizNgay/today` - Lấy quiz của ngày hôm nay
- `POST /api/QuizNgay/start` - Bắt đầu làm Quiz Ngày
- `POST /api/QuizNgay/submit` - Nộp đáp án Quiz Ngày
- `POST /api/QuizNgay/end/{attemptId}` - Kết thúc Quiz Ngày

#### Quiz Tùy Chỉnh
- `GET /api/QuizTuyChinh/my-submissions` - Danh sách đề xuất quiz của tôi
- `GET /api/QuizTuyChinh/{quizId}` - Xem chi tiết đề xuất quiz
- `POST /api/QuizTuyChinh/submit` - Gửi đề xuất quiz mới
- `DELETE /api/QuizTuyChinh/{quizId}` - Xóa đề xuất quiz

#### Câu Hỏi
- `GET /api/CauHoi/incorrect-review` - Lấy câu hỏi sai để ôn tập
- `GET /api/CauHoi/total-count` - Tổng số câu hỏi trong hệ thống
- `GET /api/CauHoi/statistics` - Thống kê câu hỏi theo chủ đề/độ khó

#### Lịch Sử Chơi
- `GET /api/LichSuChoi/my` - Danh sách kết quả của tôi (phân trang)
- `GET /api/LichSuChoi/{attemptId}` - Chi tiết một lần làm bài
- `GET /api/LichSuChoi/streak` - Chuỗi ngày chơi liên tiếp
- `GET /api/LichSuChoi/achievements` - Danh sách thành tựu đã đạt

#### Profile
- `GET /api/user/Profile/me` - Lấy thông tin hồ sơ cá nhân
- `PUT /api/user/Profile/me` - Cập nhật thông tin hồ sơ
- `PUT /api/user/Profile/settings` - Cập nhật cài đặt người dùng

#### Achievement
- `GET /api/user/Achievement/me` - Lấy thành tựu của tôi
- `GET /api/user/Achievement/streak` - Lấy chuỗi ngày chơi
- `POST /api/user/Achievement/daily-reward` - Nhận thưởng hằng ngày
- `GET /api/user/Achievement/my-rewards` - Danh sách quà tặng của tôi

#### Trận Đấu Online (Multiplayer)
- `POST /api/trandau/create` - Tạo phòng đối kháng mới
- `POST /api/trandau/join/{matchCode}` - Tham gia phòng đối kháng
- `GET /api/trandau/status/{matchCode}` - Kiểm tra trạng thái trận đấu
- `GET /api/trandau/history` - Lịch sử các trận đấu
- `GET /api/trandau/detail/{matchCode}` - Chi tiết trận đấu
- `GET /api/trandau/online-count` - Số người đang online
- `DELETE /api/trandau/cancel/{matchCode}` - Hủy phòng đối kháng

#### Ranking
- `GET /api/Ranking/leaderboard` - BXH tuần/tháng (phân trang)
- `GET /api/Ranking/achievements/my` - Thành tựu của tôi
- `GET /api/Ranking/online-count` - Tổng số người đang online

#### WebSocket
- `ws://[server]/ws/game` - Kết nối WebSocket cho chế độ đối kháng real-time
  - Sử dụng query string `?access_token=YOUR_JWT_TOKEN` để xác thực

**Authentication:**
- Tất cả API (trừ login, register) yêu cầu JWT Token trong header:
  - `Authorization: Bearer {token}`

### C. Database Schema
- User table: Lưu thông tin người dùng
- Quiz table: Lưu thông tin quiz
- Result table: Lưu kết quả quiz
- History table: Lưu lịch sử làm bài

---

**Tài liệu được tạo bởi:** [Tên nhóm]  
**Ngày hoàn thành:** [Ngày/Tháng/Năm]

