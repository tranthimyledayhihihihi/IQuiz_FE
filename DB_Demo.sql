
IF DB_ID('QuizApp') IS NOT NULL
    DROP DATABASE QuizApp;
GO

CREATE DATABASE QuizApp;
GO

USE QuizApp;
GO

/* ============================================================
   1. BẢNG NỀN: CÂU HỎI & ĐÁP ÁN
   ============================================================ */

CREATE TABLE CauHoi (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    NoiDung NVARCHAR(500) NOT NULL,
    ChuDe NVARCHAR(100) DEFAULT N'Chung',
    DoKho INT DEFAULT 1 CHECK (DoKho BETWEEN 1 AND 5),
    TaoLuc DATETIME2 DEFAULT SYSDATETIME(),
    CapNhatLuc DATETIME2 DEFAULT SYSDATETIME()
);
GO

CREATE TABLE DapAn (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    CauHoiId INT NOT NULL,
    NoiDung NVARCHAR(300) NOT NULL,
    Dung BIT NOT NULL DEFAULT 0,
    ThuTu INT DEFAULT 0,
    FOREIGN KEY (CauHoiId) REFERENCES CauHoi(Id) ON DELETE CASCADE
);
GO

CREATE INDEX IX_DapAn_CauHoi ON DapAn(CauHoiId);
GO

/* ============================================================
   2. MODULE 1: QUIZ NGÀY (QuizNgay)
   ============================================================ */

CREATE TABLE QuizNgay (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    NgayKey DATE NOT NULL,
    Seed INT NOT NULL,
    Diem INT DEFAULT 0,
    BatDauLuc DATETIME2 DEFAULT SYSDATETIME(),
    KetThucLuc DATETIME2 NULL
);
GO

CREATE UNIQUE INDEX UQ_QuizNgay_NgayKey ON QuizNgay(NgayKey);
GO

CREATE TABLE QuizNgay_ChiTiet (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    QuizNgayId INT NOT NULL,
    CauHoiId INT NOT NULL,
    DapAnChonId INT NULL,
    Dung BIT NOT NULL DEFAULT 0,
    ThoiGianMs INT DEFAULT 0,
    TaoLuc DATETIME2 DEFAULT SYSDATETIME(),
    FOREIGN KEY (QuizNgayId) REFERENCES QuizNgay(Id) ON DELETE CASCADE,
    FOREIGN KEY (CauHoiId) REFERENCES CauHoi(Id) ON DELETE CASCADE,
    FOREIGN KEY (DapAnChonId) REFERENCES DapAn(Id)
);
GO

/* ============================================================
   3. MODULE 2: CÂU SAI (CauSai)
   ============================================================ */

CREATE TABLE CauSai (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    CauHoiId INT NOT NULL,
    DapAnDungId INT NOT NULL,
    DapAnDaChonId INT NULL,
    Nguon NVARCHAR(50) DEFAULT N'Daily',
    TaoLuc DATETIME2 DEFAULT SYSDATETIME(),
    FOREIGN KEY (CauHoiId) REFERENCES CauHoi(Id) ON DELETE CASCADE,
    FOREIGN KEY (DapAnDungId) REFERENCES DapAn(Id),
    FOREIGN KEY (DapAnDaChonId) REFERENCES DapAn(Id)
);
GO

CREATE INDEX IX_CauSai_CauHoi ON CauSai(CauHoiId);
CREATE INDEX IX_CauSai_TaoLuc ON CauSai(TaoLuc DESC);
GO

-- View chi tiết cho ôn tập
CREATE VIEW v_CauSai_ChiTiet AS
SELECT
    cs.Id AS CauSaiId,
    cs.TaoLuc,
    ch.NoiDung AS CauHoi,
    dad.NoiDung AS DapAnDung,
    dac.NoiDung AS DapAnDaChon,
    cs.Nguon
FROM CauSai cs
JOIN CauHoi ch ON ch.Id = cs.CauHoiId
JOIN DapAn dad ON dad.Id = cs.DapAnDungId
LEFT JOIN DapAn dac ON dac.Id = cs.DapAnDaChonId;
GO

/* ============================================================
   4. MODULE 3: QUIZ TÙY CHỈNH (QuizTuyChinh)
   ============================================================ */

CREATE TABLE QuizTuyChinh (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Ten NVARCHAR(200) NOT NULL,
    MoTa NVARCHAR(500) DEFAULT N'',
    Tags NVARCHAR(200) DEFAULT N'',
    TaoLuc DATETIME2 DEFAULT SYSDATETIME()
);
GO

CREATE TABLE QuizTuyChinh_CauHoi (
    QuizId INT NOT NULL,
    CauHoiId INT NOT NULL,
    ThuTu INT DEFAULT 0,
    PRIMARY KEY (QuizId, CauHoiId),
    FOREIGN KEY (QuizId) REFERENCES QuizTuyChinh(Id) ON DELETE CASCADE,
    FOREIGN KEY (CauHoiId) REFERENCES CauHoi(Id) ON DELETE CASCADE
);
GO

/* ============================================================
   5. MODULE 4: QUIZ CHIA SẺ (QuizChiaSe)
   ============================================================ */

CREATE TABLE QuizChiaSe (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    TieuDe NVARCHAR(200) NOT NULL,
    MoTa NVARCHAR(500) DEFAULT N'',
    OwnerName NVARCHAR(100) DEFAULT N'Khách',
    LinkCode NVARCHAR(20) NOT NULL,
    CongKhai BIT DEFAULT 1,
    TaoLuc DATETIME2 DEFAULT SYSDATETIME()
);
GO

CREATE UNIQUE INDEX UQ_QuizChiaSe_LinkCode ON QuizChiaSe(LinkCode);
GO

CREATE TABLE QuizChiaSe_CauHoi (
    QuizChiaSeId INT NOT NULL,
    CauHoiId INT NOT NULL,
    ThuTu INT DEFAULT 0,
    PRIMARY KEY (QuizChiaSeId, CauHoiId),
    FOREIGN KEY (QuizChiaSeId) REFERENCES QuizChiaSe(Id) ON DELETE CASCADE,
    FOREIGN KEY (CauHoiId) REFERENCES CauHoi(Id) ON DELETE CASCADE
);
GO

/* ============================================================
   6. DỮ LIỆU MẪU
   ============================================================ */

INSERT INTO CauHoi (NoiDung, ChuDe, DoKho)
VALUES
(N'2 + 2 = ?', N'Toán', 1),
(N'Thủ đô Việt Nam là?', N'Địa lý', 1),
(N'Năm nhuận có bao nhiêu ngày?', N'Lịch', 1);
GO

INSERT INTO DapAn (CauHoiId, NoiDung, Dung, ThuTu)
VALUES
(1, N'3', 0, 0),
(1, N'4', 1, 1),
(1, N'5', 0, 2),
(1, N'6', 0, 3),

(2, N'Huế', 0, 0),
(2, N'Đà Nẵng', 0, 1),
(2, N'Hà Nội', 1, 2),
(2, N'Hải Phòng', 0, 3),

(3, N'365', 0, 0),
(3, N'366', 1, 1),
(3, N'367', 0, 2),
(3, N'364', 0, 3);
GO

-- Seed QuizNgày mẫu
INSERT INTO QuizNgay (NgayKey, Seed, Diem) VALUES (GETDATE(), ABS(CHECKSUM(NEWID())) % 10000, 0);
GO

-- Bộ Quiz tùy chỉnh mẫu
INSERT INTO QuizTuyChinh (Ten, MoTa, Tags)
VALUES (N'Ôn Toán Cơ Bản', N'Bộ tự tạo gồm các phép tính đơn giản', N'toán,cơ bản');
GO
INSERT INTO QuizTuyChinh_CauHoi (QuizId, CauHoiId, ThuTu)
VALUES (1,1,1), (1,2,2);
GO

-- Bộ Quiz chia sẻ mẫu
INSERT INTO QuizChiaSe (TieuDe, MoTa, OwnerName, LinkCode)
VALUES (N'Quiz Số Học', N'Chia sẻ phép tính đơn giản', N'Tiến', N'X7A3F');
GO
INSERT INTO QuizChiaSe_CauHoi (QuizChiaSeId, CauHoiId, ThuTu)
VALUES (1,1,1), (1,3,2);
GO

/* ============================================================
   7. TRUY VẤN GỢI Ý (TEST)
   ============================================================ */

-- QuizNgày: lấy 1 câu ngẫu nhiên
SELECT TOP 1 * FROM CauHoi ORDER BY NEWID();

-- QuizNgày: lấy đáp án theo câu hỏi
SELECT * FROM DapAn WHERE CauHoiId = 1 ORDER BY ThuTu;

-- QuizNgày: ghi câu sai
INSERT INTO CauSai (CauHoiId, DapAnDungId, DapAnDaChonId, Nguon)
SELECT 1,
       (SELECT TOP 1 Id FROM DapAn WHERE CauHoiId = 1 AND Dung = 1),
       (SELECT TOP 1 Id FROM DapAn WHERE CauHoiId = 1 AND Dung = 0),
       N'Daily';
GO

-- Xem lịch sử câu sai
SELECT * FROM v_CauSai_ChiTiet;
GO
