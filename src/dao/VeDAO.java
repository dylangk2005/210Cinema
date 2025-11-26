package dao;

import java.sql.Statement;
import java.sql.ResultSet;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Set;
import util.DBConnection;

public class VeDAO {

    public Set<Integer> insertVe(int maDonHang, int maSuatChieu, 
            Set<Integer> listMaGhe, BigDecimal giaVe, String trangThai) throws Exception {
        String sql = "INSERT INTO Ve(maSuatChieu, maGheNgoi, maDonHang, giaVe, trangThai) "
                   + "VALUES (?, ?, ?, ?, ?)";
        Set<Integer> listMaVe = new HashSet<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (Integer maGhe : listMaGhe) {
                ps.setInt(1, maSuatChieu);
                ps.setInt(2, maGhe);
                ps.setInt(3, maDonHang);
                ps.setBigDecimal(4, giaVe);
                ps.setString(5, trangThai);
                ps.executeUpdate();
            
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    while (rs.next()) {
                        listMaVe.add(rs.getInt(1));
                    }
                }
            }
        }
        return listMaVe;
    }
    
}
