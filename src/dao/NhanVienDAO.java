package dao;

import model.NhanVien;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NhanVienDAO {

    public NhanVien getByID(int maNV){
        String sql = " SELECT * FROM NhanVien nv WHERE nv.maNhanVien = ? ";
        try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, maNV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                NhanVien nhanVien = new NhanVien(rs.getInt("maNhanVien"), rs.getString("hoTenNhanVien"),
                        rs.getDate("ngaySinh"), rs.getString("gioiTinh"), rs.getString("soDienThoai"), rs.getInt("maChucVu"));
                return nhanVien;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
