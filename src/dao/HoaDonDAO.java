package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import util.DBConnection;
import model.HoaDon;

public class HoaDonDAO {
    
    public HoaDon getHoaDonByID(int maDH) {
        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
                ps.setInt(1, maDH);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    HoaDon dh = new HoaDon (
                        rs.getInt("maNhanVien"),
                        rs.getInt("maKhachHang"),
                        rs.getBigDecimal("tongTienPhaiTra"), 
                        rs.getString("phuongThucThanhToan")
                    );
                    return dh;
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int insertHoaDon(HoaDon dh) throws Exception {
        String sql = "INSERT INTO HoaDon(maNhanVien, maKhachHang, phuongThucThanhToan) "
                   + "VALUES (?, ?, ?)";

        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, dh.getMaNhanVien());
            ps.setObject(2, dh.getMaKhachHang() <= 0 ? null : dh.getMaKhachHang());
            ps.setString(3, dh.getPhuongThucThanhToan());
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        }
        return -1;
    }
    
}
