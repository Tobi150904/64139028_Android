package vn.ngoviethoang.duancuoiky.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "tai_khoan")
public class TaiKhoan {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String ten;      // Tên tài khoản
    public double sodu;     // Số dư
    public byte[] icon;     // Biểu tượng tài khoản (bitmap lưu trữ dưới dạng byte)
    public Date ngay;       // Ngày tạo tài khoản

    public TaiKhoan(String ten, double sodu, byte[] icon, Date ngay) {
        this.ten = ten;
        this.sodu = sodu;
        this.icon = icon;
        this.ngay = ngay;
    }

    // Getters và setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTen() {
        return ten;
    }
    public void setTen(String ten) {
        this.ten = ten;
    }
    public double getSodu() {
        return sodu;
    }
    public void setSodu(double sodu) {
        this.sodu = sodu;
    }
    public byte[] getIcon() {
        return icon;
    }
    public void setIcon(byte[] icon) {
        this.icon = icon;
    }
    public Date getNgay() {
        return ngay;
    }
    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }
}