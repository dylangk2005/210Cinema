package dao;

import model.ThongKe;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThongKeDAO {

    // LẤY DOANH THU THEO THỜI GIAN - HIỂN THỊ CẢ NGÀY/THÁNG/NĂM KHÔNG CÓ DOANH THU = 0
public List<ThongKe> getDoanhThuTheoThoiGian(int loai, Date tu, Date den) {
    List<ThongKe> list = new ArrayList<>();
    String sql = "";
    if (loai == 0) { // Theo ngày
        sql = """
            ;WITH NgaySeries AS (
                SELECT CAST(? AS DATE) AS Ngay
                UNION ALL
                SELECT DATEADD(DAY, 1, Ngay)
                FROM NgaySeries
                WHERE Ngay < ?
            )
            SELECT 
                CONVERT(VARCHAR(10), n.Ngay, 103) AS ThoiGian,
                ISNULL(COUNT(DISTINCT dh.maDonHang), 0) AS SoHD,
                ISNULL(SUM(ct.thanhTien), 0) AS DoanhThu
            FROM NgaySeries n
            LEFT JOIN DonHang dh ON CAST(dh.thoiGianTao AS DATE) = n.Ngay
                AND dh.trangThaiDonHang = N'Đã thanh toán'
            LEFT JOIN ChiTietDonHang ct ON dh.maDonHang = ct.maDonHang
            GROUP BY n.Ngay
            ORDER BY n.Ngay
            OPTION (MAXRECURSION 0)
            """;
    }
    else if (loai == 1) { // Theo tháng
        sql = """
            ;WITH ThangSeries AS (
                SELECT DATEFROMPARTS(YEAR(?), MONTH(?), 1) AS Thang
                UNION ALL
                SELECT DATEADD(MONTH, 1, Thang)
                FROM ThangSeries
                WHERE Thang < DATEFROMPARTS(YEAR(?), MONTH(?), 1)
            )
            SELECT 
                FORMAT(t.Thang, 'MM/yyyy') AS ThoiGian,
                ISNULL(COUNT(DISTINCT dh.maDonHang), 0) AS SoHD,
                ISNULL(SUM(ct.thanhTien), 0) AS DoanhThu
            FROM ThangSeries t
            LEFT JOIN DonHang dh ON FORMAT(dh.thoiGianTao, 'yyyy-MM') = FORMAT(t.Thang, 'yyyy-MM')
                AND dh.trangThaiDonHang = N'Đã thanh toán'
            LEFT JOIN ChiTietDonHang ct ON dh.maDonHang = ct.maDonHang
            GROUP BY t.Thang
            ORDER BY t.Thang
            OPTION (MAXRECURSION 0)
            """;
    }
    else { // Theo năm
        sql = """
            ;WITH NamSeries AS (
                SELECT YEAR(?) AS Nam
                UNION ALL
                SELECT Nam + 1
                FROM NamSeries
                WHERE Nam < YEAR(?)
            )
            SELECT 
                CAST(n.Nam AS VARCHAR(4)) AS ThoiGian,
                ISNULL(COUNT(DISTINCT dh.maDonHang), 0) AS SoHD,
                ISNULL(SUM(ct.thanhTien), 0) AS DoanhThu
            FROM NamSeries n
            LEFT JOIN DonHang dh ON YEAR(dh.thoiGianTao) = n.Nam
                AND dh.trangThaiDonHang = N'Đã thanh toán'
            LEFT JOIN ChiTietDonHang ct ON dh.maDonHang = ct.maDonHang
            GROUP BY n.Nam
            ORDER BY n.Nam
            OPTION (MAXRECURSION 0)
            """;
    }

    try (Connection c = DBConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        if (loai == 0) { // Ngày
            ps.setDate(1, tu);
            ps.setDate(2, den);
        }
        else if (loai == 1) { // Tháng
            ps.setDate(1, tu);
            ps.setDate(2, tu);
            ps.setDate(3, den);
            ps.setDate(4, den);
        }
        else { // Năm
            ps.setDate(1, tu);
            ps.setDate(2, den);
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new ThongKe(
                rs.getString("ThoiGian"),
                rs.getInt("SoHD"),
                rs.getLong("DoanhThu")
            ));
        }
    } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // TOP PHIM - HIỂN THỊ TẤT CẢ PHIM (kể cả chưa bán được vé)
    public List<ThongKe> getTopPhim() {
        List<ThongKe> list = new ArrayList<>();
        String sql = """
            SELECT 
                p.tenPhim,
                ISNULL(COUNT(v.maVe), 0) AS SoVe,
                ISNULL(SUM(ct.thanhTien), 0) AS DoanhThu
            FROM Phim p
            LEFT JOIN SuatChieu sc ON p.maPhim = sc.maPhim
            LEFT JOIN Ve v ON sc.maSuatChieu = v.maSuatChieu AND v.trangThai = N'Đã đặt'
            LEFT JOIN ChiTietDonHang ct ON v.maVe = ct.maVe
            GROUP BY p.maPhim, p.tenPhim
            ORDER BY DoanhThu DESC, p.tenPhim
            """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new ThongKe(
                    rs.getString(1),
                    rs.getInt(2),
                    rs.getLong(3)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // DOANH THU NHÂN VIÊN - HIỂN THỊ TẤT CẢ NHÂN VIÊN (kể cả chưa bán được đơn nào)
    public List<ThongKe> getDoanhThuNhanVien() {
        List<ThongKe> list = new ArrayList<>();
        String sql = """
            SELECT 
                nv.hoTenNhanVien,
                ISNULL(COUNT(DISTINCT dh.maDonHang), 0) AS SoHD,
                ISNULL(SUM(ct.thanhTien), 0) AS DoanhThu
            FROM NhanVien nv
            LEFT JOIN DonHang dh ON nv.maNhanVien = dh.maNhanVien 
                AND dh.trangThaiDonHang = N'Đã thanh toán'
            LEFT JOIN ChiTietDonHang ct ON dh.maDonHang = ct.maDonHang
            GROUP BY nv.maNhanVien, nv.hoTenNhanVien
            ORDER BY DoanhThu DESC, nv.hoTenNhanVien
            """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new ThongKe(
                    rs.getString(1),
                    rs.getInt(2),
                    rs.getLong(3)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // TOP SẢN PHẨM - HIỂN THỊ TẤT CẢ SẢN PHẨM (kể cả chưa bán được cái nào)
    public List<ThongKe> getSanPhamBanChay() {
        List<ThongKe> list = new ArrayList<>();
        String sql = """
            SELECT 
                sp.tenSanPham,
                ISNULL(SUM(ct.soLuong), 0) AS SoLuong,
                ISNULL(SUM(ct.thanhTien), 0) AS DoanhThu
            FROM SanPham sp
            LEFT JOIN ChiTietDonHang ct ON sp.maSanPham = ct.maSanPham
            GROUP BY sp.maSanPham, sp.tenSanPham
            ORDER BY SoLuong DESC, DoanhThu DESC, sp.tenSanPham
            """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new ThongKe(
                    rs.getString(1),
                    rs.getInt(2),
                    rs.getLong(3)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}