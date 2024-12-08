package vn.ngoviethoang.duancuoiky.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.ngoviethoang.duancuoiky.data.dao.DanhMucDao;
import vn.ngoviethoang.duancuoiky.data.database.AppDatabase;
import vn.ngoviethoang.duancuoiky.data.entity.DanhMuc;

public class DanhMucRepository {
    private final DanhMucDao danhMucDao;

    public DanhMucRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        this.danhMucDao = database.danhMucDao();
    }

    public LiveData<List<DanhMuc>> getDanhMucByLoai(String loai) {
        return danhMucDao.getDanhMucByLoai(loai);
    }

    public void insertDanhMuc(DanhMuc danhMuc) {
        AppDatabase.databaseWriteExecutor.execute(() -> danhMucDao.insertDanhMuc(danhMuc));
    }
}

