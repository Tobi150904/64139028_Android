package vn.ngoviethoang.duancuoiky.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import vn.ngoviethoang.duancuoiky.data.entity.DanhMuc;

@Dao
public interface DanhMucDao {
    @Insert
    void insertDanhMuc(DanhMuc danhMuc);

    @Update
    void updateDanhMuc(DanhMuc danhMuc);

    @Delete
    void deleteDanhMuc(DanhMuc danhMuc);

    @Query("SELECT * FROM danh_muc WHERE loai = :loai")
    LiveData<List<DanhMuc>> getDanhMucByLoai(String loai);

    @Query("SELECT * FROM danh_muc")
    LiveData<List<DanhMuc>> getAllDanhMuc();
}


