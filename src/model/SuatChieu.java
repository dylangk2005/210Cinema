package model;

import java.math.BigDecimal;
import java.util.Date;

public class SuatChieu {
    private int maSuatChieu;
    private int maPhim;
    private int maPhongChieu;
    private Date ngayGioChieu;
    private BigDecimal giaVeCoBan;

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

    @Override
    public String toString() {
        return ngayGioChieu + " - " + giaVeCoBan + "đ";
    }
}