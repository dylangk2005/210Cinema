package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import util.DBConnection;

public class GheNgoiDAO {
    public Map<String, Integer> getAllSeats(int maPhongChieu) {
        Map<String, Integer> seats = new HashMap<>();
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
    
}
