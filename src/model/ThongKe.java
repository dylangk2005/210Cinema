package model;

import java.math.BigDecimal;

public class ThongKe {
    private String ten;
    private int soLuong;
    private long doanhThu;
    private String ghiChu;

    public ThongKe(String ten, int soLuong, long doanhThu) {
        this.ten = ten;
        this.soLuong = soLuong;
        this.doanhThu = doanhThu;
    }

    public ThongKe(String ten, int soLuong, long doanhThu, String ghiChu) {
        this(ten, soLuong, doanhThu);
        this.ghiChu = ghiChu;
    }

    public String getTen() { return ten; }
    public int getSoLuong() { return soLuong; }
    public long getDoanhThu() { return doanhThu; }
    public String getGhiChu() { return ghiChu; }

    public void setTen(String ten) { this.ten = ten; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public void setDoanhThu(long doanhThu) { this.doanhThu = doanhThu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
