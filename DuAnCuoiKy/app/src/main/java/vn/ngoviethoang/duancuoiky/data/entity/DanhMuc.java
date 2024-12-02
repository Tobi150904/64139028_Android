package vn.ngoviethoang.duancuoiky.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "danh_muc")
public class DanhMuc {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String tenDanhMuc;
    public String loai;

    public DanhMuc(String tenDanhMuc, String loai) {
        this.tenDanhMuc = tenDanhMuc;
        this.loai = loai;
    }

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
