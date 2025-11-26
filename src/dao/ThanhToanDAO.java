package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import model.ThanhToan;
import util.DBConnection;


public class ThanhToanDAO {

    public void insertThanhToan(ThanhToan tt) throws Exception {
        String sql = "INSERT INTO ThanhToan(maDonHang, soTien, maNhanVien, phuongThucThanhToan) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tt.getMaDonHang());
            ps.setBigDecimal(2, tt.getSoTien());
            ps.setInt(3, tt.getMaNhanVien());
            ps.setString(4, tt.getPhuongThucThanhToan());
            ps.executeUpdate();
        }
    }
    
}

