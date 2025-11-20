package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.SanPham;
import util.DBConnection;

public class SanPhamDAO {
    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    SanPham sp = new SanPham (
                        rs.getInt("maSanPham"),
                        rs.getString("tenSanPham"),
                        rs.getBigDecimal("donGia"), 
                        rs.getString("moTa")
                    );
                    list.add(sp);
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
