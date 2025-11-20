package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.SuatChieu;
import util.DBConnection;

public class SuatChieuDAO {
    public List<SuatChieu> getSuatChieuByPhimAndPhong(int maPhim, int maPhongChieu) {
        List<SuatChieu> list = new ArrayList<>();
        String sql = "SELECT * FROM SuatChieu WHERE maPhim = ? AND maPhongChieu = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, maPhim);
                ps.setInt(2, maPhongChieu);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    SuatChieu p = new SuatChieu(
                        rs.getInt("maSuatChieu"),
                        rs.getInt("maPhim"),
                        rs.getInt("maPhongChieu"),
                        rs.getTimestamp("ngayGioChieu"),
                        rs.getBigDecimal("giaVeCoBan")
                    );
                    list.add(p);
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
