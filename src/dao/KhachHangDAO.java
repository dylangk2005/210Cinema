package dao;

import model.KhachHang;
import util.DBConnection;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // ---------------------- LẤY TẤT CẢ KHÁCH HÀNG ----------------------
    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ---------------------- CẬP NHẬT KHÁCH HÀNG ----------------------
    public boolean update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET "
                + "HoTenKH=?, NgaySinh=?, GioiTinh=?, SoDienThoai=?, Email=?, "
                + "HangThanhVien=?, DiemTichLuy=?, NgayDangKy=? "
                + "WHERE MaKH=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getHoTenKhachHang());
            ps.setDate(2, toSqlDate(kh.getNgaySinh()));
            ps.setString(3, kh.getGioiTinh());
            ps.setString(4, kh.getSoDienThoai());
            ps.setString(5, kh.getEmail());
            ps.setString(6, kh.getHangThanhVien());
            ps.setInt(7, kh.getDiemTichLuy());
            ps.setDate(8, toSqlDate(kh.getNgayDangKy()));
            ps.setInt(9, kh.getMaKhachHang());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ---------------------- XÓA KHÁCH HÀNG ----------------------
    public boolean delete(int maKH) {
        String sql = "DELETE FROM KhachHang WHERE MaKH=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maKH);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------------------- TÌM THEO SĐT HOẶC MÃ ----------------------
    public KhachHang search(String key) {
        String sql;

        boolean isNumber = key.matches("\\d+");

        if (isNumber) {
            sql = "SELECT * FROM KhachHang WHERE MaKH=? OR SoDienThoai LIKE ?";
        } else {
            sql = "SELECT * FROM KhachHang WHERE SoDienThoai LIKE ?";
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (isNumber) {
                ps.setInt(1, Integer.parseInt(key));
                ps.setString(2, "%" + key + "%");
            } else {
                ps.setString(1, "%" + key + "%");
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // ---------------------- MAP RESULTSET -> KHÁCH HÀNG ----------------------
    private KhachHang map(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();

        kh.setMaKhachHang(rs.getInt("MaKH"));
        kh.setHoTenKhachHang(rs.getString("HoTenKH"));
        kh.setNgaySinh(rs.getDate("NgaySinh"));
        kh.setGioiTinh(rs.getString("GioiTinh"));
        kh.setSoDienThoai(rs.getString("SoDienThoai"));
        kh.setEmail(rs.getString("Email"));
        kh.setHangThanhVien(rs.getString("HangThanhVien"));
        kh.setDiemTichLuy(rs.getInt("DiemTichLuy"));
        kh.setNgayDangKy(rs.getDate("NgayDangKy"));

        return kh;
    }

    // ---------------------- CHUYỂN STRING -> SQL DATE ----------------------
    private java.sql.Date toSqlDate(java.util.Date date) {
        if (date == null) return null;
        return new java.sql.Date(date.getTime());
    }

    // ---------------------- HỖ TRỢ PARSE STRING (nếu cần) ----------------------
    private java.util.Date parseDate(String s) {
        try {
            return sdf.parse(s);
        } catch (Exception ex) {
            return null;
        }
    }
}
