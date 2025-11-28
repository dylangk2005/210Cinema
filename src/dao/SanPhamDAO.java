package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.SanPham;
import util.DBConnection;

import model.SanPham;
import util.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {
    
    // ---------------------- LẤY SẢN PHẨM THEO MÃ  ----------------------
     public SanPham getById(int maSP) {
        String sql = "SELECT * FROM SanPham WHERE maSanPham = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
     
    // ---------------------- LẤY TẤT CẢ SẢN PHẨM ----------------------
    public List<SanPham> getAll() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT maSanPham, tenSanPham, donGia, moTa FROM SanPham ORDER BY maSanPham";
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

    // ---------------------- THÊM SẢN PHẨM (TRẢ VỀ ID MỚI) ----------------------
    public int insert(SanPham sp) {
        String sql = "INSERT INTO SanPham (tenSanPham, donGia, moTa) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, sp.getTenSanPham());
            ps.setBigDecimal(2, sp.getDonGia());
            ps.setString(3, sp.getMoTa());
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ---------------------- CẬP NHẬT ----------------------
    public boolean update(SanPham sp) {
        String sql = "UPDATE SanPham SET tenSanPham = ?, donGia = ?, moTa = ? WHERE maSanPham = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSanPham());
            ps.setBigDecimal(2, sp.getDonGia());
            ps.setString(3, sp.getMoTa());
            ps.setInt(4, sp.getMaSanPham());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------- XÓA ----------------------
    public boolean delete(int maSanPham) {
        String sql = "DELETE FROM SanPham WHERE maSanPham = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------- TÌM KIẾM (MÃ HOẶC TÊN) ----------------------
     public List<SanPham> search(String key, int tieuChi) {
        List<SanPham> ds = new ArrayList<>();
        if (key == null || key.trim().isEmpty()){
            return getAll();
        }
        
        key = key.trim();
        String sql = "SELECT * FROM SanPham WHERE ";
        
        try{
            switch(tieuChi){
            case 0 -> { // maSP
                    SanPham sp = getById(Integer.parseInt(key));
                    if (sp!= null) ds.add(sp);
                }
            case 1 -> { // tenSP
                sql += "tenSanPham LIKE ?";
                    try (Connection con = DBConnection.getConnection();
                         PreparedStatement ps = con.prepareStatement(sql)) {
                         ps.setString(1, "%" + key + "%");
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) ds.add(mapRow(rs));
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

    // ---------------------- MAP ROW ----------------------
    private SanPham mapRow(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham();
        sp.setMaSanPham(rs.getInt("maSanPham"));
        sp.setTenSanPham(rs.getString("tenSanPham"));
        sp.setDonGia(rs.getBigDecimal("donGia"));
        sp.setMoTa(rs.getString("moTa"));
        return sp;
    }
}