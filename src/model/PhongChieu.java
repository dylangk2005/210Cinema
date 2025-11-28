package model;

public class PhongChieu {
    private int maPhongChieu;
    private String tenPhongChieu;
    private int soGheNgoi;
    private String trangThaiPhong;
    private String loaiManHinh;
    private String heThongAmThanh;

    public PhongChieu() {}

    public PhongChieu(int maPhongChieu, String tenPhongChieu, int soGheNgoi, String trangThaiPhong, String loaiManHinh, String heThongAmThanh) {
        this.maPhongChieu = maPhongChieu;
        this.tenPhongChieu = tenPhongChieu;
        this.soGheNgoi = soGheNgoi;
        this.trangThaiPhong = trangThaiPhong;
        this.loaiManHinh = loaiManHinh;
        this.heThongAmThanh = heThongAmThanh;
    }

    // Getters and Setters
    public int getMaPhongChieu() { return maPhongChieu; }
    public void setMaPhongChieu(int maPhongChieu) { this.maPhongChieu = maPhongChieu; }

    public String getTenPhongChieu() { return tenPhongChieu; }
    public void setTenPhongChieu(String tenPhongChieu) { this.tenPhongChieu = tenPhongChieu; }

    public int getSoGheNgoi() { return soGheNgoi; }
    public void setSoGheNgoi(int soGheNgoi) { this.soGheNgoi = soGheNgoi; }

    public String getTrangThaiPhong() { return trangThaiPhong; }
    public void setTrangThaiPhong(String trangThaiPhong) { this.trangThaiPhong = trangThaiPhong; }

    public String getLoaiManHinh() { return loaiManHinh; }
    public void setLoaiManHinh(String loaiManHinh) { this.loaiManHinh = loaiManHinh; }

    public String getHeThongAmThanh() { return heThongAmThanh; }
    public void setHeThongAmThanh(String heThongAmThanh) { this.heThongAmThanh = heThongAmThanh; }

    @Override
    public String toString() {
        return tenPhongChieu + " (" + soGheNgoi + " ghế)";
    }
}