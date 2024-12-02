package vn.ngoviethoang.duancuoiky.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import vn.ngoviethoang.duancuoiky.data.database.DateConverter;

@Entity(tableName = "thu_nhap")
@TypeConverters(DateConverter.class)
public class ThuNhap {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String tenNguonThu;
    public float soTien;
    public Date ngayThang;

    public ThuNhap(String tenNguonThu, float soTien, Date ngayThang) {
        this.tenNguonThu = tenNguonThu;
        this.soTien = soTien;
        this.ngayThang = ngayThang;
    }

    // Getter và Setter
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

    public Date getNgayThang() {
        return ngayThang;
    }

    public void setNgayThang(Date ngayThang) {
        this.ngayThang = ngayThang;
    }
}
