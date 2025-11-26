package model;

import java.math.BigDecimal;

public class ChiTietDonHang {
    private int maCTDH;
    private int maDonHang;
    private Integer maSanPham;
    private Integer maVe;
    private int soLuong;
    private BigDecimal donGiaLucBan;
    private BigDecimal thanhTien;

    public ChiTietDonHang() {}

    public ChiTietDonHang(int maDonHang, Integer maSanPham, Integer maVe, int soLuong, BigDecimal donGiaLucBan, BigDecimal thanhTien) {
        this.maDonHang = maDonHang;
        this.maSanPham = maSanPham;
        this.maVe = maVe;
        this.soLuong = soLuong;
        this.donGiaLucBan = donGiaLucBan;
        this.thanhTien = thanhTien;
    }

    // Getters and Setters
    public int getMaCTDH() { return maCTDH; }
    public void setMaCTDH(int maCTDH) { this.maCTDH = maCTDH; }

    public int getMaDonHang() { return maDonHang; }
    public void setMaDonHang(int maDonHang) { this.maDonHang = maDonHang; }

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