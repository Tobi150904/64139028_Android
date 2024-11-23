package vn.ngoviethoang.duancuoiky.Data.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import vn.ngoviethoang.duancuoiky.Data.Entity.ThuNhap;

@Dao
public interface ThuNhapDao {
    @Insert
    void insertThuNhap(ThuNhap thuNhap);

    @Query("SELECT * FROM thu_nhap")
    List<ThuNhap> getAllThuNhap();

    @Query("DELETE FROM thu_nhap WHERE id = :id")
    void deleteThuNhap(int id);
}

