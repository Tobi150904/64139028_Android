package vn.ngoviethoang.duancuoiky.Data.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chi_tieu")
public class ChiTieu {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String tenDanhMuc; // Tên danh mục chi tiêu
    public float soTien;      // Số tiền chi tiêu
    public String ngayThang;  // Ngày thực hiện chi tiêu

    public ChiTieu(String tenDanhMuc, float soTien, String ngayThang) {
        this.tenDanhMuc = tenDanhMuc;
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

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
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
        return "ChiTieu{" +
                "id=" + id +
                ", tenDanhMuc='" + tenDanhMuc + '\'' +
                ", soTien=" + soTien +
                ", ngayThang='" + ngayThang + '\'' +
                '}';
    }
}

