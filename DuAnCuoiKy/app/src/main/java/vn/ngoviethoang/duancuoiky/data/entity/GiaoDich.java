package vn.ngoviethoang.duancuoiky.data.entity;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import vn.ngoviethoang.duancuoiky.data.database.DateConverter;

@Entity(
        tableName = "giao_dich",
        foreignKeys = {
                @ForeignKey(entity = TaiKhoan.class, parentColumns = "id", childColumns = "taiKhoanId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = DanhMuc.class, parentColumns = "id", childColumns = "danhMucId", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("taiKhoanId"), @Index("danhMucId")}
)
@TypeConverters(DateConverter.class)
public class GiaoDich {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int taiKhoanId;  // Liên kết đến tài khoản
    public int danhMucId;   // Liên kết đến danh mục
    public double soTien;   // Số tiền
    public Date ngay;       // Ngày giao dịch
    public String ghiChu;   // Ghi chú
    public String loai;     // Loại giao dịch: "thu_nhap" hoặc "chi_tieu"

    // Constructor
    public GiaoDich(int taiKhoanId, int danhMucId, double soTien, Date ngay, String ghiChu, String loai) {
        this.taiKhoanId = taiKhoanId;
        this.danhMucId = danhMucId;
        this.soTien = soTien;
        this.ngay = ngay;
        this.ghiChu = ghiChu;
        this.loai = loai;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaiKhoanId() {
        return taiKhoanId;
    }

    public void setTaiKhoanId(int taiKhoanId) {
        this.taiKhoanId = taiKhoanId;
    }

    public int getDanhMucId() {
        return danhMucId;
    }

    public void setDanhMucId(int danhMucId) {
        this.danhMucId = danhMucId;
    }

    public double getSoTien() {
        return soTien;
    }

    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }

    public Date getNgay() {
        return ngay;
    }

    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }
}