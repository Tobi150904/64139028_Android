package vn.ngoviethoang.duancuoiky.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chitieu")
public class ChiTieu {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String ten; // Tên chi tiêu
    public double soTien; // Số tiền chi tiêu
    public String loai; // Loại chi tiêu (ví dụ: ăn uống, đi lại)
    public long ngay; // Ngày chi tiêu (timestamp)

    // Constructor, getters, setters...
    public ChiTieu(int id, String ten, double soTien, String loai, long ngay) {
        this.id = id;
        this.ten = ten;
        this.soTien = soTien;
        this.loai = loai;
        this.ngay = ngay;
    }
    public int getId() {
        return id;
    }
    public String getTen() {
        return ten;
    }
    public double getSoTien() {
        return soTien;
    }
    public String getLoai() {
        return loai;
    }
    public long getNgay() {
        return ngay;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTen(String ten) {
        this.ten = ten;
    }
    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }
    public void setLoai(String loai) {
        this.loai = loai;
    }
    public void setNgay(long ngay) {
        this.ngay = ngay;
    }
    @Override
    public String toString() {
        return "ChiTieu{" +
                "id=" + id +
                ", ten='" + ten + '\'' +
                ", soTien=" + soTien +
                ", loai='" + loai + '\'' +
                ", ngay=" + ngay +
                '}';
    }
}
