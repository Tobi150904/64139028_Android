package vn.ngoviethoang.duancuoiky.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import vn.ngoviethoang.duancuoiky.data.entity.ChiTieu;

@Dao
public interface ChiTieuDao {
    @Insert
    void insertChiTieu(ChiTieu chiTieu);

    @Update
    void updateChiTieu(ChiTieu chiTieu);

    @Delete
    void deleteChiTieu(ChiTieu chiTieu);

    @Query("SELECT * FROM chi_tieu WHERE ngayThang BETWEEN :startDate AND :endDate")
    LiveData<List<ChiTieu>> getChiTieuByDateRange(Date startDate, Date endDate);
}



