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
    
    
    public void autoChairsGeneration(Map<Integer, Integer> seatMaps) {
        Character[] rows = new Character[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        String sql = "INSERT INTO GheNgoi (maPhongChieu, hangGhe, soGhe) "
                + "VALUES (?, ?, ?)";
        
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (var entry : seatMaps.entrySet()) {
                    ps.setInt(1, entry.getKey());
                    int row = entry.getValue() / 12;
                    for (int i = 0; i < row; i++) {
                        ps.setString(2, String.valueOf(rows[i])); 
                        for (int j = 1; j <= 12; j++) {
                            ps.setInt(3, j);
                            ps.addBatch();
                        }
                    }
                }
                ps.executeBatch();
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } 
        } catch (Exception e) {
                e.printStackTrace();
        }
    }
   

}
