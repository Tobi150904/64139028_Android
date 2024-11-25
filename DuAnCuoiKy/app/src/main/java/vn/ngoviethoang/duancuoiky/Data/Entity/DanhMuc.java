package vn.ngoviethoang.duancuoiky.Data.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "danh_muc")
public class DanhMuc {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String tenDanhMuc;
    public String loai; // "Thu nhập" hoặc "Chi tiêu"

    public DanhMuc(String tenDanhMuc, String loai) {
        this.tenDanhMuc = tenDanhMuc;
        this.loai = loai;
    }

    // Getter và Setter
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

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }
}
