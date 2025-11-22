package model;

import java.util.Date;

public class KhachHang {
    private int maKhachHang;
    private String hoTenKhachHang;
    private Date ngaySinh;
    private String gioiTinh;
    private String soDienThoai;
    private String email;
    private String hangThanhVien;
    private int diemTichLuy;
    private Date ngayDangKy;

    public KhachHang() {}

    public KhachHang(int maKhachHang, String hoTenKhachHang, Date ngaySinh, String gioiTinh, String soDienThoai, String email, String hangThanhVien, int diemTichLuy, Date ngayDangKy) {
        this.maKhachHang = maKhachHang;
        this.hoTenKhachHang = hoTenKhachHang;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.hangThanhVien = hangThanhVien;
        this.diemTichLuy = diemTichLuy;
        this.ngayDangKy = ngayDangKy;
    }

    // Getters and Setters
    public int getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(int maKhachHang) { this.maKhachHang = maKhachHang; }

    public String getHoTenKhachHang() { return hoTenKhachHang; }
    public void setHoTenKhachHang(String hoTenKhachHang) { this.hoTenKhachHang = hoTenKhachHang; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHangThanhVien() { return hangThanhVien; }
    public void setHangThanhVien(String hangThanhVien) { this.hangThanhVien = hangThanhVien; }

    public int getDiemTichLuy() { return diemTichLuy; }
    public void setDiemTichLuy(int diemTichLuy) { this.diemTichLuy = diemTichLuy; }

    public Date getNgayDangKy() { return ngayDangKy; }
    public void setNgayDangKy(Date ngayDangKy) { this.ngayDangKy = ngayDangKy; }

    @Override
    public String toString() {
        return hoTenKhachHang + " - " + soDienThoai + " (" + diemTichLuy + " điểm)";
    }
}