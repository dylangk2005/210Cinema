package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.PhongChieu;
import util.DBConnection;
import dao.GheNgoiDAO;

public class PhongChieuDAO {
    public List<PhongChieu> getAllPhongChieu() {
        List<PhongChieu> list = new ArrayList<>();
        String sql = "SELECT * FROM PhongChieu";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    PhongChieu p = new PhongChieu(
                        rs.getInt("maPhongChieu"),
                        rs.getString("tenPhongChieu"),
                        rs.getInt("soGheNgoi"),
                        rs.getString("trangThaiPhong"),
                        rs.getString("loaiManHinh"),
                        rs.getString("heThongAmThanh")
                    );
                    list.add(p);
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public int getCurrentPhongChieuID() {
        String sql = "SELECT ISNULL(IDENT_CURRENT('PhongChieu'), 0)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public void updateDatabase(List<PhongChieu> newList) {
        List<PhongChieu> oldList = getAllPhongChieu();
        Map<Integer, Integer> newSeatsMap = new HashMap<>();

        Map<Integer, PhongChieu> oldMap = new HashMap<>();
        for (PhongChieu pc : oldList) {
            oldMap.put(pc.getMaPhongChieu(), pc);
        }
        
        String sqlInsert = "INSERT INTO PhongChieu (tenPhongChieu, soGheNgoi, trangThaiPhong, loaiManHinh, heThongAmThanh) "
                + "VALUES (?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE PhongChieu "
                + "SET tenPhongChieu=?, soGheNgoi=?, trangThaiPhong=?, loaiManHinh=?, heThongAmThanh=? "
                + "WHERE maPhongChieu=?";
        String sqlDelete = "DELETE FROM PhongChieu WHERE maPhongChieu=?";
        
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement insert = con.prepareStatement(sqlInsert);
                PreparedStatement update = con.prepareStatement(sqlUpdate);
                PreparedStatement delete = con.prepareStatement(sqlDelete)) {
                for (PhongChieu pc : newList) {
                    int id = pc.getMaPhongChieu();
                    if (id <= 0 || !oldMap.containsKey(id)) {
                        insert.setString(1, pc.getTenPhongChieu());
                        insert.setInt(2, pc.getSoGheNgoi());
                        insert.setString(3, pc.getTrangThaiPhong());
                        insert.setString(4, pc.getLoaiManHinh());
                        insert.setString(5, pc.getHeThongAmThanh());
                        insert.addBatch();
                        newSeatsMap.put(id, pc.getSoGheNgoi());
                    } else {
                        update.setString(1, pc.getTenPhongChieu());
                        update.setInt(2, pc.getSoGheNgoi());
                        update.setString(3, pc.getTrangThaiPhong());
                        update.setString(4, pc.getLoaiManHinh());
                        update.setString(5, pc.getHeThongAmThanh());
                        update.setInt(6, id);
                        update.addBatch();
                        oldMap.remove(id);
                     }
                }
                
                insert.executeBatch();
                update.executeBatch();
                
                for (Integer idDelete : oldMap.keySet()) {
                    try {
                        delete.setInt(1, idDelete);
                        delete.executeUpdate();
                    } catch (SQLException ex) {
                        System.err.println("Không thể xóa phòng chiếu id=" + idDelete + ": " + ex.getMessage());
                    }
                }
                
                for (var entry : newSeatsMap.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } 
        } catch (Exception e) {
                e.printStackTrace();
        }
        new GheNgoiDAO().autoChairsGeneration(newSeatsMap);
    }

}
