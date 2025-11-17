package model;
    public class ChucVu {
        private int maChucVu;
        private String tenChucVu;
        private String moTaQuyen;

        public ChucVu() {}

        public ChucVu(int maChucVu, String tenChucVu, String moTaQuyen) {
            this.maChucVu = maChucVu;
            this.tenChucVu = tenChucVu;
            this.moTaQuyen = moTaQuyen;
        }

        // Getters and Setters
        public int getMaChucVu() { return maChucVu; }
        public void setMaChucVu(int maChucVu) { this.maChucVu = maChucVu; }

        public String getTenChucVu() { return tenChucVu; }
        public void setTenChucVu(String tenChucVu) { this.tenChucVu = tenChucVu; }

        public String getMoTaQuyen() { return moTaQuyen; }
        public void setMoTaQuyen(String moTaQuyen) { this.moTaQuyen = moTaQuyen; }

        @Override
        public String toString() {
            return tenChucVu;
    }
}
