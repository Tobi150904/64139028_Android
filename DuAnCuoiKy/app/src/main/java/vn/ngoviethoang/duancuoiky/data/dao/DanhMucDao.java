package vn.ngoviethoang.duancuoiky.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import vn.ngoviethoang.duancuoiky.data.entity.DanhMuc;

@Dao
public interface DanhMucDao {
    @Insert
    void insertDanhMuc(DanhMuc danhMuc);

    @Query("SELECT * FROM danh_muc")
    LiveData<List<DanhMuc>> getAllDanhMuc();
}

