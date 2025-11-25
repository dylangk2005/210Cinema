package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import util.DBConnection;
import model.ChiTietDonHang;

public class ChiTietDonHangDAO {

    public void insertChiTietDonHang(List<ChiTietDonHang> ctlists) throws Exception {
        String sql = "INSERT INTO ChiTietDonHang(maDonHang, maSanPham, maVe, soLuong, donGiaLucBan, thanhTien) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            for (ChiTietDonHang ct : ctlists) {
                ps.setInt(1, ct.getMaDonHang());
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
