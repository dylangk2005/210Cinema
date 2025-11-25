package model;

public class Phim {
    private int maPhim;
    private String tenPhim;
    private int thoiLuong;
    private String theLoai;
    private String gioiHanTuoi;
    private java.sql.Date ngayKhoiChieu;
    private String moTa;

    public Phim() {}

    // Getters and Setters
    public int getMaPhim() { return maPhim; }
    public void setMaPhim(int maPhim) { this.maPhim = maPhim; }

    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }

    public int getThoiLuong() { return thoiLuong; }
    public void setThoiLuong(int thoiLuong) { this.thoiLuong = thoiLuong; }

    public String getTheLoai() { return theLoai; }
    public void setTheLoai(String theLoai) { this.theLoai = theLoai; }

    public String getGioiHanTuoi() { return gioiHanTuoi; }
    public void setGioiHanTuoi(String gioiHanTuoi) { this.gioiHanTuoi = gioiHanTuoi; }

    public java.sql.Date getNgayKhoiChieu() { return ngayKhoiChieu; }
    public void setNgayKhoiChieu(java.sql.Date ngayKhoiChieu) { this.ngayKhoiChieu = ngayKhoiChieu; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

}