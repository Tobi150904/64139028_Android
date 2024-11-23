package vn.ngoviethoang.duancuoiky.Data.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "thu_nhap")
public class ThuNhap {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String tenNguonThu; // Tên nguồn thu nhập
    public float soTien;       // Số tiền thu nhập
    public String ngayThang;   // Ngày nhận thu nhập

    public ThuNhap(String tenNguonThu, float soTien, String ngayThang) {
        this.tenNguonThu = tenNguonThu;
        this.soTien = soTien;
        this.ngayThang = ngayThang;
    }
    // Các phương thức getter và setter cho các thuộc tính
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTenNguonThu() {
        return tenNguonThu;
    }
    public void setTenNguonThu(String tenNguonThu) {
        this.tenNguonThu = tenNguonThu;
    }
    public float getSoTien() {
        return soTien;
    }
    public void setSoTien(float soTien) {
        this.soTien = soTien;
    }
    public String getNgayThang() {
        return ngayThang;
    }
    public void setNgayThang(String ngayThang) {
        this.ngayThang = ngayThang;
    }
    @Override
    public String toString() {
        return "ThuNhap{" +
                "id=" + id +
                ", tenNguonThu='" + tenNguonThu + '\'' +
                ", soTien=" + soTien +
                ", ngayThang='" + ngayThang + '\'' +
                '}';
    }
}

