package vn.ngoviethoang.duancuoiky.model;

public class LoaiChiTieu {
    private String ten; // Tên loại (ví dụ: ăn uống, mua sắm, đi lại)
    private double tongSoTien; // Tổng số tiền cho loại này

    // Constructor, getters, setters...
    public LoaiChiTieu(String ten, double tongSoTien) {
        this.ten = ten;
        this.tongSoTien = tongSoTien;
    }
    public String getTen() {
        return ten;
    }
    public double getTongSoTien() {
        return tongSoTien;
    }
    public void setTen(String ten) {
        this.ten = ten;
    }
    public void setTongSoTien(double tongSoTien) {
        this.tongSoTien = tongSoTien;
    }
    @Override
    public String toString() {
        return "LoaiChiTieu{" +
                "ten='" + ten + '\'' +
                ", tongSoTien=" + tongSoTien +
                '}';
    }
}
