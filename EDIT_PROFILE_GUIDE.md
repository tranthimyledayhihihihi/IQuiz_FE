# Hướng Dẫn Sử Dụng Trang Chỉnh Sửa Profile

## Tổng Quan
Trang chỉnh sửa profile cho phép người dùng cập nhật thông tin cá nhân bao gồm:
- Họ tên
- Email
- Ảnh đại diện

## Các File Đã Tạo

### 1. Layout
- `activity_edit_profile.xml` - Layout chính cho trang chỉnh sửa
- `activity_test_edit_profile.xml` - Layout test

### 2. Activity
- `EditProfileActivity.java` - Activity chính xử lý logic chỉnh sửa profile
- `TestEditProfileActivity.java` - Activity test

### 3. Drawable Resources
- `bg_btn_outline.xml` - Background cho nút outline
- `bg_btn_primary.xml` - Background cho nút primary
- `ic_save.xml` - Icon lưu

## Cách Sử Dụng

### 1. Từ ProfileActivity
Người dùng nhấn vào nút edit (biểu tượng bút) ở góc phải trên cùng của ProfileActivity để mở trang chỉnh sửa.

### 2. Chỉnh Sửa Thông Tin
- **Họ tên**: Bắt buộc nhập
- **Email**: Bắt buộc nhập và phải đúng định dạng email
- **Ảnh đại diện**: Nhấn vào nút edit trên avatar để chọn ảnh từ thư viện

### 3. Lưu Thay Đổi
- Nhấn nút "Lưu Thay Đổi" hoặc icon save ở header
- Hệ thống sẽ hiển thị dialog xác nhận
- Sau khi lưu thành công, tự động quay về ProfileActivity và reload dữ liệu

### 4. Hủy Thay Đổi
- Nhấn nút "Hủy" hoặc nút back
- Nếu có thay đổi chưa lưu, hệ thống sẽ hỏi xác nhận

## Tính Năng Đặc Biệt

### 1. Validation
- Kiểm tra định dạng email
- Kiểm tra các field bắt buộc

### 2. Image Picker
- Chọn ảnh từ thư viện điện thoại
- Preview ảnh ngay lập tức
- Hỗ trợ các định dạng ảnh phổ biến

### 3. API Integration
- Gọi API `updateMyProfile()` để cập nhật thông tin
- Xử lý lỗi và hiển thị thông báo phù hợp

### 4. UX/UI
- Material Design components
- Gradient header đẹp mắt
- Loading states và feedback
- Responsive design

## Test

Để test trang chỉnh sửa profile:

1. Thêm `TestEditProfileActivity` vào menu debug hoặc launcher
2. Chạy app và mở TestEditProfileActivity
3. Nhấn "Mở Trang Chỉnh Sửa Profile"

## Lưu Ý Kỹ Thuật

### Dependencies
- Material Design Components (đã có)
- Glide cho load ảnh (đã có)
- Retrofit cho API calls (đã có)

### Permissions
- `READ_EXTERNAL_STORAGE` - để đọc ảnh từ thư viện
- `READ_MEDIA_IMAGES` - cho Android 13+

### API Endpoints Sử Dụng
- `GET /api/user/profile/me` - Lấy thông tin profile
- `PUT /api/user/profile/me` - Cập nhật profile

## Tương Lai

Có thể mở rộng thêm:
- Upload ảnh lên server thay vì lưu URI local
- Crop ảnh trước khi upload
- Thêm validation phức tạp hơn
- Hỗ trợ dark mode
- Animation transitions
- Trang đổi mật khẩu riêng biệt