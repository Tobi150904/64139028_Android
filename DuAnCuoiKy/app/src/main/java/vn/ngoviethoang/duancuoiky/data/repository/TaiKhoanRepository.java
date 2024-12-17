package vn.ngoviethoang.duancuoiky.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import vn.ngoviethoang.duancuoiky.data.dao.TaiKhoanDao;
import vn.ngoviethoang.duancuoiky.data.database.AppDatabase;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;

public class TaiKhoanRepository {
    private final TaiKhoanDao taiKhoanDao;

    public TaiKhoanRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        taiKhoanDao = db.taiKhoanDao();
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

    public LiveData<TaiKhoan> getTaiKhoanById(int id) {
        return taiKhoanDao.getTaiKhoanById(id);
    }
}