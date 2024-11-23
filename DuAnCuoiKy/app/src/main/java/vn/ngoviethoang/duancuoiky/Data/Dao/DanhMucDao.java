package vn.ngoviethoang.duancuoiky.Data.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import vn.ngoviethoang.duancuoiky.Data.Entity.DanhMuc;

@Dao
public interface DanhMucDao {
    @Insert
    void insertDanhMuc(DanhMuc danhMuc);

    @Query("SELECT * FROM danh_muc")
    List<DanhMuc> getAllDanhMuc();

    @Query("DELETE FROM danh_muc WHERE id = :id")
    void deleteDanhMuc(int id);
}
