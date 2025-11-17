package model;

import java.math.BigDecimal;
import java.util.Date;

public class DonHang {
    private int maDonHang;
    private Integer maNhanVien;
    private Integer maKhachHang;
    private Date thoiGianTao;
    private BigDecimal tongTienPhaiTra;
    private String trangThaiDonHang;

    public DonHang() {}

    // Getters and Setters
    public int getMaDonHang() { return maDonHang; }
    public void setMaDonHang(int maDonHang) { this.maDonHang = maDonHang; }

    public Integer getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(Integer maNhanVien) { this.maNhanVien = maNhanVien; }

    public Integer getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(Integer maKhachHang) { this.maKhachHang = maKhachHang; }

    public Date getThoiGianTao() { return thoiGianTao; }
    public void setThoiGianTao(Date thoiGianTao) { this.thoiGianTao = thoiGianTao; }

    public BigDecimal getTongTienPhaiTra() { return tongTienPhaiTra; }
    public void setTongTienPhaiTra(BigDecimal tongTienPhaiTra) { this.tongTienPhaiTra = tongTienPhaiTra; }

    public String getTrangThaiDonHang() { return trangThaiDonHang; }
    public void setTrangThaiDonHang(String trangThaiDonHang) { this.trangThaiDonHang = trangThaiDonHang; }

}