package model;

public class GheNgoi {
    private int maGheNgoi;
    private int maPhongChieu;
    private String hangGhe;
    private String soGhe;

    public GheNgoi() {}

    public GheNgoi(int maGheNgoi, int maPhongChieu, String hangGhe, String soGhe) {
        this.maGheNgoi = maGheNgoi;
        this.maPhongChieu = maPhongChieu;
        this.hangGhe = hangGhe;
        this.soGhe = soGhe;
    }

    // Getters and Setters
    public int getMaGheNgoi() { return maGheNgoi; }
    public void setMaGheNgoi(int maGheNgoi) { this.maGheNgoi = maGheNgoi; }

    public int getMaPhongChieu() { return maPhongChieu; }
    public void setMaPhongChieu(int maPhongChieu) { this.maPhongChieu = maPhongChieu; }

    public String getHangGhe() { return hangGhe; }
    public void setHangGhe(String hangGhe) { this.hangGhe = hangGhe; }

    public String getSoGhe() { return soGhe; }
    public void setSoGhe(String soGhe) { this.soGhe = soGhe; }

    @Override
    public String toString() {
        return hangGhe + soGhe;
    }
}