package vn.ngoviethoang.duancuoiky.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;

@Dao
public interface TaiKhoanDao {
    @Query("SELECT * FROM tai_khoan")
    LiveData<List<TaiKhoan>> getAllTaiKhoan();

    @Insert
    void insertTaiKhoan(TaiKhoan taiKhoan);

    @Update
    void updateTaiKhoan(TaiKhoan taiKhoan);

    @Query("SELECT * FROM tai_khoan WHERE id = :id")
    LiveData<TaiKhoan> getTaiKhoanById(int id);
}