package model;

import java.util.Date;

public class NhanVien {
    private int maNhanVien;
    private String hoTenNhanVien;
    private Date ngaySinh;
    private String gioiTinh;
    private String soDienThoai;
    private Integer maChucVu;

    public NhanVien(){}
    public NhanVien(int maNhanVien, String hoTenNhanVien, Date ngaySinh, String gioiTinh, String soDienThoai, Integer maChucVu) {
        this.maNhanVien = maNhanVien;
        this.hoTenNhanVien = hoTenNhanVien;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.maChucVu = maChucVu;
    }
    // Getters and Setters
    public int getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(int maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getHoTenNhanVien() { return hoTenNhanVien; }
    public void setHoTenNhanVien(String hoTenNhanVien) { this.hoTenNhanVien = hoTenNhanVien; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public Integer getMaChucVu() { return maChucVu; }
    public void setMaChucVu(Integer maChucVu) { this.maChucVu = maChucVu; }

}
