package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.PhongChieu;
import util.DBConnection;

public class PhongChieuDAO {
    public List<PhongChieu> getAllPhongChieu() {
        List<PhongChieu> list = new ArrayList<>();
        String sql = "SELECT * FROM PhongChieu";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    PhongChieu p = new PhongChieu(
                        rs.getInt("maPhongChieu"),
                        rs.getString("tenPhongChieu"),
                        rs.getInt("soGheNgoi"),
                        rs.getString("trangThaiPhong"),
                        rs.getString("loaiManHinh"),
                        rs.getString("heThongAmThanh")
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
