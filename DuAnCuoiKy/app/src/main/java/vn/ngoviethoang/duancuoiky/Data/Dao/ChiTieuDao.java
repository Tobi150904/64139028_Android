package vn.ngoviethoang.duancuoiky.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import vn.ngoviethoang.duancuoiky.Data.Entity.ChiTieu;

@Dao
public interface ChiTieuDao {
    @Insert
    void insertChiTieu(ChiTieu chiTieu);

    @Query("SELECT * FROM chi_tieu")
    LiveData<List<ChiTieu>> getAllChiTieu();
}


