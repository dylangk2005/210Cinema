package dao;

import model.KhachHang;
import util.DBConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    // ---------------------- LẤY KHÁCH HÀNG THEO MÃ  ----------------------
     public KhachHang getById(int maKH) {
        String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
     
    // ---------------------- LẤY TẤT CẢ KHÁCH HÀNG ----------------------
    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ---------------------- CẬP NHẬT KHÁCH HÀNG ----------------------
    public boolean update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET "
                + "hoTenKhachHang=?, ngaySinh=?, gioiTinh=?, soDienThoai=?, email=?, "
                + "hangThanhVien=?, diemTichLuy=?, ngayDangKy=? "
                + "WHERE maKhachHang=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getHoTenKhachHang());
            ps.setDate(2, toSqlDate(kh.getNgaySinh()));
            ps.setString(3, kh.getGioiTinh());
            ps.setString(4, kh.getSoDienThoai());
            ps.setString(5, kh.getEmail());
            ps.setString(6, kh.getHangThanhVien());
            ps.setInt(7, kh.getDiemTichLuy());
            ps.setDate(8, toSqlDate(kh.getNgayDangKy()));
            ps.setInt(9, kh.getMaKhachHang());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ---------------------- XÓA KHÁCH HÀNG ----------------------
    public boolean delete(int maKH) {
        String sql = "DELETE FROM KhachHang WHERE maKhachHang=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maKH);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------------------- TÌM THEO MÃ, Họ tên, SĐT ----------------------
    public List<KhachHang> search(String key, int tieuChi) {
        List<KhachHang> ds = new ArrayList<>();
        if (key == null || key.trim().isEmpty()){
            return getAll();
        }
        
        key = key.trim();
        String sql = "SELECT * FROM KhachHang WHERE ";
        
        try{
            switch(tieuChi){
            case 0 -> { // maKH
                    KhachHang kh = getById(Integer.parseInt(key));
                    if (kh != null) ds.add(kh);
                }
            case 1 -> { // tenKH
                sql += "hoTenKhachHang LIKE ?";
                    try (Connection con = DBConnection.getConnection();
                         PreparedStatement ps = con.prepareStatement(sql)) {
                         ps.setString(1, "%" + key + "%");
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) ds.add(map(rs));
                        }
                    }
                }
            case 2 ->{ // Sdt
                    sql += "soDienThoai LIKE ?";
                    try (Connection con = DBConnection.getConnection();
                         PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setString(1, "%" + key + "%"); // Tìm có chứa từ khóa
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) ds.add(map(rs));
                        }
                    }
                }
            }
        }
        catch (NumberFormatException e){
            
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return ds;
    }


    // ---------------------- MAP RESULTSET -> KHÁCH HÀNG ----------------------
    private KhachHang map(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();

        kh.setMaKhachHang(rs.getInt("maKhachHang"));
        kh.setHoTenKhachHang(rs.getString("hoTenKhachHang"));
        kh.setNgaySinh(rs.getDate("ngaySinh"));
        kh.setGioiTinh(rs.getString("gioiTinh"));
        kh.setSoDienThoai(rs.getString("soDienThoai"));
        kh.setEmail(rs.getString("email"));
        kh.setHangThanhVien(rs.getString("hangThanhVien"));
        kh.setDiemTichLuy(rs.getInt("diemTichLuy"));
        kh.setNgayDangKy(rs.getDate("ngayDangKy"));

        return kh;
    }

    // ---------------------- CHUYỂN STRING -> SQL DATE ----------------------
    private java.sql.Date toSqlDate(java.util.Date date) {
        if (date == null) return null;
        return new java.sql.Date(date.getTime());
    }

    // ---------------------- HỖ TRỢ PARSE STRING (nếu cần) ----------------------
    private java.util.Date parseDate(String s) {
        try {
            return sdf.parse(s);
        } catch (Exception ex) {
            return null;
        }
    }
}
