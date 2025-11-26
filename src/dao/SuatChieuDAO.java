package dao;

import model.SuatChieu;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SuatChieuDAO {
    public List<SuatChieu> getSuatChieuByPhimAndPhong(int maPhim, int maPhongChieu) {
        List<SuatChieu> list = new ArrayList<>();
        String sql = "SELECT * FROM SuatChieu WHERE maPhim = ? AND maPhongChieu = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, maPhim);
                ps.setInt(2, maPhongChieu);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    SuatChieu p = new SuatChieu(
                        rs.getInt("maSuatChieu"),
                        rs.getInt("maPhim"),
                        rs.getInt("maPhongChieu"),
                        rs.getTimestamp("ngayGioChieu"),
                        rs.getBigDecimal("giaVeCoBan")
                    );
                    list.add(p);
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // chuyển dòng thành đối tg sc
    private SuatChieu mapRow(ResultSet rs) throws SQLException {
        SuatChieu sc = new SuatChieu();
        sc.setMaSuatChieu(rs.getInt("maSuatChieu"));
        sc.setMaPhim(rs.getInt("maPhim"));
        sc.setMaPhongChieu(rs.getInt("maPhongChieu"));
        sc.setNgayGioChieu(rs.getTimestamp("ngayGioChieu"));
        sc.setGiaVeCoBan(rs.getBigDecimal("giaVeCoBan"));
        sc.setTenPhim(rs.getString("tenPhim"));
        sc.setTenPhongChieu(rs.getString("tenPhongChieu"));
        return sc;
    }
    
    // lấy tất cả sc
    public List<SuatChieu> selectAll() {
        List<SuatChieu> list = new ArrayList<>();
        String sql = """
                SELECT sc.*, p.tenPhim, pc.tenPhongChieu
                FROM SuatChieu sc
                JOIN Phim p ON sc.maPhim = p.maPhim
                JOIN PhongChieu pc ON sc.maPhongChieu = pc.maPhongChieu
                ORDER BY sc.maSuatChieu ASC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // lấy sc theo mã
    public SuatChieu selectById(int maSuatChieu) {
        String sql = """
                SELECT sc.*, p.tenPhim, pc.tenPhongChieu
                FROM SuatChieu sc
                JOIN Phim p ON sc.maPhim = p.maPhim
                JOIN PhongChieu pc ON sc.maPhongChieu = pc.maPhongChieu
                WHERE sc.maSuatChieu = ?
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // thêm sc mới
    public boolean insert(SuatChieu sc) {
        if (hasConflict(sc, -1)) return false;
        String sql = "INSERT INTO SuatChieu (maPhim, maPhongChieu, ngayGioChieu, giaVeCoBan) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sc.getMaPhim());
            ps.setInt(2, sc.getMaPhongChieu());
            ps.setTimestamp(3, new Timestamp(sc.getNgayGioChieu().getTime()));
            ps.setBigDecimal(4, sc.getGiaVeCoBan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // cập nhật sc
    public boolean update(SuatChieu sc) {
        if (hasConflict(sc, sc.getMaSuatChieu())) return false;
        String sql = "UPDATE SuatChieu SET maPhim = ?, maPhongChieu = ?, ngayGioChieu = ?, giaVeCoBan = ? WHERE maSuatChieu = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sc.getMaPhim());
            ps.setInt(2, sc.getMaPhongChieu());
            ps.setTimestamp(3, new Timestamp(sc.getNgayGioChieu().getTime()));
            ps.setBigDecimal(4, sc.getGiaVeCoBan());
            ps.setInt(5, sc.getMaSuatChieu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // xóa sc (k cho xóa nếu có vé đặt r)
    public boolean delete(int maSuatChieu) {
        if (hasTicketsSold(maSuatChieu)) return false;
        String sql = "DELETE FROM SuatChieu WHERE maSuatChieu = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm kiếm theo mã suất, phim, phòng
    public List<SuatChieu> search(String keyword, int tieuChi) {
        List<SuatChieu> list = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return selectAll();
        }

        keyword = keyword.trim();
        String sql = """
                SELECT sc.*, p.tenPhim, pc.tenPhongChieu
                FROM SuatChieu sc
                JOIN Phim p ON sc.maPhim = p.maPhim
                JOIN PhongChieu pc ON sc.maPhongChieu = pc.maPhongChieu
                WHERE 
                """;

        try {
            switch (tieuChi) {
                case 0 -> { // Mã suất chiếu
                    int ma = Integer.parseInt(keyword);
                    sql += "sc.maSuatChieu = ?";
                    try (Connection con = DBConnection.getConnection();
                         PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setInt(1, ma);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) list.add(mapRow(rs));
                        }
                    }
                }
                case 1 -> { // Tên phim
                    sql += "p.tenPhim LIKE ?";
                    try (Connection con = DBConnection.getConnection();
                         PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setString(1, "%" + keyword + "%");
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) list.add(mapRow(rs));
                        }
                    }
                }
                case 2 -> { // Tên phòng
                    sql += "pc.tenPhongChieu LIKE ?";
                    try (Connection con = DBConnection.getConnection();
                         PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setString(1, "%" + keyword + "%");
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) list.add(mapRow(rs));
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            // Nếu tìm mã suất mà không phải số → bỏ qua
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Kiểm tra trùng lịch chiếu 
    public boolean hasConflict(SuatChieu sc, int excludeId) {
        String sql = """
                SELECT COUNT(*) FROM SuatChieu sc
                JOIN Phim p ON sc.maPhim = p.maPhim
                WHERE sc.maPhongChieu = ?
                  AND sc.maSuatChieu != ?
                  AND sc.ngayGioChieu >= DATEADD(MINUTE, -p.thoiLuong-30, ?)
                  AND sc.ngayGioChieu <= DATEADD(MINUTE, p.thoiLuong+30, ?)
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sc.getMaPhongChieu());
            ps.setInt(2, excludeId);
            ps.setTimestamp(3, new Timestamp(sc.getNgayGioChieu().getTime()));
            ps.setTimestamp(4, new Timestamp(sc.getNgayGioChieu().getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // kiếm tra vé đã bán chưa
    private boolean hasTicketsSold(int maSuatChieu) {
        String sql = "SELECT COUNT(*) FROM Ve WHERE maSuatChieu = ? AND trangThai = N'Đã đặt'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }
}