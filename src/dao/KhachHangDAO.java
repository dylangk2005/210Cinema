package dao;

import model.KhachHang;
import util.DBConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class KhachHangDAO {
    public void updateDiemTichLuy(int maKhachHang, int diemTichLuy) throws SQLException {
        String sql = "UPDATE KhachHang SET diemTichLuy = ? WHERE maKhachHang = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, diemTichLuy);
            ps.setInt(2, maKhachHang);
            ps.executeUpdate();
        }
    }
    
    public int insertKhachHangAndReturnId(KhachHang kh) throws Exception {
        String sql = "INSERT INTO KhachHang(hoTenKhachHang, ngaySinh, gioiTinh, soDienThoai, email, hangThanhVien, diemTichLuy) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, kh.getHoTenKhachHang());
            ps.setObject(2, kh.getNgaySinh());
            ps.setString(3, kh.getGioiTinh());
            ps.setString(4, kh.getSoDienThoai());
            ps.setString(5, kh.getEmail());
            ps.setString(6, kh.getHangThanhVien());
            ps.setInt(7, kh.getDiemTichLuy());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }
     
    // ---------------- LẤY THÔNG TIN KHÁCH HÀNG ------------------
    public KhachHang getKhachHangBySDT(String soDienThoai) {
        String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, soDienThoai);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    KhachHang kh = new KhachHang(
                        rs.getInt("maKhachHang"),
                        rs.getString("hoTenKhachHang"),
                        rs.getDate("ngaySinh"),
                        rs.getString("gioiTinh"),
                        rs.getString("soDienThoai"),
                        rs.getString("email"),
                        rs.getString("hangThanhVien"),
                        rs.getInt("diemTichLuy")
                    ); 
                    return kh;
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}