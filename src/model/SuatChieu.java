package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class SuatChieu {
    private int maSuatChieu;
    private int maPhim;
    private int maPhongChieu;
    private Timestamp ngayGioChieu;
    private BigDecimal giaVeCoBan;
    
    // thêm 2 field để hiển thị tên
    private String tenPhim;
    private String tenPhongChieu;

    public SuatChieu() {}

    public SuatChieu(int maSuatChieu, int maPhim, int maPhongChieu, Timestamp ngayGioChieu, BigDecimal giaVeCoBan) {
        this.maSuatChieu = maSuatChieu;
        this.maPhim = maPhim;
        this.maPhongChieu = maPhongChieu;
        this.ngayGioChieu = ngayGioChieu;
        this.giaVeCoBan = giaVeCoBan;
    }

    // Getters and Setters
    public int getMaSuatChieu() { return maSuatChieu; }
    public void setMaSuatChieu(int maSuatChieu) { this.maSuatChieu = maSuatChieu; }
    public int getMaPhim() { return maPhim; }
    public void setMaPhim(int maPhim) { this.maPhim = maPhim; }
    public int getMaPhongChieu() { return maPhongChieu; }
    public void setMaPhongChieu(int maPhongChieu) { this.maPhongChieu = maPhongChieu; }

    public Timestamp getNgayGioChieu() { return ngayGioChieu; }
    public void setNgayGioChieu(Timestamp ngayGioChieu) { this.ngayGioChieu = ngayGioChieu; }

    public BigDecimal getGiaVeCoBan() { return giaVeCoBan; }
    public void setGiaVeCoBan(BigDecimal giaVeCoBan) { this.giaVeCoBan = giaVeCoBan; }

    // THÊM 2 GETTER/SETTER NÀY
    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }
    public String getTenPhongChieu() { return tenPhongChieu; }
    public void setTenPhongChieu(String tenPhongChieu) { this.tenPhongChieu = tenPhongChieu; }

    @Override
    public String toString() {
        return ngayGioChieu.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}