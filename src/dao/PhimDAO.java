package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Phim;
import util.DBConnection;

public class PhimDAO {
    
    // Chuyển dữ liệu trong ResultSet sang đối tượng phim
    private Phim mapRow(ResultSet rs) throws SQLException {
        Phim p = new Phim();
        p.setMaPhim(rs.getInt("maPhim"));
        p.setTenPhim(rs.getString("tenPhim"));
        p.setThoiLuong(rs.getInt("thoiLuong"));
        p.setTheLoai(rs.getString("theLoai"));
        p.setGioiHanTuoi(rs.getString("gioiHanTuoi"));
        p.setNgayKhoiChieu(rs.getDate("ngayKhoiChieu"));
        p.setMoTa(rs.getString("moTa"));
        return p;
    }
    
    // Liệt kê tất cả các phim
    public List<Phim> selectAll() {
        List<Phim> list = new ArrayList<>();
        String sql = "SELECT * FROM Phim;";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    // Tìm phim theo mã
    public Phim selectById(int maPhim) {
        String sql = "SELECT * FROM Phim WHERE maPhim = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhim);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    // Thêm phim
    public boolean insert(Phim p) {
        String sql = "INSERT INTO Phim (tenPhim, thoiLuong, theLoai, gioiHanTuoi, ngayKhoiChieu, moTa) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTenPhim());
            ps.setInt(2, p.getThoiLuong());
            ps.setString(3, p.getTheLoai());
            ps.setString(4, p.getGioiHanTuoi());
            ps.setDate(5, p.getNgayKhoiChieu());
            ps.setString(6, p.getMoTa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    // Cập nhật tt phim
    public boolean update(Phim p) {
        String sql = "UPDATE Phim SET tenPhim = ?, thoiLuong = ?, theLoai = ?, gioiHanTuoi = ?, ngayKhoiChieu = ?, moTa = ? WHERE maPhim = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTenPhim());
            ps.setInt(2, p.getThoiLuong());
            ps.setString(3, p.getTheLoai());
            ps.setString(4, p.getGioiHanTuoi());
            ps.setDate(5, p.getNgayKhoiChieu());
            ps.setString(6, p.getMoTa());
            ps.setInt(7, p.getMaPhim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    // Delete Phim
    public boolean delete(int maPhim) {
        String sql = "DELETE FROM Phim WHERE maPhim = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhim);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // Tìm kiếm phim theo 3 tiêu chí
    public List<Phim> search(String keyword, int tieuChi) {
        List<Phim> list = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return selectAll();
        }

        keyword = keyword.trim();
        String sql = "SELECT * FROM Phim WHERE ";

        try {
            switch (tieuChi) {
                case 0 -> { // Mã phim
                    int ma = Integer.parseInt(keyword);
                    sql += "maPhim = ?";
                    try (Connection con = DBConnection.getConnection();
                         PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setInt(1, ma);
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) list.add(mapRow(rs));
                        }
                    }
                }
                case 1 -> { // Tên phim
                    sql += "tenPhim LIKE ?";
                    try (Connection con = DBConnection.getConnection();
                         PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setString(1, "%" + keyword + "%");
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) list.add(mapRow(rs));
                        }
                    }
                }
                case 2 -> { // Thể loại
                    sql += "theLoai LIKE ?";
                    try (Connection con = DBConnection.getConnection();
                         PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setString(1, "%" + keyword + "%"); // Tìm có chứa từ khóa
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) list.add(mapRow(rs));
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            // Nếu gõ sai mã phim → bỏ qua
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Kiểm tra xem phim đã có suất chiếu chưa
    public boolean daCoSuatChieu(int maPhim) {
        String sql = "SELECT COUNT(*) FROM SuatChieu WHERE MaPhim = ?";
        try (var conn = DBConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maPhim);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // nếu đếm được >= 1 suất chiếu → true
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}