
USE quiz_game;
GO

DECLARE @sql NVARCHAR(MAX) = N'';

-- Tạo script để xóa tất cả FK
SELECT @sql += 'ALTER TABLE [' + OBJECT_NAME(parent_object_id) + '] DROP CONSTRAINT [' + name + '];'
FROM sys.foreign_keys;

EXEC sp_executesql @sql;
GO

-- =============================================
-- 🗑️ BƯỚC 2: XÓA TOÀN BỘ BẢNG (THEO THỨ TỰ TỰ ĐỘNG)
-- =============================================
DECLARE @sql2 NVARCHAR(MAX) = N'';

SELECT @sql2 += 'DROP TABLE [' + name + '];'
FROM sys.objects
WHERE type = 'U';

EXEC sp_executesql @sql2;
GO

-- =============================================
-- 🏗️ BƯỚC 3: TẠO LẠI TOÀN BỘ DATABASE quiz_game
-- =============================================

-- Nếu database chưa có thì tạo
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'quiz_game')
BEGIN
    CREATE DATABASE quiz_game;
END
GO
USE quiz_game;
GO

-- =============================================
-- 1️⃣ HỆ THỐNG TÀI KHOẢN & NGƯỜI DÙNG
-- =============================================

IF OBJECT_ID('NguoiDung', 'U') IS NOT NULL DROP TABLE NguoiDung;
CREATE TABLE NguoiDung (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    TenDangNhap NVARCHAR(50) NOT NULL UNIQUE,
    MatKhau NVARCHAR(255) NOT NULL,
    Email NVARCHAR(100) UNIQUE,
    HoTen NVARCHAR(100),
    AnhDaiDien NVARCHAR(255),
    NgayDangKy DATETIME DEFAULT GETDATE(),
    LanDangNhapCuoi DATETIME,
    TrangThai BIT DEFAULT 1
);
GO

IF OBJECT_ID('CaiDatNguoiDung', 'U') IS NOT NULL DROP TABLE CaiDatNguoiDung;
CREATE TABLE CaiDatNguoiDung (
    SettingID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    AmThanh BIT DEFAULT 1,
    NhacNen BIT DEFAULT 1,
    ThongBao BIT DEFAULT 1,
    NgonNgu NVARCHAR(20) DEFAULT N'vi',
    FOREIGN KEY (UserID) REFERENCES NguoiDung(UserID)
);
GO

IF OBJECT_ID('PhienDangNhap', 'U') IS NOT NULL DROP TABLE PhienDangNhap;
CREATE TABLE PhienDangNhap (
    SessionID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    Token NVARCHAR(500),
    ThoiGianDangNhap DATETIME DEFAULT GETDATE(),
    ThoiGianHetHan DATETIME,
    TrangThai BIT DEFAULT 1,
    FOREIGN KEY (UserID) REFERENCES NguoiDung(UserID)
);
GO

-- =============================================
-- 2️⃣ HỆ THỐNG CÂU HỎI & CHƠI QUIZ
-- =============================================

IF OBJECT_ID('ChuDe', 'U') IS NOT NULL DROP TABLE ChuDe;
CREATE TABLE ChuDe (
    ChuDeID INT IDENTITY(1,1) PRIMARY KEY,
    TenChuDe NVARCHAR(100) NOT NULL,
    MoTa NVARCHAR(255),
    TrangThai BIT DEFAULT 1
);
GO

IF OBJECT_ID('DoKho', 'U') IS NOT NULL DROP TABLE DoKho;
CREATE TABLE DoKho (
    DoKhoID INT IDENTITY(1,1) PRIMARY KEY,
    TenDoKho NVARCHAR(50) NOT NULL,
    DiemThuong INT DEFAULT 0
);
GO

IF OBJECT_ID('CauHoi', 'U') IS NOT NULL DROP TABLE CauHoi;
CREATE TABLE CauHoi (
    CauHoiID INT IDENTITY(1,1) PRIMARY KEY,
    ChuDeID INT NOT NULL,
    DoKhoID INT NOT NULL,
    NoiDung NVARCHAR(500) NOT NULL,
    DapAnA NVARCHAR(255),
    DapAnB NVARCHAR(255),
    DapAnC NVARCHAR(255),
    DapAnD NVARCHAR(255),
    DapAnDung NVARCHAR(10),
    FOREIGN KEY (ChuDeID) REFERENCES ChuDe(ChuDeID),
    FOREIGN KEY (DoKhoID) REFERENCES DoKho(DoKhoID)
);
GO

IF OBJECT_ID('TroGiup', 'U') IS NOT NULL DROP TABLE TroGiup;
CREATE TABLE TroGiup (
    TroGiupID INT IDENTITY(1,1) PRIMARY KEY,
    TenTroGiup NVARCHAR(50),
    MoTa NVARCHAR(255)
);
GO

-- =============================================
-- 3️⃣ KẾT QUẢ & PHẦN THƯỞNG
-- =============================================

IF OBJECT_ID('KetQua', 'U') IS NOT NULL DROP TABLE KetQua;
CREATE TABLE KetQua (
    KetQuaID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    Diem INT,
    SoCauDung INT,
    TongCauHoi INT,
    KetQua NVARCHAR(10),
    ThoiGian DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES NguoiDung(UserID)
);
GO

IF OBJECT_ID('ChuoiNgay', 'U') IS NOT NULL DROP TABLE ChuoiNgay;
CREATE TABLE ChuoiNgay (
    ChuoiID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    SoNgayLienTiep INT DEFAULT 0,
    NgayCapNhatCuoi DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES NguoiDung(UserID)
);
GO

IF OBJECT_ID('ThuongNgay', 'U') IS NOT NULL DROP TABLE ThuongNgay;
CREATE TABLE ThuongNgay (
    ThuongID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    NgayNhan DATE DEFAULT GETDATE(),
    PhanThuong NVARCHAR(100),
    DiemThuong INT DEFAULT 0,
    TrangThaiNhan BIT DEFAULT 0,
    FOREIGN KEY (UserID) REFERENCES NguoiDung(UserID)
);
GO

IF OBJECT_ID('ThanhTuu', 'U') IS NOT NULL DROP TABLE ThanhTuu;
CREATE TABLE ThanhTuu (
    ThanhTuuID INT IDENTITY(1,1) PRIMARY KEY,
    TenThanhTuu NVARCHAR(100),
    MoTa NVARCHAR(255),
    BieuTuong NVARCHAR(255),
    DieuKien NVARCHAR(255)
);
GO

IF OBJECT_ID('ThongKeNguoiDung', 'U') IS NOT NULL DROP TABLE ThongKeNguoiDung;
CREATE TABLE ThongKeNguoiDung (
    ThongKeID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    Ngay DATE DEFAULT GETDATE(),
    SoTran INT DEFAULT 0,
    SoCauDung INT DEFAULT 0,
    DiemTrungBinh FLOAT DEFAULT 0,
    FOREIGN KEY (UserID) REFERENCES NguoiDung(UserID)
);
GO

-- =============================================
-- 4️⃣ CHƠI ONLINE & MẠNG XÃ HỘI
-- =============================================

IF OBJECT_ID('PhongChoi', 'U') IS NOT NULL DROP TABLE PhongChoi;
CREATE TABLE PhongChoi (
    PhongID INT IDENTITY(1,1) PRIMARY KEY,
    TenPhong NVARCHAR(100),
    MaPhong NVARCHAR(20) UNIQUE,
    TrangThai NVARCHAR(20) DEFAULT N'Đang chờ',
    ThoiGianTao DATETIME DEFAULT GETDATE()
);
GO

IF OBJECT_ID('TranDau', 'U') IS NOT NULL DROP TABLE TranDau;
CREATE TABLE TranDau (
    TranDauID INT IDENTITY(1,1) PRIMARY KEY,
    PhongID INT NOT NULL,
    User1ID INT NOT NULL,
    User2ID INT NOT NULL,
    DiemUser1 INT,
    DiemUser2 INT,
    KetQua NVARCHAR(10),
    ThoiGian DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (PhongID) REFERENCES PhongChoi(PhongID),
    FOREIGN KEY (User1ID) REFERENCES NguoiDung(UserID),
    FOREIGN KEY (User2ID) REFERENCES NguoiDung(UserID)
);
GO

IF OBJECT_ID('BanBe', 'U') IS NOT NULL DROP TABLE BanBe;
CREATE TABLE BanBe (
    BanBeID INT IDENTITY(1,1) PRIMARY KEY,
    UserID1 INT NOT NULL,
    UserID2 INT NOT NULL,
    TrangThai NVARCHAR(20) DEFAULT N'Chờ xác nhận',
    NgayKetBan DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID1) REFERENCES NguoiDung(UserID),
    FOREIGN KEY (UserID2) REFERENCES NguoiDung(UserID)
);
GO

IF OBJECT_ID('BXH', 'U') IS NOT NULL DROP TABLE BXH;
CREATE TABLE BXH (
    BXHID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    DiemTuan INT DEFAULT 0,
    DiemThang INT DEFAULT 0,
    HangTuan INT,
    HangThang INT,
    FOREIGN KEY (UserID) REFERENCES NguoiDung(UserID)
);
GO

IF OBJECT_ID('NguoiDungOnline', 'U') IS NOT NULL DROP TABLE NguoiDungOnline;
CREATE TABLE NguoiDungOnline (
    OnlineID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    TrangThai NVARCHAR(20) DEFAULT N'Online',
    ThoiGianCapNhat DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES NguoiDung(UserID)
);
GO

-- =============================================
-- 5️⃣ CHẾ ĐỘ ĐẶC BIỆT & TÙY CHỈNH
-- =============================================

IF OBJECT_ID('QuizNgay', 'U') IS NOT NULL DROP TABLE QuizNgay;
CREATE TABLE QuizNgay (
    QuizNgayID INT IDENTITY(1,1) PRIMARY KEY,
    Ngay DATE DEFAULT GETDATE(),
    CauHoiID INT,
    FOREIGN KEY (CauHoiID) REFERENCES CauHoi(CauHoiID)
);
GO

IF OBJECT_ID('CauSai', 'U') IS NOT NULL DROP TABLE CauSai;
CREATE TABLE CauSai (
    CauSaiID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    CauHoiID INT NOT NULL,
    NgaySai DATE DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES NguoiDung(UserID),
    FOREIGN KEY (CauHoiID) REFERENCES CauHoi(CauHoiID)
);
GO

IF OBJECT_ID('QuizTuyChinh', 'U') IS NOT NULL DROP TABLE QuizTuyChinh;
CREATE TABLE QuizTuyChinh (
    QuizTuyChinhID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    TenQuiz NVARCHAR(100),
    MoTa NVARCHAR(255),
    NgayTao DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES NguoiDung(UserID)
);
GO

IF OBJECT_ID('QuizChiaSe', 'U') IS NOT NULL DROP TABLE QuizChiaSe;
CREATE TABLE QuizChiaSe (
    QuizChiaSeID INT IDENTITY(1,1) PRIMARY KEY,
    QuizTuyChinhID INT NOT NULL,
    UserGuiID INT NOT NULL,
    UserNhanID INT,
    NgayChiaSe DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (QuizTuyChinhID) REFERENCES QuizTuyChinh(QuizTuyChinhID),
    FOREIGN KEY (UserGuiID) REFERENCES NguoiDung(UserID),
    FOREIGN KEY (UserNhanID) REFERENCES NguoiDung(UserID)
);
GO