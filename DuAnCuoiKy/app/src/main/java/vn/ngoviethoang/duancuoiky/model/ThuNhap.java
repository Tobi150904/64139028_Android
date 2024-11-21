package vn.ngoviethoang.duancuoiky.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "thunhap")
public class ThuNhap {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nguon; // Nguồn thu nhập (ví dụ: lương, thưởng)
    public double soTien; // Số tiền thu nhập
    public long ngay; // Ngày nhận thu nhập (timestamp)

    // Constructor, getters, setters...
    public ThuNhap(int id, String nguon, double soTien, long ngay) {
        this.id = id;
        this.nguon = nguon;
        this.soTien = soTien;
        this.ngay = ngay;
    }
    public int getId() {
        return id;
    }
    public String getNguon() {
        return nguon;
    }
    public double getSoTien() {
        return soTien;
    }
    public long getNgay() {
        return ngay;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setNguon(String nguon) {
        this.nguon = nguon;
    }
    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }
    public void setNgay(long ngay) {
        this.ngay = ngay;
    }
    @Override
    public String toString() {
        return "ThuNhap{" +
                "id=" + id +
                ", nguon='" + nguon + '\'' +
                ", soTien=" + soTien +
                ", ngay=" + ngay +
                '}';
    }
}
