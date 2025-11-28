package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import util.DBConnection;
import model.ChiTietHoaDon;

public class ChiTietHoaDonDAO {

    public void insertChiTietHoaDon(List<ChiTietHoaDon> ctlists) throws Exception {
        if(ctlists.isEmpty()) return;
        
        String sql = "INSERT INTO ChiTietHoaDon(maHoaDon, maSanPham, maVe, soLuong, donGiaLucBan, thanhTien) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            for (ChiTietHoaDon ct : ctlists) {
                ps.setInt(1, ct.getMaHoaDon());
                ps.setObject(2, ct.getMaSanPham() <= 0 ? null : ct.getMaSanPham());
                ps.setObject(3, ct.getMaVe() <= 0 ? null : ct.getMaVe());
                ps.setInt(4, ct.getSoLuong());
                ps.setBigDecimal(5, ct.getDonGiaLucBan());
                ps.setBigDecimal(6, ct.getThanhTien());
                ps.addBatch();
            }
            
            ps.executeBatch();
        }

    }
    
}
