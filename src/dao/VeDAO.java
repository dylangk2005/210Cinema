package dao;

import java.sql.Statement;
import java.sql.ResultSet;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import util.DBConnection;

public class VeDAO {

    public Set<Integer> insertVe(int maSuatChieu, 
            Set<Integer> listMaGhe) throws Exception {
        String sql = "INSERT INTO Ve(maSuatChieu, maGheNgoi)"
                   + "VALUES (?, ?)";
        Set<Integer> listMaVe = new HashSet<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (Integer maGhe : listMaGhe) {
                ps.setInt(1, maSuatChieu);
                ps.setInt(2, maGhe);
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
    
     // kiểm tra xem suất chiếu đã có ng mua vé chưa
    public boolean hasTicketsSold(int maSuatChieu) {
        String sql = "SELECT COUNT(*) FROM Ve WHERE maSuatChieu = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }
}
