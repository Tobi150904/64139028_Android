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
    private DanhMucDao danhMucDao;
    private ExecutorService executorService;

    public DanhMucRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        danhMucDao = database.danhMucDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<DanhMuc>> getAllDanhMuc() {
        return danhMucDao.getAllDanhMuc();
    }

    public void insertDanhMuc(DanhMuc danhMuc) {
        executorService.execute(() -> danhMucDao.insertDanhMuc(danhMuc));
    }
}
