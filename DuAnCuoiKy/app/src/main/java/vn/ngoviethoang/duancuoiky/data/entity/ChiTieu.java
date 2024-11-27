package vn.ngoviethoang.duancuoiky.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import vn.ngoviethoang.duancuoiky.data.database.DateConverter;

@Entity(
        tableName = "chi_tieu",
        foreignKeys = @ForeignKey(
                entity = DanhMuc.class,
                parentColumns = "id",
                childColumns = "danhMucId",
                onDelete = ForeignKey.CASCADE
        )
)
@TypeConverters(DateConverter.class)
public class ChiTieu {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int danhMucId;
    public float soTien;
    public Date ngayThang;

    public ChiTieu(int danhMucId, float soTien, Date ngayThang) {
        this.danhMucId = danhMucId;
        this.soTien = soTien;
        this.ngayThang = ngayThang;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDanhMucId() {
        return danhMucId;
    }

    public void setDanhMucId(int danhMucId) {
        this.danhMucId = danhMucId;
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
