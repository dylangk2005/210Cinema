package model;

import java.math.BigDecimal;
import java.util.Date;

public class SuatChieu {
    private int maSuatChieu;
    private int maPhim;
    private int maPhongChieu;
    private Date ngayGioChieu;
    private BigDecimal giaVeCoBan;
    
    // thêm 2 field để hiển thị tên
    private String tenPhim;
    private String tenPhongChieu;

    public SuatChieu() {}

    // Getters and Setters
    public int getMaSuatChieu() { return maSuatChieu; }
    public void setMaSuatChieu(int maSuatChieu) { this.maSuatChieu = maSuatChieu; }
    public int getMaPhim() { return maPhim; }
    public void setMaPhim(int maPhim) { this.maPhim = maPhim; }
    public int getMaPhongChieu() { return maPhongChieu; }
    public void setMaPhongChieu(int maPhongChieu) { this.maPhongChieu = maPhongChieu; }
    public Date getNgayGioChieu() { return ngayGioChieu; }
    public void setNgayGioChieu(Date ngayGioChieu) { this.ngayGioChieu = ngayGioChieu; }
    public BigDecimal getGiaVeCoBan() { return giaVeCoBan; }
    public void setGiaVeCoBan(BigDecimal giaVeCoBan) { this.giaVeCoBan = giaVeCoBan; }

    // THÊM 2 GETTER/SETTER NÀY
    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }
    public String getTenPhongChieu() { return tenPhongChieu; }
    public void setTenPhongChieu(String tenPhongChieu) { this.tenPhongChieu = tenPhongChieu; }

    @Override
    public String toString() {
        return ngayGioChieu + " - " + giaVeCoBan + "đ";
    }
}