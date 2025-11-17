IF DB_ID('CinemaDB') IS NOT NULL
    DROP DATABASE CinemaDB;
GO

CREATE LOGIN cinema_user with password = '1234567';
GO

CREATE DATABASE CinemaDB;
GO

USE CinemaDB;
CREATE USER cinema_user FOR LOGIN cinema_user;
GO

EXEC sp_addrolemember 'db_datareader', 'cinema_user';
EXEC sp_addrolemember 'db_datawriter', 'cinema_user';

-- 1. Chức vụ
CREATE TABLE ChucVu (
    maChucVu INT IDENTITY(1,1) PRIMARY KEY,
    tenChucVu NVARCHAR(50),
    moTaQuyen NVARCHAR(255)
);
GO

-- 2. Nhân viên
CREATE TABLE NhanVien (
    maNhanVien INT IDENTITY(1,1) PRIMARY KEY,
    hoTenNhanVien NVARCHAR(100),
    ngaySinh DATE,
    gioiTinh NVARCHAR(10),
    soDienThoai NVARCHAR(15),
    maChucVu INT NULL,
    FOREIGN KEY (maChucVu)
        REFERENCES ChucVu(maChucVu)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);
GO

-- 3. Tài khoản
CREATE TABLE TaiKhoan (
    tenDangNhap NVARCHAR(50) PRIMARY KEY,
    matKhau NVARCHAR(100),
    email NVARCHAR(100),
    maNhanVien INT UNIQUE,
    FOREIGN KEY (maNhanVien)
        REFERENCES NhanVien(maNhanVien)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
GO

-- 4. Thanh toán
CREATE TABLE ThanhToan (
    maThanhToan INT IDENTITY(1,1) PRIMARY KEY,
    maDonHang INT NULL,
    soTien DECIMAL(10,2),
    thoiGianThanhToan DATETIME DEFAULT GETDATE(),
    maNhanVien INT NULL,
    phuongThucThanhToan NVARCHAR(50),
    FOREIGN KEY (maNhanVien)
        REFERENCES NhanVien(maNhanVien)
        ON UPDATE CASCADE
        ON DELETE SET NULL

);
GO

-- 5. Khách hàng
CREATE TABLE KhachHang (
    maKhachHang INT IDENTITY(1,1) PRIMARY KEY,
    hoTenKhachHang NVARCHAR(100),
    ngaySinh DATE,
    gioiTinh NVARCHAR(10),
    soDienThoai NVARCHAR(15),
    email NVARCHAR(100),
    hangThanhVien NVARCHAR(30),
    diemTichLuy INT DEFAULT 0,
    ngayDangKy DATETIME DEFAULT GETDATE()
);
GO


-- 6. Đơn hàng
CREATE TABLE DonHang (
    maDonHang INT IDENTITY(1,1) PRIMARY KEY,
    maNhanVien INT NULL,
	maKhachHang INT NULL,
    thoiGianTao DATETIME DEFAULT GETDATE(),
    tongTienPhaiTra DECIMAL(10,2),
    trangThaiDonHang NVARCHAR(50),
    FOREIGN KEY (maNhanVien)
        REFERENCES NhanVien(maNhanVien)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
	 FOREIGN KEY (maKhachHang)
        REFERENCES KhachHang(maKhachHang)
        ON UPDATE CASCADE
		ON DELETE SET NULL
);
GO

-- 7. Phim
CREATE TABLE Phim (
    maPhim INT IDENTITY(1,1) PRIMARY KEY,
    tenPhim NVARCHAR(100),
    thoiLuong INT,
    theLoai NVARCHAR(100),
    gioiHanTuoi NVARCHAR(20),
    ngayKhoiChieu DATE,
    moTa NVARCHAR(255)
);
GO

-- 8. Phòng chiếu
CREATE TABLE PhongChieu (
    maPhongChieu INT IDENTITY(1,1) PRIMARY KEY,
    tenPhongChieu NVARCHAR(50),
    soGheNgoi INT,
    trangThaiPhong NVARCHAR(30),
    loaiManHinh NVARCHAR(50),
    heThongAmThanh NVARCHAR(50)
);
GO

-- 9. Ghế ngồi
CREATE TABLE GheNgoi (
    maGheNgoi INT IDENTITY(1,1) PRIMARY KEY,
    maPhongChieu INT,
    hangGhe NVARCHAR(10),
    soGhe NVARCHAR(10),
    FOREIGN KEY (maPhongChieu)
        REFERENCES PhongChieu(maPhongChieu)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
GO

-- 10. Suất chiếu
CREATE TABLE SuatChieu (
    maSuatChieu INT IDENTITY(1,1) PRIMARY KEY,
    maPhim INT,
    maPhongChieu INT,
    ngayGioChieu DATETIME,
    giaVeCoBan DECIMAL(10,2),
    FOREIGN KEY (maPhim)
        REFERENCES Phim(maPhim)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (maPhongChieu)
        REFERENCES PhongChieu(maPhongChieu)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
GO

-- 11. Vé (đã sửa để tránh multiple cascade paths)
CREATE TABLE Ve (
    maVe INT IDENTITY(1,1) PRIMARY KEY,
    maSuatChieu INT,
    maGheNgoi INT,
    maDonHang INT NULL,
    giaVe DECIMAL(10,2),
    trangThai NVARCHAR(30),
    FOREIGN KEY (maSuatChieu)
        REFERENCES SuatChieu(maSuatChieu)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (maGheNgoi)
        REFERENCES GheNgoi(maGheNgoi)
		ON DELETE NO ACTION,
    FOREIGN KEY (maDonHang)
        REFERENCES DonHang(maDonHang)
		ON DELETE CASCADE
);
GO


-- 12. Sản phẩm
CREATE TABLE SanPham (
    maSanPham INT IDENTITY(1,1) PRIMARY KEY,
    tenSanPham NVARCHAR(100),
    donGia DECIMAL(10,2),
    moTa NVARCHAR(255),

);
GO

-- 13. Chi tiết đơn hàng
CREATE TABLE ChiTietDonHang (
    maCTDH INT IDENTITY(1,1) PRIMARY KEY,
    maDonHang INT,
    maSanPham INT NULL,
    maVe INT NULL,
    soLuong INT,
    donGiaLucBan DECIMAL(10,2),
    thanhTien DECIMAL(10,2),
    FOREIGN KEY (maDonHang)
        REFERENCES DonHang(maDonHang)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (maSanPham)
        REFERENCES SanPham(maSanPham)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    FOREIGN KEY (maVe)
        REFERENCES Ve(maVe)
);
GO

ALTER TABLE ThanhToan
ADD CONSTRAINT FK_ThanhToan_DonHang
FOREIGN KEY (maDonHang)
REFERENCES DonHang(maDonHang) ON DELETE CASCADE;
GO




-- ================== Thêm dữ liệu mẫu vào bảng   ===========================
INSERT INTO ChucVu (tenChucVu, moTaQuyen) VALUES
(N'Nhân viên bán vé', N'Bán hàng, đăng ký thẻ khách hàng và cộng điểm cho khách'),
(N'Quản lý', N'Toàn quyền: nhân viên, quản lý rạp phim, báo cáo doanh thu');

INSERT INTO NhanVien (hoTenNhanVien, ngaySinh, gioiTinh, soDienThoai, maChucVu) VALUES
(N'Trương Tuấn Tú', '1999-06-03', N'Nam', '0945678901', 1),
(N'Trần Minh Đức', '2001-07-22', N'Nam', '0912345678', 2);


INSERT INTO TaiKhoan (tenDangNhap, matKhau, email, maNhanVien) VALUES
('tu_ql', 'admin123', 'tusena36@gmail.com', 1),
('duc_nv', '123456', 'duc@gmail.com', 2);


INSERT INTO KhachHang (hoTenKhachHang, ngaySinh, gioiTinh, soDienThoai, email, hangThanhVien, diemTichLuy) VALUES
(N'Vũ Minh Châu', '2000-04-12', N'Nữ', '0851234567', 'chau@gmail.com', N'Vàng', 1500),
(N'Đỗ Hoàng Duy', '1999-08-25', N'Nam', '0862345678', 'duy@gmail.com', N'Bạc', 800),
(N'Nguyễn Lan Hương', '2001-12-01', N'Nữ', '0873456789', 'huong@gmail.com', N'Kim cương', 3000);

INSERT INTO Phim (tenPhim, thoiLuong, gioiHanTuoi, ngayKhoiChieu, moTa, theLoai) VALUES
(N'Avatar 3: The Seed Bearer', 180, N'P', '2025-12-19',  N'Phần tiếp theo của Avatar', N'Khoa học viễn tưởng, Hành động'),
(N'Godzilla x Kong', 140, N'C13', '2025-11-15', N'Quái vật đại chiến', N'Hành động, Phiêu lưu'),
(N'Doraemon: Nobita''s Earth Symphony', 100, N'P', '2025-11-08', N'Hoạt hình Nhật Bản', N'Hoạt hình, Gia đình'),
(N'Deadpool 3', 130, N'C18', '2025-11-22', N'Siêu anh hùng hài hước', N'Hành động, Hài hước, Siêu anh hùng');

INSERT INTO PhongChieu (tenPhongChieu, soGheNgoi, trangThaiPhong, loaiManHinh, heThongAmThanh) VALUES
(N'Phòng 1', 120, N'Hoạt động', N'3D', N'Dolby Atmos'),
(N'Phòng 2',120, N'Hoạt động', N'2D', N'Dolby 7.1'),
(N'Phòng 3',120, N'Bảo trì', N'IMAX', N'Dolby Atmos');


DECLARE @maPhong INT;
DECLARE @hang CHAR(1);
DECLARE @cot INT;
DECLARE @i INT = 1;

-- Lặp qua 3 phòng
WHILE @i <= 3
BEGIN
    SET @maPhong = @i;

    -- Tạo 10 hàng (A → J) x 12 cột = 120 ghế
    SET @hang = 'A';
    WHILE @hang <= 'J'
    BEGIN
        SET @cot = 1;
        WHILE @cot <= 10
        BEGIN
            INSERT INTO GheNgoi (maPhongChieu, hangGhe, soGhe)
            VALUES (@maPhong, @hang, CAST(@cot AS NVARCHAR(2)));

            SET @cot = @cot + 1;
        END
        SET @hang = CHAR(ASCII(@hang) + 1); -- A → B → C...
    END

    SET @i = @i + 1;
END
GO

INSERT INTO SuatChieu (maPhim, maPhongChieu, ngayGioChieu, giaVeCoBan) VALUES
(1, 1, '2025-11-15 14:00:00', 120000), -- Avatar 3
(2, 3, '2025-11-15 19:00:00', 150000), -- Godzilla
(3, 2, '2025-11-16 10:00:00', 70000),  -- Doraemon
(1, 1, '2025-11-16 17:00:00', 120000), -- Avatar 3
(4, 2, '2025-11-16 09:00:00', 110000); -- Deadpool 3

INSERT INTO SanPham (tenSanPham, donGia, moTa) VALUES
(N'Bắp rang bơ lớn', 65000, N'Bắp rang bơ size L'),
(N'Bắp rang caramel', 70000, N'Bắp rang caramel ngọt'),
(N'Nước ngọt Coca 500ml', 25000, N'Nước ngọt có gas'),
(N'Nước suối', 15000, N'Nước suối tinh khiết');


-- ========================================
-- 1. TRIGGER TỰ ĐỘNG TÍNH TỔNG
-- ========================================
IF EXISTS (SELECT * FROM sys.triggers WHERE name = 'trg_UpdateTongTienDonHang')
    DROP TRIGGER trg_UpdateTongTienDonHang;
GO

CREATE TRIGGER trg_UpdateTongTienDonHang
ON ChiTietDonHang
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE DonHang
    SET tongTienPhaiTra = ISNULL((
        SELECT SUM(thanhTien)
        FROM ChiTietDonHang
        WHERE maDonHang = DonHang.maDonHang
    ), 0)
    WHERE maDonHang IN (
        SELECT DISTINCT maDonHang FROM inserted
        UNION
        SELECT DISTINCT maDonHang FROM deleted
    );
END;
GO

-- ========================================
-- 2. INSERT DonHang (KHÔNG CẦN ĐIỀN tongTienPhaiTra → trigger tự động)
-- ========================================
INSERT INTO DonHang (maNhanVien, maKhachHang, trangThaiDonHang) VALUES
(1, 1, N'Đã thanh toán'),
(2, 3, N'Đã thanh toán'),
(1, NULL, N'Đã thanh toán');
GO

-- ========================================
-- 3. INSERT Ve
-- ========================================
INSERT INTO Ve (maSuatChieu, maGheNgoi, maDonHang, giaVe, trangThai) VALUES
(1, 1, 1, 120000, N'Đã đặt'),
(1, 2, 1, 120000, N'Đã đặt'),
(2, 121, 2, 85000, N'Đã đặt'),
(2, 122, 3, 85000, N'Đã đặt');
GO

-- ========================================
-- 4. INSERT ChiTietDonHang → trigger tự động cập nhật tổng
-- ========================================
INSERT INTO ChiTietDonHang (maDonHang, maSanPham, maVe, soLuong, donGiaLucBan, thanhTien) VALUES
-- ĐƠN 1: 2 vé + 1 bắp = 305,000
(1, NULL, 1, 1, 120000, 120000),
(1, NULL, 2, 1, 120000, 120000),
(1, 1, NULL, 1, 65000, 65000),

-- ĐƠN 2: 1 vé + 1 Coca = 110,000
(2, NULL, 3, 1, 85000, 85000),
(2, 3, NULL, 1, 25000, 25000),

-- ĐƠN 3: 1 vé = 85,000
(3, NULL, 4, 1, 85000, 85000);

-- ========================================
-- 5. INSERT ThanhToan
-- ========================================
INSERT INTO ThanhToan (maDonHang, soTien, maNhanVien, phuongThucThanhToan, thoiGianThanhToan)
VALUES
(1, 305000, 1, 'Tiền mặt', '2025-11-15 12:00:00'),
(2, 110000, 2, N'Chuyển khoản', '2025-11-15 12:15:00'),  -- Đơn 2: NV2, chuyển khoản
(3,  85000, 1, N'Thẻ tín dụng','2025-11-15 12:20:00');  -- Đơn 3: NV1, thẻ
GO

