package vn.ngoviethoang.duancuoiky.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;

@Dao
public interface GiaoDichDao {
    @Insert
    void insert(GiaoDich giaoDich);

    @Update
    void update(GiaoDich giaoDich); // Thêm phương thức update

    @Query("SELECT * FROM giao_dich")
    LiveData<List<GiaoDich>> getAllGiaoDich();
}