package vn.ngoviethoang.duancuoiky.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;
import vn.ngoviethoang.duancuoiky.data.entity.ThuNhap;

@Dao
public interface ThuNhapDao {
    @Insert
    void insertThuNhap(ThuNhap thuNhap);

    @Update
    void updateThuNhap(ThuNhap thuNhap);

    @Delete
    void deleteThuNhap(ThuNhap thuNhap);

    @Query("SELECT * FROM thu_nhap WHERE ngayThang BETWEEN :startDate AND :endDate")
    LiveData<List<ThuNhap>> getThuNhapByDateRange(Date startDate, Date endDate);
}


