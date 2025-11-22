package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.KhachHang;
import util.DBConnection;

public class KhachHangDAO {
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
                        rs.getInt("diemTichLuy"),
                        rs.getDate("ngayDangKy")
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
