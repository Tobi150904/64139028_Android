package vn.ngoviethoang.duancuoiky.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import vn.ngoviethoang.duancuoiky.data.dao.GiaoDichDao;
import vn.ngoviethoang.duancuoiky.data.database.AppDatabase;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;

public class GiaoDichRepository {
    private final GiaoDichDao giaoDichDao;

    public GiaoDichRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        this.giaoDichDao = database.giaoDichDao();
    }

    public LiveData<List<GiaoDich>> getGiaoDichByLoai(String loai) {
        return giaoDichDao.getGiaoDichByLoai(loai);
    }

    public void insertGiaoDich(GiaoDich giaoDich) {
        AppDatabase.databaseWriteExecutor.execute(() -> giaoDichDao.insertGiaoDich(giaoDich));
    }

    public void deleteGiaoDich(GiaoDich giaoDich) {
        AppDatabase.databaseWriteExecutor.execute(() -> giaoDichDao.deleteGiaoDich(giaoDich));
    }
}
