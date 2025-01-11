package vn.ngoviethoang.duancuoiky.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import vn.ngoviethoang.duancuoiky.data.entity.DanhMuc;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;

@Dao
public interface GiaoDichDao {
    @Insert
    void insertGiaoDich(GiaoDich giaoDich);

    @Query("SELECT * FROM giao_dich WHERE loai = :loai")
    LiveData<List<GiaoDich>> getGiaoDichByLoai(String loai);

    @Query("SELECT * FROM giao_dich WHERE loai = :loai AND ngay BETWEEN :startDate AND :endDate")
    LiveData<List<GiaoDich>> getGiaoDichByLoaiAndDateRange(String loai, Date startDate, Date endDate);

    @Query("SELECT * FROM giao_dich")
    LiveData<List<GiaoDich>> getAllGiaoDich();

    @Query("SELECT * FROM giao_dich WHERE ngay LIKE :dateRange AND loai = :type")
    public abstract LiveData<List<GiaoDich>> getFilteredGiaoDich(String dateRange, String type);
}
