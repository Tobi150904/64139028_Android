package vn.ngoviethoang.duancuoiky.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;

import java.util.List;

import vn.ngoviethoang.duancuoiky.data.dao.TaiKhoanDao;
import vn.ngoviethoang.duancuoiky.data.database.AppDatabase;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;

public class TaiKhoanRepository {
    private final TaiKhoanDao taiKhoanDao;

    public TaiKhoanRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        this.taiKhoanDao = database.taiKhoanDao();
    }

    public LiveData<List<TaiKhoan>> getAllTaiKhoan() {
        return taiKhoanDao.getAllTaiKhoan();
    }

    public void insertTaiKhoan(TaiKhoan taiKhoan) {
        AppDatabase.databaseWriteExecutor.execute(() -> taiKhoanDao.insertTaiKhoan(taiKhoan));
    }

    public void updateTaiKhoan(TaiKhoan taiKhoan) {
        AppDatabase.databaseWriteExecutor.execute(() -> taiKhoanDao.updateTaiKhoan(taiKhoan));
    }
}