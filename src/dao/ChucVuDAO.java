package dao;

import model.ChucVu;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ChucVuDAO {
    public ChucVu getByIDChucVu(int maChucVu){
        String sql = "SELECT * FROM ChucVu cv WHERE cv.maChucVu = ?";
        try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, maChucVu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new ChucVu(
                  rs.getInt("maChucVu"),
                  rs.getString("tenChucVu"),
                  rs.getString("moTaQuyen")
                );
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
