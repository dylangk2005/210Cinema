package dao;

import model.Phim;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhimDAO {
    private Phim mapResultSetToPhim(ResultSet rs) throws SQLException {
        Phim phim = new Phim();
        phim.setMaPhim(rs.getInt("maPhim"));
        phim.setTenPhim(rs.getString("tenPhim"));
        phim.setThoiLuong(rs.getInt("thoiLuong"));
        phim.setTheLoai(rs.getString("theLoai"));
        phim.setGioiHanTuoi(rs.getString("gioiHanTuoi"));
        phim.setNgayKhoiChieu(rs.getDate("ngayKhoiChieu"));
        phim.setMoTa(rs.getString("moTa"));
        return phim;
    }
    
    public boolean insert(Phim phim) {
        String sql = "INSERT INTO Phim (tenPhim, thoiLuong, theLoai, gioiHanTuoi, ngayKhoiChieu, moTa) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, phim.getTenPhim());
            pstmt.setInt(2, phim.getThoiLuong());
            pstmt.setString(3, phim.gettheLoai());
            pstmt.setString(4, phim.getGioiHanTuoi());
            pstmt.setDate(5, phim.getNgayKhoiChieu());
            pstmt.setString(6, phim.getMoTa());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Phim> selectAll() {
        List<Phim> danhSachPhim = new ArrayList<>();
        String sql = "SELECT * FROM Phim";
        
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Phim phim = mapResultSetToPhim(rs);
                danhSachPhim.add(phim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhSachPhim;
    }
    
    public Phim selectById(int maPhim) {
        String sql = "SELECT * FROM Phim WHERE maPhim = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, maPhim);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPhim(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean update(Phim phim) {
        String sql = "UPDATE Phim SET tenPhim = ?, thoiLuong = ?, theLoai = ?, " +
                     "gioiHanTuoi = ?, ngayKhoiChieu = ?, moTa = ? WHERE maPhim = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, phim.getTenPhim());
            pstmt.setInt(2, phim.getThoiLuong());
            pstmt.setString(3, phim.gettheLoai());
            pstmt.setString(4, phim.getGioiHanTuoi());
            pstmt.setDate(5, phim.getNgayKhoiChieu());
            pstmt.setString(6, phim.getMoTa());
            pstmt.setInt(7, phim.getMaPhim());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean delete(int maPhim) {
        String sql = "DELETE FROM Phim WHERE maPhim = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, maPhim);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // SEARCH - Search movies by name and genre
    public List<Phim> search(String tenPhim, String theLoai) {
        List<Phim> danhSachPhim = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Phim WHERE 1=1");
        
        if (tenPhim != null && !tenPhim.trim().isEmpty()) {
            sql.append(" AND tenPhim LIKE ?");
        }
        if (theLoai != null && !theLoai.trim().isEmpty() && !theLoai.equals("")) {
            sql.append(" AND theLoai = ?");
        }
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            
            if (tenPhim != null && !tenPhim.trim().isEmpty()) {
                pstmt.setString(paramIndex++, "%" + tenPhim + "%");
            }
            if (theLoai != null && !theLoai.trim().isEmpty() && !theLoai.equals("")) {
                pstmt.setString(paramIndex, theLoai);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Phim phim = mapResultSetToPhim(rs);
                danhSachPhim.add(phim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhSachPhim;
    }
    
    // Search movie by ID (String parameter)
    public Phim searchById(String maPhim) {
        try {
            int ma = Integer.parseInt(maPhim);
            return selectById(ma);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
