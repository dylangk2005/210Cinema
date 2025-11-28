package model;

import java.math.BigDecimal;

public class ChiTietHoaDon {
    private int maCTHD;
    private int maHoaDon;
    private Integer maSanPham;
    private Integer maVe;
    private int soLuong;
    private BigDecimal donGiaLucBan;
    private BigDecimal thanhTien;

    public ChiTietHoaDon() {}

    public ChiTietHoaDon(int maDonHang, Integer maSanPham, Integer maVe, int soLuong, BigDecimal donGiaLucBan, BigDecimal thanhTien) {
        this.maHoaDon = maDonHang;
        this.maSanPham = maSanPham;
        this.maVe = maVe;
        this.soLuong = soLuong;
        this.donGiaLucBan = donGiaLucBan;
        this.thanhTien = thanhTien;
    }

    // Getters and Setters
    public int getMaCTHD() { return maCTHD; }
    public void setMaCTHD(int maCTHD) { this.maCTHD = maCTHD; }

    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }

    public Integer getMaSanPham() { return maSanPham; }
    public void setMaSanPham(Integer maSanPham) { this.maSanPham = maSanPham; }

    public Integer getMaVe() { return maVe; }
    public void setMaVe(Integer maVe) { this.maVe = maVe; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public BigDecimal getDonGiaLucBan() { return donGiaLucBan; }
    public void setDonGiaLucBan(BigDecimal donGiaLucBan) { this.donGiaLucBan = donGiaLucBan; }

    public BigDecimal getThanhTien() { return thanhTien; }
    public void setThanhTien(BigDecimal thanhTien) { this.thanhTien = thanhTien; }
}