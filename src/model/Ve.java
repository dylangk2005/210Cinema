package model;

import java.math.BigDecimal;

public class Ve {
    private int maVe;
    private int maSuatChieu;
    private int maGheNgoi;
    private Integer maDonHang;
    private BigDecimal giaVe;
    private String trangThai;

    public Ve() {}

    // Getters and Setters
    public int getMaVe() { return maVe; }
    public void setMaVe(int maVe) { this.maVe = maVe; }

    public int getMaSuatChieu() { return maSuatChieu; }
    public void setMaSuatChieu(int maSuatChieu) { this.maSuatChieu = maSuatChieu; }

    public int getMaGheNgoi() { return maGheNgoi; }
    public void setMaGheNgoi(int maGheNgoi) { this.maGheNgoi = maGheNgoi; }

    public Integer getMaDonHang() { return maDonHang; }
    public void setMaDonHang(Integer maDonHang) { this.maDonHang = maDonHang; }

    public BigDecimal getGiaVe() { return giaVe; }
    public void setGiaVe(BigDecimal giaVe) { this.giaVe = giaVe; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}