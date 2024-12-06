package vn.ngoviethoang.duancuoiky.data.entity;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "giao_dich")
public class GiaoDich {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double soTien;
    private String taiKhoan;
    private String danhMuc;
    private String ngay;
    private String ghiChu;
    private String loai;
    private Bitmap anh;

    public GiaoDich(double soTien, String taiKhoan, String danhMuc, String ngay, String ghiChu, String loai, Bitmap anh) {
        this.soTien = soTien;
        this.taiKhoan = taiKhoan;
        this.danhMuc = danhMuc;
        this.ngay = ngay;
        this.ghiChu = ghiChu;
        this.loai = loai;
        this.anh = anh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSoTien() {
        return soTien;
    }

    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
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

    public Bitmap getAnh() {
        return anh;
    }

    public void setAnh(Bitmap anh) {
        this.anh = anh;
    }
}