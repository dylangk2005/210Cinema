package dao;

import model.SuatChieu;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class SuatChieuDAO {
    
    // Map ResultSet to SuatChieu Model
    private SuatChieu mapResultSetToSuatChieu(ResultSet rs) throws SQLException {
        SuatChieu sc = new SuatChieu();
        sc.setMaSuatChieu(rs.getInt("maSuatChieu"));
        sc.setMaPhim(rs.getInt("maPhim"));
        sc.setMaPhongChieu(rs.getInt("maPhongChieu"));
        sc.setNgayGioChieu(rs.getTimestamp("ngayGioChieu"));
        sc.setGiaVeCoBan(rs.getBigDecimal("giaVeCoBan"));
        
        // Get additional info if available (from JOIN)
        try {
            sc.setTenPhim(rs.getString("tenPhim"));
            sc.setTenPhongChieu(rs.getString("tenPhongChieu"));
        } catch (SQLException e) {
            // Columns not available in this query
        }
        
        return sc;
    }
    
    // CREATE - Insert new showtime
    public boolean insert(SuatChieu suatChieu) {
        // Check for time conflict first
        if (hasTimeConflict(suatChieu)) {
            return false;
        }
        
        String sql = "INSERT INTO SuatChieu (maPhim, maPhongChieu, ngayGioChieu, giaVeCoBan) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, suatChieu.getMaPhim());
            pstmt.setInt(2, suatChieu.getMaPhongChieu());
            pstmt.setTimestamp(3, new Timestamp(suatChieu.getNgayGioChieu().getTime()));
            pstmt.setBigDecimal(4, suatChieu.getGiaVeCoBan());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // READ - Get all showtimes with movie and room names
    public List<SuatChieu> selectAll() {
        List<SuatChieu> list = new ArrayList<>();
        String sql = "SELECT sc.*, p.tenPhim, pc.tenPhongChieu " +
                     "FROM SuatChieu sc " +
                     "INNER JOIN Phim p ON sc.maPhim = p.maPhim " +
                     "INNER JOIN PhongChieu pc ON sc.maPhongChieu = pc.maPhongChieu " +
                     "ORDER BY sc.ngayGioChieu DESC";
        
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                SuatChieu sc = mapResultSetToSuatChieu(rs);
                list.add(sc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    // READ - Get showtime by ID
    public SuatChieu selectById(int maSuatChieu) {
        String sql = "SELECT sc.*, p.tenPhim, pc.tenPhongChieu " +
                     "FROM SuatChieu sc " +
                     "INNER JOIN Phim p ON sc.maPhim = p.maPhim " +
                     "INNER JOIN PhongChieu pc ON sc.maPhongChieu = pc.maPhongChieu " +
                     "WHERE sc.maSuatChieu = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, maSuatChieu);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSuatChieu(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // UPDATE - Update showtime
    public boolean update(SuatChieu suatChieu) {
        // Check for time conflict (excluding current showtime)
        if (hasTimeConflictForUpdate(suatChieu)) {
            return false;
        }
        
        String sql = "UPDATE SuatChieu SET maPhim = ?, maPhongChieu = ?, " +
                     "ngayGioChieu = ?, giaVeCoBan = ? WHERE maSuatChieu = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, suatChieu.getMaPhim());
            pstmt.setInt(2, suatChieu.getMaPhongChieu());
            pstmt.setTimestamp(3, new Timestamp(suatChieu.getNgayGioChieu().getTime()));
            pstmt.setBigDecimal(4, suatChieu.getGiaVeCoBan());
            pstmt.setInt(5, suatChieu.getMaSuatChieu());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // DELETE - Delete showtime (only if no tickets sold)
    public boolean delete(int maSuatChieu) {
        // Check if any tickets have been sold
        if (hasTicketsSold(maSuatChieu)) {
            return false;
        }
        
        String sql = "DELETE FROM SuatChieu WHERE maSuatChieu = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, maSuatChieu);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // SEARCH - Search by date, movie, or room
    public List<SuatChieu> search(Date ngay, Integer maPhim, Integer maPhongChieu) {
        List<SuatChieu> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT sc.*, p.tenPhim, pc.tenPhongChieu " +
            "FROM SuatChieu sc " +
            "INNER JOIN Phim p ON sc.maPhim = p.maPhim " +
            "INNER JOIN PhongChieu pc ON sc.maPhongChieu = pc.maPhongChieu " +
            "WHERE 1=1"
        );
        
        if (ngay != null) {
            sql.append(" AND CAST(sc.ngayGioChieu AS DATE) = ?");
        }
        if (maPhim != null && maPhim > 0) {
            sql.append(" AND sc.maPhim = ?");
        }
        if (maPhongChieu != null && maPhongChieu > 0) {
            sql.append(" AND sc.maPhongChieu = ?");
        }
        
        sql.append(" ORDER BY sc.ngayGioChieu DESC");
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            
            if (ngay != null) {
                pstmt.setDate(paramIndex++, new java.sql.Date(ngay.getTime()));
            }
            if (maPhim != null && maPhim > 0) {
                pstmt.setInt(paramIndex++, maPhim);
            }
            if (maPhongChieu != null && maPhongChieu > 0) {
                pstmt.setInt(paramIndex, maPhongChieu);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                SuatChieu sc = mapResultSetToSuatChieu(rs);
                list.add(sc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    // Check for time conflict when adding new showtime
    private boolean hasTimeConflict(SuatChieu suatChieu) {
        String sql = "SELECT COUNT(*) FROM SuatChieu sc " +
                     "INNER JOIN Phim p ON sc.maPhim = p.maPhim " +
                     "WHERE sc.maPhongChieu = ? " +
                     "AND ABS(DATEDIFF(MINUTE, sc.ngayGioChieu, ?)) < (p.thoiLuong + 30)";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, suatChieu.getMaPhongChieu());
            pstmt.setTimestamp(2, new Timestamp(suatChieu.getNgayGioChieu().getTime()));
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Check for time conflict when updating (exclude current showtime)
    private boolean hasTimeConflictForUpdate(SuatChieu suatChieu) {
        String sql = "SELECT COUNT(*) FROM SuatChieu sc " +
                     "INNER JOIN Phim p ON sc.maPhim = p.maPhim " +
                     "WHERE sc.maPhongChieu = ? " +
                     "AND sc.maSuatChieu != ? " +
                     "AND ABS(DATEDIFF(MINUTE, sc.ngayGioChieu, ?)) < (p.thoiLuong + 30)";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, suatChieu.getMaPhongChieu());
            pstmt.setInt(2, suatChieu.getMaSuatChieu());
            pstmt.setTimestamp(3, new Timestamp(suatChieu.getNgayGioChieu().getTime()));
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Check if any tickets have been sold for this showtime
    private boolean hasTicketsSold(int maSuatChieu) {
        // Nếu chưa có bảng Ve, trả về false để cho phép xóa
        String sql = "SELECT COUNT(*) FROM Ve WHERE maSuatChieu = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, maSuatChieu);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            // Bảng Ve chưa tồn tại hoặc lỗi khác
            // Cho phép xóa trong trường hợp này
            return false;
        }
        
        return false;
    }
    
    // Get showtimes by movie
    public List<SuatChieu> selectByPhim(int maPhim) {
        List<SuatChieu> list = new ArrayList<>();
        String sql = "SELECT sc.*, p.tenPhim, pc.tenPhongChieu " +
                     "FROM SuatChieu sc " +
                     "INNER JOIN Phim p ON sc.maPhim = p.maPhim " +
                     "INNER JOIN PhongChieu pc ON sc.maPhongChieu = pc.maPhongChieu " +
                     "WHERE sc.maPhim = ? " +
                     "ORDER BY sc.ngayGioChieu ASC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, maPhim);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                SuatChieu sc = mapResultSetToSuatChieu(rs);
                list.add(sc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    // Get showtimes by room
    public List<SuatChieu> selectByPhong(int maPhongChieu) {
        List<SuatChieu> list = new ArrayList<>();
        String sql = "SELECT sc.*, p.tenPhim, pc.tenPhongChieu " +
                     "FROM SuatChieu sc " +
                     "INNER JOIN Phim p ON sc.maPhim = p.maPhim " +
                     "INNER JOIN PhongChieu pc ON sc.maPhongChieu = pc.maPhongChieu " +
                     "WHERE sc.maPhongChieu = ? " +
                     "ORDER BY sc.ngayGioChieu ASC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, maPhongChieu);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                SuatChieu sc = mapResultSetToSuatChieu(rs);
                list.add(sc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
}
