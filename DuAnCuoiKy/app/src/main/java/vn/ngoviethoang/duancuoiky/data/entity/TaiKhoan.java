package vn.ngoviethoang.duancuoiky.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tai_khoan")
public class TaiKhoan {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String ten;     // Tên tài khoản
    public double sodu;    // Số dư
    public int iconId;     // Biểu tượng tài khoản

    // Constructor
    public TaiKhoan(String ten, double sodu, int iconId) {
        this.ten = ten;
        this.sodu = sodu;
        this.iconId = iconId;
    }

    // Getters and setters
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

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
