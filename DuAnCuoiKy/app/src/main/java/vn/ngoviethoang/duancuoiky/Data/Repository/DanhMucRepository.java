package vn.ngoviethoang.duancuoiky.Data.Repository;

import android.content.Context;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.ngoviethoang.duancuoiky.Data.Dao.DanhMucDao;
import vn.ngoviethoang.duancuoiky.Data.Database.AppDatabase;
import vn.ngoviethoang.duancuoiky.Data.Entity.DanhMuc;

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
