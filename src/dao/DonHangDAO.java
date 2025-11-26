package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import util.DBConnection;
import model.DonHang;

public class DonHangDAO {
    
    public DonHang getDonHangByID(int maDH) {
        String sql = "SELECT * FROM DonHang WHERE maDonHang = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
                ps.setInt(1, maDH);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    DonHang dh = new DonHang (
                        rs.getInt("maNhanVien"),
                        rs.getInt("maKhachHang"),
                        rs.getBigDecimal("tongTienPhaiTra"), 
                        rs.getString("trangThaiDonHang")
                    );
                    return dh;
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int insertDonHang(DonHang dh) throws Exception {
        String sql = "INSERT INTO DonHang(maNhanVien, maKhachHang, trangThaiDonHang) "
                   + "VALUES (?, ?, ?)";

        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, dh.getMaNhanVien());
            ps.setObject(2, dh.getMaKhachHang() <= 0 ? null : dh.getMaKhachHang());
            ps.setString(3, dh.getTrangThaiDonHang());
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        }
        return -1;
    }
    
}
