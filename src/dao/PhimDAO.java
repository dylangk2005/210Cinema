package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Phim;
import util.DBConnection;

public class PhimDAO {
    public List<Phim> getAllPhim() {
        List<Phim> list = new ArrayList<>();
        String sql = "SELECT * FROM Phim";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    Phim p = new Phim(
                        rs.getInt("maPhim"),
                        rs.getString("tenPhim"),
                        rs.getInt("thoiLuong"),
                        rs.getString("theLoai"),
                        rs.getString("gioiHanTuoi"),
                        rs.getDate("ngayKhoiChieu"),
                        rs.getString("moTa")
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
