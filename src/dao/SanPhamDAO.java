package dao;

import model.SanPham;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {

    // Lấy tất cả sản phẩm
    public List<SanPham> getAll() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT maSanPham, tenSanPham, donGia, moTa FROM SanPham ORDER BY maSanPham DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSanPham(rs.getInt("maSanPham"));
                sp.setTenSanPham(rs.getString("tenSanPham"));
                sp.setDonGia(rs.getBigDecimal("donGia"));
                sp.setMoTa(rs.getString("moTa"));
                list.add(sp);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Thêm sản phẩm (trả về id mới hoặc -1 nếu thất bại)
    public int insert(SanPham sp) {
        String sql = "INSERT INTO SanPham(tenSanPham, donGia, moTa) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, sp.getTenSanPham());
            ps.setBigDecimal(2, sp.getDonGia());
            ps.setString(3, sp.getMoTa());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    // Cập nhật sản phẩm
    public boolean update(SanPham sp) {
        String sql = "UPDATE SanPham SET tenSanPham=?, donGia=?, moTa=? WHERE maSanPham=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, sp.getTenSanPham());
            ps.setBigDecimal(2, sp.getDonGia());
            ps.setString(3, sp.getMoTa());
            ps.setInt(4, sp.getMaSanPham());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Xóa sản phẩm
    public boolean delete(int maSanPham) {
        String sql = "DELETE FROM SanPham WHERE maSanPham=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maSanPham);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Tìm: nếu key là số -> tìm theo mã, còn không -> tìm theo tên (LIKE)
    public List<SanPham> search(String key) {
        List<SanPham> result = new ArrayList<>();
        boolean isNumber = key.matches("\\d+");

        String sqlById = "SELECT maSanPham, tenSanPham, donGia, moTa FROM SanPham WHERE maSanPham = ?";
        String sqlByName = "SELECT maSanPham, tenSanPham, donGia, moTa FROM SanPham WHERE tenSanPham LIKE ?";

        try (Connection con = DBConnection.getConnection()) {
            if (isNumber) {
                try (PreparedStatement ps = con.prepareStatement(sqlById)) {
                    ps.setInt(1, Integer.parseInt(key));
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) result.add(map(rs));
                    }
                }
                // Ngoài ra vẫn tìm theo tên chứa key (ví dụ user nhập '12' muốn tìm tên chứa 12)
                try (PreparedStatement ps2 = con.prepareStatement(sqlByName)) {
                    ps2.setString(1, "%" + key + "%");
                    try (ResultSet rs2 = ps2.executeQuery()) {
                        while (rs2.next()) result.add(map(rs2));
                    }
                }
            } else {
                try (PreparedStatement ps = con.prepareStatement(sqlByName)) {
                    ps.setString(1, "%" + key + "%");
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) result.add(map(rs));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    // Map ResultSet -> SanPham
    private SanPham map(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham();
        sp.setMaSanPham(rs.getInt("maSanPham"));
        sp.setTenSanPham(rs.getString("tenSanPham"));
        sp.setDonGia(rs.getBigDecimal("donGia"));
        sp.setMoTa(rs.getString("moTa"));
        return sp;
    }
}
