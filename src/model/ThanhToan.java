package model;

import java.math.BigDecimal;
import java.util.Date;

public class ThanhToan {
    private int maThanhToan;
    private Integer maDonHang;
    private BigDecimal soTien;
    private Date thoiGianThanhToan;
    private Integer maNhanVien;
    private String phuongThucThanhToan;

    public ThanhToan() {}

    // Getters and Setters
    public int getMaThanhToan() { return maThanhToan; }
    public void setMaThanhToan(int maThanhToan) { this.maThanhToan = maThanhToan; }

    public Integer getMaDonHang() { return maDonHang; }
    public void setMaDonHang(Integer maDonHang) { this.maDonHang = maDonHang; }

    public BigDecimal getSoTien() { return soTien; }
    public void setSoTien(BigDecimal soTien) { this.soTien = soTien; }

    public Date getThoiGianThanhToan() { return thoiGianThanhToan; }
    public void setThoiGianThanhToan(Date thoiGianThanhToan) { this.thoiGianThanhToan = thoiGianThanhToan; }

    public Integer getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(Integer maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }
}