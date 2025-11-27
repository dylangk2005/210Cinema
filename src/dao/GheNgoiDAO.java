package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;
import util.DBConnection;

public class GheNgoiDAO {
    public Map<String, Integer> getAllSeats(int maPhongChieu) {
        Map<String, Integer> seats = new LinkedHashMap<>();
        String sql = "SELECT * FROM GheNgoi WHERE maPhongChieu = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, maPhongChieu);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String row = rs.getString("hangGhe");  
                    int col = rs.getInt("soGhe");
                    String seatCode = String.format("%s%02d", row, col); 
                    seats.put(seatCode, rs.getInt("maGheNgoi"));
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return seats;
    }
    
    public Set<Integer> getAllBookedSeats(int maSuatChieu) {
        Set<Integer> bookedSeats = new HashSet<>();
        String sql = "SELECT * FROM Ve WHERE maSuatChieu = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, maSuatChieu);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    bookedSeats.add(rs.getInt("maGheNgoi"));
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bookedSeats;
    }
    
    // xóa ghế theo mã phòng chiếu
    public void xoaGheTheoPhong(int maPhongChieu) {
        String sql = "DELETE FROM GheNgoi WHERE maPhongChieu = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhongChieu);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // tự sinh ghế ngồi theo mã phòng chiếu + số ghế
    public void autoChairsGeneration(int maPhongChieu, int soGheNgoi) {
        
        // Xóa ghế cũ trước (nếu có)
        xoaGheTheoPhong(maPhongChieu);

        char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M'}; // Tối đa 13 hàng
        int soCotMacDinh = 12;
        int soHangDay = soGheNgoi / soCotMacDinh;        // Số hàng đầy đủ 12 ghế

        String sql = "INSERT INTO GheNgoi (maPhongChieu, hangGhe, soGhe) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            con.setAutoCommit(false);

            int gheDaTao = 0;

            // Tạo các hàng đầy đủ (12 ghế)
            for (int h = 0; h < soHangDay; h++) {
                char hang = rows[h];
                for (int g = 1; g <= soCotMacDinh; g++) {
                    ps.setInt(1, maPhongChieu);
                    ps.setString(2, String.valueOf(hang));
                    ps.setInt(3, g);
                    ps.addBatch();
                    gheDaTao++;
                }
            }
            ps.executeBatch();
            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
