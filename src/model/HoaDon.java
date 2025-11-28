package model;

import java.math.BigDecimal;
import java.util.Date;

public class HoaDon {
    private int maHoaDon;
    private Integer maNhanVien;
    private Integer maKhachHang;
    private Date thoiGianTao;
    private BigDecimal tongTienPhaiTra;
    private String PhuongThucThanhToan;

    public HoaDon() {}

    public HoaDon(Integer maNhanVien, Integer maKhachHang, BigDecimal tongTienPhaiTra, String trangThaiDonHang) {
        this.maNhanVien = maNhanVien;
        this.maKhachHang = maKhachHang;
        this.tongTienPhaiTra = tongTienPhaiTra;
        this.PhuongThucThanhToan = trangThaiDonHang;
    }

    // Getters and Setters
    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }

    public Integer getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(Integer maNhanVien) { this.maNhanVien = maNhanVien; }

    public Integer getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(Integer maKhachHang) { this.maKhachHang = maKhachHang; }

    public Date getThoiGianTao() { return thoiGianTao; }
    public void setThoiGianTao(Date thoiGianTao) { this.thoiGianTao = thoiGianTao; }

    public BigDecimal getTongTienPhaiTra() { return tongTienPhaiTra; }
    public void setTongTienPhaiTra(BigDecimal tongTienPhaiTra) { this.tongTienPhaiTra = tongTienPhaiTra; }

    public String getPhuongThucThanhToan() { return PhuongThucThanhToan; }
    public void setPhuongThucThanhToan(String PhuongThucThanhToan) { this.PhuongThucThanhToan = PhuongThucThanhToan; }
    
    @Override
    public String toString() {
        return "maNV = " + maNhanVien + ", maKH = " + maKhachHang + ", tongTienPhaiTra = " + tongTienPhaiTra
                + ", trangThaiDonHang = " + PhuongThucThanhToan;
    }

}