package dao;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.TaiKhoan;


public class TaiKhoanDAO {
    public TaiKhoan checkLogin(String username, String password) {
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                
                // lấy đc username => check mật khẩu trong java (sql k phân biệt hoa thường)
                if (rs.next()){
                    String passwordDB = rs.getString("matKhau");
                    if (passwordDB.equals(password)){
                        TaiKhoan tk = new TaiKhoan();
                        tk.setTenDangNhap(rs.getString("tenDangNhap"));
                        tk.setMatKhau(rs.getString("matKhau"));
                        tk.setMaNhanVien(rs.getInt("maNhanVien"));
                        return tk;
                    }
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
