package vn.ngoviethoang.duancuoiky.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import vn.ngoviethoang.duancuoiky.data.entity.SoDu;

@Dao
public interface SoDuDao {
    @Insert
    void insert(SoDu soDu);

    @Update
    void update(SoDu soDu);

    @Query("SELECT * FROM SoDu LIMIT 1")
    LiveData<SoDu> getBalance();
}
