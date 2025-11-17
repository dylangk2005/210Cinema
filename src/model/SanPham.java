package model;

import java.math.BigDecimal;

public class SanPham {
    private int maSanPham;
    private String tenSanPham;
    private BigDecimal donGia;
    private String moTa;

    public SanPham() {}

    // Getters and Setters
    public int getMaSanPham() { return maSanPham; }
    public void setMaSanPham(int maSanPham) { this.maSanPham = maSanPham; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }

    public BigDecimal getDonGia() { return donGia; }
    public void setDonGia(BigDecimal donGia) { this.donGia = donGia; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    @Override
    public String toString() {
        return tenSanPham + " - " + donGia + "đ";
    }
}