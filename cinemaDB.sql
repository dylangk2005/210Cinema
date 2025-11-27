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

-- 4. Phim
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


-- 5. Phòng chiếu
CREATE TABLE PhongChieu (
    maPhongChieu INT IDENTITY(1,1) PRIMARY KEY,
    tenPhongChieu NVARCHAR(50),
    soGheNgoi INT,
    trangThaiPhong NVARCHAR(30),
    loaiManHinh NVARCHAR(50),
    heThongAmThanh NVARCHAR(50)
);
GO

-- 6. Ghế ngồi
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


-- 7. Suất chiếu
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

-- 8. Sản phẩm
CREATE TABLE SanPham (
    maSanPham INT IDENTITY(1,1) PRIMARY KEY,
    tenSanPham NVARCHAR(100),
    donGia DECIMAL(10,2),
    moTa NVARCHAR(255),

);
GO

-- 9. Khách hàng
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


-- 10. Đơn hàng
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

-- 11. Vé 
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

-- 12. Chi tiết đơn hàng
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

-- 13. Thanh toán
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
        ON DELETE SET NULL,
	FOREIGN KEY (maDonHang)
		REFERENCES DonHang(maDonHang)
		ON DELETE CASCADE
);
GO

-- Tạo index cho các bảng để truy vấn
CREATE INDEX IX_NhanVien_hoTenNV ON NhanVien(hoTenNhanVien);
CREATE INDEX IX_NhanVien_SDT ON NhanVien(soDienThoai);

CREATE INDEX IX_Phim_TenPhim ON Phim(tenPhim);
CREATE INDEX IX_Phim_TheLoai ON Phim(theLoai);

CREATE INDEX IX_PhongChieu_TenPhong ON PhongChieu(tenPhongChieu);
CREATE INDEX IX_PhongChieu_TrangThai ON PhongChieu(trangThaiPhong);

CREATE INDEX IX_Ghe_maPhongChieu ON GheNgoi(maPhongChieu);

CREATE INDEX IX_SuatChieu_maPhim ON SuatChieu(maPhim);
CREATE INDEX IX_SuatChieu_maPhongChieu ON SuatChieu(maPhongChieu);
CREATE INDEX IX_SuatChieu_ngayGioChieu ON SuatChieu(ngayGioChieu);

CREATE INDEX IX_SanPham_tenSanPham ON SanPham(tenSanPham);

CREATE INDEX IX_KhachHang_TenKH ON KhachHang(hoTenKhachHang);
CREATE INDEX IX_KhachHang_hoTenKH ON KhachHang(soDienThoai);

CREATE INDEX IX_DonHang_TGTao ON DonHang(thoiGianTao);
CREATE INDEX IX_DonHang_TrangThai ON DonHang(trangThaiDonHang);
CREATE INDEX IX_DonHang_maNV ON DonHang(maNhanVien);

CREATE INDEX IX_Ve_maSuatChieu ON Ve(maSuatChieu);
CREATE INDEX IX_Ve_trangThaiVe On VE(trangThai);

CREATE INDEX IX_CTDonHang_maDonHang ON ChiTietDonHang(maDonHang);
CREATE INDEX IX_CTDonHang_maVe ON ChiTietDonHang(maVe);
CREATE INDEX IX_CTDonHang_maSanPham ON ChiTietDonHang(maSanPham);

-- ================== Thêm dữ liệu mẫu vào bảng   ===========================
INSERT INTO ChucVu (tenChucVu, moTaQuyen) VALUES
(N'Nhân viên bán vé', N'Bán hàng, đăng ký thẻ khách hàng và cộng điểm cho khách'),
(N'Quản lý', N'Toàn quyền: nhân viên, quản lý rạp phim, báo cáo doanh thu');

INSERT INTO NhanVien (hoTenNhanVien, ngaySinh, gioiTinh, soDienThoai, maChucVu) VALUES
(N'Trương Tuấn Tú', '1999-06-03', N'Nam', '0945678901', 2),
(N'Trần Minh Đức', '2001-09-11', N'Nam', '0912345678', 1);

INSERT INTO TaiKhoan (tenDangNhap, matKhau, email, maNhanVien) VALUES
('tu_ql', '1234567', 'tusena36@gmail.com', 1),
('duc_nv', '1234567', 'duc@gmail.com', 2);

INSERT INTO Phim (tenPhim, thoiLuong, theLoai, gioiHanTuoi, ngayKhoiChieu, moTa) VALUES
(N'Avatar 3: The Seed Bearer', 197, N'Khoa học viễn tưởng, Hành động',  N'P', '2025-12-03',  N'Phần tiếp theo của Avatar'),
(N'Godzilla x Kong', 115, N'Hành động, phiêu lưu',  N'C13', '2025-12-03', N'Quái vật đại chiến'),
(N'Doraemon: Nobita''s Earth Symphony', 115, N'Hoạt hình, Gia đình',  N'P', '2025-12-03', N'Hoạt hình Nhật Bản'),
(N'Deadpool 3', 127, N'Hành động, Hài hước, Siêu anh hùng',  N'C18', '2025-12-03', N'Siêu anh hùng hài hước');

INSERT INTO PhongChieu (tenPhongChieu, soGheNgoi, trangThaiPhong, loaiManHinh, heThongAmThanh) VALUES
(N'Phòng 1', 120, N'Hoạt động', N'3D', N'Dolby Atmos'),
(N'Phòng 2',120, N'Hoạt động', N'2D', N'Dolby 7.1'),
(N'Phòng 3',120, N'Bảo trì', N'IMAX', N'Dolby Atmos');

-- Sinh 120 ghế cho 3 phòng
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
        WHILE @cot <= 12
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
(1, 1, '2025-12-03 09:45:00', 150000), -- Avatar 3
(2, 2, '2025-12-03 09:45:00', 100000), -- Godzilla
(3, 2, '2025-12-03 12:15:00', 70000), -- Doraemon
(2, 1, '2025-12-03 14:45:00', 100000), -- Godzilla
(1, 2, '2025-12-03 14:45:00', 150000), -- Avatar 3
(3, 1, '2025-12-03 17:15:00', 70000),  -- Doraemon
(4, 1, '2025-12-03 19:45:00', 110000), -- Deadpool 3
(4, 2, '2025-12-03 19:45:00', 110000), -- Deadpool 3
(1, 1, '2025-12-03 22:15:00', 150000), -- Avatar 3
(2, 2, '2025-12-03 22:15:00', 100000); -- Godzilla

INSERT INTO SanPham (tenSanPham, donGia, moTa) VALUES
(N'Bắp rang bơ lớn', 65000, N'Bắp rang bơ size L'),
(N'Bắp rang Caramel', 70000, N'Bắp rang caramel ngọt'),
(N'Pepsi tươi', 25000, N'Nước ngọt có gas'),
(N'Nước suối 500ml', 15000, N'Nước suối tinh khiết');

INSERT INTO KhachHang (hoTenKhachHang, ngaySinh, gioiTinh, soDienThoai, email, hangThanhVien, diemTichLuy, ngayDangKy) VALUES
(N'Vũ Minh Châu', '2000-04-12', N'Nữ', '0123456789', 'chau@gmail.com', N'Vàng', 1500, '2020-01-01'),
(N'Đỗ Hoàng Duy', '1999-08-25', N'Nam', '0987654321', 'duy@gmail.com', N'Bạc', 800, '2023-04-12'),
(N'Nguyễn Lan Hương', '2001-12-01', N'Nữ', '0135792468', 'huong@gmail.com', N'Kim cương', 3000, '2017-03-18');

-- ========================================
--  TRIGGER TỰ ĐỘNG TÍNH TỔNG
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

