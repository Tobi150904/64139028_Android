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

    @Query("SELECT * FROM giao_dich WHERE ngay BETWEEN :startDate AND :endDate")
    LiveData<List<GiaoDich>> getGiaoDichByDateRange(Date startDate, Date endDate);

    @Delete
    void deleteGiaoDich(GiaoDich giaoDich);

    @Query("SELECT * FROM giao_dich")
    LiveData<List<GiaoDich>> getAllGiaoDich();
}
