package model;


public class GioHang {
    private String ten;
    private int soLuong;
    private double gia;
    
    public GioHang() {}

    public GioHang(String ten, int soLuong, double gia) {
        this.ten = ten;
        this.soLuong = soLuong;
        this.gia = gia;
    }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }
}

