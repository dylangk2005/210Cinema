package dao;

import model.NhanVien;
import util.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    // Lấy thông tin nhân viên bằng mã nhân viên
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
    
    // Lấy tất cả nhân viên
    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien ORDER BY maNhanVien";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getInt("maNhanVien"),
                    rs.getString("hoTenNhanVien"),
                    rs.getDate("ngaySinh"),
                    rs.getString("gioiTinh"),
                    rs.getString("soDienThoai"),
                    rs.getInt("maChucVu")
                );
                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm nhân viên
    public boolean add(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (hoTenNhanVien, ngaySinh, gioiTinh, soDienThoai, maChucVu) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getHoTenNhanVien());
            ps.setDate(2, (Date) nv.getNgaySinh());
            ps.setString(3, nv.getGioiTinh());
            ps.setString(4, nv.getSoDienThoai());
            ps.setInt(5, nv.getMaChucVu());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sửa nhân viên
    public boolean update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET hoTenNhanVien=?, ngaySinh=?, gioiTinh=?, soDienThoai=?, maChucVu=? WHERE maNhanVien=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getHoTenNhanVien());
            ps.setDate(2, nv.getNgaySinh());
            ps.setString(3, nv.getGioiTinh());
            ps.setString(4, nv.getSoDienThoai());
            ps.setInt(5, nv.getMaChucVu());
            ps.setInt(6, nv.getMaNhanVien());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa nhân viên
    public boolean delete(int maNV) {
        String sql = "DELETE FROM NhanVien WHERE maNhanVien = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

       // Tìm kiếm nâng cao theo tiêu chí
    public List<NhanVien> timKiemNangCao(String keyword, int tieuChi) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "";
        
        // tieuChi: 0 = Mã NV, 1 = Họ tên, 2 = SĐT
        switch (tieuChi) {
            case 0: // Mã nhân viên (chính xác hoặc chứa)
                sql = "SELECT * FROM NhanVien WHERE CAST(maNhanVien AS CHAR) LIKE ?";
                break;
            case 1: // Họ tên (không phân biệt hoa thường)
                sql = "SELECT * FROM NhanVien WHERE LOWER(hoTenNhanVien) LIKE LOWER(?)";
                break;
            case 2: // Số điện thoại
                sql = "SELECT * FROM NhanVien WHERE soDienThoai LIKE ?";
                break;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getInt("maNhanVien"),
                    rs.getString("hoTenNhanVien"),
                    rs.getDate("ngaySinh"),
                    rs.getString("gioiTinh"),
                    rs.getString("soDienThoai"),
                    rs.getInt("maChucVu")
                );
                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
