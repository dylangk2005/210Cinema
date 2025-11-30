package dao;

import model.PhongChieu;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhongChieuDAO {

    // Lấy tất cả phòng chiếu
    public List<PhongChieu> getAllPhongChieu() {
        List<PhongChieu> list = new ArrayList<>();
        String sql = "SELECT * FROM PhongChieu";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy tất cả phòng chiếu đang hoạt động
     public List<PhongChieu> getAllPhongChieuHD() {
        List<PhongChieu> list = new ArrayList<>();
        String sql = "SELECT * FROM PhongChieu WHERE trangThaiPhong = N'Hoạt động'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
        
    // Lấy phòng theo ID
    public PhongChieu getById(int maPhong) {
        String sql = "SELECT * FROM PhongChieu WHERE maPhongChieu = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // thêm phòng mới + tự động sinh ghế
    public boolean insert(PhongChieu pc) {
        String sql = "INSERT INTO PhongChieu (tenPhongChieu, soGheNgoi, trangThaiPhong, loaiManHinh, heThongAmThanh) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pc.getTenPhongChieu());
            ps.setInt(2, pc.getSoGheNgoi());
            ps.setString(3, pc.getTrangThaiPhong());
            ps.setString(4, pc.getLoaiManHinh());
            ps.setString(5, pc.getHeThongAmThanh());

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1);
                        new GheNgoiDAO().autoChairsGeneration(newId, pc.getSoGheNgoi());
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //  update phòng (k cho sửa nếu phòng đã có suất chiếu) + reset ghế mới
    public boolean update(PhongChieu pc) {
        int maPhong = pc.getMaPhongChieu();
      
        if (new SuatChieuDAO().hasShowTimes(maPhong)) {
            return false;
        }

        String sql = "UPDATE PhongChieu SET tenPhongChieu=?, soGheNgoi=?, trangThaiPhong=?, loaiManHinh=?, heThongAmThanh=? " +
                     "WHERE maPhongChieu=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pc.getTenPhongChieu());
            ps.setInt(2, pc.getSoGheNgoi());
            ps.setString(3, pc.getTrangThaiPhong());
            ps.setString(4, pc.getLoaiManHinh());
            ps.setString(5, pc.getHeThongAmThanh());
            ps.setInt(6, maPhong);

            if (ps.executeUpdate() > 0) {

                new GheNgoiDAO().xoaGheTheoPhong(maPhong);
                new GheNgoiDAO().autoChairsGeneration(maPhong, pc.getSoGheNgoi());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // xóa phòng , k cho xóa nếu có suất chiếu
    public boolean delete(int maPhong) {
        // KIỂM TRA CÓ SUẤT CHIẾU KHÔNG
        if (new SuatChieuDAO().hasShowTimes(maPhong)) {
            return false;
        }

        // Xóa ghế trước
        new GheNgoiDAO().xoaGheTheoPhong(maPhong);

        String sql = "DELETE FROM PhongChieu WHERE maPhongChieu = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Kiểm tra trùng sản phẩm
     public boolean kiemTraTrungPhongChieu(String tenPC, int maPC) {
        String sql = "SELECT COUNT(*) FROM PhongChieu WHERE tenPhongChieu = ? AND maPhongChieu != ?";
        try (var conn = DBConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenPC);
            ps.setInt(2, maPC);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
     
    // Map ResultSet → PhongChieu
    private PhongChieu mapRow(ResultSet rs) throws SQLException {
        return new PhongChieu(
            rs.getInt("maPhongChieu"),
            rs.getString("tenPhongChieu"),
            rs.getInt("soGheNgoi"),
            rs.getString("trangThaiPhong"),
            rs.getString("loaiManHinh"),
            rs.getString("heThongAmThanh")
        );
    }
}